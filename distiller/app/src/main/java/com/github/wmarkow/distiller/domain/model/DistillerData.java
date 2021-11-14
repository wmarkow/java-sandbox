package com.github.wmarkow.distiller.domain.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class DistillerData {
    public String deviceAddress;
    public long deviceUpTimeMillis;
    private long utcTimestampMillis;
    public double coldWaterTemp;
    public double hotWaterTemp;
    public double headerTemp;
    public double boilerTemp;
    public double waterRpm;

    public DistillerData() {
        utcTimestampMillis = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public long getUtcTimestampMillis() {
        return utcTimestampMillis;
    }

}
