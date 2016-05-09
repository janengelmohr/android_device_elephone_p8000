package com.silead.fp.setting;

import java.util.ArrayList;
import java.util.HashMap;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcFPInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FingerAdapter extends BaseAdapter {

	private final static String TAG = "FingerAdapter";
    private ArrayList<SLFpsvcFPInfo> mfingerlist;
    @SuppressLint("UseSparseArrays")
	private static HashMap<Integer, Boolean> check = new HashMap<Integer, Boolean>();
	@SuppressWarnings("unused")
	private Context mContext;
    private LayoutInflater mInflater;
	public static String BACKGROUND_COLOR_ENABLED = "#FFFFFF";
	public static String BACKGROUND_COLOR_DISABLED = "#CCCCCC";
	public static String BACKGROUND_COLOR_ONTOUCH = "#CCCCCC";
//	public static boolean isTouch = true;
	private FpControllerNative fpControllerNative;	
    public FingerAdapter(Context context, ArrayList<SLFpsvcFPInfo> mFingerList) {
        mContext = context;
        mfingerlist = (ArrayList<SLFpsvcFPInfo>) mFingerList;
        mInflater = LayoutInflater.from(context);
    }
    	
    public void setFingerList(ArrayList<SLFpsvcFPInfo> mlist) {
        mfingerlist = mlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mfingerlist.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return mfingerlist.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }
    
    
    @Override
    public View getView(final int pos, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
		fpControllerNative = FpControllerNative.getInstance();
		final ViewHolder holder = new ViewHolder();
        View view = mInflater.inflate(R.layout.finger_item, null);
        final TextView textView = (TextView) view.findViewById(R.id.name);
        holder.iv = (ImageView) view.findViewById(R.id.iv_over);
        String fp_name = mfingerlist.get(pos).getFingerName();
		//Log.d(TAG, "getView fp_name = "+fp_name);
        textView.setText(fp_name);
       	CheckBox enableBox = (CheckBox) view.findViewById(R.id.enable);
       	int enrollIndex = mfingerlist.get(pos).enrollIndex;
			if (mfingerlist.get(pos).getEnable() == 0
					|| check.get(enrollIndex) == false) {
				enableBox.setChecked(false);
				textView.setBackgroundColor(Color
						.parseColor(BACKGROUND_COLOR_DISABLED));
			} else if (mfingerlist.get(pos).getEnable() == 1
					|| check.get(enrollIndex) == true) {
				enableBox.setChecked(true);
				textView.setBackgroundColor(Color
						.parseColor(BACKGROUND_COLOR_ENABLED));
			}
		enableBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {		

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				//Log.d(TAG, "onCheckedChanged isChecked = "+isChecked);
				if(isChecked){
					mfingerlist.get(pos).setEnable(1);
					int enrollIndex = mfingerlist.get(pos).enrollIndex;
					fpControllerNative.EnalbeCredential(enrollIndex, 1);
					check.put(enrollIndex, true);
					textView.setBackgroundColor(Color.parseColor(BACKGROUND_COLOR_ENABLED));
				}else if(!isChecked){
					mfingerlist.get(pos).setEnable(0);
					int enrollIndex = mfingerlist.get(pos).enrollIndex;
					check.put(enrollIndex, false);
					fpControllerNative.EnalbeCredential(enrollIndex, 0);
					textView.setBackgroundColor(Color.parseColor(BACKGROUND_COLOR_DISABLED));
				}
			}
		});
		
		view.setTag(holder);
        return view;
    }
    
    static class ViewHolder{
    	ImageView iv;
    }
	public static HashMap<Integer, Boolean> getCheck() {
		return check;
	}

	public static void setCheck(HashMap<Integer, Boolean> check) {
		FingerAdapter.check = check;
	}
    
    
}

