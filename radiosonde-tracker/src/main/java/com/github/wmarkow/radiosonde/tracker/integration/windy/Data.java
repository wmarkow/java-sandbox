package com.github.wmarkow.radiosonde.tracker.integration.windy;

import com.google.gson.annotations.SerializedName;

/***
 * Wind speed and direction defined by a two-dimensional vector. The component u defines the speed of a wind
 * blowing from the West towards the East (a negative value therefore implies the opposite direction). The
 * component v similarly defines the speed of a wind blowing from the South towards the North. Wind speed
 * value is in knots.
 * 
 * @author wmarkowski
 */
class Data
{
    @SerializedName( value = "wind_u-surface" )
    public double[] windUSurface;
    @SerializedName( value = "wind_u-1000h" )
    public double[] windU1000h;
    @SerializedName( value = "wind_u-950h" )
    public double[] windU950h;
    @SerializedName( value = "wind_u-925h" )
    public double[] windU925h;
    @SerializedName( value = "wind_u-900h" )
    public double[] windU900h;
    @SerializedName( value = "wind_u-850h" )
    public double[] windU850h;
    @SerializedName( value = "wind_u-800h" )
    public double[] windU800h;
    @SerializedName( value = "wind_u-700h" )
    public double[] windU700h;
    @SerializedName( value = "wind_u-600h" )
    public double[] windU600h;
    @SerializedName( value = "wind_u-500h" )
    public double[] windU500h;
    @SerializedName( value = "wind_u-400h" )
    public double[] windU400h;
    @SerializedName( value = "wind_u-300h" )
    public double[] windU300h;
    @SerializedName( value = "wind_u-250h" )
    public double[] windU250h;
    @SerializedName( value = "wind_u-200h" )
    public double[] windU200h;
    @SerializedName( value = "wind_u-150h" )
    public double[] windU150h;

    @SerializedName( value = "wind_v-surface" )
    public double[] windVSurface;
    @SerializedName( value = "wind_v-1000h" )
    public double[] windV1000h;
    @SerializedName( value = "wind_v-950h" )
    public double[] windV950h;
    @SerializedName( value = "wind_v-925h" )
    public double[] windV925h;
    @SerializedName( value = "wind_v-900h" )
    public double[] windV900h;
    @SerializedName( value = "wind_v-850h" )
    public double[] windV850h;
    @SerializedName( value = "wind_v-800h" )
    public double[] windV800h;
    @SerializedName( value = "wind_v-700h" )
    public double[] windV700h;
    @SerializedName( value = "wind_v-600h" )
    public double[] windV600h;
    @SerializedName( value = "wind_v-500h" )
    public double[] windV500h;
    @SerializedName( value = "wind_v-400h" )
    public double[] windV400h;
    @SerializedName( value = "wind_v-300h" )
    public double[] windV300h;
    @SerializedName( value = "wind_v-250h" )
    public double[] windV250h;
    @SerializedName( value = "wind_v-200h" )
    public double[] windV200h;
    @SerializedName( value = "wind_v-150h" )
    public double[] windV150h;

    @SerializedName( value = "hours" )
    public long[] hours;
}
