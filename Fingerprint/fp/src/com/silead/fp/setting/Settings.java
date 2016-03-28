package com.silead.fp.setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.silead.fp.R;
import com.silead.fp.setting.FingerAdapter.ViewHolder;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcFPInfo;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

public class Settings extends Activity {

	// private static final int
//	private static final int CREATE_NEW_FINGER_NAME = 0;
//	private static final int RENAME_FINGER = 1;

	private final static String TAG = "Settings";
	private ListView mFingerListView;
	private Button bt_entry;
	private TextView mFingerPrompt;
	private CheckBox mSwitch;
	private CheckBox enable;
	private ImageView back;
	private static boolean allChecked = false;
	Vibrator vibrator;
	private FingerAdapter mAdapter;
	private HashMap<Integer, Boolean> check;

	private ArrayList<SLFpsvcFPInfo> mFingerList;
	private FpControllerNative mControllerNative;
	final int Max_Enroll_Touch_Count = 8;
	final int SL_ENROLL = 0x8001;
//	private int mEnrollCount = 0;
//	private final int MAX_FINGER_RECORD = 5;
//	private final String SLFpsvcFPInfo = "SLFpsvcFPInfo";
//	private static final String FILENAME = "fpfile";
//	private SharedPreferences sharedPrefrences;
//	private static boolean identify;
	private SLFpsvcIndex fpsvcIndex;
	public SLFpsvcFPInfo fPInfos;

	public final static String ACTION_ENROLL_SUCCESS = "com.silead.fp.Settings.EnrollSuccess";
	public final static String ACTION_NONSUPPORT_LOCKSCREEN_ACTIVE = "com.silead.fp.Settings.NONSUPPORT_LOCKSCREEN_ACTIVE";
//	private HashMap<Integer, Boolean> check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fp_setting);
//		identify = false;
		mControllerNative = FpControllerNative.getInstance();
		mControllerNative.initFPSystem();
		initView();
		
		Log.d(TAG, "enrollCredentialRSP $$$$$$$ GetFpInfo begin");

		mFingerList = new ArrayList<SLFpsvcFPInfo>();
		Log.d(TAG, "mFingerList size $$$$$$$ " + mFingerList.size());

		fpsvcIndex = mControllerNative.GetFpInfo();

		Log.d(TAG, "enrollCredentialRSP $$$$$$$ GetFpInfo end  ");

		System.out.println("mControllerNative" + mControllerNative);
		for (int i = 0; i < 5; i++) {
			System.out.println("fpsvcIndex.FPInfo[i].enrollIndex"
					+ fpsvcIndex.FPInfo[i].enrollIndex + "fpsvcIndex.FPInfo[i].slot"
					+ fpsvcIndex.FPInfo[i].slot + "fpsvcIndex.FPInfo[i].enable"
					+ fpsvcIndex.FPInfo[i].enable +"fpsvcIndex.FPInfo[i].fingerName"
					+ fpsvcIndex.FPInfo[i].fingerName );
		}
		System.out.println(" $$$ 333 fpsvcIndex.max = " + fpsvcIndex.max);
		if (fpsvcIndex.max > 5) {
			fpsvcIndex.max = 5;
		}
		for (int i = 0; i < fpsvcIndex.max; i++) {
			if(fpsvcIndex.FPInfo[i].slot == 1){
				Log.d(TAG, " $$$ fpsvcIndex.fingerName 222 = " + fpsvcIndex.FPInfo[i].fingerName+":"+fpsvcIndex.FPInfo[i].enrollIndex);
				mFingerList.add(fpsvcIndex.FPInfo[i]);
			}
		}
		
		System.out.println("before initFpAdapter" + mFingerList.size());
		if(mFingerList.size() >= 5){
			bt_entry.setEnabled(false);
		}else {
			bt_entry.setEnabled(true);
		}
		if(mFingerList.size() > 0){
			mFingerPrompt.setText("");
		}else {
			mFingerPrompt.setText(getString(R.string.fp_prompt_str));
		}
		System.out.println("mAdapter.notifyDataSetChanged() end");
		// sharedPrefrences = getSharedPreferences(tAG1, MODE_PRIVATE);
		// edit = sharedPrefrences.edit();

		// for (int i = 0; i < mFingerList.size(); i++) {
		// //
		// edit.putInt("fingerSlot+i", mFingerList.get(i).slot);
		// edit.putInt("fingerEnable+i", mFingerList.get(i).enable);
		// edit.commit();
		// edit.clear();
		// }

//		mDateManager = new DataUtil(this);

//		boolean checked = mDateManager.getFPSwitch();
		check = FingerAdapter.getCheck();
		mSwitch.setChecked(allChecked);
		for (int j = 0; j < fpsvcIndex.max; j++) {
			if(fpsvcIndex.FPInfo[j].enable == 0){
				check.put(j, false);
			}else if(fpsvcIndex.FPInfo[j].enable == 1){
				check.put(j, true);
			}
		}
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
//				// TODO Auto-generated method stub
//				mDateManager.storeFPSwitch(fpswitch);
				if(isChecked){
					System.out.println("mSwitch onCheckedChanged isChecked");
					for (int i = 0; i < mFingerList.size(); i++) {
						if(mFingerList.get(i).getEnable() == 0){
							mFingerList.get(i).setEnable(1);
							mControllerNative.EnalbeCredential(mFingerList.get(i).enrollIndex, 1);
							check.put(mFingerList.get(i).enrollIndex, true);
							allChecked = true;
							mAdapter.notifyDataSetChanged();
							System.out.println("");
						}
					}
				}else if(!isChecked){
					System.out.println("mSwitch onCheckedChanged no Checked");
					for (int i = 0; i < mFingerList.size(); i++) {
						if(mFingerList.get(i).getEnable() == 1){
							mFingerList.get(i).setEnable(0);
							mControllerNative.EnalbeCredential(mFingerList.get(i).enrollIndex, 0);
							check.put(mFingerList.get(i).enrollIndex, false);             
							allChecked = false;
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		});
		initFpAdapter();
		mAdapter.notifyDataSetChanged();
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
		
	private void initFpAdapter() {
  		mAdapter = new FingerAdapter(Settings.this, mFingerList);
		Log.d(TAG, "$$$$$$ initFpAdapter end $$$$$$$$$ ");
		mFingerListView.setAdapter(mAdapter);
		mFingerListView
		.setOnItemLongClickListener(new FingerListItemOnItemLongClick());
	}
		
	class FingerListItemOnItemLongClick implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int pos1, long pos2) {
			SLFpsvcFPInfoonLongClick(pos1);
			return true;
		}
	}
	
	public static void DisableFp() {
		Log.d(TAG, "DisableFp $$$$$$$ DisableFp 2222 ");
		FpControllerNative controllerNative = FpControllerNative.getInstance();
		controllerNative.initFPSystem();
		SLFpsvcIndex fpIndex = controllerNative.GetFpInfo();
		for (int i = 0; i < fpIndex.max; i++) {
			if(fpIndex.FPInfo[i].enable == 1){
				Log.d(TAG, "DisableFp $$$$$$$ DisableFp enable["+i+"] = "+fpIndex.FPInfo[i].enable);
				fpIndex.FPInfo[i].setEnable(0);
			}
		}
		controllerNative.SetFpInfo(fpIndex);

	}
	
	@SuppressLint("NewApi")
	private void SLFpsvcFPInfoonLongClick(final int pos) {

		final String[] mItems = {getString(R.string.rename), getString(R.string.delete)};
		AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
		builder.setItems(mItems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 点击后弹出窗口选择了第几项
				switch (which) {
				case 0:
					renameSLFpsvcFPInfo(pos);
					break;
				case 1:
					// 顶层删除finger
					Log.d(TAG,"SLFpsvcFPInfoonLongClick 1  pos = "+pos+":"+mFingerList.size());
					fPInfos = (SLFpsvcFPInfo) mFingerList.get(pos);
					fpsvcIndex = mControllerNative.GetFpInfo();
					int deleteIndex = fPInfos.enrollIndex;
					mControllerNative.removeCredential(deleteIndex);
					mFingerList.remove(pos);
					check.put(deleteIndex, false);
					for (int i = 0; i < 5; i++) {
						System.out.println("removeCredential fpsvcIndex.FPInfo[i].enrollIndex"
								+ fpsvcIndex.FPInfo[i].enrollIndex + "fpsvcIndex.FPInfo[i].slot"
								+ fpsvcIndex.FPInfo[i].slot + "fpsvcIndex.FPInfo[i].enable"
								+ fpsvcIndex.FPInfo[i].enable +"fpsvcIndex.FPInfo[i].fingerName"
								+ fpsvcIndex.FPInfo[i].fingerName );
					}
					mAdapter.notifyDataSetChanged();
					if(mFingerList.size()<5){   
						bt_entry.setEnabled(true);
					}
					if(mFingerList.size() == 0){
						mFingerPrompt.setText(getString(R.string.fp_prompt_str));
					}
					
					Log.d(TAG,"SLFpsvcFPInfoonLongClick 1  deleteIndex = "+deleteIndex+":"+mFingerList.size());
					if (mFingerList.size() > pos) {
						Log.d(TAG,
								"mFingerList.get(pos).slot = "
										+ mFingerList.get(pos).slot);
					}
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
	}
	class EntryOnClickListener implements View.OnClickListener {
		
		@Override
		public void onClick(View arg0) {
			System.out.println("EntryOnClickListener onClick");
			Intent intent = new Intent(Settings.this, EnrollActivity.class);
			startActivityForResult(intent, 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if( resultCode == 0 && data!= null){
			Bundle extras = data.getExtras();
			int enrollIndex = extras.getInt("enrollIndex");
			fpsvcIndex = mControllerNative.GetFpInfo();
			mFingerList.add(fpsvcIndex.FPInfo[enrollIndex]);
			check.put(enrollIndex, true);
		}
		if(mFingerList.size()>=5){
			bt_entry.setEnabled(false);
		}
		if(mFingerList.size()>0){
			mFingerPrompt.setText("");
		}
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.fp_settings_back_bt);
		bt_entry = (Button) findViewById(R.id.bt_entry);
		bt_entry.setOnClickListener(new EntryOnClickListener());
		mFingerListView = (ListView) findViewById(R.id.fp_finger_list);
		mFingerPrompt = (TextView) findViewById(R.id.fp_prompt);
		System.out.println("3333 (CheckBox) findViewById(R.id.enable);"
				+ enable);
		mSwitch = (CheckBox) findViewById(R.id.fp_switch_cb);
	}

	public void myLog(String msg) {
		Log.d(TAG, msg);
	}
	
	public void onResume() {
		super.onResume();
		System.out.println("Settings onResume 启动了");
		mAdapter.notifyDataSetChanged();
	}

	public void onPause() {
		super.onPause();
		System.out.println("Settings onPause 启动了");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		System.out.println("Settings onstart 启动了");
	}

	public void onStop() {
		super.onStop();
		Log.d(TAG, "run onstop(), cancel enrolling");
//		mControllerNative.destroyFPSystem();
		System.out.println("Settings onStop 启动了");
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("Settings onRestart 启动了");
	}
	
	public void onDestroy() {
		super.onDestroy();
//		identify = false;
		//mControllerNative.FpCancelOperation();
		mControllerNative.destroyFPSystem();
		System.out.println("Settings onDestroy 启动了");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("backkey onKeyDown" + keyCode + ":"
				+ KeyEvent.KEYCODE_BACK);
		super.onKeyDown(keyCode, event);

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 按返回键退出录入
			//mControllerNative.FpCancelOperation();
			
			return true;
		case KeyEvent.KEYCODE_HOME:
			//mControllerNative.FpCancelOperation();
			return true;
		case KeyEvent.KEYCODE_MENU:
			//mControllerNative.FpCancelOperation();
			return true;
		case KeyEvent.KEYCODE_POWER:
		default:
			System.out.println("backkey onKeyDown default");
			break;
		}
		return true;
	}

	public void Mytoast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void renameSLFpsvcFPInfo(final int index) {

		final SLFpsvcFPInfo mfinger = mFingerList.get(index);
		AlertDialog.Builder mDialog = new Builder(Settings.this);

		String title = getString(R.string.entry_finger_name);
		String ok = getString(R.string.ok);
		String cancel = getString(R.string.cancel);
		mDialog.setTitle(title);
		mDialog.setIcon(android.R.drawable.ic_dialog_info);

		final EditText et = new EditText(Settings.this);
		// et.setHint(mfinger.getFingerName());
		mDialog.setView(et);
		et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
		et.setMaxLines(1);
		mDialog.setPositiveButton(ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				String fingerName = et.getText().toString().trim();
				mfinger.setFingerName(fingerName);
				int enrollIndex2 = mfinger.enrollIndex;
				 fpsvcIndex = mControllerNative.GetFpInfo();
				 System.out.println("fpsvcIndex.FPInfo[enrollIndex2].enrollIndex"
							+ fpsvcIndex.FPInfo[enrollIndex2].enrollIndex + "fpsvcIndex.FPInfo[index].slot"
							+ fpsvcIndex.FPInfo[enrollIndex2].slot + "fpsvcIndex.FPInfo[index].enable"
							+ fpsvcIndex.FPInfo[enrollIndex2].enable +"fpsvcIndex.FPInfo[index].fingerName"
							+ fpsvcIndex.FPInfo[enrollIndex2].fingerName );
				 fpsvcIndex.FPInfo[enrollIndex2].setFingerName(fingerName);
				fpsvcIndex.setFPInfo(fpsvcIndex.FPInfo);
				mControllerNative.SetFpInfo(fpsvcIndex);
				mAdapter.notifyDataSetChanged();
			}
		});
		mDialog.setNeutralButton(cancel, null);
		mDialog.show();

	}

}
