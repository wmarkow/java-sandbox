//******************************************************************
//                                                                 
//  AverageCalculator.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.fox.hunting.domain;

public class AverageValueCalculator implements AverageValueCalculatorIf
{
    protected int[] probes = new int[ 32 ];
    
    public AverageValueCalculator()
    {
        for( int index = 0; index < probes.length; index++ )
        {
            probes[ index ] = 0;
        }
    }

    @Override
    public int calculate(int currentValue)
    {
        int summ = 0;

        for( int index = 0; index < probes.length - 1; index++ )
        {
            probes[ index ] = probes[ index + 1 ];
            summ += probes[ index ];
        }

        probes[ probes.length - 1 ] = currentValue;
        summ += currentValue;

        return summ / probes.length;
    }
}


