package com.github.wmarkow.call.sign.finder.domain.morse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MorseAlphabetReader
{
    public MorseAlphabet readFromFile( String filePath )
    {
        MorseAlphabet result = new MorseAlphabet();

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new FileReader( filePath ) );

            String line = null;
            while( (line = reader.readLine()) != null )
            {
                String[] split = line.trim().split( " " );
                if( split == null )
                {
                    continue;
                }
                if( split.length != 2 )
                {
                    // something is wrong here
                    throw new IllegalArgumentException( String.format( "Wrong Morse line: %s", line ) );
                }

                String latinCharacter = split[ 0 ];
                String morseEncoding = split[ 1 ];

                result.add( new MorseCharacter( latinCharacter, morseEncoding ) );
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if( reader != null )
                {
                    reader.close();
                }
            }
            catch( IOException e )
            {
                // silently fail
            }
        }

        return result;
    }
}
