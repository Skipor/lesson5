package ru.skipor.RssReader.RSSFeedReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vladimir Skipor on 10/17/13.
 * Email: vladimirskipor@gmail.com
 */
public class RSSItem {
    static Set<String> itemTags, titleTags, linkTags, descriptionTags, dateTags, feedTags;

    static {
        String[]
                items = {"item", "entry"},
                titles = {"title"},
                links = {"link"},
                descriptions = {"summary", "description", "content"},
                dates = {"pubDate", "published"},
                feeds = {"feed", "channel"};

        itemTags = new HashSet<String>(Arrays.asList(items));
        titleTags = new HashSet<String>(Arrays.asList(titles));
        linkTags = new HashSet<String>(Arrays.asList(links));
        descriptionTags = new HashSet<String>(Arrays.asList(descriptions));
        dateTags = new HashSet<String>(Arrays.asList(dates));
        feedTags = new HashSet<String>(Arrays.asList(feeds));


    }

    static SimpleDateFormat FORMATTER =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    private String title;
    private String link;
    private String description;
    private Date date;
    private String feed;

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public void setLink(String link) {
        this.link = link;

    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date) {
//        try {
//            this.date = FORMATTER.parse(date.trim());
//        } catch (ParseException e) {
//            throw new IllegalArgumentException(e);            //todo: remove comments
//        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RSSItem)) return false;

        RSSItem item = (RSSItem) o;

        if (date != null ? !date.equals(item.date) : item.date != null) return false;
        if (description != null ? !description.equals(item.description) : item.description != null)
            return false;
        if (feed != null ? !feed.equals(item.feed) : item.feed != null) return false;
        if (link != null ? !link.equals(item.link) : item.link != null) return false;
        if (title != null ? !title.equals(item.title) : item.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (feed != null ? feed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "feed" + feed +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
