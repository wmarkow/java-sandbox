package com.github.wmarkow.radiosonde.tracker.geotools;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.time.ZonedDateTime;
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
import com.github.wmarkow.radiosonde.tracker.domain.ClimbingDataSet;
import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;
import com.github.wmarkow.radiosonde.tracker.domain.DataSet;
import com.github.wmarkow.radiosonde.tracker.domain.LandingPoint;
import com.github.wmarkow.radiosonde.tracker.domain.LandingPointPredictor;

public class RadioSondeMapContent extends MapContent
{
    private DataSet fullDataSet = null;
    private DataSet sondeDataSet = null;
    private FeatureLayer sondeLayer = null;
    private FeatureLayer predictionLayer = null;
    private FeatureLayer advancedPredictionLayer = null;
    private PointStyleFactory pointStyleFactory = new PointStyleFactory();
    private ZonedDateTime avgLandingTimeRedPrediction;
    private ZonedDateTime avgLandingTimeYellowPrediction;

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

    public ZonedDateTime getAvgLandingTimeRedPrediction()
    {
        return avgLandingTimeRedPrediction;
    }

    public ZonedDateTime getAvgLandingTimeYellowPrediction()
    {
        return avgLandingTimeYellowPrediction;
    }

    public DataPoint getSondeLatestDataPoint()
    {
        return getSondeDataSet().getYoungestDataPoint();
    }

    public void recalculateSondeData( int olderThanMinutes )
    {
        if( getFullDataSet() == null )
        {
            return;
        }

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

    public void recalculatePrediction( int notOlderThanSeconds )
    {
        if( getSondeDataSet() == null )
        {
            return;
        }

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        LandingPointPredictor calc = new LandingPointPredictor();

        ArrayList< DataPoint > dataPoints =
            getSondeDataSet().getEntriesOlderThanTheYoungestButWithMaxAge( notOlderThanSeconds );
        avgLandingTimeRedPrediction = null;
        for( DataPoint dp : dataPoints )
        {
            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }

            // Sonde is falling down. Let's predict its landing point.
            LandingPoint landingPoint = calc.predict( dp );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint(
                new Coordinate( landingPoint.getLocation().getX(), landingPoint.getLocation().getY() ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );

            // TODO: maybe calculate the average landing time?
            if( avgLandingTimeRedPrediction == null )
            {
                avgLandingTimeRedPrediction = landingPoint.getUtcDateTime();
            }
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

    public void recalculateAdvancedPrediction( int notOlderThanSeconds )
    {
        if( getSondeDataSet() == null )
        {
            return;
        }

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // LandingPointPredictor calc = new LandingPointPredictor();
        ClimbingDataSet climbingDataSet = ClimbingDataSet.valueOf( getFullDataSet() );
        AdvancedLandingPointPredictor calc = new AdvancedLandingPointPredictor( climbingDataSet );

        ArrayList< DataPoint > dataPoints =
            getSondeDataSet().getEntriesOlderThanTheYoungestButWithMaxAge( notOlderThanSeconds );
        avgLandingTimeYellowPrediction = null;
        for( DataPoint dp : dataPoints )
        {
            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }

            // Sonde is falling down. Let's predict its landing point.
            LandingPoint landingPoint = calc.predict( dp );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint(
                new Coordinate( landingPoint.getLocation().getX(), landingPoint.getLocation().getY() ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );

            // TODO: maybe calculate the average landing time?
            if( avgLandingTimeYellowPrediction == null )
            {
                avgLandingTimeYellowPrediction = landingPoint.getUtcDateTime();
            }
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

    public void setFullDataSet( DataSet dataSet )
    {
        this.fullDataSet = dataSet;

        recalculateSondeData( 0 );
        recalculatePrediction( 100 );
        recalculateAdvancedPrediction( 100 );
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
        return fullDataSet;
    }

    public DataSet getSondeDataSet()
    {
        if( sondeDataSet == null && fullDataSet != null )
        {
            sondeDataSet = new DataSet( getFullDataSet().getDataPoints() );
        }

        return sondeDataSet;
    }

    private File getTileCacheDirectory()
    {
        String userHome = System.getProperty( "user.home" );
        File cacheDirectory = new File( userHome + "/.radiosonde-tracker/" );
        cacheDirectory.mkdirs();

        return cacheDirectory;
    }
}
