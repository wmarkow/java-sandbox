package com.github.wmarkow.fox.hunting.domain;

public class EventsCalculator
{
    private double startMillis = 0;
    private double count = 0;

    public void event()
    {
        long now = System.currentTimeMillis();
        if( now - startMillis > 10000 )
        {
            reset();
        }

        count++;
    }

    public double calculate()
    {
        return 1000.0 * count / (System.currentTimeMillis() - startMillis);
    }

    public void reset()
    {
        startMillis = System.currentTimeMillis();
        count = 0.5 * count;
    }
}
