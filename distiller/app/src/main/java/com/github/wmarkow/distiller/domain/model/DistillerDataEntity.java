package com.github.wmarkow.distiller.domain.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity(tableName = "DistillerData")
public class DistillerDataEntity {
    @PrimaryKey
    public long utcTimestampMillis;

    @ColumnInfo(name = "device_up_time_millis")
    public long deviceUpTimeMillis;

    @ColumnInfo(name = "cold_water_temp")
    public Double coldWaterTemp;

    @ColumnInfo(name = "hot_water_temp")
    public Double hotWaterTemp;

    @ColumnInfo(name = "header_temp")
    public Double headerTemp;

    @ColumnInfo(name = "boiler_temp")
    public Double boilerTemp;

    @ColumnInfo(name = "water_rpm")
    public double waterRpm;

    public DistillerDataEntity() {
        utcTimestampMillis = ZonedDateTime.now(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }
}
