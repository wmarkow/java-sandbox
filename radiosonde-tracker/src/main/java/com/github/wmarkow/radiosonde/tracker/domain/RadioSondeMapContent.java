package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.Color;
import java.awt.geom.Point2D;
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

import com.github.wmarkow.radiosonde.tracker.domain.radiosondy.CsvReader;
import com.github.wmarkow.radiosonde.tracker.domain.radiosondy.DataPoint;
import com.github.wmarkow.radiosonde.tracker.domain.radiosondy.DataSet;

public class RadioSondeMapContent extends MapContent
{
    private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    private DataSet dataSet = null;
    private FeatureLayer predictionLayer = null;

    public RadioSondeMapContent() throws SchemaException
    {
        super();
    }

    public void prepareLayers() throws SchemaException
    {
        String baseURL = "https://tile.openstreetmap.org/";
        TileService service = new OSMService( "OSM", baseURL );

        addLayer( new TileLayer( service ) );
        addLayer( prepareSondeMapLayer() );
        recalculatePrediction(100);
    }

    private FeatureLayer prepareSondeMapLayer() throws SchemaException
    {
        DataSet dataSet = getDataSet();

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        for( int q = 0; q < dataSet.getDataPoints().size(); q += 10 )
        {
            DataPoint dp = dataSet.getDataPoints().get( q );

            /* Longitude (= x coord) first ! */
            Point point = geometryFactory.createPoint( new Coordinate( dp.longitude, dp.latitude ) );
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder( TYPE );
            featureBuilder.add( point );
            SimpleFeature feature = featureBuilder.buildFeature( null );
            featureList.add( feature );
        }

        ListFeatureCollection lfc = new ListFeatureCollection( TYPE, featureList );
        FeatureLayer layer = new FeatureLayer( lfc, createPointStyle() );

        return layer;
    }

    public void recalculatePrediction( int notOlderThanMinutes )
    {
        DataSet dataSet = getDataSet();

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );

        ArrayList< DataPoint > dataPoints = dataSet.getDataPointsYoungerThan( notOlderThanMinutes * 60 );

        for( DataPoint dp : dataPoints )
        {
            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }

            // Sonde is falling down. Let's predict its landing point.
            double time_h = dp.altitude_m / Math.abs( dp.climbing_m_s ) / 3600.0;
            double distance_km = dp.speed_km_h * time_h;
            double distance_m = distance_km * 1000;

            calc.setStartingGeographicPoint( dp.longitude, dp.latitude );
            calc.setDirection( dp.course, distance_m );
            Point2D dstPoint = calc.getDestinationGeographicPoint();

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
        ArrayList< DataPoint > dataPoints =
            csvReader.readDataPoints( "src/main/resources/sondes/V3742166/V3742166.csv" );
        // ArrayList< DataPoint > dataPoints =
        // csvReader.readDataPoints( "src/main/resources/sondes/V3742167/V3742167.csv" );

        return new DataSet( dataPoints );
    }
}
