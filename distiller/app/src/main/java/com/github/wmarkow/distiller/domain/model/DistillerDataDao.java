package com.github.wmarkow.distiller.domain.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DistillerDataDao {
    @Insert
    void insert(DistillerDataEntity entity);

    @Query("SELECT * FROM DistillerData WHERE utcTimestampMillis > (:lastTimestampInMillis) ORDER BY utcTimestampMillis ASC LIMIT 200")
    List<DistillerDataEntity> loadLatestByTimestamp(long lastTimestampInMillis);
}
