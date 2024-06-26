package com.github.wmarkow.radiosonde.tracker.domain;

public class WindData
{
    private int altitude;
    private double speed_km_h;
    private double course;

    public WindData(int altitude, double speed_km_h, double course)
    {
        this.altitude = altitude;
        this.speed_km_h = speed_km_h;
        this.course = course;
    }

    public int getAltitude()
    {
        return altitude;
    }

    public double getSpeed_km_h()
    {
        return speed_km_h;
    }

    public double getCourse()
    {
        return course;
    }
}
