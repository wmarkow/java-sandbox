//******************************************************************
//                                                                 
//  ThreeSigmaAverageValueCalculator.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.fox.hunting.domain;

public class ThreeSigmaAverageValueCalculator extends AverageValueCalculator
{

    @Override
    public int calculate( int currentValue )
    {
        int avg = super.calculate( currentValue );

        // return (int)Math.abs( 1000 * Math.log10( avg / 390.0 ) );

        // calculate standard deviation
        int summ = 0;
        for( int index = 0; index < probes.length; index++ )
        {
            summ += (probes[ index ] - avg) * (probes[ index ] - avg);
        }
        int sigma = (int)Math.sqrt( summ / (probes.length - 1) );

        // calculate average again but discarding samples by the "three sigma" rule

        summ = 0;
        int count = 0;
        for( int index = 0; index < probes.length; index++ )
        {
            int diff = Math.abs( probes[ index ] - avg );
            if( diff < 3 * sigma )
            {
                summ += probes[ index ];
                count++;
            }
            else
            {
                // discard the sample, use avg instead
                // summ += avg;
                System.out.println( "Discard sample " + probes[ index ] + ". Avg is " + avg );
            }
        }

        // return summ / probes.length;
        return summ / count;
    }
}
