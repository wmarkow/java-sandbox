package com.github.wmarkow.call.sign.finder.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CallSignsListReader
{

    public CallSignsList readFromFile(String prefix, String filePath)
    {
        CallSignsList result = new CallSignsList();
        
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new FileReader( filePath ) );

            String line = null;
            while( (line = reader.readLine()) != null )
            {
                if(line.trim().isEmpty())
                {
                    continue;
                }
                
                result.add(new CallSign(prefix, line.trim()));
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
