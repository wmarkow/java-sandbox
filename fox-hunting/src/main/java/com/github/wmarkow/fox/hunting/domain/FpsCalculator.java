package com.github.wmarkow.fox.hunting.domain;

public class FpsCalculator
{
    private boolean isBatchTransmission = false;
    private long beginningOfBatchTransmissionMillis = 0;
    private long lastReceivedFrameMillis = 0;
    private long framesCount = 0;

    public void frameReceived()
    {
        checkForBatchTransmission();

        if( isBatchTransmission == false )
        {
            // we have beginning of the batch transmission
            isBatchTransmission = true;
            beginningOfBatchTransmissionMillis = System.currentTimeMillis();
            framesCount = 0;
        }

        lastReceivedFrameMillis = System.currentTimeMillis();
        framesCount++;
    }

    public Long getCurrentFps()
    {
        checkForBatchTransmission();
        
        if( framesCount <= 8 )
        {
            return null;
        }

        System.out.println( "####" + framesCount );
        double dt = lastReceivedFrameMillis - beginningOfBatchTransmissionMillis;
        return (long)(1000 * framesCount / dt);
    }

    private void checkForBatchTransmission()
    {
        if( System.currentTimeMillis() - lastReceivedFrameMillis > 100 )
        {
            // last frame was some time ago
            // mark the transmission is over
            isBatchTransmission = false;
        }
    }
}
