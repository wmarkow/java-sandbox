package com.github.wmarkow.radiosonde.tracker.domain.radiosondy;

import java.time.ZonedDateTime;

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
