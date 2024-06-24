package com.github.wmarkow.radiosonde.tracker.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class WindDataDistribution
{
    private double latitude;
    private double longitude;
    private ZonedDateTime zonedDateTime;
    private List< WindData > windData = new ArrayList<>();

    public WindDataDistribution( double latitude, double longitude, ZonedDateTime zonedDateTime )
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.zonedDateTime = zonedDateTime;
    }

    public void addWindData( WindData windData )
    {
        this.windData.add( windData );
    }

    public WindData getWindData( int altitude )
    {
        // calculate the wind data at specific altitude
        return null;
    }
}
