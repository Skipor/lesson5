package ru.skipor.RssReader.RSSFeedReader;

/**
 * Created by Vladimir Skipor on 11/7/13.
 * Email: vladimirskipor@gmail.com
 */

import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class SAXRSSReader implements RSSFeedReader {
    public static final String CHARSET_REGEX = "encoding=\"(?+)\"";
    public static final String TAG = "SAXRSSReader";
    private String feedURL;

    public SAXRSSReader(String feedURL) {
        this.feedURL = feedURL;
    }


    @Override
    public List<RSSItem> parse() throws RSSFeedReaderException {
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(feedURL);
        HttpResponse response;
        List<RSSItem> rssItems = null;
        
        try {
            response = httpclient.execute(httpget);
            Log.i(TAG, "Connection status: " + response.getStatusLine().toString());


            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String xml = EntityUtils.toString(entity);
//                String xml = EntityUtils.toString(entity, "utf-8");
                Log.d(TAG, "Encoding can be founded:" + getEncondingFromEntity(entity));
                RSSHandler rssHandler = new RSSHandler();
                Xml.parse(xml, rssHandler);
                rssItems = rssHandler.getItemList();
            } else {

                throw new RSSFeedReaderException("Null Entity in response");
            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rssItems;
    }


//    private String getEncodingCharset(String feedDump) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new StringReader(feedDump));
//        String firstLine = bufferedReader.readLine();
//        Matcher charsetMatcher = Pattern.compile()
//
//
//    }


    private String getEncondingFromEntity(HttpEntity entity){
        if(entity.getContentType()!=null){
            //Content-Type: text/xml; charset=ISO-8859-1
            //Content-Type: text/xml; charset=UTF-8
            for(String str : entity.getContentType().getValue().split(";")){
                if(str.toLowerCase().contains("charset")){
                    return str.toLowerCase().replace("charset=","").replace(";","").replace(" ","");
                }
            }
        }
        return null;
    }


}

class RSSHandler extends DefaultHandler {
    public static final String TAG = "RSSHandler";
    private String channel = null;
    private boolean channelOn = false;
    private StringBuilder elementValueBuilder;
    private Boolean elementOn = false;
    private RSSItem rssItem = new RSSItem();

    private List<RSSItem> itemList = new ArrayList<RSSItem>();

    public List<RSSItem> getItemList() {
        return itemList;
    }

    /**
     * This will be called when the tags of the XML starts.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementOn = true;
        elementValueBuilder = new StringBuilder();

        if (RSSItem.feedTags.contains(localName)) {
            channelOn = true;
        }

        if (RSSItem.itemTags.contains(localName)) {
            rssItem = new RSSItem();
        }

        Log.d(TAG, localName + " tag is opened");

    }

    /**
     * This will be called when the tags of the XML end.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elementOn = false;
        /**
         * Sets the values after retrieving the values from the XML tags
         * */
        Log.d(TAG, localName + " tag is closed with value " + elementValueBuilder.toString());


         if (RSSItem.itemTags.contains(localName)) {
            itemList.add(rssItem);
            Log.d(TAG, "Item " + rssItem.toString() + " added");
        } else if (RSSItem.titleTags.contains(localName)) {
            if (channelOn) {
                channelOn = false;
                channel = elementValueBuilder.toString();
                rssItem = new RSSItem(); /// chanel fields are in current rssItem

                Log.d("TAG", "Channel name is: " + channel);
            } else {
                rssItem.setTitle(elementValueBuilder.toString());
            }
        } else if (RSSItem.descriptionTags.contains(localName)) {
            rssItem.setDescription(elementValueBuilder.toString());
        } else if (RSSItem.linkTags.contains(localName)) {
            rssItem.setLink(elementValueBuilder.toString());
        } else if (RSSItem.dateTags.contains(localName)) {
            rssItem.setDate(elementValueBuilder.toString());
        } else {
            Log.d(TAG, "tag " + localName + " skiped");
        }

    }

    /**
     * This is called to get the tags value
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementOn) {
            elementValueBuilder.append(ch, start, length);
            elementOn = false;
        }
    }




}
