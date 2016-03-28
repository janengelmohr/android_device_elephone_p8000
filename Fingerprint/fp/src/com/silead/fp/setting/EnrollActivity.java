package com.silead.fp.setting;


import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.AnimationDrawable;

public class EnrollActivity extends Activity {
	private final String TAG = "EnrollActivity";
	private final String TAG1 = "mFingerList";

	private ProgressBar enrollPrg;
	private TextView title;
	private ImageView finger;

	public int enrollIndex;
	private int progress;
	int screenHeight;
	int screenWidth;
	private static final int MAX_ENROLL_NUM = 20;
	private static final int MIN_ENROLL_NUM = 5;
	static int currShowProg = 0;
	static int lastRetProg = 0;
	int retProg = 0;
	static int minProgStep = 0;// 100/MAX_ENROLL_NUM;
	static int maxProgStep = 0;// 100/MIN_ENROLL_NUM;
	static int minProg = 0;
	static int maxProg = 0;
	static int lastImageIndex = 0;
	static int overlayIndex = 0;
	static int enrollCount = 0;
	static int enrollMaxCount = 8;
        private AnimationDrawable aniDraw;

	private boolean mIsEnrolling = false;

	Animation alphaAnimation;
	Vibrator vibrator;

	private FpControllerNative mControllerNative;
	private EnrollHandler mEnrollHandler = new EnrollHandler();
//	private ArrayList<SLFpsvcFPInfo> mFingerList;
	private SLFpsvcIndex fpsvcIndex;
	Drawable[] array = new Drawable[15];
	int bitValue[] = new int[14];
	int[] images3 = new int[] { R.drawable.zwt_01, R.drawable.zwt_02,
			R.drawable.zwt_03, R.drawable.zwt_04, R.drawable.zwt_05,
			R.drawable.zwt_06, R.drawable.zwt_07, R.drawable.zwt_08,
			R.drawable.zwt_09, R.drawable.zwt_10, R.drawable.zwt_11,
			R.drawable.zwt_12, R.drawable.zwt_13, R.drawable.zwt_14 };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fp_enroll);
	        enrollCount = 0;
		enrollPrg = (ProgressBar) findViewById(R.id.enrollPrg);
		title = (TextView) findViewById(R.id.title);
		finger = (ImageView) findViewById(R.id.finger);
		initHandler();
//		mFingerList = new ArrayList<SLFpsvcFPInfo>();
		fpsvcIndex = mControllerNative.GetFpInfo();
		if (fpsvcIndex.max > 5) {
			fpsvcIndex.max = 5;
		}
//		Log.d(TAG, "fpsvcIndex===== mFingerList.size()"
//				+ mFingerList.size());
//		Log.d(TAG, "fpsvcIndex===== mFingerList.size()"
//				+ mFingerList.size());
		minProgStep = 100 / MAX_ENROLL_NUM;
		maxProgStep = 100 / MIN_ENROLL_NUM;
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		InitAnimation();
		array[0] = this.getApplicationContext().getResources()
		    .getDrawable(R.drawable.zwt_00);
		enrollStart();
		IntentFilter mFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mBroadcastReceiver, mFilter);
                ImageView animation = (ImageView)findViewById(R.id.loading);
                aniDraw = (AnimationDrawable) animation.getDrawable();
	}

	private void initHandler() {
		mControllerNative = FpControllerNative.getInstance();
		mControllerNative.setHandler(mEnrollHandler);
		mControllerNative.initFPSystem();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
                if (aniDraw.isRunning())
                    aniDraw.stop();
		Log.d(TAG, "EnrollActivity onPause 启动了");
		finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
                if (aniDraw.isRunning())
                    aniDraw.stop();
		Log.d(TAG, "EnrollActivity onStop 启动了");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "EnrollActivity onstart 启动了");
		// enrollStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
                aniDraw.start();
		Log.d(TAG, "EnrollActivity onResume 启动了");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
                if (aniDraw.isRunning())
                    aniDraw.stop();
		Log.d(TAG, "EnrollActivity onDestroy 启动了");
		enrollCancel();
		mControllerNative.destroyFPSystem();
		if(mBroadcastReceiver != null){
			try {
				unregisterReceiver(mBroadcastReceiver);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int enrollStart() {
		Log.d(TAG, "enrollStart mIsEnrolling = "+mIsEnrolling);
		mIsEnrolling = true;
		int index =findEnrollMinIndex();
		if(index < 5 && index >= 0) 
		{
			mControllerNative.enrollCredentialREQ(index);
			return 0;
		}
		else
		{
			return -1;
		}
	}

	public int findEnrollMinIndex() {
		int index;
		// sharedPrefrences = getApplicationContext().getSharedPreferences(
		// FILENAME, Context.MODE_WORLD_READABLE);
		for (index = 0; index < fpsvcIndex.max; index++) {// 5 items at most
			if (fpsvcIndex.FPInfo[index].slot == 0) {// judge
				Log.d(TAG, "findEnrollMinIndex mFingerList.get(index).slot ===== index"
						+ index);
				break;
			}
		}
		return index;
	}

	@SuppressLint("HandlerLeak")
	class EnrollHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int msgwt = msg.what;
			// 下面的数据是要改变的
			// int value = msg.what;
			Log.d(TAG, "handleMessage: msgwt = " + msgwt);
			switch (msgwt) {
			case FpControllerNative.IDENTIFY:
				int[] idetifyIntArray = (int[]) msg.obj;
				int idetifyIndex = idetifyIntArray[0];
				int idetifyResult = idetifyIntArray[1];
				identifyFinger(idetifyIndex, idetifyResult);
				break;
			case FpControllerNative.ENROLL_CREDENTIAL_RSP:
				int[] intArray = (int[]) msg.obj;
				enrollIndex = intArray[0];
				progress = intArray[1];
				int enrollResult = intArray[2];
				int area = intArray[3];
				Log.d(TAG, "handleMessage:  ENROLL_CREDENTIAL_RSP index = "
						+ enrollIndex + ":" + progress + ":" + enrollResult
						+ ":" + area);
				enrollRSP(enrollIndex, progress, enrollResult, area);
				break;
			case FpControllerNative.INIT_FP_FAIL:

				break;
			case FpControllerNative.FP_GENERIC_CB:

				break;
			default:
				break;
			}
			// updateEntryFingerInfoDialogTitle(result);
		}

	}

	public int[] intToArray(int area) {
		int a[] = new int[14];
		for (int i = 0; i < a.length; i++) {
			a[i] = ((area >> i) & 0x1);
		}
		return a;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "backkey onKeyDown" + keyCode + ":"
				+ KeyEvent.KEYCODE_BACK);

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 按返回键退出录入
			Log.d(TAG, "EnrollActivity : 你按下了返回键");
			EnrollActivity.this.finish();
			enrollCancel();
			return true;
		case KeyEvent.KEYCODE_HOME:
			enrollCancel();
			return true;
		case KeyEvent.KEYCODE_MENU:
			enrollCancel();
			return true;
		case KeyEvent.KEYCODE_POWER:
			Log.d(TAG, "EnrollActivity : 你按下了 POWER键");
			enrollCancel();
			finish();
			return true;
		default:
			Log.d(TAG, "backkey onKeyDown default");
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(Intent.ACTION_SCREEN_OFF.equals(action)){
				EnrollActivity.this.finish();
			}
		}
	};
	private void enrolling(int index, int progress, int area) {
		// add vibrate
		Log.d(TAG, "enrolling index = " + index + ":" + progress + ":" + area);
		if (progress >= 0 && progress < 100) {
			setDialogProgress(progress);
		} else {
			enrollSuccess(index);
			Mytoast(getString(R.string.enroll_success));
		}

		int[] bitValue = intToArray(area);

		for (int l = 0; l < bitValue.length; l++) {
			Log.d(TAG, "bitValue[l] = " + bitValue[l]);
		}
                int count = progress / 10;
                if (count > 10)
                    count = 10;
                array[0] = EnrollActivity.this.getApplicationContext()
                    .getResources().getDrawable(images3[count]);
		finger.setImageDrawable(array[0]);
	}

	private void enrollSuccess(final int index) {
		Log.d(TAG, "enroll success, enroll index = " + index+":"+mIsEnrolling);
                mIsEnrolling = false;
		setDialogProgress(100);
		finger.setImageResource(R.drawable.zwt_10);
		title.setText(getString(R.string.enroll_success));
		Intent intent = getIntent();
		intent.putExtra("enrollIndex", index);
		EnrollActivity.this.setResult(0, intent);
		createNewSLFpsvcFPInfo(index);
		EnrollActivity.this.finish();
	}

	private void enrollError() {
		Log.e(TAG, "Enroll times surpass Max_Enroll_Touch_Count");
		finish();
		Mytoast("surpass Max_Enroll_Touch_Count");
		//enrollFailed();
	}
	private void enrollnotsupport() {
		Log.e(TAG, "Enroll index surpass Max_Enroll_Count");
		finish();
	}
	private void enrollRSP(int enrollIndex, int progress, int enrollResult,
			int area) {
		Log.d(TAG, "enrollRSP result = " + enrollResult + ":" + enrollIndex
				+ ":" + progress + ":" + area+":"+mIsEnrolling);
                if (enrollResult != FpControllerNative.ENROLLING) {
                    mIsEnrolling = false;
                }
		if (enrollResult == FpControllerNative.ENROLLING) {
			enrolling(enrollIndex, progress, area);
		} else if (enrollResult == FpControllerNative.ENROLL_CANCLED) {
			title.setText(getString(R.string.enroll_finger_remove));
			enrollCancelled();
		} else if (enrollResult == FpControllerNative.ENROLL_NOT_SUPPORT) {
			enrollnotsupport();
		} else if (enrollResult == FpControllerNative.ENROLL_FAIL) {
			enrollError();
		}
	}

	private void identifyFinger(int identifyIndex, int identifyResult) {
		Log.e(TAG, " $$$ identifyFinger identifyIndex = " + identifyIndex + ":"
				+ identifyResult);
		if (identifyResult == FpControllerNative.IDENTIFY_SUCCESS) {
			// identify = true;
			title.setText(R.string.identify_success);
		} else if (identifyResult == FpControllerNative.ENROLL_CANCLED) {
			title.setText(R.string.identify_errol);
			mControllerNative.destroyFPSystem();
		} else if (identifyResult == FpControllerNative.IDENTIFY_FAIL) {
			title.setText(R.string.identify_fail);
			mControllerNative.destroyFPSystem();
		}
	}

	private void enrollFailed() {
		Mytoast(this.getString(R.string.finger_count_full));
		enrollCancel();
	}

	private void enrollCancel() {
        Log.d(TAG, "enrollCancel mIsEnrolling = "+mIsEnrolling);
        if (!mIsEnrolling) {
            Log.d(TAG, "enrollCancel isnot enrolling return !");
            return;
        }
        mIsEnrolling = false;
		mControllerNative.FpCancelOperation();
	}

    private void enrollCancelWithToast() {
		Mytoast(this.getString(R.string.cancel));
		enrollCancel();
    }

    private void enrollCancelled() {
        
    }

	private void setDialogProgress(int progress) {
		if (progress >= 0 && progress <= 100) {
			enrollPrg.setProgress(progress);
		}
	}

	public void myLog(String msg) {
		Log.d(TAG1, msg);
	}

	public void Mytoast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void createNewSLFpsvcFPInfo(int index) {
		String default_string = getString(R.string.finger_name_defualt);
		String fingerName = default_string + index;
		final long starttime = System.currentTimeMillis();
		System.out.println("start:" + starttime);
		 fpsvcIndex = mControllerNative.GetFpInfo();
		 System.out.println("GetFpInfo _debug_ takes time :" + (System.currentTimeMillis() - starttime)); 
		fpsvcIndex.FPInfo[index].setFingerName(fingerName);
		Log.d(TAG, "enroll success, enroll fingerName = " + fingerName);
		fpsvcIndex.FPInfo[index].setEnrollIndex(index);
		fpsvcIndex.setFPInfo(fpsvcIndex.FPInfo);
		mControllerNative.SetFpInfo(fpsvcIndex);
	}

	public void doStopAnimation(View view) {

		view.clearAnimation();
	}

	public void doStartAnimation(View view) {
		view.startAnimation(alphaAnimation);
	}

	public void InitAnimation() {
		alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(200);
		alphaAnimation.setInterpolator(new LinearInterpolator());
		alphaAnimation.setRepeatCount(Animation.INFINITE);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
	}
}
