package com.silead.fp.lockscreen;
 
import android.app.KeyguardManager;
import android.app.KeyguardManager.OnKeyguardExitResult;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.os.PowerManager;
import android.os.UserHandle;
import android.content.ServiceConnection;
import android.content.ComponentName;
import com.android.internal.policy.IKeyguardService;
import android.os.RemoteException;

import com.silead.fp.MainActivity;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;


public class FpService extends Service implements FpControllerNative.OnIdentifyRspCB {

    public static final String TAG = "FpService";
	public static final boolean DEBUG = false;
    private static final String FP_LOCK_SCREEN_SERVICE_ACTION = "com.silead.fp.lockscreen.service.ACTION";
    private static final String FP_LOCK_SCREEN_ACTIVITY_ACTION = "com.silead.fp.lockscreen.action.VIEW";
    public static final String FINGER_PRINT_UNMATCH_ACTION = "com.silead.fp.lockscreen.action.UNMATCH";
    public static final String KEYGUARD_PACKAGE = "com.android.systemui";
    public static final String KEYGUARD_CLASS = "com.android.systemui.keyguard.KeyguardService";	
    public static final int UNMATCH_DIALOG_NORMAL_TIMEOUT = 1500;
    public static final int UNMATCH_DIALOG_MAX_TIMEOUT = 3000;
    
    private KeyguardManager.KeyguardLock mLock;
    private KeyguardManager mKeyguardManager;
    private TelephonyManager mTelemanager;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPowerManager;
	
    private FpControllerNative mFpControllerNative;
    private int mCount = 0;
    private int mErrorCount = 0;
    private Handler mHandler = new Handler();

    private boolean mIdentifying = false;
    private boolean mCanceling = false;
    private boolean mNeedReqAfterCanceling = false;
    private IKeyguardService mKeyguardService;
    
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //Log.d(TAG, "FpService $$$$ 444 $$$$ onCreate");
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mLock = mKeyguardManager.newKeyguardLock("KeyguardLock");
        
        //mLock = mKeyguardManager.newKeyguardLock("KeyguardLock");
        //mLock.disableKeyguard();
        mFpControllerNative = FpControllerNative.getInstance();
        initFPSystem();
        //mFpControllerNative.identifyCredentialREQ(0);
        mFpControllerNative.setIdentifyListener(this);
        IntentFilter filter = new IntentFilter();
        // 注册一个监听屏幕开启和关闭的广播
        // filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(FP_LOCK_SCREEN_SERVICE_ACTION);
		filter.addAction(ErrorNotifyDialogActivity.ERROR_NOTIFY_DIALOG_FINISH_ACTION);
        registerReceiver(screenReceiver, filter);
        
        mTelemanager = (TelephonyManager)getSystemService(Service.TELEPHONY_SERVICE);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		bindService(this);
        //
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //Log.d(TAG, "FpService onDestroy");
        mLock.reenableKeyguard();
        unregisterReceiver(screenReceiver);
    }

    @Override
    public void onIdentifyRsp(int index, int result,int fingerid) {
        //Log.d(TAG, "FpService $$$$ onIdentifyRsp 222 $$$$ index = "+index+"result:"+result+"fingerid:"+fingerid+"result"+result);
        mIdentifying = false;
//        if (result == FpControllerNative.IDENTIFY_CANCELED) {
//            mCanceling = false;
//			releaseWakeLock();
//			if (mNeedReqAfterCanceling) {
//                identifyCredentialREQ(0);
//			}
//        }
        if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
            //Log.d(TAG," onIdentifyRsp no keyguard is showing !!! ");
            return;
        }
        if (result == FpControllerNative.IDENTIFY_SUCCESS) {
            mErrorCount = 0;
            boolean isScreenOn = mPowerManager.isScreenOn();
            if(isScreenOn)
            	unLock();
        } else if (result == FpControllerNative.IDENTIFY_ERR_MATCH) {
            onIdentifyError();
        }
    }


    public void bindService(Context context) {
        Intent intent = new Intent();
        intent.setClassName(KEYGUARD_PACKAGE, KEYGUARD_CLASS);
        if (!context.bindService(intent, mKeyguardConnection,
                Context.BIND_AUTO_CREATE)) {
            if (DEBUG) Log.v(TAG, "*** bindService: can't bind to " + KEYGUARD_CLASS);
        } else {
            if (DEBUG) Log.v(TAG, "*** bindService started");
        }
    }

    private final ServiceConnection mKeyguardConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (DEBUG) Log.v(TAG, "*** Keyguard connected (yay!)");
            mKeyguardService = IKeyguardService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (DEBUG) Log.v(TAG, "*** Keyguard disconnected (boo!)");
            mKeyguardService = null;
        }
    };


    public int initFPSystem(){
        //Log.d(TAG," initFPSystem ");
        return mFpControllerNative.initFPSystem();
    }

    public int identifyCredentialREQ (int fpIndex) {
        //Log.d(TAG," identifyCredentialREQ 222  mIdentifying = "+mIdentifying);
        if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
            //Log.d(TAG," identifyCredentialREQ no keyguard is showing !!! ");
            return -1;
        }
        if (mPowerManager != null && !mPowerManager.isScreenOn()) {
            //Log.d(TAG," identifyCredentialREQ screen is off !!! ");
            return -1;
        }
//        if (mCanceling) {
//            mNeedReqAfterCanceling = true;
//            return -1;
//        } else {
//            mNeedReqAfterCanceling = false;
//        }
        if (mIdentifying) {
            //Log.d(TAG," identifyCredentialREQ  mIdentifying before return");
	    return -1;
        }
        if (!hasFingerEnabled()) {
            //Log.d(TAG," identifyCredentialREQ  has no Finger Enabled");
            return -1;
        }
        mIdentifying = true;
        return mFpControllerNative.identifyCredentialREQ(fpIndex);
    }

    public int destroyFPSystem () {
        return mFpControllerNative.destroyFPSystem();
    }

    public void cancelOperation () {
//        //Log.d(TAG," cancelOperation begin mCanceling = "+mCanceling+":"+mIdentifying);
        if (mIdentifying) 
//        {
//             return;
//        }
        {
        	mFpControllerNative.FpCancelOperation();
        	mIdentifying = false;
        }
        	//        //Log.d(TAG," cancelOperation after cancel result = "+result);
//        if (result >= 0) {
//			aquireWakeLock();
//            mCanceling = true;
//        } else {
//            mCanceling = false;
//        }
    }

    public boolean unLock() {
        //Log.d(TAG," unLock before keyguardDone");
        try {
            mKeyguardService.keyguardDone(false, true);
        } catch (RemoteException e) {
            //Log.d(TAG," unLock RemoteException when keyguardDone");
        }
        //mKeyguardManager.dismissKeyguard();	
        return true;
    }

    private void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
    
    private void aquireWakeLock() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            mWakeLock.acquire();
        }
    }

    private void showIdentifyError(int errorCount, String msg, int timeout) {
        //Log.d(TAG," showIdentifyError  errorCount 444 = "+errorCount+":"+msg);
        /* //start activity
        Intent intent = new Intent(this,ErrorNotifyDialogActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("error_count",errorCount);
        intent.putExtra("error_msg", msg);
        startActivity(intent);
        */
        //sendBroadcast
        Intent intent = new Intent();
        intent.setAction(FINGER_PRINT_UNMATCH_ACTION);
        intent.putExtra("msg_shown", msg);
        intent.putExtra("msg_timeout", timeout);
        sendBroadcast(intent);
    }

    private void onIdentifyError() {
    	Vibrator vibAttempt = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String msg;
        mErrorCount++;
        //Log.d(TAG,"onIdentifyError before wakelock aquire 5s mErrorCount = "+mErrorCount);
        PowerManager.WakeLock timeoutWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        timeoutWakeLock.acquire(5000);
        if (mErrorCount >= FpControllerNative.IDENTIFY_MAX) {
            //mErrorCount = 0;
            msg = "You have to unlock the device manually.";
            showIdentifyError(mErrorCount, msg, UNMATCH_DIALOG_MAX_TIMEOUT);
            return;
            } else {
            //Log.d(TAG,"onIdentifyError");
            vibAttempt.vibrate(100);
            msg = "Could not identify. Please try again. ["+mErrorCount+"/"+FpControllerNative.IDENTIFY_MAX+"]";
            showIdentifyError(mErrorCount, msg, UNMATCH_DIALOG_NORMAL_TIMEOUT);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    //Log.d(TAG,"postDelayed before identifyCredentialREQ ");
                    identifyCredentialREQ(0);
                }
            }, UNMATCH_DIALOG_NORMAL_TIMEOUT);
        }
        
    }
    
    private boolean hasFingerEnabled() {
	    //Log.d(TAG,"$$$$$$ hasFingerEnabled ");
        SLFpsvcIndex fpsvcIndex = mFpControllerNative.GetFpInfo();
		boolean hasEnabled = false;
        for (int i = 0; i < fpsvcIndex.max ; i++) {
            if (fpsvcIndex.FPInfo[i].enable == 1) {
                hasEnabled = true;
				break;
			}
		}
		return hasEnabled;
	}

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
	@Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            
	    int mCallState = mTelemanager.getCallState();
            boolean mCall = false;
	    if(mCallState == TelephonyManager.CALL_STATE_OFFHOOK){
	        mCall = true;
	    }
	    String action = intent.getAction();
	    //Log.d(TAG,"$$$$$$ onReceive action = "+action);
	    if (action.equals(Intent.ACTION_SCREEN_OFF) && !(mCall)){//濡傛灉鎺ュ彈鍒板叧闂睆骞曠殑骞挎挱
		//Log.d(TAG, "#######onReceive 555 ");
	        mLock.reenableKeyguard();
	        cancelOperation();	
		/*
		PowerManagerWakeLock.acquire(FpService.this);
		Intent intent2 = new Intent(FpService.this,LockScreen.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent2);
		PowerManagerWakeLock.release();
		*/
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
		//Log.d(TAG," onReceive  ACTION_SCREEN_ON ");
		mErrorCount = 0;
		identifyCredentialREQ(0);
		/*
		mKeyguardManager.exitKeyguardSecurely(new OnKeyguardExitResult () {
		public void onKeyguardExitResult(final boolean success) {
		//Log.d(TAG,"$$$$$$$ onReceive success = "+success);
		}
		});
		*/
		//startNativeFPService();
	    } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                //Log.d(TAG," onReceive Intent.ACTION_USER_PRESENT ");
				mErrorCount = 0;
                cancelOperation();
	        } else if (action.equals(ErrorNotifyDialogActivity.ERROR_NOTIFY_DIALOG_FINISH_ACTION)) {
				if (mErrorCount < FpControllerNative.IDENTIFY_MAX) {
	                //Log.d(TAG," onReceive ERROR_NOTIFY_DIALOG_FINISH_ACTION mErrorCount = "+mErrorCount);
					identifyCredentialREQ(0);
				} else {
	                //Log.d(TAG," onReceive ERROR_NOTIFY_DIALOG_FINISH_ACTION times out of max mErrorCount = "+mErrorCount);
					mErrorCount = 0;
				}
			}
        }
    };

}
