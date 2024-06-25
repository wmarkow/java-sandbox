package com.github.wmarkow.radiosonde.tracker.integration.windy;

import com.google.gson.Gson;

public class SoundingDataProvider
{
    /***
     * Parses sounding data JSON file and creates sounding data intermediate object.
     * 
     * @param soundingJson
     * @return a SoundingData object
     */
    SoundingData parse( String soundingJson )
    {
        Gson gson = new Gson();

        return gson.fromJson( soundingJson, SoundingData.class );
    }
}
