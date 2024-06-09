package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import java.util.ArrayList;

public class DataSet
{
    private ArrayList< DataPoint > dataPoints;

    public DataSet( ArrayList< DataPoint > dataPoints )
    {
        this.dataPoints = dataPoints;
    }

    public ArrayList< DataPoint > getDataPoints()
    {
        return dataPoints;
    }
}
