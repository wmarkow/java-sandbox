package com.github.wmarkow.call.sign.finder.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.call.sign.finder.domain.CallSign;
import com.github.wmarkow.call.sign.finder.domain.CallSignsList;
import com.github.wmarkow.call.sign.finder.domain.CallSignsListReader;
import com.github.wmarkow.call.sign.finder.domain.morse.MorseAlphabet;
import com.github.wmarkow.call.sign.finder.domain.morse.MorseAlphabetReader;
import com.github.wmarkow.call.sign.finder.domain.morse.MorseDitLengthCalculator;

public class CallSignFinder
{
    private final static Logger LOGGER = LoggerFactory.getLogger( CallSignFinder.class );

    public static void main( String args[] )
    {
        MorseAlphabetReader mar = new MorseAlphabetReader();
        MorseAlphabet morseAlphabet = mar.readFromFile( "src/main/resources/morse.txt" );
        
        CallSignsListReader reader = new CallSignsListReader();
        CallSignsList signs = reader.readFromFile( "SP3", "src/main/resources/unikalne.txt" );
        signs.updateMorseDitLength( new MorseDitLengthCalculator(morseAlphabet) );
        signs.sortByMorseDitLengthAsc();
        
        
        CallSignsList narrow = signs.getByRegexUnMatch( "^SP3K.+" ); // throw way when starting with SP3K
        narrow = narrow.getByRegexUnMatch( "^SP3P.+" ); // throw way when starting with SP3P
        narrow = narrow.getByRegexUnMatch( "^SP3Y.+" ); // throw way when starting with SP3Y
        narrow = narrow.getByRegexUnMatch( "^SP3Z.+" ); // throw way when starting with SP3Z
        narrow = narrow.getByRegexUnMatch( ".*V.*" ); // throw away V misunderstanding in polish Wioletta
        narrow = narrow.getByRegexUnMatch( ".*X.*" ); // throw away X misunderstanding in polish Ksawery
        narrow = narrow.getByRegexUnMatch( ".*F.*" ); // throw away F fokstrot hard to pronounce
        narrow = narrow.getByRegexUnMatch( ".*N.*" ); // throw away N nowember hard to pronounce
        narrow = narrow.getByRegexUnMatch( ".*U.*" ); // throw away U juniform hard to pronounce
        narrow = narrow.getByRegexMatch( "^.+(A|J|K|M|O|Q|T|U|V|W|X|Y)$" ); // accept when ending with Morse dah
        
        LOGGER.info(String.format( "Found %s call signs", narrow.getList().size() ));
        int q = 0;
        for( CallSign callSign : narrow.getList() )
        {
            q++;
            
            LOGGER.info( String.format( "%s. Call sign is %s dit-length is %s", q, callSign.getFullSign(), callSign.getMorseDitLength() ) );
        }
    }
}
