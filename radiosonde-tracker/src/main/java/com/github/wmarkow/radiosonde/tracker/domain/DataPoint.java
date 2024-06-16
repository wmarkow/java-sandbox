package com.github.wmarkow.radiosonde.tracker.domain;

import java.time.ZonedDateTime;

/***
 * Represents a single point of data
 * 
 * @author wmarkowski
 */
public class DataPoint
{
    public ZonedDateTime utcDateTime;
    public double latitude;
    public double longitude;
    public int course;
    public int speed_km_h;
    public int altitude_m;
    public double climbing_m_s;
}
