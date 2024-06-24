package com.github.wmarkow.radiosonde.tracker.integration.radiosondy;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;

/***
 * Reads dynamic sonde data directly from HTML document.
 * 
 * @author wmarkowski
 */
public class DynamicHtmlReader
{
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    public ArrayList< DataPoint > readDataPoints( Document doc )
    {
        ArrayList< DataPoint > result = new ArrayList< DataPoint >();

        Elements trElements = doc.select( "fieldset div table > tbody > tr" );
        for( Element trElement : trElements )
        {
            Elements tdElements = trElement.select( "td" );

            DataPoint dp = new DataPoint();
            dp.altitude_m = parseIntFromUnit( tdElements.get( 7 ).text() );
            dp.climbing_m_s = parseDoubleFromUnit( tdElements.get( 8 ).text() );
            dp.course = parseIntFromUnit( tdElements.get( 5 ).text() );
            dp.latitude = parseDoubleFromUnit( tdElements.get( 3 ).text() );
            dp.longitude = parseDoubleFromUnit( tdElements.get( 4 ).text() );
            dp.speed_km_h = parseIntFromUnit( tdElements.get( 6 ).text() );
            dp.utcDateTime = parseDateTime( tdElements.get( 2 ).text() );
            
            result.add( dp );
        }

        return result;
    }

    private int parseIntFromUnit( String textWithUnit )
    {
        String[] split = textWithUnit.split( " " );
        return Integer.valueOf( split[ 0 ] );
    }

    private double parseDoubleFromUnit( String textWithUnit )
    {
        String[] split = textWithUnit.split( " " );
        return Double.valueOf( split[ 0 ] );
    }

    private ZonedDateTime parseDateTime( String text )
    {
        text = text.trim();
        if(text.endsWith( "z" ))
        {
            text = text.substring( 0, text.length() - 1 );
        }
        LocalDateTime ldt = LocalDateTime.parse( text, formatter );
        return ZonedDateTime.of( ldt, ZoneId.of( "UTC" ) );
    }
}
