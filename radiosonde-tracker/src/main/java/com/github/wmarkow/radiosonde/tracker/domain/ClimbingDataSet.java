package com.github.wmarkow.radiosonde.tracker.domain;

import java.util.ArrayList;

public class ClimbingDataSet extends DataSet
{

    private ClimbingDataSet( ArrayList< DataPoint > dataPoints )
    {
        super( dataPoints );
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
}
