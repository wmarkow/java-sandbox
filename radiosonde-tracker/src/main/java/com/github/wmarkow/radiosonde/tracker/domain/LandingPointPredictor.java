package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;

import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;

public class LandingPointPredictor
{
    private GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );

    public Point2D predict( DataPoint dataPoint )
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

        return landingPoint;
    }
}
