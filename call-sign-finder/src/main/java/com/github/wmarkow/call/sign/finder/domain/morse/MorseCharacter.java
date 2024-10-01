package com.github.wmarkow.call.sign.finder.domain.morse;

public class MorseCharacter
{
    public final char DIT = '*';
    public final char DAH = '-';

    private String latinCharacter;
    private String morseEncoding;

    public MorseCharacter( String latinCharacter, String morseEncoding )
    {
        this.latinCharacter = latinCharacter;
        this.morseEncoding = morseEncoding;
    }

    public String getLatinCharacter()
    {
        return latinCharacter;
    }

    public String getMorseEncoding()
    {
        return morseEncoding;
    }

    public int getMorseDitLength()
    {
        int result = 0;
        for( int index = 0; index < morseEncoding.length(); index++ )
        {
            char charAtIndex = morseEncoding.charAt( index );
            if( DIT == charAtIndex )
            {
                result += 1;
            }
            else
            {
                result += 3;
            }
        }

        return result;
    }

    @Override
    public String toString()
    {
        return "MorseCharacter [" + latinCharacter + " " + morseEncoding + "]";
    }

}
