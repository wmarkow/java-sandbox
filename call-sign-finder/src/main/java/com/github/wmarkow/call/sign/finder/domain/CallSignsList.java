package com.github.wmarkow.call.sign.finder.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.github.wmarkow.call.sign.finder.domain.morse.MorseDitLengthCalculator;

public class CallSignsList
{
    private List< CallSign > list = new ArrayList<>();

    public CallSignsList()
    {

    }

    public CallSignsList( List< CallSign > list )
    {
        this.list = list;
    }

    public void add( CallSign callSign )
    {
        list.add( callSign );
    }

    public List< CallSign > getList()
    {
        return list;
    }

    public void updateMorseDitLength( MorseDitLengthCalculator calculator )
    {
        for( CallSign callSign : list )
        {
            int length = calculator.calculate( callSign.getFullSign() );

            callSign.setMorseDitLength( length );
        }
    }

    public CallSignsList getByRegexMatch( String regex )
    {
        List< CallSign > resultList = new ArrayList< CallSign >();

        for( CallSign callSign : list )
        {
            if( callSign.getFullSign().matches( regex ) )
            {
                resultList.add( callSign );
            }
        }

        return new CallSignsList( resultList );
    }
    
    public CallSignsList getByRegexUnMatch( String regex )
    {
        List< CallSign > resultList = new ArrayList< CallSign >();

        for( CallSign callSign : list )
        {
            if( !callSign.getFullSign().matches( regex ) )
            {
                resultList.add( callSign );
            }
        }

        return new CallSignsList( resultList );
    }

    public void sortByMorseDitLengthAsc()
    {
        list.sort( new CallSignByMorsDitLengthComparator() );
    }

    private class CallSignByMorsDitLengthComparator implements Comparator< CallSign >
    {

        @Override
        public int compare( CallSign o1, CallSign o2 )
        {
            if( o1.getMorseDitLength() < o2.getMorseDitLength() )
            {
                return -1;
            }

            if( o1.getMorseDitLength() > o2.getMorseDitLength() )
            {
                return 1;
            }

            return 0;
        }

    }
}
