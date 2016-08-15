package com.silead.fp.setting;

import java.util.ArrayList;
import java.util.HashMap;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcFPInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FingerAdapter extends BaseAdapter {

    private ArrayList<SLFpsvcFPInfo> mfingerlist;
    private static HashMap<Integer, Boolean> check = new HashMap<>();
    private LayoutInflater mInflater;
    private FpControllerNative fpControllerNative;

    public FingerAdapter(Context context, ArrayList<SLFpsvcFPInfo> mFingerList) {
        Context mContext = context;
        mfingerlist = mFingerList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mfingerlist.size();
    }

    @Override
    public Object getItem(int pos) {
        return mfingerlist.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int pos, View arg1, ViewGroup arg2) {
        fpControllerNative = FpControllerNative.getInstance();
        View view = mInflater.inflate(R.layout.finger_item, null);
        final TextView textView = (TextView) view.findViewById(R.id.name);
        String fp_name = mfingerlist.get(pos).getFingerName();
        textView.setText(fp_name);
        /*CheckBox enableBox = (CheckBox) view.findViewById(R.id.enable);
        int enrollIndex = mfingerlist.get(pos).enrollIndex;
        if (mfingerlist.get(pos).getEnable() == 0 || !check.get(enrollIndex)) {
            enableBox.setChecked(false);
        } else if (mfingerlist.get(pos).getEnable() == 1 || check.get(enrollIndex)) {
            enableBox.setChecked(true);
        }

        enableBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mfingerlist.get(pos).setEnable(1);
                    int enrollIndex = mfingerlist.get(pos).enrollIndex;
                    fpControllerNative.EnalbeCredential(enrollIndex, 1);
                    check.put(enrollIndex, true);
                } else {
                    mfingerlist.get(pos).setEnable(0);
                    int enrollIndex = mfingerlist.get(pos).enrollIndex;
                    check.put(enrollIndex, false);
                    fpControllerNative.EnalbeCredential(enrollIndex, 0);
                }
            }
        });*/

        return view;
    }

    public static HashMap<Integer, Boolean> getCheck() {
        return check;
    }


}

