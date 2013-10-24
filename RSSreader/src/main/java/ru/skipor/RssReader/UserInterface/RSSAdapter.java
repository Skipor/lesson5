package ru.skipor.RssReader.UserInterface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import ru.skipor.RssReader.R;
import ru.skipor.RssReader.RSSFeedReader.RSSItem;

/**
 * Created by Vladimir Skipor on 10/24/13.
 * Email: vladimirskipor@gmail.com
 */
public class RSSAdapter extends BaseAdapter {
    private static final String TAG = "RSSAdapter";
    public final Context context;
    private ArrayList<RSSItem> items;

    public RSSAdapter(Context context) {
        this.context = context;
        Log.d(TAG, "RSSAdapter created");

    }

    public void setItems(Collection<RSSItem> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();

    }

    public void add(RSSItem item) {
        items.add(item);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rss_item_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_title);
        textView.setText(items.get(position).getTitle());

        return convertView;
    }
}
