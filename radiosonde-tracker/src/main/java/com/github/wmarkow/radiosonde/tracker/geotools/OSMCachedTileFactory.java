package com.github.wmarkow.radiosonde.tracker.geotools;

import java.io.File;

import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.ZoomLevel;
import org.geotools.tile.impl.osm.OSMTileFactory;
import org.geotools.tile.util.CachedImageLoader;

public class OSMCachedTileFactory extends OSMTileFactory
{
    private OSMCachedImageLoader cachedImageLoader;

    public OSMCachedTileFactory( File tileCacheDirectory )
    {
        cachedImageLoader = new OSMCachedImageLoader(tileCacheDirectory);
    }

    @Override
    public Tile findTileAtCoordinate( double lon, double lat, ZoomLevel zoomLevel, TileService service )
    {
        Tile result = super.findTileAtCoordinate( lon, lat, zoomLevel, service );
        cachedImageLoader.setTileService( service );
        result.setImageLoader( cachedImageLoader );
        
        return result;
    }

    @Override
    public Tile findRightNeighbour( Tile tile, TileService service )
    {
        Tile result = super.findRightNeighbour( tile, service );
        cachedImageLoader.setTileService( service );
        result.setImageLoader( cachedImageLoader );

        return result;
    }

    @Override
    public Tile findLowerNeighbour( Tile tile, TileService service )
    {
        Tile result = super.findLowerNeighbour( tile, service );
        cachedImageLoader.setTileService( service );
        result.setImageLoader( cachedImageLoader );

        return result;
    }
}
