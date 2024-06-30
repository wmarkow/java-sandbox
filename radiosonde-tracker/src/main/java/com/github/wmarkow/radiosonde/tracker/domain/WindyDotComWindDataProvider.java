package com.github.wmarkow.radiosonde.tracker.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.radiosonde.tracker.gui.WindyCefBrowser;

public class WindyDotComWindDataProvider implements WindDataProvider, WindDataDistributionListener
{
    private final static Logger LOGGER = LoggerFactory.getLogger( WindyDotComWindDataProvider.class );

    private List< WindDataDistribution > distributions = new ArrayList<>();

    private WindyCefBrowser windyCefBrowser;

    public void setWindyCefBrowser( WindyCefBrowser windyCefBrowser )
    {
        this.windyCefBrowser = windyCefBrowser;
    }

    @Override
    public Double getWindSpeed( double latitude, double longitude, double altitude )
    {
        WindDataDistribution distribution = getClosestWindDataDistribution();
        if( distribution == null )
        {
            fireWindDataRequest( latitude, longitude );

            return null;
        }

        return distribution.getWindData( (int)altitude ).getSpeed_km_h();
    }

    @Override
    public Double getWindCourse( double latitude, double longitude, double altitude )
    {
        WindDataDistribution distribution = getClosestWindDataDistribution();
        if( distribution == null )
        {
            fireWindDataRequest( latitude, longitude );

            return null;
        }

        return distribution.getWindData( (int)altitude ).getCourse();
    }

    @Override
    public void onNewWindDataDistributionAvailable( WindDataDistribution windDataDistribution )
    {
        LOGGER.info( String.format( "onNewWindDataDistributionAvailable() called with %s",
            windDataDistribution.toString() ) );

        distributions.add( windDataDistribution );
    }

    private WindDataDistribution getClosestWindDataDistribution()
    {
        if( distributions.size() == 0 )
        {
            return null;
        }

        // FIXME: do not return the first one. Find the latest and the nearest one (or something like this);
        // if not found then trigger data download.
        return distributions.get( 0 );
    }

    private void fireWindDataRequest( double latitude, double longitude )
    {
        if( windyCefBrowser == null )
        {
            LOGGER.error(
                "Windy CEF Browser is null. Wind data will not be fetched and WindyDotComWindDataProvider may not work as expected." );
            return;
        }

        windyCefBrowser.fireWindDataRequest( latitude, longitude );
    }

}
