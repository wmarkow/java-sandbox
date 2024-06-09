package com.github.wmarkow.radiosonde.tracker.domain;

import java.io.File;

import org.geotools.tile.TileFactory;
import org.geotools.tile.impl.osm.OSMService;

public class OSMCachedService extends OSMService
{
    private TileFactory tileFactory;

    public OSMCachedService( String name, String baseUrl, File tileCacheDirectory )
    {
        super( name, baseUrl );
        tileFactory = new OSMCachedTileFactory(tileCacheDirectory);
    }

    @Override
    public TileFactory getTileFactory()
    {
        return tileFactory;
    }
}
