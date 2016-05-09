package com.silead.fp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<Appinfo> mAppList;
    private DataUtil mDataManger;

    public AppsAdapter(Context context, ArrayList<Appinfo> mApps, DataUtil data) {

        mDataManger = data;
        mAppList = mApps;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAppList.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public View getView(int pos, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view = mInflater.inflate(R.layout.app_item, null);
        final ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);
        TextView titleView = (TextView) view.findViewById(R.id.app_title);
        final Appinfo item = mAppList.get(pos);
        Boolean mAppLock = mDataManger.getAppLockState(item.componentName);
        if (mAppLock) {
            iconView.setImageBitmap(item.getAppLockIcon());
        } else {
            iconView.setImageBitmap(item.getAppIcon());
        }
        titleView.setText(item.getTitle());
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Boolean mAppLock = mDataManger
                        .getAppLockState(item.componentName);
                if (mAppLock) {
                    mDataManger.removeLockApp(item.componentName);
                    iconView.setImageBitmap(item.getAppIcon());
                } else {
                    mDataManger.storeLockApp(item.componentName);
                    iconView.setImageBitmap(item.getAppLockIcon());
                }

            }
        });
        return view;
    }

}
