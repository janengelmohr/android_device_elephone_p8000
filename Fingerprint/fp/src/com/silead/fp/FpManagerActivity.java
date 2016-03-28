package com.silead.fp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class FpManagerActivity extends Activity {

    private GridView mGridView;
    private ArrayList<Appinfo> mApps;
    private AppsAdapter mAdapter;

    private DataUtil mDateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_app_manager);

        mDateManager = new DataUtil(this);
        mGridView = (GridView) findViewById(R.id.apps_view);
        FpModel mfp = new FpModel(this);
        mApps = mfp.getAppsList();
        mAdapter = new AppsAdapter(this, mApps, mDateManager);
        mGridView.setAdapter(mAdapter);
    }

}
