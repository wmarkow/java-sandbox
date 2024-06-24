package com.github.wmarkow.radiosonde.tracker;

import org.geotools.feature.SchemaException;

import com.github.wmarkow.radiosonde.tracker.gui.RadioSondeTrackerFrame;
import com.github.wmarkow.radiosonde.tracker.integration.geotools.RadioSondeMapContent;

public class RadioSondeTrackerApp
{
    public static void main( String args[] ) throws SchemaException
    {
        RadioSondeMapContent map = new RadioSondeMapContent();
        map.prepareLayers();
        RadioSondeTrackerFrame.showMap( map );
    }
}
