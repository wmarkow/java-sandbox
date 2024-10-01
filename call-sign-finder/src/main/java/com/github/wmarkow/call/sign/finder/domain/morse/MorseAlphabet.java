package com.github.wmarkow.call.sign.finder.domain.morse;

import java.util.HashMap;

public class MorseAlphabet
{
    private HashMap< String, MorseCharacter > alphabet = new HashMap< String, MorseCharacter >();

    public void add( MorseCharacter morseCharacter )
    {
        if( alphabet.containsKey( morseCharacter.getLatinCharacter() ) )
        {
            throw new IllegalArgumentException( String.format(
                "Morse alphabet already contains latin letter %s", morseCharacter.getLatinCharacter() ) );
        }

        alphabet.put( morseCharacter.getLatinCharacter(), morseCharacter );
    }

    public MorseCharacter getByLatinCharacter( String latinCharacter )
    {
        if( !alphabet.containsKey( latinCharacter ) )
        {
            throw new IllegalArgumentException(
                String.format( "Morse alphabet doesn't contain a latin character of %s", latinCharacter ) );
        }
        return alphabet.get( latinCharacter );
    }
}
