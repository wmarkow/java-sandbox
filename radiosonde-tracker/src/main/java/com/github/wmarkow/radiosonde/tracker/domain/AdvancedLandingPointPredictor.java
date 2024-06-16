package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;

/***
 * Advanced landing point predictor.
 * <p>
 * It calculates location of the landing point based on sonde's current track point and the historical wind
 * data from the climbing part of the sonde's track.
 * 
 * @author wmarkowski
 */
public class AdvancedLandingPointPredictor
{
    private static final Logger LOGGER = Logging.getLogger( AdvancedLandingPointPredictor.class );

    private GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );
    private ClimbingDataSet dataSet = null;

    /***
     * Creates the object based on the climbing data points, which will be used to provide prediction of the
     * landing point.
     * 
     * @param dataSet
     */
    public AdvancedLandingPointPredictor( ClimbingDataSet dataSet )
    {
        this.dataSet = dataSet;
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
     * <li>calculate the landing point - based on current sonde location, altitude and historical wind data
     * from the climbing part of the track</li>
     * </ul>
     * Assumptions (simplifications) made during prediction:
     * <ul>
     * <li>falling speed of the sonde doesn't change over time</li>
     * <li>wind data (speed and course) of the falling part are the same as wind data of climbing part of the
     * flight (rough simplification: wind data at landing area is the same as at starting place)</li>
     * </ul>
     * <p>
     * This prediction method is more complex but it gives more accurate results than
     * {@linkplain LandingPointPredictor}, even when the sonde is at middle altitude (5-10km)
     * 
     * @param dataPoint
     * @return
     */
    public Point2D predict( DataPoint dataPoint )
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

        while( altitude_m > 0.0 )
        {
            double windSpeedAtAltitude_km_h = dataSet.getWindSpeed( altitude_m );
            double windCourseAtAltitude = dataSet.getWindCourse( altitude_m );

            altitude_m = altitude_m + climb_m_s * dt_s;
            double distance_m = windSpeedAtAltitude_km_h * 1000.0 / 3600.0 * dt_s;

            // calculate new longitude and latitude
            calc.setStartingGeographicPoint( lon, lat );
            calc.setDirection( windCourseAtAltitude, distance_m );
            Point2D newPoint = calc.getDestinationGeographicPoint();
            lon = newPoint.getX();
            lat = newPoint.getY();
        }

        return new DirectPosition2D( lon, lat );
    }
}
