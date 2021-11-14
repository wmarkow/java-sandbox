package com.github.wmarkow.distiller.domain.model;

import androidx.room.Dao;
import androidx.room.Insert;

import java.util.List;

@Dao
public interface DistillerDataDao {
    @Insert
    void insert(DistillerDataEntity entity);
}
