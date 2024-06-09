package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CsvReader
{

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    public ArrayList< DataPoint > readDataPoints( String filePath )
    {
        ArrayList< DataPoint > result = new ArrayList<>();

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new FileReader( filePath ) );

            String line = null;
            while( (line = reader.readLine()) != null )
            {
                System.out.println( line );
                DataPoint dp = parseDataPoint( line );
                if( dp != null )
                {
                    result.add( dp );
                }
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if( reader != null )
                {
                    reader.close();
                }
            }
            catch( IOException e )
            {
                // silently fail
            }
        }

        return result;
    }

    // KMP56-14;V3742166;2024-06-08 19:59:56;52.5792;16.6496;318;19;123;Clb=-3.0m/s t=19.5C h=64.3%
    // p=994.5hPa 405.100 MHz Type=RS41-SGP ser=V3742166 Radiosonde
    private DataPoint parseDataPoint( String line )
    {
        if( line == null )
        {
            return null;
        }
        if( line.isEmpty() )
        {
            return null;
        }

        String[] splits = line.split( ";" );
        DataPoint result = new DataPoint();

        try
        {
            result.utcDateTime = parseDateTime( splits[ 2 ] );
            result.latitude = parseDouble( splits[ 3 ] );
            result.longitude = parseDouble( splits[ 4 ] );
            result.course = parseInteger( splits[ 5 ] );
            result.speed_km_h = parseInteger( splits[ 6 ] );
            result.altitude_m = parseInteger( splits[ 7 ] );
            result.climbing_m_s = parseClimbing( splits[ 8 ] );
        }
        catch( Exception ex )
        {
            return null;
        }

        return result;
    }

    private ZonedDateTime parseDateTime( String text )
    {
        LocalDateTime ldt = LocalDateTime.parse( text, formatter );
        return ZonedDateTime.of( ldt, ZoneId.of( "UTC" ) );
    }

    private double parseDouble( String text )
    {
        return Double.valueOf( text );
    }

    private int parseInteger( String text )
    {
        return Integer.valueOf( text );
    }

    /***
     * Clb=-3.0m/s t=19.5C h=64.3% p=994.5hPa 405.100 MHz Type=RS41-SGP ser=V3742166 Radiosonde
     * 
     * @param description
     * @return
     */
    private double parseClimbing( String description )
    {
        String[] split = description.split( " " );
        String climbString = split[ 0 ].trim().toLowerCase();
        climbString = climbString.replace( "clb=", "" );
        climbString = climbString.replace( "m/s", "" );

        return Double.parseDouble( climbString );
    }
}
