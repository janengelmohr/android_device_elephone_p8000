package com.silead.fp.lockscreen;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;

public class LockScreen extends Activity {

    public static final String TAG = "LockScreen";
    public static final boolean DEBUG = true;
    private Button unlock_bt;
    
    private FpControllerNative mFpControllerNative;
    

	private static final int SL_IDENTIFY = 0x8004;
    private static final int  IDENTIFY_CREDENTIAL_RSP = 0;
	private static final String FILENAME = "fpfile";
	private SharedPreferences sharedPrefrences;
	private Thread mIdentifyThread;
	private static String FP_ON_OFF = "switchonoff";  
	private Context mContext;
	private final int SL_SUCCESS	= 0;
	private final int SL_FAILED 	= -1;
	private final int SL_WRONG_PARAM	= -1001;
	private final int SL_ENROLL_ERROR = -1002;
	private final int SL_NOT_MATCH	= -1003;
	private final int SL_TOUCH_ERROR	= -1004;
	private final int SL_IDENTIFY_ERROR = -1005;
	private final int SL_ENROLL_CANCELED	= -1006;
	private final int SL_IDENTIFY_CANCELED =  -1007;
	private final int SL_FINGER_NOT_EXIST =	-1008;
	private final int SL_DEVICE_NOT_READY =   -1009;
	private final int SL_MEMORY_ERROR = -1010;
	private final int SL_ALGORITHM_INIT_ERROR  =  -1011;
	private final int SL_NOT_SUPPORT	= -1012;	//finger index > 5 error
	private final int SL_LOAD_API_ERROR 	=	-1013;
	private final int SL_UNKNOWN_ERROR = -1100;

	private boolean bIdentify = false;

    class LockScreenHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int result = msg.what;
			
			  switch (result) {
			case IDENTIFY_CREDENTIAL_RSP:
				finish();
				break;

			default:
				break;
			}
		}
    }
   
	private LockScreenHandler mHandler;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
		mContext = this;
        setContentView(R.layout.lockscreen);
        unlock_bt = (Button) findViewById(R.id.unlock_bt);

        // 用于模拟指纹解锁
        unlock_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        mHandler = new LockScreenHandler();
		mFpControllerNative = FpControllerNative.getInstance();
		mFpControllerNative.setHandler(mHandler);
        //Log.d("[wq]","[wq]:new FpAPIFromJNI\n");        
    }


	/** modify by Lufang start***/
    private Boolean isFingerLockOn() {
		Context context = null;
		try {
		    context = mContext.createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
   		if (context != null) {
		    sharedPrefrences = context.getSharedPreferences(FILENAME, Context.MODE_MULTI_PROCESS);
		}
		return (sharedPrefrences.getBoolean(FP_ON_OFF, false));
    }

    private void onFingerMatched() {
	Log.d(TAG, "<<Finger Identify>> Go into <onFingerMatched>");
        if (DEBUG) Log.d(TAG, "onFingerMatched");
        /*
	Message msg = mHandler.obtainMessage(HIDE);
        mHandler.sendMessage(msg);
	   //addbylufang
		if(isFingerLockOn()){
		    Log.d(TAG, "<<Finger Identify>> in onFingerMatched -- addbylufang Unlock & Deinit bIdentify = "+bIdentify);
		    if(bIdentify){
				cancelIdentify(mFpControllerNative.IDENTIFY_INDEX);
		    }
		    deinitFPSystem();
		    Log.d(TAG, "<<Finger Identify>> in onFingerMatched -- after addbylufang deinitFPSystem");
		}
	  // addbylufang
	Log.d(TAG, "<<Finger Identify>> exit addbylufang <onFingerMatched>");
	*/
    }

	public int initFPSystem(){
		return mFpControllerNative.initFPSystem();
	}

	public int identifyCredentialREQ (int fpIndex) {
		return mFpControllerNative.identifyCredentialREQ(fpIndex);
	}

	public int destroyFPSystem () {
		return mFpControllerNative.destroyFPSystem();
	}

	private void scheduleScreenState(boolean on) {
		Log.d(TAG, " scheduleScreenState addbylufang on = "+on);
		mHandler.removeCallbacks(mSendScreenStateOff);
		if (on) {
			startScreenOnOff(true);
		} else {
			mHandler.postDelayed(mSendScreenStateOff, 2000);
		}
	}
	
	private void startScreenOnOff(boolean on) {
		if (on) {
			// Added by MGao: 2014-05-22				
			if(isFingerLockOn()){
				Log.d(TAG, "$$$$$ startScreenOnOff <<Finger Identify>>  -- addbylufang Start FP Thread bIdentify = "+bIdentify);
				if (bIdentify) {
					return;
				}
				bIdentify = true;
				int ret = initFPSystem();
				if(ret == 0){
					mIdentifyThread = new Thread(mRunnable);
					mIdentifyThread.start();
				}
			} else {
				bIdentify = false;
				Log.d(TAG, "$$$$$ startScreenOnOff <<Finger Identify>> -- addbylufang Finger Lock OFF");
			}
			// Added END
		} else {
			// Added by MGao: 2014-05-22
			if(isFingerLockOn()){
				Log.d(TAG, "$$$$ startScreenOnOff <<Finger Identify>> -- addbylufang Unlock & Deinit");
			}
			// Added END
		}
	}

	Runnable mSendScreenStateOff = new Runnable() {
		public void run() {
			Log.d(TAG, " mSendScreenState addbylufang run ");
			startScreenOnOff(false);
		}
	};

	/** modify by Lufang end***/

	
    Runnable mRunnable = new Runnable() {
    	public void run(){
    		IdentifyUserLoop();
    	}
    };
 
    
    void IdentifyUserLoop(){
	Log.d(TAG, "<<Finger Identify>> IdentifyUserLoop addbylufang --- start:" + bIdentify);
	while(bIdentify){
		int identifyResult = 1;// = identifyCredentialREQ();
	    Log.d(TAG, "<<Finger Identify>> --- Result:" + identifyResult);
	    if(identifyResult >= 0){
			Log.d(TAG, "<<Finger Identify>> --- UNLOCK");
			bIdentify = false;
			onFingerMatched();
			Log.d(TAG, "<<Finger Identify>> after hideLocked");
	    } else {
			Log.d(TAG, "<<Finger Identify>> -- Finger UNMATCHED");
			//send msg
			Intent intent = new Intent();
			intent.setAction("com.android.internal.policy.impl.PhoneWindowManager.KEYGUARD_UNLOCK");
		    mContext.sendBroadcast(intent);
	    }	    
	}
	Log.d(TAG, "<<Finger Identify>> IdentifyUserLoop addbylufang --- end");
	
    } 

}
