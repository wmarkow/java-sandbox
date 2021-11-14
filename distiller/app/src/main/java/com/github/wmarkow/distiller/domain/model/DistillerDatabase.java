package com.github.wmarkow.distiller.domain.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DistillerDataEntity.class}, version = 1)
public abstract class DistillerDatabase extends RoomDatabase {
    public abstract DistillerDataDao distillerDataDao();
}
