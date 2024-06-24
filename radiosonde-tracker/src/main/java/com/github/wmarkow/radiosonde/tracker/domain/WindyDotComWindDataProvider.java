package com.github.wmarkow.radiosonde.tracker.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindyDotComWindDataProvider implements WindDataProvider, WindDataDistributionListener
{
    private final static Logger LOGGER = LoggerFactory.getLogger( WindyDotComWindDataProvider.class );

    private List<WindDataDistribution> distributions = new ArrayList<>();
    
    @Override
    public Double getWindSpeed( double latitude, double longitude, double altitude )
    {
        return null;
    }

    @Override
    public Double getWindCourse( double latitude, double longitude, double altitude )
    {
        return null;
    }

    @Override
    public void onNewWindDataDistributionAvailable( WindDataDistribution windDataDistribution )
    {
        // TODO new data with wind data distribution are ready
        LOGGER.info( String.format( "onNewWindDataDistributionAvailable() called with %s",
            windDataDistribution.toString() ) );

    }

}
