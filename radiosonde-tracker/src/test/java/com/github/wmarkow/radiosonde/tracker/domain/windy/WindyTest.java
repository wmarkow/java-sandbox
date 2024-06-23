package com.github.wmarkow.radiosonde.tracker.domain.windy;

import org.junit.Test;

public class WindyTest
{

    @Test
    public void test()
    {
        WindyApi api = new WindyApi();
        api.getWindData( 0, 0 );
    }
}
