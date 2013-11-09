package ru.skipor.RssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Vladimir Skipor on 11/7/13.
 * Email: vladimirskipor@gmail.com
 */
public class FeedActivity extends Activity {

        private static String stackFeed = "http://stackoverflow.com/feeds/tag/android";
    private static String lentaFeed = "http://lenta.ru/rss/articles";
private static String bashFeed = "http://bash.im/rss/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_feed);
    }

    public void stackOnClick(View view) {
        openFeed(stackFeed);
    }

    private void openFeed(String rssFeed) {
        Intent intent = new Intent(this, TitlesActivity.class);
        intent.putExtra(TitlesActivity.EXTRA_MESSAGE, rssFeed);
        startActivity(intent);
    }

    public void lentaOnClick(View view) {
        openFeed(lentaFeed);
    }

    public void bashOnClick(View view) {
        openFeed(bashFeed);

    }
}