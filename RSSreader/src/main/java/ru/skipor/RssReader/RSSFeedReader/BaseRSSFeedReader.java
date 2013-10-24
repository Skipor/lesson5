package ru.skipor.RssReader.RSSFeedReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
            DownloadWebpageTask task = new DownloadWebpageTask();
            task.execute(feedUrl);
            String feedString = task.get();
            return new ByteArrayInputStream(feedString.getBytes());
        } catch (InterruptedException e) {
            throw new RSSFeedReaderException(e);
        } catch (ExecutionException e) {
            throw new RSSFeedReaderException(e);
        }
    }
}