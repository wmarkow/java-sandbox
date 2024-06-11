package com.github.wmarkow.radiosonde.tracker.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataSet
{
    protected ArrayList< DataPoint > dataPoints;

    public DataSet( ArrayList< DataPoint > dataPoints )
    {
        this.dataPoints = dataPoints;

        Collections.sort( this.dataPoints, new DataSetByDateTimeComparator() );
    }

    public ArrayList< DataPoint > getDataPoints()
    {
        return dataPoints;
    }

    public DataPoint getYoungestDataPoint()
    {
        if( getDataPoints().size() == 0 )
        {
            return null;
        }

        int lastIndex = getDataPoints().size() - 1;

        return getDataPoints().get( lastIndex );
    }

    public ArrayList< DataPoint > getEntriesOlderThanTheYoungestButWithMaxAge( int maxAgeInSeconds )
    {
        ArrayList< DataPoint > result = new ArrayList<>();

        DataPoint youngestDataPoint = getYoungestDataPoint();
        if( youngestDataPoint == null )
        {
            return result;
        }

        long youngestEpochSeconds = youngestDataPoint.utcDateTime.toEpochSecond();
        long borderSeconds = youngestEpochSeconds - maxAgeInSeconds;

        for( DataPoint dp : getDataPoints() )
        {
            long currentEpochSeconds = dp.utcDateTime.toEpochSecond();
            if( currentEpochSeconds > borderSeconds )
            {
                result.add( dp );
            }
        }

        return result;
    }

    public ArrayList< DataPoint > getEntriesOlderThanTheYoungestButWithMinAge( int minAgeInSeconds )
    {
        ArrayList< DataPoint > result = new ArrayList<>();

        DataPoint youngestDataPoint = getYoungestDataPoint();
        if( youngestDataPoint == null )
        {
            return result;
        }

        long youngestEpochSeconds = youngestDataPoint.utcDateTime.toEpochSecond();
        long borderSeconds = youngestEpochSeconds - minAgeInSeconds;

        for( DataPoint dp : getDataPoints() )
        {
            long currentEpochSeconds = dp.utcDateTime.toEpochSecond();
            if( currentEpochSeconds < borderSeconds )
            {
                result.add( dp );
            }
        }

        return result;
    }

    /***
     * Iterates over the data point and returns first data point by altitude match with specified accuracy. It
     * is silently assumed that a data point from the climbing part of the flight is found.
     * 
     * @param altitude
     * @return a valid data point or null (if not found)
     */
    public DataPoint getFirstDataPointByAltitude( double altitude, double accuracy )
    {
        if( dataPoints.size() == 0 )
        {
            return null;
        }
        if( altitude < dataPoints.get( 0 ).altitude_m )
        {
            return dataPoints.get( 0 );
        }

        for( DataPoint dp : dataPoints )
        {
            if( Math.abs( dp.altitude_m - altitude ) < accuracy )
            {
                return dp;
            }
        }

        return null;
    }

    private class DataSetByDateTimeComparator implements Comparator< DataPoint >
    {

        @Override
        public int compare( DataPoint o1, DataPoint o2 )
        {
            long o1EpochSecond = o1.utcDateTime.toEpochSecond();
            long o2EpochSecond = o2.utcDateTime.toEpochSecond();

            if( o1EpochSecond < o2EpochSecond )
            {
                return -1;
            }
            if( o1EpochSecond > o2EpochSecond )
            {
                return 1;
            }

            return 0;
        }

    }
}
