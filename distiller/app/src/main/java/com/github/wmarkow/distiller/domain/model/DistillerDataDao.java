package com.github.wmarkow.distiller.domain.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface DistillerDataDao {
    @Insert
    void insert(DistillerDataEntity entity);

    @Query("SELECT * FROM DistillerData WHERE utcTimestampMillis > (:lastTimestampInMillis) ORDER BY utcTimestampMillis ASC LIMIT 500")
    List<DistillerDataEntity> loadLatestByTimestamp(long lastTimestampInMillis);

    @Query("SELECT * FROM DistillerData WHERE utcTimestampMillis BETWEEN  (:startTimestampInMillis) AND (:endTimestampInMillis) ORDER BY utcTimestampMillis ASC")
    List<DistillerDataEntity> loadByTimestampRange(long startTimestampInMillis, long endTimestampInMillis);
}
