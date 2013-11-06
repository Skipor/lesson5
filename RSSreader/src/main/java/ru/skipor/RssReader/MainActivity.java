package ru.skipor.RssReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.skipor.RssReader.RSSFeedReader.RSSItem;
import ru.skipor.RssReader.UserInterface.RSSAdapter;

public class MainActivity extends Activity {


    public static final String TAG = "MainActivity";



//    private static String RssFeed = "http://lenta.ru/rss/articles";
    private static String RssFeed = "http://www.thetimes.co.uk/tto/news/rss";
//private static String RssFeed = "http://bash.im/rss/";


    private String XMLoutput;

    //    private RSSFeedReader feedReader;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);


//            feedReader = new DOMRSSReader(RssFeed);
//            List<RSSItem> rssItems = feedReader.parse();

        RSSAdapter rssAdapter = new RSSAdapter(this, RssFeed);
//            rssAdapter.setItems(rssItems);
        listView.setAdapter(rssAdapter);

        rssAdapter.showContent();

        final Context context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RSSAdapter rssAdapter = (RSSAdapter) listView.getAdapter();
                RSSItem item = rssAdapter.getItem(position);
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra(DescriptionActivity.EXTRA_MESSAGE, item.getDescription());
                startActivity(intent);
            }
        });




    }


}
