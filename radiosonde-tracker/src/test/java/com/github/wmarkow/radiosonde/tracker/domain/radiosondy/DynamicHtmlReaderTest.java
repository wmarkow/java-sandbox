package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;

public class DynamicHtmlReaderTest
{
    private DynamicHtmlReader subject;

    @Before
    public void init()
    {
        subject = new DynamicHtmlReader();
    }

    @Test
    public void testForNoExceptions() throws IOException
    {
        File input = new File( "src/main/resources/sondes/dyn/W2041255_dyn.html" );
        Document doc = Jsoup.parse( input, "UTF-8" );

        subject.readDataPoints( doc );
    }

    @Test
    public void testForCount() throws IOException
    {
        File input = new File( "src/main/resources/sondes/dyn/W2041255_dyn.html" );
        Document doc = Jsoup.parse( input, "UTF-8" );

        ArrayList< DataPoint > result = subject.readDataPoints( doc );

        assertEquals( 1000, result.size() );
    }

    @Test
    public void testForCorrectData() throws IOException
    {
        File input = new File( "src/main/resources/sondes/dyn/W2041255_dyn.html" );
        Document doc = Jsoup.parse( input, "UTF-8" );

        ArrayList< DataPoint > result = subject.readDataPoints( doc );

        assertEquals( 30384, result.get( 0 ).altitude_m );
        assertEquals( -24.0, result.get( 0 ).climbing_m_s, 0.01 );
        assertEquals( 260, result.get( 0 ).course );
        assertEquals( 52.1229, result.get( 0 ).latitude, 0.00001 );
        assertEquals( 14.5547, result.get( 0 ).longitude, 0.00001 );
        assertEquals( 57, result.get( 0 ).speed_km_h );
        assertEquals( 2024, result.get( 0 ).utcDateTime.getYear() );
        assertEquals( Month.JUNE, result.get( 0 ).utcDateTime.getMonth() );
        assertEquals( 20, result.get( 0 ).utcDateTime.getDayOfMonth() );
        assertEquals( 6, result.get( 0 ).utcDateTime.getHour() );
        assertEquals( 30, result.get( 0 ).utcDateTime.getMinute() );
        assertEquals( 34, result.get( 0 ).utcDateTime.getSecond() );
        assertEquals( "UTC", result.get( 0 ).utcDateTime.getZone().getId() );
    }
}
