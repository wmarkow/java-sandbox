package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.util.logging.Logger;

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
        final double daccuracy_m = 50.0; // in meters

        int counter = 0;
        while( altitude_m > 0.0 )
        {
            DataPoint dpAtAltitude = dataSet.getFirstDataPointByAltitude( altitude_m, daccuracy_m );
            altitude_m = altitude_m + climb_m_s * dt_s;

            if( dpAtAltitude == null )
            {
                LOGGER.warning( String.format( "Can't fint DataPoint for altitude %s with accuracy %s",
                    altitude_m, daccuracy_m ) );
                counter++;

                continue;
            }

            double distance_m = dpAtAltitude.speed_km_h * 1000.0 / 3600.0 * dt_s;

            // calculate new longitude and latitude
            calc.setStartingGeographicPoint( lon, lat );
            calc.setDirection( dpAtAltitude.course, distance_m );
            Point2D newPoint = calc.getDestinationGeographicPoint();
            lon = newPoint.getX();
            lat = newPoint.getY();
        }
        if( counter > 0 )
        {
            LOGGER.warning(
                String.format( "Couldn't find %s DataPoints in total for this prediction.", counter ) );
        }

        return new DirectPosition2D( lon, lat );
    }
}
