package com.silead.fp.utils;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Message;

public class FpControllerNative {
    public static class SLFpsvcFPInfo implements Parcelable {
        public int slot;//0=false 1 = true
        public int enable;
        public String fingerName;
        public int enrollIndex;

        public SLFpsvcFPInfo() {
            // TODO Auto-generated constructor stub
        }

        public SLFpsvcFPInfo(int slot, int enable, String fingerName, int enrollIndex) {
            super();
            this.slot = slot;
            this.enable = enable;
            this.fingerName = fingerName;
            this.enrollIndex = enrollIndex;
        }

        public int getEnrollIndex() {
            return enrollIndex;
        }

        public void setEnrollIndex(int enrollIndex) {
            this.enrollIndex = enrollIndex;
        }

        public String getFingerName() {
            return fingerName;
        }

        public void setFingerName(String fingerName) {
            this.fingerName = fingerName;
        }

        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            this.slot = slot;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        //把数据写入到Parcel
        public void writeToParcel(Parcel dest, int flags) {
            // TODO Auto-generated method stub
            dest.writeString(fingerName);
            dest.writeInt(slot);
            dest.writeInt(enable);

        }

//		public static final Parcelable.Creator<SLFpsvcFPInfo> CREATOR = new Parcelable.Creator<SLFpsvcFPInfo>() {
//			//创建对象  从Parcel中获取数据
//			public SLFpsvcFPInfo createFromParcel(Parcel in) {
//				SLFpsvcFPInfo fpInfo = new SLFpsvcFPInfo();
//				fpInfo.fingerName = in.readString();
//				fpInfo.slot = in.readInt();
//				fpInfo.enable = in.readInt();
//				return fpInfo;
//			}
//
//			public SLFpsvcFPInfo[] newArray(int size) {
//				return new SLFpsvcFPInfo[size];
//			}
//		};

    }

    public class SLFpsvcIndex {
        public int total;
        public int max;
        public int wenable;
        public int frame_w;
        public int frame_h;
        public SLFpsvcFPInfo[] FPInfo;

        public SLFpsvcFPInfo[] getFPInfo() {
            return FPInfo;
        }

        public void setFPInfo(SLFpsvcFPInfo[] fPInfo) {
            FPInfo = fPInfo;
        }
    }

    public interface OnIdentifyRspCB {
        void onIdentifyRsp(int index, int result, int fingerid);
    }

    public static final String TAG = "FpControllerNative for_debug";

    public static final int CHECK_IMG_FAIL = -1;

    public static final int ENROLL_SUCCESS = 0;
    public static final int ENROLL_CANCLED = -2;
    public static final int ENROLL_NOT_SUPPORT = -3;
    public static final int ENROLL_ERROR = -4;
    public static final int ENROLL_FAIL = -5;

    public static final int ENROLLING = 0;
    public static final int REENROLL = 5;
    public static final int ENROLL_INDEX = 1;
    public static final int ENROLL_CREDENTIAL_RSP = 1;

    public static final int INIT_FP_FAIL = 0;
    public static final int INIT_FP_SUCCESS = 1;

    public static final int IDENTIFY = 2;
    public static final int IDENTIFY_SUCCESS = 0;
    public static final int IDENTIFY_TMEOUT = -1;
    public static final int IDENTIFY_CANCELED = -2;
    public static final int IDENTIFY_ERR_MATCH = -3;
    public static final int IDENTIFY_ERROR = -4;
    public static final int IDENTIFY_FAIL = -5;
    public static final int IDENTIFY_MAX = 10;

    public static final int IDENTIFY_INDEX = 2;
    public static final int IDENTIFY_CREDENTIAL_RSP = 0;

    public static final int FP_GENERIC_CB = 3;
    public static final int FP_KEY_CB = 4;
    private Handler mFPhandler;

    private OnIdentifyRspCB mOnIdentifyRsp;

    private boolean mCanceling = false;

    private DefaultHandler mDefaultHandler = new DefaultHandler();

    class DefaultHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            int result = msg.what;
            //Log.d(TAG,"handleMessage $$$$$$$ result = "+result);
            switch (result) {
                case IDENTIFY_CREDENTIAL_RSP:
                    break;
                case ENROLL_CREDENTIAL_RSP:
                    break;
                case FP_GENERIC_CB:
                    break;
                case FP_KEY_CB:
                    break;
                default:
                    break;
            }
        }
    }

    private FpControllerNative() {
        mFPhandler = mDefaultHandler;
        //Log.d(TAG,"enrollCredentialRSP $$$$$$$ handle = "+mFPhandler);
    }

    private static FpControllerNative sFpControllerNative = null;// 声明一个Emperor类的引用

    public static FpControllerNative getInstance() {// 实例化引用
        if (sFpControllerNative == null) {
            sFpControllerNative = new FpControllerNative();
            sFpControllerNative.nativeInit();
        }
        return sFpControllerNative;
    }

    public void setHandler(Handler handler) {
        mFPhandler = handler;
    }

    private native void nativeInit();

    //初始化FP  //InitFPService  初始化成功或者失败通过返回值判断
    private native int nativeInitFPSystem();

    //关闭FP 	//DeinitFPService
    private native int nativeDestoryFPSystem();

    //登记指纹
    private native int nativeEnrollCredentialREQ(int index);

    //发出消息识别指纹
    private native int nativeIdentifyCredentialREQ(int index);

    //从底层移除指纹

    private native int nativeRemoveCredential(int index);

    private native int nativeResetFPService();

    private native int nativeEnalbeCredential(int index, int enable);

    private native int nativeGetEnableCredential(int index);

    private native int nativeFpCancelOperation();

    private native int nativeSetFpInfo(SLFpsvcIndex fpindex);

    private native SLFpsvcIndex nativeGetFpInfo();

    static {
        try {
            System.loadLibrary("slfpjni");
        } catch (UnsatisfiedLinkError e) {

        }
    }

    public int initFPSystem() {
        //Log.d(TAG, "initFPSystem");
        return nativeInitFPSystem();
    }

    public int destroyFPSystem() {
        //Log.d(TAG, "destroyFPSystem");
        return nativeDestoryFPSystem();
    }

    public int enrollCredentialREQ(int index) {
        //Log.d(TAG, "enrollCredentialREQ");
        return nativeEnrollCredentialREQ(index);
    }

    public int identifyCredentialREQ(int index) {
        //Log.d(TAG, "identifyCredentialREQ");
        return nativeIdentifyCredentialREQ(index);
    }

    public int removeCredential(int index) {
        //Log.d(TAG, "removeCredential");
        return nativeRemoveCredential(index);
    }

    public int EnalbeCredential(int index, int enable) {
        //Log.d(TAG, "EnalbeCredential");
        return nativeEnalbeCredential(index, enable);
    }

    public int GetEnableCredential(int index) {
        //Log.d(TAG, "GetEnableCredential");
        return nativeGetEnableCredential(index);
    }

    public int FpCancelOperation() {
//		//Log.d(TAG, "FpCancelOperation mCanceling = "+mCanceling);
//        if (mCanceling) {
//			Log.w(TAG, "FpCancelOperation canceling, return");
//            return -1;
//		}
//		mCanceling = true;
        int result = nativeFpCancelOperation();
//		if (result < 0) {
//            mCanceling = false;
//		}
        return result;
    }

    public int resetFPService() {
        //Log.d(TAG, "resetFPService");
        return nativeResetFPService();
    }

    public SLFpsvcIndex GetFpInfo() {
        //Log.d(TAG, "GetFpInfo");
        return nativeGetFpInfo();
    }

    public int SetFpInfo(SLFpsvcIndex fpindex) {
        //Log.d(TAG, "SetFpInfo");
        return nativeSetFpInfo(fpindex);
    }

    public void setIdentifyListener(OnIdentifyRspCB callback) {
        mOnIdentifyRsp = callback;
    }

    //call from native
    private void enrollCredentialRSP(int index, int percent, int result, int area) { //percent
        //Log.d(TAG,"enrollCredentialRSP $$$$$$$ index 222 = "+index+":"+percent+":"+result);
        //Log.d(TAG,"enrollCredentialRSP $$$$$$$ handle = "+mFPhandler);
        if (result == ENROLL_CANCLED) {
            mCanceling = false;
        }
        int[] intArray = new int[4];
        intArray[0] = index;
        intArray[1] = percent;
        intArray[2] = result;
        intArray[3] = area;
        Message msg = mFPhandler.obtainMessage(ENROLL_CREDENTIAL_RSP, intArray);
        mFPhandler.sendMessage(msg);
    }

    //call from native
    private void identifyCredentialRSP(int index, int result, int fingerid) {
        //Log.d(TAG,"identifyCredentialRSP $$$$$$$ index = "+index+"result :"+result+"fingerid :"+fingerid);
        if (mOnIdentifyRsp != null) {
            mOnIdentifyRsp.onIdentifyRsp(index, result, fingerid);
        }
        if (result == ENROLL_CANCLED) {
            mCanceling = false;
        }		
        /*
        int[] intArray = new int[2];
		intArray[0] = index;
		Message msg = mFPhandler.obtainMessage(IDENTIFY_CREDENTIAL_RSP, intArray);
        	mFPhandler.sendMessage(msg);
        	*/
    }

    //call from native
    private void fpGenericCB(int index, String event_name, int result, String event_data) {
        //Log.d(TAG,"fpGenericCB $$$$$$$ index = "+index+":"+event_name+":"+result+":"+event_data+":"+mCanceling);
        if (result == ENROLL_CANCLED) {
            mCanceling = false;
        }
        Message msg = mFPhandler.obtainMessage(FP_GENERIC_CB);
        mFPhandler.sendMessage(msg);
    }

    private void slfpkeyRSP(int keyret) {
        //Message msg = mFPhandler.obtainMessage(FP_KEY_CB,keyret);
        //mFPhandler.sendMessage(msg);
    }
}

