package com.github.wmarkow.radiosonde.tracker.domain.windy;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WindyApi
{
    private final static String API_BASE_URL = "https://api.windy.com";

    public void getWindData( double latitude, double longitude )
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder().baseUrl( API_BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() ).client( httpClient.build() ).build();

        WindService service = retrofit.create( WindService.class );

        WindRequest windRequest = new WindRequest();
        windRequest.lat = latitude;
        windRequest.lon = longitude;
        Call< WindResponse > callSync = service.getWindData( windRequest );

        try
        {
            Response< WindResponse > response = callSync.execute();
            WindResponse apiResponse = response.body();
            System.out.println( apiResponse );
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }
}
