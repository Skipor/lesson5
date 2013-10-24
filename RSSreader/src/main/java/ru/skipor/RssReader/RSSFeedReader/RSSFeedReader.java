package ru.skipor.RssReader.RSSFeedReader;

import java.util.List;

/**
 * Created by Vladimir Skipor on 10/17/13.
 * Email: vladimirskipor@gmail.com
 */
public interface RSSFeedReader {
    List<RSSItem> parse() throws RSSFeedReaderException;
}
