package com.github.wmarkow.distiller.ui;

import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;

import java.util.List;

public interface DistillerDataViewIf {
    void showNewDistillerData(List<DistillerDataEntity> newData);
    void setXRangeResolutionSeconds(int resolutionInSeconds);
    void setXRangeVisibleSpanSeconds(int spanInSeconds);
}
