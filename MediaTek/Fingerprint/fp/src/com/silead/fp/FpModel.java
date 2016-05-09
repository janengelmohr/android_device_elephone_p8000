package com.silead.fp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class FpModel {

    private Context mContext;
    private ArrayList<Appinfo> mApplist;

    public FpModel(Context context) {
        mContext = context;
        mApplist = new ArrayList<Appinfo>();
        loadAllAppsByPM();
    }

    private void loadAllAppsByPM() {

        int N = Integer.MAX_VALUE;
        final PackageManager packageManager = mContext.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = null;
        apps = packageManager.queryIntentActivities(mainIntent, 0);
        N = apps.size();

        for (int i = 0; i < N; i++) {
            mApplist.add(new Appinfo(mContext, apps.get(i)));
        }
    }

    public ArrayList<Appinfo> getAppsList() {
        return mApplist;
    }

}
