package com.github.wmarkow.radiosonde.tracker.integration.windy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class SoundingDataProviderTest
{

    private SoundingDataProvider subject;

    @Before
    public void init()
    {
        subject = new SoundingDataProvider();
    }

    @Test
    public void testForNoException() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        subject.parse( jsonString );
    }

    @Test
    public void testForNotNullData() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        SoundingData soundingData = subject.parse( jsonString );

        assertNotNull( soundingData.header );
        assertNotNull( soundingData.header.model );

        assertNotNull( soundingData.data );
        assertNotNull( soundingData.data.hours );

        assertNotNull( soundingData.data.windUSurface );
        assertNotNull( soundingData.data.windU1000h );
        assertNotNull( soundingData.data.windU950h );
        assertNotNull( soundingData.data.windU925h );
        assertNotNull( soundingData.data.windU900h );
        assertNotNull( soundingData.data.windU850h );
        assertNotNull( soundingData.data.windU800h );
        assertNotNull( soundingData.data.windU700h );
        assertNotNull( soundingData.data.windU600h );
        assertNotNull( soundingData.data.windU500h );
        assertNotNull( soundingData.data.windU400h );
        assertNotNull( soundingData.data.windU300h );
        assertNotNull( soundingData.data.windU250h );
        assertNotNull( soundingData.data.windU200h );
        assertNotNull( soundingData.data.windU150h );

        assertNotNull( soundingData.data.windVSurface );
        assertNotNull( soundingData.data.windV1000h );
        assertNotNull( soundingData.data.windV950h );
        assertNotNull( soundingData.data.windV925h );
        assertNotNull( soundingData.data.windV900h );
        assertNotNull( soundingData.data.windV850h );
        assertNotNull( soundingData.data.windV800h );
        assertNotNull( soundingData.data.windV700h );
        assertNotNull( soundingData.data.windV600h );
        assertNotNull( soundingData.data.windV500h );
        assertNotNull( soundingData.data.windV400h );
        assertNotNull( soundingData.data.windV300h );
        assertNotNull( soundingData.data.windV250h );
        assertNotNull( soundingData.data.windV200h );
        assertNotNull( soundingData.data.windV150h );
    }

    @Test
    public void testForCorrectCounts() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        SoundingData soundingData = subject.parse( jsonString );

        assertEquals( 43, soundingData.data.hours.length );

        assertEquals( 43, soundingData.data.windUSurface.length );
        assertEquals( 43, soundingData.data.windU1000h.length );
        assertEquals( 43, soundingData.data.windU950h.length );
        assertEquals( 43, soundingData.data.windU925h.length );
        assertEquals( 43, soundingData.data.windU900h.length );
        assertEquals( 43, soundingData.data.windU850h.length );
        assertEquals( 43, soundingData.data.windU800h.length );
        assertEquals( 43, soundingData.data.windU700h.length );
        assertEquals( 43, soundingData.data.windU600h.length );
        assertEquals( 43, soundingData.data.windU500h.length );
        assertEquals( 43, soundingData.data.windU400h.length );
        assertEquals( 43, soundingData.data.windU300h.length );
        assertEquals( 43, soundingData.data.windU250h.length );
        assertEquals( 43, soundingData.data.windU200h.length );
        assertEquals( 43, soundingData.data.windU150h.length );

        assertEquals( 43, soundingData.data.windVSurface.length );
        assertEquals( 43, soundingData.data.windV1000h.length );
        assertEquals( 43, soundingData.data.windV950h.length );
        assertEquals( 43, soundingData.data.windV925h.length );
        assertEquals( 43, soundingData.data.windV900h.length );
        assertEquals( 43, soundingData.data.windV850h.length );
        assertEquals( 43, soundingData.data.windV800h.length );
        assertEquals( 43, soundingData.data.windV700h.length );
        assertEquals( 43, soundingData.data.windV600h.length );
        assertEquals( 43, soundingData.data.windV500h.length );
        assertEquals( 43, soundingData.data.windV400h.length );
        assertEquals( 43, soundingData.data.windV300h.length );
        assertEquals( 43, soundingData.data.windV250h.length );
        assertEquals( 43, soundingData.data.windV200h.length );
        assertEquals( 43, soundingData.data.windV150h.length );
    }

    @Test
    public void testForCorrectData() throws IOException
    {
        File file = new File( "src/main/resources/windy.com/sounding_data.json" );
        byte[] bytes = Files.readAllBytes( file.toPath() );
        String jsonString = new String( bytes );

        SoundingData soundingData = subject.parse( jsonString );

        assertEquals( "ECMWF-HRES", soundingData.header.model );

        assertEquals( 1719241200000L, soundingData.data.hours[ 0 ] );

        assertEquals( -0.01, soundingData.data.windUSurface[ 0 ], 0.001 );
        assertEquals( 0.03, soundingData.data.windU1000h[ 0 ], 0.001 );
        assertEquals( 0.09, soundingData.data.windU950h[ 0 ], 0.001 );
        assertEquals( 0.13, soundingData.data.windU925h[ 0 ], 0.001 );
        assertEquals( 0.14, soundingData.data.windU900h[ 0 ], 0.001 );
        assertEquals( 0.08, soundingData.data.windU850h[ 0 ], 0.001 );
        assertEquals( -0.16, soundingData.data.windU800h[ 0 ], 0.001 );
        assertEquals( -4.34, soundingData.data.windU700h[ 0 ], 0.001 );
        assertEquals( -4.23, soundingData.data.windU600h[ 0 ], 0.001 );
        assertEquals( -0.31, soundingData.data.windU500h[ 0 ], 0.001 );
        assertEquals( -0.35, soundingData.data.windU400h[ 0 ], 0.001 );
        assertEquals( 2.16, soundingData.data.windU300h[ 0 ], 0.001 );
        assertEquals( -0.02, soundingData.data.windU250h[ 0 ], 0.001 );
        assertEquals( -2.81, soundingData.data.windU200h[ 0 ], 0.001 );
        assertEquals( 0.71, soundingData.data.windU150h[ 0 ], 0.001 );

        assertEquals( -2.35, soundingData.data.windVSurface[ 0 ], 0.001 );
        assertEquals( -2.84, soundingData.data.windV1000h[ 0 ], 0.001 );
        assertEquals( -3.3, soundingData.data.windV950h[ 0 ], 0.001 );
        assertEquals( -3.33, soundingData.data.windV925h[ 0 ], 0.001 );
        assertEquals( -3.38, soundingData.data.windV900h[ 0 ], 0.001 );
        assertEquals( -3.18, soundingData.data.windV850h[ 0 ], 0.001 );
        assertEquals( -2.27, soundingData.data.windV800h[ 0 ], 0.001 );
        assertEquals( -5.27, soundingData.data.windV700h[ 0 ], 0.001 );
        assertEquals( -6.1, soundingData.data.windV600h[ 0 ], 0.001 );
        assertEquals( -5.03, soundingData.data.windV500h[ 0 ], 0.001 );
        assertEquals( -1.73, soundingData.data.windV400h[ 0 ], 0.001 );
        assertEquals( -2.7, soundingData.data.windV300h[ 0 ], 0.001 );
        assertEquals( -5.64, soundingData.data.windV250h[ 0 ], 0.001 );
        assertEquals( -11.89, soundingData.data.windV200h[ 0 ], 0.001 );
        assertEquals( -2.6, soundingData.data.windV150h[ 0 ], 0.001 );
    }
}
