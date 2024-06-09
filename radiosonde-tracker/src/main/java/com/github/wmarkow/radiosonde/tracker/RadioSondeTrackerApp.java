package com.github.wmarkow.radiosonde.tracker;

import org.geotools.feature.SchemaException;

import com.github.wmarkow.radiosonde.tracker.domain.RadioSondeMapContent;
import com.github.wmarkow.radiosonde.tracker.gui.RadioSondeTrackerFrame;

public class RadioSondeTrackerApp
{
    public static void main( String args[] ) throws SchemaException
    {
        RadioSondeMapContent map = new RadioSondeMapContent();
        map.prepareLayers();
        RadioSondeTrackerFrame.showMap( map );
    }
}
