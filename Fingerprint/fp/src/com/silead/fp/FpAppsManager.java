package com.silead.fp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class FpAppsManager extends Activity {

    private GridView mGridView;
    private ArrayList<Appinfo> mApps;
    private AppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fp_app_manager);
        mGridView = (GridView) findViewById(R.id.apps_view);

        FpModel mfp = new FpModel(this);
        mApps = mfp.getAppsList();
        DataUtil data = new DataUtil(this);
        mAdapter = new AppsAdapter(this, mApps, data);
        mGridView.setAdapter(mAdapter);
    }

}
