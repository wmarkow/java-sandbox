package com.github.wmarkow.radiosonde.tracker.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class ClimbingDataSet extends DataSet
{
    private PolynomialSplineFunction windSpeedFunction_km_h = null;
    private PolynomialSplineFunction windCourseFunction = null;

    private ClimbingDataSet( ArrayList< DataPoint > dataPoints )
    {
        super( dataPoints );

        // Make the wind data monotonic by altitude, as initial sonde data can contain multiple entries for
        // the same altitude.
        Map< Integer, WindData > windDataDistribution = new HashMap< Integer, WindData >();
        for( DataPoint dp : dataPoints )
        {
            final int altitude_m = dp.altitude_m;
            if( windDataDistribution.containsKey( altitude_m ) )
            {
                // Wind data distribution already contains data at specific altitude: ignore new data.
                // TODO: maybe we should calculate average values here? Attention: for course we need to
                // calculate vector average instead of arithmetic average, i.e. imagine the courses like 359
                // and 1: vector average is 360 degree, while arithmetic would be 180.
            }
            else
            {
                // Wind data doesn't contain data at specific altitude
                WindData windData = new WindData();
                windData.altitude_m = altitude_m;
                windData.speed_km_h = dp.speed_km_h;
                windData.course = dp.course;

                windDataDistribution.put( altitude_m, windData );
            }
        }
        ArrayList< WindData > monotonicWindData = new ArrayList< WindData >( windDataDistribution.values() );
        Collections.sort( monotonicWindData, new MonotonicWindDataByAltitudeAscComparator() );

        // Create an interpolation function.
        int size = monotonicWindData.size();
        double altitude_m[] = new double[ size ];
        double windSpeed_km_h[] = new double[ size ];
        double windCourse[] = new double[ size ];
        for( int q = 0; q < size; q++ )
        {
            WindData dp = monotonicWindData.get( q );
            altitude_m[ q ] = dp.altitude_m;
            windSpeed_km_h[ q ] = dp.speed_km_h;
            windCourse[ q ] = dp.course;
        }

        LinearInterpolator linearInerpolator = new LinearInterpolator();
        windSpeedFunction_km_h = linearInerpolator.interpolate( altitude_m, windSpeed_km_h );
        windCourseFunction = linearInerpolator.interpolate( altitude_m, windCourse );
    }

    /***
     * Gets the wind speed at the specific altitude.
     * 
     * @param altitude
     *            in meters
     * @return wind speed in km/h
     */
    public double getWindSpeed( double altitude )
    {
        if( altitude < 0.0 )
        {
            throw new IllegalArgumentException(
                String.format( "Passed altitude value %s must not be negative.", altitude ) );
        }

        if( windSpeedFunction_km_h.isValidPoint( altitude ) )
        {
            return windSpeedFunction_km_h.value( altitude );
        }

        // TODO: it may be memory optimized by taking source code of PolynomialSplineFunction and introducing
        // new methods, i.e. getMinX() and getMaxX()
        double[] altitudeKnots = windSpeedFunction_km_h.getKnots();
        if( altitude < altitudeKnots[ 0 ] )
        {
            return windSpeedFunction_km_h.value( altitudeKnots[ 0 ] );
        }

        int n = windSpeedFunction_km_h.getN();
        return windSpeedFunction_km_h.value( altitudeKnots[ n ] );
    }

    /***
     * Gets the wind course at the specific altitude.
     * 
     * @param altitude
     *            in meters
     * @return wind course
     */
    public double getWindCourse( double altitude )
    {
        if( altitude < 0.0 )
        {
            throw new IllegalArgumentException(
                String.format( "Passed altitude value %s must not be negative.", altitude ) );
        }

        if( windCourseFunction.isValidPoint( altitude ) )
        {
            return windCourseFunction.value( altitude );
        }

        double[] courseKnots = windCourseFunction.getKnots();
        if( altitude < courseKnots[ 0 ] )
        {
            return windCourseFunction.value( courseKnots[ 0 ] );
        }

        int n = windSpeedFunction_km_h.getN();
        return windCourseFunction.value( courseKnots[ n ] );
    }

    public final static ClimbingDataSet valueOf( DataSet dataSet )
    {
        DataPoint highestDataPoint = dataSet.getHighestDataPoint();

        if( highestDataPoint == null )
        {
            return new ClimbingDataSet( new ArrayList< DataPoint >() );
        }

        ArrayList< DataPoint > dataPoints = new ArrayList< DataPoint >();
        for( DataPoint dp : dataSet.getDataPoints() )
        {
            dataPoints.add( dp );

            if( dp == highestDataPoint )
            {
                // we have reached the highest data point and climbing data set has been found
                break;
            }
        }

        return new ClimbingDataSet( dataPoints );
    }

    private class WindData
    {
        int altitude_m;
        double speed_km_h;
        double course;
    }

    private class MonotonicWindDataByAltitudeAscComparator implements Comparator< WindData >
    {

        @Override
        public int compare( WindData o1, WindData o2 )
        {
            if( o1.altitude_m < o2.altitude_m )
            {
                return -1;
            }
            if( o1.altitude_m > o2.altitude_m )
            {
                return 1;
            }

            // this should never happen in monotonic data distribution
            throw new RuntimeException( "NonMonotonicSequenceException" );
        }

    }
}
