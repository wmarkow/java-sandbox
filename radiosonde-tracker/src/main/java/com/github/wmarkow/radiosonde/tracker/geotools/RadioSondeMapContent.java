package com.github.wmarkow.radiosonde.tracker.geotools;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.TileService;
import org.geotools.tile.util.TileLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.github.wmarkow.radiosonde.tracker.domain.AdvancedLandingPointPredictor;
import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;
import com.github.wmarkow.radiosonde.tracker.domain.DataSet;
import com.github.wmarkow.radiosonde.tracker.domain.LandingPointPredictor;
import com.github.wmarkow.radiosonde.tracker.domain.radiosondy.CsvReader;

public class RadioSondeMapContent extends MapContent
{
    private DataSet fullDataSet = null;
    private DataSet sondeDataSet = null;
    private FeatureLayer sondeLayer = null;
    private FeatureLayer predictionLayer = null;
    private FeatureLayer advancedPredictionLayer = null;
    private PointStyleFactory pointStyleFactory = new PointStyleFactory();

    public RadioSondeMapContent() throws SchemaException
    {
        super();
    }

    public void prepareLayers() throws SchemaException
    {
        String baseURL = "https://tile.openstreetmap.org/";
        TileService service = new OSMCachedService( "OSM", baseURL, getTileCacheDirectory() );

        addLayer( new TileLayer( service ) );
        recalculateSondeData( 0 );
        recalculatePrediction( 100 );
        recalculateAdvancedPrediction( 100 );
    }

    public void recalculateSondeData( int olderThanMinutes )
    {
        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        ArrayList< DataPoint > dataPoints =
            getFullDataSet().getEntriesOlderThanTheYoungestButWithMinAge( olderThanMinutes * 60 );
        sondeDataSet = new DataSet( dataPoints );
        for( int q = 0; q < dataPoints.size(); q += 10 )
        {
            DataPoint dp = dataPoints.get( q );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint( new Coordinate( dp.longitude, dp.latitude ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );
        }

        // remove old sonde layer
        if( sondeLayer != null )
        {
            removeLayer( sondeLayer );
        }

        // create new sonde layer
        ListFeatureCollection lfc = new ListFeatureCollection( TYPE, featureList );
        sondeLayer = new FeatureLayer( lfc, pointStyleFactory.create( 10, Color.BLUE, Color.CYAN ) );

        // add sonde layer
        addLayer( sondeLayer );
    }

    public void recalculatePrediction( int notOlderThanMinutes )
    {
        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        LandingPointPredictor calc = new LandingPointPredictor();

        ArrayList< DataPoint > dataPoints =
            getSondeDataSet().getEntriesOlderThanTheYoungestButWithMaxAge( notOlderThanMinutes * 60 );
        for( DataPoint dp : dataPoints )
        {
            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }

            // Sonde is falling down. Let's predict its landing point.
            Point2D dstPoint = calc.predict( dp );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint( new Coordinate( dstPoint.getX(), dstPoint.getY() ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );
        }

        // remove old prediction layer
        if( predictionLayer != null )
        {
            removeLayer( predictionLayer );
        }

        // create new predicition layer
        ListFeatureCollection lfc = new ListFeatureCollection( TYPE, featureList );
        predictionLayer = new FeatureLayer( lfc, pointStyleFactory.create( 10, Color.RED, Color.RED ) );

        // add predicition layer
        addLayer( predictionLayer );
    }

    public void recalculateAdvancedPrediction( int notOlderThanMinutes )
    {
        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // LandingPointPredictor calc = new LandingPointPredictor();
        AdvancedLandingPointPredictor calc = new AdvancedLandingPointPredictor( getFullDataSet() );

        ArrayList< DataPoint > dataPoints =
            getSondeDataSet().getEntriesOlderThanTheYoungestButWithMaxAge( notOlderThanMinutes * 60 );
        for( DataPoint dp : dataPoints )
        {
            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }

            // Sonde is falling down. Let's predict its landing point.
            Point2D dstPoint = calc.predict( dp );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint( new Coordinate( dstPoint.getX(), dstPoint.getY() ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );
        }

        // remove old prediction layer
        if( advancedPredictionLayer != null )
        {
            removeLayer( advancedPredictionLayer );
        }

        // create new advanced predicition layer
        ListFeatureCollection lfc = new ListFeatureCollection( TYPE, featureList );
        advancedPredictionLayer =
            new FeatureLayer( lfc, pointStyleFactory.create( 10, Color.YELLOW, Color.YELLOW ) );

        // add advanced predicition layer
        addLayer( advancedPredictionLayer );
    }

    private SimpleFeatureType createFeatureType()
    {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName( "Location" );
        builder.setCRS( DefaultGeographicCRS.WGS84 ); // <- Coordinate reference system

        // add attributes in order
        builder.add( "the_geom", Point.class );

        // build the type
        final SimpleFeatureType LOCATION = builder.buildFeatureType();

        return LOCATION;
    }

    private DataSet getFullDataSet()
    {
        if( fullDataSet == null )
        {
            fullDataSet = readDataSet();
        }

        return fullDataSet;
    }

    private DataSet getSondeDataSet()
    {
        if( sondeDataSet == null )
        {
            sondeDataSet = new DataSet( getFullDataSet().getDataPoints() );
        }

        return sondeDataSet;
    }

    private DataSet readDataSet()
    {
        CsvReader csvReader = new CsvReader();
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3742166/V3742166.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3742167/V3742167.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3731074/V3731074.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3731069/V3731069.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2410903/V2410903.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V5240965/V5240965.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V5241124/V5241124.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1940765/V1940765.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2411341/V2411341.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2350521/V2350521.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1010198/V1010198.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1020968/V1020968.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2350507/V2350507.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1021069/V1021069.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1010246/V1010246.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1010268/V1010268.csv" );
        ArrayList< DataPoint > dataPoints =
            csvReader.readDataPoints( "src/main/resources/sondes/V3640890/V3640890.csv" );
        

        return new DataSet( dataPoints );
    }

    private File getTileCacheDirectory()
    {
        String userHome = System.getProperty( "user.home" );
        File cacheDirectory = new File( userHome + "/.radiosonde-tracker/" );
        cacheDirectory.mkdirs();

        return cacheDirectory;
    }
}
