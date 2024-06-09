package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.image.io.ImageIOExt;
import org.geotools.tile.ImageLoader;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.util.logging.Logging;

public class OSMCachedImageLoader implements ImageLoader
{
    private static final Logger LOGGER = Logging.getLogger( OSMCachedImageLoader.class );

    private File cacheDirectory;
    private TileService tileService;

    public OSMCachedImageLoader( File cacheDirectory )
    {
        this.cacheDirectory = cacheDirectory;
    }
    
    public void setTileService(TileService tileService)
    {
        this.tileService = tileService;
    }

    @Override
    public BufferedImage loadImageTileImage( Tile tile ) throws IOException
    {
        BufferedImage img = null;

        File imgFile = new File( this.cacheDirectory, tile.getId() + ".png" );
        if( imgFile.exists() )
        {
            if( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine(
                    "Found image in cache for '" + tile.getId() + "' at " + imgFile.getAbsolutePath() );
            }
            img = ImageIOExt.readBufferedImage( imgFile );

        }
        else
        {
            if( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "Not found in cache '" + tile.getId() + "'. Loading from " + tile.getUrl() );
            }

            // load the image with the help of tile service
            img =  tileService.loadImageTileImage( tile );
            
            ImageIO.write( img, "png", imgFile );
            if( LOGGER.isLoggable( Level.FINE ) )
            {
                LOGGER.fine( "Wrote to cache " + imgFile.getAbsolutePath() );
            }
        }
        return img;
    }

}
