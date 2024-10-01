package com.github.wmarkow.call.sign.finder.domain.morse;

public class MorseDitLengthCalculator
{
    private MorseAlphabet morseAlphabet;
    
    public MorseDitLengthCalculator(MorseAlphabet morseAlphabet)
    {
        this.morseAlphabet = morseAlphabet;
    }
    
    public int calculate(String text)
    {
        int result = 0;
        for( int index = 0; index < text.length(); index++ )
        {
            char charAtIndex = text.charAt( index );
            MorseCharacter mc = morseAlphabet.getByLatinCharacter( String.valueOf( charAtIndex ) );
            result += mc.getMorseDitLength();
        }
        
        return result;
    }
}
