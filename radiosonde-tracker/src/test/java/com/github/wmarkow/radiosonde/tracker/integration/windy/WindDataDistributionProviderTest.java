package com.github.wmarkow.radiosonde.tracker.integration.windy;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Month;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistribution;

public class WindDataDistributionProviderTest
{
    private WindDataDistributionProvider subject;

    @Before
    public void init()
    {
        subject = new WindDataDistributionProvider();
    }

    @Test
    public void testForNoException() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        subject.parse( jsonString, 0.0, 0.0 );
    }
    
    @Test
    public void testForCorrectCounts() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        WindDataDistribution wdd = subject.parse( jsonString, 0.0, 0.0 );
        
        assertEquals(15, wdd.getRawWindData().size());
    }
    
    @Test
    public void testForCorrectData() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        WindDataDistribution wdd = subject.parse( jsonString, 0.0, 0.0 );
        
        assertEquals(2024, wdd.getDateTime().getYear());
        assertEquals(Month.JUNE, wdd.getDateTime().getMonth());
        assertEquals(24, wdd.getDateTime().getDayOfMonth());
        assertEquals(21, wdd.getDateTime().getHour());
        assertEquals(0, wdd.getDateTime().getMinute());
        assertEquals(0, wdd.getDateTime().getSecond());
        assertEquals(ZoneId.of( "UTC" ), wdd.getDateTime().getZone());
        
        assertEquals(0, wdd.getRawWindData().get( 0 ).getAltitude());
        assertEquals(4.168, wdd.getRawWindData().get( 0 ).getSpeed_km_h(), 0.001);
        assertEquals(264.3, wdd.getRawWindData().get( 0 ).getCourse(), 0.1);
        
        assertEquals(170.7, wdd.getRawWindData().get( 14 ).getCourse(), 0.1);
    }
}
