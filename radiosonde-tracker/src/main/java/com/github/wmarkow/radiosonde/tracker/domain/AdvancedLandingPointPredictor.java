package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;

/***
 * Advanced landing point predictor.
 * <p>
 * It calculates location of the landing point based on sonde's current track point and wind data.
 * 
 * @author wmarkowski
 */
public class AdvancedLandingPointPredictor
{
    private static final Logger LOGGER = Logging.getLogger( AdvancedLandingPointPredictor.class );

    private GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );
    private WindDataProvider windDataProvider = null;

    public AdvancedLandingPointPredictor( WindDataProvider windDataProvider )
    {
        this.windDataProvider = windDataProvider;
    }

    /***
     * Predicts the landing point (latitude, longitude) based on the following data:
     * <ul>
     * <li>current location</li>
     * <li>current altitude [m]</li>
     * <li>current falling speed [m/s]</li>
     * <li>wind speed and course data of the climbing part</li>
     * </ul>
     * Theory of calculation:
     * <ul>
     * <li>calculate the time when sonde hits the ground - based on current altitude and falling speed</li>
     * <li>calculate the landing point - based on current sonde location, altitude and wind data</li>
     * </ul>
     * Assumptions (simplifications) made during prediction:
     * <ul>
     * <li>falling speed of the sonde doesn't change over time</li>
     * <li>wind data (speed and course) accuracy has the biggest impact to the generated prediction
     * output</li>
     * </ul>
     * <p>
     * This prediction method is more complex but it gives more accurate results than
     * {@linkplain BasicLandingPointPredictor}, even when the sonde is at middle altitude (5-10km)
     * 
     * @param dataPoint
     * @return landing point prediction
     */
    public LandingPoint predict( DataPoint dataPoint )
    {
        if( dataPoint == null )
        {
            return null;
        }

        if( dataPoint.climbing_m_s > 0 )
        {
            return null;
        }

        // Sonde is falling down. Let's predict its landing point.

        double lat = dataPoint.latitude;
        double lon = dataPoint.longitude;
        double altitude_m = dataPoint.altitude_m;
        final double climb_m_s = dataPoint.climbing_m_s; // assuming it is negative
        final double dt_s = 10; // in seconds
        double time_s = 0;

        while( altitude_m > 0.0 )
        {
            Double windSpeedAtAltitude_km_h = windDataProvider.getWindSpeed( lat, lon, altitude_m );
            if( windSpeedAtAltitude_km_h == null )
            {
                return null;
            }
            Double windCourseAtAltitude = windDataProvider.getWindCourse( lat, lon, altitude_m );
            if( windCourseAtAltitude == null )
            {
                return null;
            }

            altitude_m = altitude_m + climb_m_s * dt_s;
            double distance_m = windSpeedAtAltitude_km_h * 1000.0 / 3600.0 * dt_s;
            time_s += dt_s;

            // calculate new longitude and latitude
            calc.setStartingGeographicPoint( lon, lat );
            calc.setDirection( windCourseAtAltitude, distance_m );
            Point2D newPoint = calc.getDestinationGeographicPoint();
            lon = newPoint.getX();
            lat = newPoint.getY();
        }

        Point2D location = new DirectPosition2D( lon, lat );
        ZonedDateTime landingTime = dataPoint.utcDateTime.plusSeconds( (long)(time_s) );

        return new LandingPoint( location, landingTime );
    }
}
