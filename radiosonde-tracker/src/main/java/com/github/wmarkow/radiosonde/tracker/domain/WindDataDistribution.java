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

    public ZonedDateTime getDateTime()
    {
        return zonedDateTime;
    }

    public WindData getWindData( int altitude )
    {
        if( altitude < 0 )
        {
            throw new IllegalArgumentException(
                String.format( "Passed altitude %s m must not be negative", altitude ) );
        }
        
        // TODO: implement an interpolation function
        int smallestAltitudeDelta = Integer.MAX_VALUE;
        WindData result = null;
        for(WindData windData : windData)
        {
            int altitudeDelta = Math.abs( windData.getAltitude() - altitude );
            if(altitudeDelta < smallestAltitudeDelta)
            {
                result = windData;
                smallestAltitudeDelta = altitudeDelta;
            }
        }
        
        return result;
    }

    public List< WindData > getRawWindData()
    {
        return windData;
    }

    @Override
    public String toString()
    {
        return "WindDataDistribution [latitude=" + latitude + ", longitude=" + longitude + ", zonedDateTime="
            + zonedDateTime + ", windData=" + windData + "]";
    }
}
