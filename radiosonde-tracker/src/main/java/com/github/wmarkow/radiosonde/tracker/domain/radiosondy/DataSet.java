package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.junit.experimental.theories.DataPoints;

public class DataSet
{
    private ArrayList< DataPoint > dataPoints;
    private int returnDataPointsOlderThanFromLatest = 0;

    public DataSet( ArrayList< DataPoint > dataPoints )
    {
        this.dataPoints = dataPoints;

        Collections.sort( this.dataPoints, new DataSetByDateTimeComparator() );
    }
    
    public void setReturnDataPointsOlderThanFromLatest(int timeInSeconds)
    {
        this.returnDataPointsOlderThanFromLatest = timeInSeconds;
    }

    public ArrayList< DataPoint > getDataPoints()
    {
        ArrayList<DataPoint> result = new ArrayList<>();
        
        DataPoint youngestDataPoint = getYoungestDataPoint0();
        if(youngestDataPoint == null)
        {
            return result;
        }
        
        long youngestEpochSeconds = youngestDataPoint.utcDateTime.toEpochSecond();
        long newYoungestEpochSeconds = youngestEpochSeconds - returnDataPointsOlderThanFromLatest;
        for(DataPoint dp : dataPoints)
        {
            long currentEpochSeconds = dp.utcDateTime.toEpochSecond();
            if(currentEpochSeconds < newYoungestEpochSeconds)
            {
                result.add( dp );
            }
        }
        
        return result;
    }

    public ArrayList< DataPoint > getDataPointsYoungerThanFromLatest( int timeInSeconds )
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
    
    private DataPoint getYoungestDataPoint0()
    {
        if(dataPoints.size() == 0)
        {
            return null;
        }
        
        int lastIndex = dataPoints.size() - 1;

        return dataPoints.get( lastIndex );
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
