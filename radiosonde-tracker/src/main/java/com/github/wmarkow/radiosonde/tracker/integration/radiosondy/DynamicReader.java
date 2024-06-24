package com.github.wmarkow.radiosonde.tracker.integration.radiosondy;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.github.wmarkow.radiosonde.tracker.domain.DataPoint;

/***
 * Reads dynamic sonde data directly from HTTP.
 * 
 * @author wmarkowski
 */
public class DynamicReader
{
    private final static String URL_TEMPLATE = "https://radiosondy.info/dyn/get_sondeinfo.php?sondenumber=%s";
    private final static String USER_AGENT =
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:127.0) Gecko/20100101 Firefox/127.0";

    public ArrayList< DataPoint > readDataPoints( String sondeName )
    {
        // create http client
        String url = String.format( URL_TEMPLATE, sondeName );
        Connection connection = Jsoup.connect( url );
        connection.userAgent( USER_AGENT );
        try
        {
            // get HTML document
            Document doc = connection.get();

            // read data points
            DynamicHtmlReader reader = new DynamicHtmlReader();
            return reader.readDataPoints( doc );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
