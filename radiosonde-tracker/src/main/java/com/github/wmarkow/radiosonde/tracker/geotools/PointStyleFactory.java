package com.github.wmarkow.radiosonde.tracker.geotools;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;

public class PointStyleFactory
{
    private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    public Style create( int radius, Color borderColor, Color fillColor )
    {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
            styleFactory.createStroke( filterFactory.literal( borderColor ), filterFactory.literal( 2 ) ) );

        mark.setFill( styleFactory.createFill( filterFactory.literal( fillColor ) ) );

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add( mark );
        gr.setSize( filterFactory.literal( radius ) );

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
