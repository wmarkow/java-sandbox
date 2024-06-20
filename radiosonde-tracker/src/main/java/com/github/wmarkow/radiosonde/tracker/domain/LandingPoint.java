package com.github.wmarkow.radiosonde.tracker.domain;

import java.awt.geom.Point2D;
import java.time.ZonedDateTime;

public class LandingPoint
{
    private Point2D location;
    private ZonedDateTime utcDateTime;
    
    public LandingPoint(Point2D location, ZonedDateTime utcDateTime)
    {
        this.location = location;
        this.utcDateTime = utcDateTime;
    }

    public Point2D getLocation()
    {
        return location;
    }

    public ZonedDateTime getUtcDateTime()
    {
        return utcDateTime;
    }
}
