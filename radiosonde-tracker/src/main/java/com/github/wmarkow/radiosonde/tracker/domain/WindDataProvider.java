package com.github.wmarkow.radiosonde.tracker.domain;

public interface WindDataProvider
{
    public Double getWindSpeed( double latitude, double longitude, double altitude );

    public Double getWindCourse( double latitude, double longitude, double altitude );
}
