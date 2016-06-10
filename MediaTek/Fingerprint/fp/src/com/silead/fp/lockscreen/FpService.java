package com.silead.fp.lockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.os.PowerManager;
import android.content.ServiceConnection;
import android.content.ComponentName;

import com.android.internal.policy.IKeyguardService;

import android.os.RemoteException;
import android.os.Vibrator;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;


public class FpService extends Service implements FpControllerNative.OnIdentifyRspCB {

    public static final String TAG = "FpService";
    public static final boolean DEBUG = false;
    private static final String ERROR_NOTIFY_DIALOG_FINISH_ACTION = "com.silead.fp.lockscreen.action.finish";
    private static final String FP_LOCK_SCREEN_SERVICE_ACTION = "com.silead.fp.lockscreen.service.ACTION";
    public static final String FINGER_PRINT_UNMATCH_ACTION = "com.silead.fp.lockscreen.action.UNMATCH";
    public static final String KEYGUARD_PACKAGE = "com.android.systemui";
    public static final String KEYGUARD_CLASS = "com.android.systemui.keyguard.KeyguardService";
    public static final int UNMATCH_DIALOG_NORMAL_TIMEOUT = 200;
    public static final int UNMATCH_DIALOG_MAX_TIMEOUT = 3000;

    private KeyguardManager.KeyguardLock mLock;
    private KeyguardManager mKeyguardManager;
    private TelephonyManager mTelemanager;
    private PowerManager mPowerManager;

    private FpControllerNative mFpControllerNative;
    private int mErrorCount = 0;
    private Handler mHandler = new Handler();

    private boolean mIdentifying = false;
    private IKeyguardService mKeyguardService;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mLock = mKeyguardManager.newKeyguardLock("KeyguardLock");

        mFpControllerNative = FpControllerNative.getInstance();
        initFPSystem();
        mFpControllerNative.setIdentifyListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(FP_LOCK_SCREEN_SERVICE_ACTION);
        filter.addAction(ERROR_NOTIFY_DIALOG_FINISH_ACTION);
        registerReceiver(screenReceiver, filter);

        mTelemanager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        bindService(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLock.reenableKeyguard();
        unregisterReceiver(screenReceiver);
    }

    @Override
    public void onIdentifyRsp(int index, int result, int fingerid) {
        mIdentifying = false;
        if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
            //Log.d(TAG," onIdentifyRsp no keyguard is showing !!! ");
            return;
        }
        if (result == FpControllerNative.IDENTIFY_SUCCESS) {
            mErrorCount = 0;
            boolean isScreenOn = mPowerManager.isScreenOn();
            if (isScreenOn) {
				unLock();
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(50);
			}
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


    public int initFPSystem() {
        return mFpControllerNative.initFPSystem();
    }

    public int identifyCredentialREQ(int fpIndex) {
        if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
            return -1;
        }
        if (mPowerManager != null && !mPowerManager.isScreenOn()) {
            return -1;
        }
        if (mIdentifying) {
            return -1;
        }
        if (!hasFingerEnabled()) {
            return -1;
        }
        mIdentifying = true;
        return mFpControllerNative.identifyCredentialREQ(fpIndex);
    }

    public int destroyFPSystem() {
        return mFpControllerNative.destroyFPSystem();
    }

    public void cancelOperation() {
        if (mIdentifying) {
            mFpControllerNative.FpCancelOperation();
            mIdentifying = false;
        }
    }

    public boolean unLock() {
        try {
            mKeyguardService.keyguardDone(false, true);
        } catch (RemoteException e) {
        }
        //mKeyguardManager.dismissKeyguard();	
        return true;
    }


    private void showIdentifyError(int errorCount, String msg, int timeout) {
        Intent intent = new Intent();
        intent.setAction(FINGER_PRINT_UNMATCH_ACTION);
        intent.putExtra("msg_shown", msg);
        intent.putExtra("msg_timeout", timeout);
        sendBroadcast(intent);
    }

    private void onIdentifyError() {
        String msg;
        mErrorCount++;
        PowerManager.WakeLock timeoutWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        timeoutWakeLock.acquire(5000);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 100, 100};
        v.vibrate(pattern, -1);

        if (mErrorCount >= FpControllerNative.IDENTIFY_MAX) {
            msg = getString(R.string.identify_error) + " (" + mErrorCount + "/" + FpControllerNative.IDENTIFY_MAX + ") \n" + getString(R.string.must_unlock_manually);
            showIdentifyError(mErrorCount, msg, UNMATCH_DIALOG_MAX_TIMEOUT);
        } else {
            msg = getString(R.string.identify_error) + " (" + mErrorCount + "/" + FpControllerNative.IDENTIFY_MAX + ") \n" + getString(R.string.try_again);
            showIdentifyError(mErrorCount, msg, UNMATCH_DIALOG_NORMAL_TIMEOUT);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    identifyCredentialREQ(0);
                }
            }, UNMATCH_DIALOG_NORMAL_TIMEOUT);
        }

    }

    private boolean hasFingerEnabled() {
        SLFpsvcIndex fpsvcIndex = mFpControllerNative.GetFpInfo();
        boolean hasEnabled = false;
        for (int i = 0; i < fpsvcIndex.max; i++) {
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

            int mCallState = mTelemanager.getCallState();
            boolean mCall = false;
            if (mCallState == TelephonyManager.CALL_STATE_OFFHOOK) {
                mCall = true;
            }
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF) && !(mCall)) {
                mLock.reenableKeyguard();
                cancelOperation();
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                mErrorCount = 0;
                identifyCredentialREQ(0);
            } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                mErrorCount = 0;
                cancelOperation();
            } else if (action.equals(ERROR_NOTIFY_DIALOG_FINISH_ACTION)) {
                if (mErrorCount < FpControllerNative.IDENTIFY_MAX) {
                    identifyCredentialREQ(0);
                } else {
                    mErrorCount = 0;
                }
            }
        }
    };

}
