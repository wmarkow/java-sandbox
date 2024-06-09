package com.github.wmarkow.radiosonde.tracker;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.geotools.brewer.styling.builder.StrokeBuilder;
import org.geotools.data.DataUtilities;
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
import org.geotools.swing.JMapFrame;
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

public class RadioSondeTrackerApp
{
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    public static void main( String args[] ) throws SchemaException
    {
        String baseURL = "https://tile.openstreetmap.org/";
        TileService service = new OSMService( "OSM", baseURL );
        MapContent map = new MapContent();
        map.addLayer( new TileLayer( service ) );
        map.addLayer( prepareSondeMapLayer() );
        map.addLayer( prepareSondePredictionMapLayer() );

        JMapFrame.showMap( map );
    }

    public static FeatureLayer prepareSondeMapLayer() throws SchemaException
    {
        CsvReader csvReader = new CsvReader();
        ArrayList< DataPoint > dataPoints =
            csvReader.readDataPoints( "src/main/resources/sondes/V3742166/V3742166.csv" );
//        ArrayList< DataPoint > dataPoints =
//          csvReader.readDataPoints( "src/main/resources/sondes/V3742167/V3742167.csv" );
        

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        for( int q = 0; q < dataPoints.size(); q+=10 )
        {
            DataPoint dp = dataPoints.get( q );

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

    public static FeatureLayer prepareSondePredictionMapLayer() throws SchemaException
    {
        CsvReader csvReader = new CsvReader();
        ArrayList< DataPoint > dataPoints =
            csvReader.readDataPoints( "src/main/resources/sondes/V3742166/V3742166.csv" );
//        ArrayList< DataPoint > dataPoints =
//            csvReader.readDataPoints( "src/main/resources/sondes/V3742167/V3742167.csv" );

        final SimpleFeatureType TYPE = createFeatureType();

        List< SimpleFeature > featureList = new ArrayList< SimpleFeature >();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        GeodeticCalculator calc = new GeodeticCalculator( DefaultGeographicCRS.WGS84 );

        for( int q = 0; q < dataPoints.size(); q++ )
        {
            DataPoint dp = dataPoints.get( q );

            if( dp.climbing_m_s >= 0 )
            {
                continue;
            }
            if(dp.altitude_m > 1500)
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

        ListFeatureCollection lfc = new ListFeatureCollection( TYPE, featureList );
        FeatureLayer layer = new FeatureLayer( lfc, createPredictionPointStyle() );

        return layer;
    }

    private static SimpleFeatureType createFeatureType()
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
    private static Style createPointStyle()
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

    private static Style createPredictionPointStyle()
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
}
