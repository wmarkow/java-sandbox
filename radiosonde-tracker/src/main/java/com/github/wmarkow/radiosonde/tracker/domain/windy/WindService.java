package com.github.wmarkow.radiosonde.tracker.domain.windy;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WindService
{
    @POST("/api/point-forecast/v2")
    Call<WindResponse> getWindData(@Body WindRequest request );
}
