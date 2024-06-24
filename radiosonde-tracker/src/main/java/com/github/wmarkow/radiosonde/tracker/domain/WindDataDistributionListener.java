package com.github.wmarkow.radiosonde.tracker.domain;

public interface WindDataDistributionListener
{
    void onNewWindDataDistributionAvailable(WindDataDistribution windDataDistribution);
}
