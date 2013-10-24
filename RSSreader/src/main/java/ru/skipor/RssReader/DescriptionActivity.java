package ru.skipor.RssReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class DescriptionActivity extends Activity {
    public static final String EXTRA_MESSAGE = "ru.skipor.RssReader.DescriptionActivity";
    public static final String ENCODING_CHARSET = "UTF-8";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        webView = (WebView) findViewById(R.id.item_description);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        webView.loadData(message, "text/html", ENCODING_CHARSET);




    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.description, menu);
//        return true;
//    }
//
//
}
