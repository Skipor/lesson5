package ru.skipor.RssReader;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import ru.skipor.RssReader.FeedsDatabase.FeedsDatabaseHelper;
import ru.skipor.RssReader.RSSFeedReader.RSSFeed;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReader;
import ru.skipor.RssReader.RSSFeedReader.RSSFeedReaderException;
import ru.skipor.RssReader.RSSFeedReader.RSSItem;
import ru.skipor.RssReader.RSSFeedReader.SAXRSSReader;

/**
 * Created by Vladimir Skipor on 11/9/13.
 * Email: vladimirskipor@gmail.com
 */
public class RSSUpdateService extends IntentService {
    private static final String TAG = "RSSUpdateService";

    public static final String ACTION_UPDATE_ALL = "Update all";
    public static final String ACTION_UPDATE_ONE = "Update one";
    public static final String EXTRA_FEED_URL = "Feed url";
    public static final String EXTRA_INFORM_ABOUT_UPDATE = "Inform about update";
    RSSFeedReader feedReader;
    FeedsDatabaseHelper myDatabaseHelper = FeedsDatabaseHelper.getInstance(this);


    Handler uiHandler = new Handler(Looper.getMainLooper());

    private static final String UPDATE_MESSAGE = "Feeds are up to date";

//    private static String[] feedsLinks = {"http://stackoverflow.com/feeds/tag/android", "http://lenta.ru/rss/articles", "http://bash.im/rss/"};
//    private static RSSFeed[] feeds = new RSSFeed[feedsLinks.length];

//    private static String stackFeed = "http://stackoverflow.com/feeds/tag/android";
//    private static String lentaFeed = "http://lenta.ru/rss/articles";
//    private static String bashFeed = "http://bash.im/rss/";


    public RSSUpdateService() {
        super("ru.skipor.RssReader.RSSUpdateService");
        feedReader = new SAXRSSReader();
    }

    @Override
    public void onCreate() {
        myDatabaseHelper.open();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        myDatabaseHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String actionType = intent.getAction();

        Log.i(TAG, "service intent handle");
        Log.i(TAG, "Action type is: " + actionType);

        if (ACTION_UPDATE_ALL.equals(actionType)) {
            updateAll();
            if (intent.getBooleanExtra(EXTRA_INFORM_ABOUT_UPDATE, false)) {
                makeToast(UPDATE_MESSAGE);

            }

        } else if (ACTION_UPDATE_ONE.equals(actionType)) {

            updateOne(intent.getStringExtra(EXTRA_FEED_URL));
            if (intent.getBooleanExtra(EXTRA_INFORM_ABOUT_UPDATE, false)) { // use resultReceiver

            }
        }


    }

    private void updateAll() {
        Cursor cursor = null;
        try {
            cursor = myDatabaseHelper.fetchAllFeeds();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){

                String coulumnName = cursor.getColumnName(cursor.getColumnIndexOrThrow(FeedsDatabaseHelper.KEY_URL));
                Log.d(TAG, coulumnName + " " + FeedsDatabaseHelper.KEY_URL);
                String feedURL = cursor.getString(cursor.getColumnIndex(FeedsDatabaseHelper.KEY_URL));
                updateOne(feedURL);

                cursor.moveToNext();

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private void updateOne(String feedURL) {
        myDatabaseHelper.createOrRecreateFeedTable(feedURL);
        try {
            RSSFeed uptodateFeed = feedReader.parse(feedURL);
            for(RSSItem item : uptodateFeed.getItemList()) {
                myDatabaseHelper.createTitle(feedURL, item.getTitle(), item.getDescription());
            }
            makeToast("Feed " + feedURL + " is up to date");
        } catch (RSSFeedReaderException e) {
            Log.e(TAG, "Error " + feedURL, e);
            myDatabaseHelper.createTitle(feedURL, "", "Sorry, can't get RSSfeed from " + feedURL + "\n Check URL, connection or try later ");
        }



    }

    private boolean makeToast(String message) {
        return uiHandler.post(new ToastRunnable(message, Toast.LENGTH_LONG));
    }

    class ToastRunnable implements Runnable {
        private final String toastText;
        private final int toastDuration;

        ToastRunnable(String toastText, int toastDuration) {
            this.toastDuration = toastDuration;
            this.toastText = toastText;
        }

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), toastText, toastDuration).show();

        }
    }
}
