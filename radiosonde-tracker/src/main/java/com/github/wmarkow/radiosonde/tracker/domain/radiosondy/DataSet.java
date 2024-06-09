package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataSet
{
    private ArrayList< DataPoint > dataPoints;

    public DataSet( ArrayList< DataPoint > dataPoints )
    {
        this.dataPoints = dataPoints;

        Collections.sort( this.dataPoints, new DataSetByDateTimeComparator() );
    }

    public ArrayList< DataPoint > getDataPoints()
    {
        return dataPoints;
    }

    public ArrayList< DataPoint > getDataPointsYoungerThan( int timeInSeconds )
    {
        ArrayList<DataPoint> result = new ArrayList<>();
        
        DataPoint youngestDataPoint = getYoungestDataPoint();
        if(youngestDataPoint == null)
        {
            return result;
        }
        
        long youngestEpochSeconds = youngestDataPoint.utcDateTime.toEpochSecond();
        long oldestEpochSeconds = youngestEpochSeconds - timeInSeconds;
        
        for(DataPoint dp : getDataPoints())
        {
            long currentEpochSeconds = dp.utcDateTime.toEpochSecond();
            if(currentEpochSeconds > oldestEpochSeconds)
            {
                result.add( dp );
            }
        }
        
        return result;
    }

    public DataPoint getYoungestDataPoint()
    {
        if(getDataPoints().size() == 0)
        {
            return null;
        }
        
        int lastIndex = getDataPoints().size() - 1;

        return getDataPoints().get( lastIndex );
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
                return -1;
            }

            return 0;
        }

    }
}
