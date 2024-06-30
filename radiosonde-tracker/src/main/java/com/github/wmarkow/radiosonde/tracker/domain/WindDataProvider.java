package com.github.wmarkow.radiosonde.tracker.domain;

public interface WindDataProvider
{
    /***
     * Gets the wind speed.
     * 
     * @param latitude
     * @param longitude
     * @param altitude
     * @return wind speed in km/h
     */
    public Double getWindSpeed( double latitude, double longitude, double altitude );

    /***
     * Gets the wind course measured as an angle from NORTH towards EAST direction.
     * 
     * @param latitude
     * @param longitude
     * @param altitude
     * @return
     */
    public Double getWindCourse( double latitude, double longitude, double altitude );
}
