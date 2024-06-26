package com.github.wmarkow.radiosonde.tracker.integration.windy;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.github.wmarkow.radiosonde.tracker.domain.WindData;
import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistribution;

public class WindDataDistributionProvider
{

    /***
     * Parses sounding JSON file and creates wind data distribution.
     * 
     * @param soundingJson
     * @param latitude
     * @param longitude
     * @return
     */
    public WindDataDistribution parse( String soundingJson, double latitude, double longitude )
    {
        SoundingDataProvider sdp = new SoundingDataProvider();
        SoundingData sd = sdp.parse( soundingJson );

        // the value under index 0 is 6 hours ago
        // the value under index 1 is 3 hours ago
        // the value under index 2 is "current" time
        final int index = 2;
        long epochMillis = sd.data.hours[ index ];

        Instant i = Instant.ofEpochMilli( epochMillis );
        ZonedDateTime dateTime = ZonedDateTime.ofInstant( i, ZoneId.of( "UTC" ) );

        WindDataDistribution result = new WindDataDistribution( latitude, longitude, dateTime );
        result.addWindData( getWindData( 0, sd.data.windUSurface[ index ], sd.data.windVSurface[ index ] ) );
        result.addWindData( getWindData( 100, sd.data.windU1000h[ index ], sd.data.windU1000h[ index ] ) );
        result.addWindData( getWindData( 600, sd.data.windU950h[ index ], sd.data.windV950h[ index ] ) );
        result.addWindData( getWindData( 750, sd.data.windU925h[ index ], sd.data.windV925h[ index ] ) );
        result.addWindData( getWindData( 900, sd.data.windU900h[ index ], sd.data.windV900h[ index ] ) );
        result.addWindData( getWindData( 1500, sd.data.windU850h[ index ], sd.data.windV850h[ index ] ) );
        result.addWindData( getWindData( 2000, sd.data.windU800h[ index ], sd.data.windV800h[ index ] ) );
        result.addWindData( getWindData( 3000, sd.data.windU700h[ index ], sd.data.windV700h[ index ] ) );
        result.addWindData( getWindData( 4200, sd.data.windU600h[ index ], sd.data.windV600h[ index ] ) );
        result.addWindData( getWindData( 5500, sd.data.windU500h[ index ], sd.data.windV500h[ index ] ) );
        result.addWindData( getWindData( 7000, sd.data.windU400h[ index ], sd.data.windV400h[ index ] ) );
        result.addWindData( getWindData( 9000, sd.data.windU300h[ index ], sd.data.windV300h[ index ] ) );
        result.addWindData( getWindData( 10000, sd.data.windU250h[ index ], sd.data.windV250h[ index ] ) );
        result.addWindData( getWindData( 11700, sd.data.windU200h[ index ], sd.data.windV200h[ index ] ) );
        result.addWindData( getWindData( 13500, sd.data.windU150h[ index ], sd.data.windV150h[ index ] ) );

        return result;
    }

    private WindData getWindData( int altitude, double speedU, double speedV )
    {
        double speed_kt = Math.sqrt( speedU * speedU + speedV * speedV );
        double speed_km_h = 1.852 * speed_kt;
        double course = Math.toDegrees( Math.atan2( speedU, speedV ));
        if(course < 0.0 )
        {
            course += 360.0;
        }
        
        return new WindData(altitude, speed_km_h, course);
    }
}
