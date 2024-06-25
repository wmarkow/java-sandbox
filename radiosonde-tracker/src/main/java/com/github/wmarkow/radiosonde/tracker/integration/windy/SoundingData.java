package com.github.wmarkow.radiosonde.tracker.integration.windy;

import com.google.gson.annotations.SerializedName;

class SoundingData
{
    @SerializedName( value = "header" )
    public Header header;

    @SerializedName( value = "data" )
    public Data data;
}
