package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.time.ZonedDateTime;

import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;

/***
 * A simple landing point predictor.
 * <p>
 * It calculates location of the landing point only based on sonde's current track point.
 * 
 * @author wmarkowski
 */
public class LandingPointPredictor
{
    private GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );

    /***
     * Predicts the landing point (latitude, longitude) based on the following data:
     * <ul>
     * <li>current location</li>
     * <li>current altitude [m]</li>
     * <li>current falling speed [m/s]</li>
     * <li>current speed [km/h]</li>
     * <li>current course</li>
     * </ul>
     * Theory of calculation:
     * <ul>
     * <li>calculate the time when sonde hits the ground - based on current altitude and falling speed</li>
     * <li>calculate the landing point - based on current sonde location, altitude, speed and course</li>
     * <li></li>
     * </ul>
     * Assumptions (simplifications) made during prediction:
     * <ul>
     * <li>falling speed of the sonde doesn't change over time</li>
     * <li>sonde speed (wind speed) doesn' change over time</li>
     * <li>sonde course (wind course) doesn't change over time</li>
     * </ul>
     * <p>
     * This prediction method is very simple but also not accurate, especially when the sonde begins to fall
     * down. The accuracy improves with time, i.e. when sonde is on lower altitudes (<5km), i.e. closer to the
     * ground (landing point).
     * 
     * @param dataPoint
     *            sonde's current data point
     * @return predicted landing point
     */
    public LandingPoint predict( DataPoint dataPoint )
    {
        if( dataPoint.climbing_m_s > 0 )
        {
            return null;
        }

        // Sonde is falling down. Let's predict its landing point.

        // Calculate the time of landing
        double time_h = dataPoint.altitude_m / Math.abs( dataPoint.climbing_m_s ) / 3600.0;
        // Calculate the distance of landing from current data point
        double distance_km = dataPoint.speed_km_h * time_h;
        double distance_m = distance_km * 1000;

        // Calculate the landing point based on the distance and course from current data point
        calc.setStartingGeographicPoint( dataPoint.longitude, dataPoint.latitude );
        calc.setDirection( dataPoint.course, distance_m );
        Point2D landingPoint = calc.getDestinationGeographicPoint();

        ZonedDateTime landingTime = dataPoint.utcDateTime.plusSeconds( (long)(time_h * 3600) );

        return new LandingPoint( landingPoint, landingTime );
    }
}
