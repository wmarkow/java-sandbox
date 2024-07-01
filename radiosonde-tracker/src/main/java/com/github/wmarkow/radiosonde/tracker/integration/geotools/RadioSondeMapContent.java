package com.github.wmarkow.radiosonde.tracker.integration.geotools;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.geotools.map.MapLayerListListener;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.JMapPane;
import org.geotools.tile.TileService;
import org.geotools.tile.util.TileLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.radiosonde.tracker.domain.AdvancedLandingPointPredictor;
import com.github.wmarkow.radiosonde.tracker.domain.ClimbingDataSet;
import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;
import com.github.wmarkow.radiosonde.tracker.domain.DataSet;
import com.github.wmarkow.radiosonde.tracker.domain.LandingPoint;
import com.github.wmarkow.radiosonde.tracker.domain.RadioSondeTrackerContext;
import com.github.wmarkow.radiosonde.tracker.domain.WindDataByClimbingDataSetProvider;
import com.github.wmarkow.radiosonde.tracker.domain.BasicLandingPointPredictor;

public class RadioSondeMapContent extends MapContent
{
    private static final Logger LOGGER = LoggerFactory.getLogger( RadioSondeMapContent.class );

    private DataSet fullDataSet = null;
    private DataSet sondeDataSet = null;
    private FeatureLayer sondeLayer = null;
    private FeatureLayer predictionLayer = null;
    private FeatureLayer advancedPredictionLayer = null;
    private PointStyleFactory pointStyleFactory = new PointStyleFactory();
    private ZonedDateTime avgLandingTimeRedPrediction;
    private ZonedDateTime avgLandingTimeYellowPrediction;

    private JMapPane jMapPane = null;

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
        int dq = 10;
        for( int q = 0; q < dataPoints.size(); q += dq )
        {
            if( dataPoints.size() - q < 100 )
            {
                // Render all latest 100 points
                dq = 1;
            }
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

        BasicLandingPointPredictor calc = new BasicLandingPointPredictor();

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

        AdvancedLandingPointPredictor calc =
            new AdvancedLandingPointPredictor( RadioSondeTrackerContext.windyDotComWindDataProvider );

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

            if( landingPoint == null )
            {
                continue;
            }

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

        forceRepaint();
    }

    public void addMapLayerListListener( MapLayerListListener listener )
    {
        super.addMapLayerListListener( listener );

        if( listener instanceof JMapPane )
        {
            this.jMapPane = (JMapPane)listener;
        }
    }

    /***
     * Forces to repaint the map.
     * <p>
     * It is useful when i.e. a lot of layer operations (remove or add) have been done on the map in a short
     * time; every layer operation triggers map repaint but there is only one repaint worker (and repaint
     * takes a bit of time), so not all layers may be painted correctly.
     * <p>
     * Call this method after you finish layer operations, so the map can be repainted correctly.
     */
    public void forceRepaint()
    {
        if( jMapPane == null )
        {
            return;
        }

        try
        {
            Method maxProtectedMethod = JMapPane.class.getSuperclass().getDeclaredMethod( "onImageMoved" );
            maxProtectedMethod.setAccessible( true );
            maxProtectedMethod.invoke( jMapPane );
        }
        catch( NoSuchMethodException | SecurityException e )
        {
            LOGGER.error( e.getMessage(), e );
        }
        catch( IllegalAccessException e )
        {
            LOGGER.error( e.getMessage(), e );
        }
        catch( IllegalArgumentException e )
        {
            LOGGER.error( e.getMessage(), e );
        }
        catch( InvocationTargetException e )
        {
            LOGGER.error( e.getMessage(), e );
        }
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
