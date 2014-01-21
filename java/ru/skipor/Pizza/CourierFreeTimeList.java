package ru.skipor.Pizza;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Vladimir Skipor on 1/21/14.
 * Email: vladimirskipor@gmail.com
 */
public class CourierFreeTimeList {
    private final static String TAG = "CourierFreeTimeList";
    ArrayList<Integer> list;
    public final int courierId;

    public CourierFreeTimeList(int courierId) {
        this.courierId = courierId;

        list = new ArrayList<Integer>();



    }

    public  void addFreeTime(int deliveryId ){
        list.add(deliveryId);
    }
}
