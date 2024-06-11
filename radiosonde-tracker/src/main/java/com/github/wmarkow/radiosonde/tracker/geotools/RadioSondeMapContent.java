package com.github.wmarkow.radiosonde.tracker.geotools;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.osm.OSMService;
import org.geotools.tile.util.TileLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import com.github.wmarkow.radiosonde.tracker.domain.AdvancedLandingPointPredictor;
import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;
import com.github.wmarkow.radiosonde.tracker.domain.DataSet;
import com.github.wmarkow.radiosonde.tracker.domain.LandingPointPredictor;
import com.github.wmarkow.radiosonde.tracker.domain.radiosondy.CsvReader;

public class RadioSondeMapContent extends MapContent
{
    private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    private DataSet dataSet = null;
    private FeatureLayer sondeLayer = null;
    private FeatureLayer predictionLayer = null;
    private FeatureLayer advancedPredictionLayer = null;

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
        DataSet dataSet = getDataSet();
        dataSet.setReturnDataPointsOlderThanFromLatest( olderThanMinutes * 60 );

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        ArrayList< DataPoint > dataPoints = dataSet.getDataPoints();
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
        sondeLayer = new FeatureLayer( lfc, createPointStyle() );

        // add sonde layer
        addLayer( sondeLayer );
    }

    public void recalculatePrediction( int notOlderThanMinutes )
    {
        DataSet dataSet = getDataSet();

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // LandingPointPredictor calc = new LandingPointPredictor();
        LandingPointPredictor calc = new LandingPointPredictor( );

        ArrayList< DataPoint > dataPoints =
            dataSet.getDataPointsYoungerThanFromLatest( notOlderThanMinutes * 60 );

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
        predictionLayer = new FeatureLayer( lfc, createPredictionPointStyle() );

        // add predicition layer
        addLayer( predictionLayer );
    }

    public void recalculateAdvancedPrediction( int notOlderThanMinutes )
    {
        DataSet dataSet = getDataSet();

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // LandingPointPredictor calc = new LandingPointPredictor();
        AdvancedLandingPointPredictor calc = new AdvancedLandingPointPredictor( dataSet );

        ArrayList< DataPoint > dataPoints =
            dataSet.getDataPointsYoungerThanFromLatest( notOlderThanMinutes * 60 );

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
        advancedPredictionLayer = new FeatureLayer( lfc, createAdvancedPredictionPointStyle() );

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

    /**
     * Create a Style to draw point features as circles with blue outlines and cyan fill
     */
    private Style createPointStyle()
    {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
            styleFactory.createStroke( filterFactory.literal( Color.BLUE ), filterFactory.literal( 2 ) ) );

        mark.setFill( styleFactory.createFill( filterFactory.literal( Color.CYAN ) ) );

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add( mark );
        gr.setSize( filterFactory.literal( 10 ) );

        /*
         * Setting the geometryPropertyName arg to null signals that we want to draw the default geomettry of
         * features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer( gr, null );

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add( sym );
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle( new Rule[]
        { rule } );
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add( fts );

        return style;
    }

    private Style createPredictionPointStyle()
    {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
            styleFactory.createStroke( filterFactory.literal( Color.RED ), filterFactory.literal( 2 ) ) );

        mark.setFill( styleFactory.createFill( filterFactory.literal( Color.RED ) ) );

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add( mark );
        gr.setSize( filterFactory.literal( 10 ) );

        /*
         * Setting the geometryPropertyName arg to null signals that we want to draw the default geomettry of
         * features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer( gr, null );

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add( sym );
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle( new Rule[]
        { rule } );
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add( fts );

        return style;
    }
    
    private Style createAdvancedPredictionPointStyle()
    {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
            styleFactory.createStroke( filterFactory.literal( Color.YELLOW ), filterFactory.literal( 2 ) ) );

        mark.setFill( styleFactory.createFill( filterFactory.literal( Color.YELLOW ) ) );

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add( mark );
        gr.setSize( filterFactory.literal( 10 ) );

        /*
         * Setting the geometryPropertyName arg to null signals that we want to draw the default geomettry of
         * features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer( gr, null );

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add( sym );
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle( new Rule[]
        { rule } );
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add( fts );

        return style;
    }

    private DataSet getDataSet()
    {
        if( dataSet == null )
        {
            dataSet = readDataSet();
        }

        return dataSet;
    }

    private DataSet readDataSet()
    {
        CsvReader csvReader = new CsvReader();
//         ArrayList< DataPoint > dataPoints =
//         csvReader.readDataPoints( "src/main/resources/sondes/V3742166/V3742166.csv" );
//         ArrayList< DataPoint > dataPoints =
//         csvReader.readDataPoints( "src/main/resources/sondes/V3742167/V3742167.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3731074/V3731074.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/V3731069/V3731069.csv" );
//        ArrayList< DataPoint > dataPoints =
//          csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2410903/V2410903.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V5240965/V5240965.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V5241124/V5241124.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1940765/V1940765.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2411341/V2411341.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V2350521/V2350521.csv" );
        ArrayList< DataPoint > dataPoints =
            csvReader.readDataPoints( "src/main/resources/sondes/Poznan/V1010198/V1010198.csv" );
        
        
        
        
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
