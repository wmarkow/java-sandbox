package com.github.wmarkow.geotools.quickstart;

import java.io.File;
import java.util.logging.Logger;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * GeoTools Quickstart demo application. Opens a shapefile and displays its contents on the screen in a map
 * frame
 */
public class Quickstart
{
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger( Quickstart.class );

    public static void main( String[] args ) throws Exception
    {
        // display a data store file chooser dialog for shapefiles
        LOGGER.info( "Quickstart" );
        LOGGER.config( "Welcome Developers" );
        LOGGER
            .info( "java.util.logging.config.file=" + System.getProperty( "java.util.logging.config.file" ) );

        File file = new File( "src/main/resources/ne_110m_admin_0_countries/ne_110m_admin_0_countries.shp" );

        FileDataStore store = FileDataStoreFinder.getDataStore( file );
        SimpleFeatureSource featureSource = store.getFeatureSource();

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle( "Quickstart" );

        Style style = SLD.createSimpleStyle( featureSource.getSchema() );
        Layer layer = new FeatureLayer( featureSource, style );
        map.addLayer( layer );

        // Now display the map
        JMapFrame.showMap( map );
    }
}
