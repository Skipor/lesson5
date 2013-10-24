package ru.skipor.RssReader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import ru.skipor.RssReader.RSSFeedReader.DOMRSSReader;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReader;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReaderException;
import ru.skipor.RssReader.RSSFeedReader.RSSItem;
import ru.skipor.RssReader.UserInterface.RSSAdapter;

public class MainActivity extends Activity {


    public static final String TAG = "MainActivity";

    private static String RssFeed = "http://www.thetimes.co.uk/tto/news/rss";

    private String XMLoutput;

    //    TextView testTextView;
    private RSSFeedReader feedReader;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);


//        testTextView = (TextView) findViewById(R.id.testTextView);

        try {
            feedReader = new DOMRSSReader(RssFeed);
            List<RSSItem> rssItems = feedReader.parse();

            RSSAdapter rssAdapter = new RSSAdapter(this);
            rssAdapter.setItems(rssItems);
            listView.setAdapter(rssAdapter);

//            StringBuilder stringBuilder = new StringBuilder();
//            for (RSSItem item : rssItems) {
//                stringBuilder.append(item.toString());
//            }
////            testTextView.setText(stringBuilder.toString());

        } catch (RSSFeedReaderException e) {
            e.printStackTrace();
        }


//        DownloadWebpageTask task = new DownloadWebpageTask();
//        task.execute(RssFeed);
//        try {
//            testTextView.setText(task.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


    }


}
