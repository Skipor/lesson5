package ru.skipor.RssReader.RSSFeedReader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vladimirskipor on 10/17/13.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "DownloadWebpageTask ";

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {

            Log.d(TAG, "Unable to retrieve web page. URL may be invalid.", e);
            return null;
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