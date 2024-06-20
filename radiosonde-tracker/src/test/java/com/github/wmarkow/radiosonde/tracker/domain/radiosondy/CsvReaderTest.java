package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import static org.junit.Assert.assertEquals;

import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;

public class CsvReaderTest
{
    private CsvReader subject;

    @Before
    public void init()
    {
        subject = new CsvReader();
    }

    @Test
    public void testForNoExceptions()
    {
        subject.readDataPoints( "src/main/resources/sondes/archive/Poznan/V3742166_Baborowo/V3742166.csv" );
    }

    @Test
    public void testForCount()
    {
        ArrayList< DataPoint > dataPoints =
            subject.readDataPoints( "src/main/resources/sondes/archive/Poznan/V3742166_Baborowo/V3742166.csv" );

        assertEquals( 9459, dataPoints.size() );
    }
    
    @Test
    public void testForCorrectData()
    {
        ArrayList< DataPoint > dataPoints =
            subject.readDataPoints( "src/main/resources/sondes/archive/Poznan/V3742166_Baborowo/V3742166.csv" );

        assertEquals( ZoneId.of( "UTC" ), dataPoints.get( 0 ).utcDateTime.getZone());
        assertEquals( 2024, dataPoints.get( 0 ).utcDateTime.getYear());
        assertEquals( Month.JUNE, dataPoints.get( 0 ).utcDateTime.getMonth());
        assertEquals( 8, dataPoints.get( 0 ).utcDateTime.getDayOfMonth());
        assertEquals( 19, dataPoints.get( 0 ).utcDateTime.getHour());
        assertEquals( 59, dataPoints.get( 0 ).utcDateTime.getMinute());
        assertEquals( 56, dataPoints.get( 0 ).utcDateTime.getSecond());
        
        assertEquals( 52.5792, dataPoints.get( 0 ).latitude, 0.00001);
        assertEquals( 16.6496, dataPoints.get( 0 ).longitude, 0.00001);
        
        assertEquals( 318, dataPoints.get( 0 ).course);
        
        assertEquals( 19, dataPoints.get( 0 ).speed_km_h);
        
        assertEquals( 123, dataPoints.get( 0 ).altitude_m);
        
        assertEquals( -3.0, dataPoints.get( 0 ).climbing_m_s, 0.01);
    }
}
