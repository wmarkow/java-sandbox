package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.logging.Logger;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;

public class AdvancedLandingPointPredictor
{
    private static final Logger LOGGER = Logging.getLogger( AdvancedLandingPointPredictor.class );

    private GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );
    private ClimbingDataSet dataSet = null;

    public AdvancedLandingPointPredictor( ClimbingDataSet dataSet )
    {
        this.dataSet = dataSet;
    }

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
