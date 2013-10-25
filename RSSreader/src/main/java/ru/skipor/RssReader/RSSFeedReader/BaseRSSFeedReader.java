package ru.skipor.RssReader.RSSFeedReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vladimir Skipor on 10/17/13.
 * Email: vladimirskipor@gmail.com
 */
public abstract class BaseRSSFeedReader implements RSSFeedReader {

    // names of the XML tags
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";

    final String feedUrl;

    protected BaseRSSFeedReader(String feedUrl) throws RSSFeedReaderException {
        try {
            this.feedUrl = feedUrl;
            new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RSSFeedReaderException(e);
        }

    }

    protected InputStream getInputStream() throws RSSFeedReaderException {

        try {

            String feedString = downloadUrl(feedUrl);
            return new ByteArrayInputStream(feedString.getBytes());

        } catch (IOException e) {
            throw new RSSFeedReaderException(e);
        }
    }


    private String downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = conn.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String resultString = bufferedReader.readLine();


            while (resultString != null) {
                stringBuilder.append(resultString);
                resultString = bufferedReader.readLine();
            }


        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }

        return stringBuilder.toString();

    }
}