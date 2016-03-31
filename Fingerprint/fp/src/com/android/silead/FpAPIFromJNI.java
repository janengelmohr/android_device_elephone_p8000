package com.android.silead;

import android.os.Handler;
import android.util.Log;

public class FpAPIFromJNI {
	
	public static final int ENROLL_FAIL =  -2;
	public static final int ENROLL_ERROR = -1;
	
	public static final int INIT_FP_FAIL = 0;
	public static final int INIT_FP_SUCCESS = 1 ;
	public static final int ENROLLING = 3;
	public static final int ENROLL_SUCCESS = 4;
	public static final int REENROLL = 5;
    
	private Handler  mFPhandler;
	public FpAPIFromJNI(){
		
	}
    public FpAPIFromJNI(Handler handler) {
    	mFPhandler = handler;
    }
    
    //初始化FP  //InitFPService  初始化成功或者失败通过返回值判断
    public native int InitFPSystem();
    
    //关闭FP 	//DeinitFPService 
	public native int DestoryFPSystem();
	
	//登记指纹
	public native int EnrollCredentialREQ(int index);
	
	//登记操作完成后。这里有返回值 根据不同的状态进行下一步操作
	public  void   EnrollCredentialRSP(int index)
	{
		//Log.d("[wq]","[wq]:EnrollCredentialRSP:in JAVA\n");
		//return 1;
	}
	
	//发出消息识别指纹
	public native int  IdentifyCredentialREQ(int index);
	
	//指纹识别后返回
	public native int IdentifyCredentialRSP(int index, int inret);
	
	//从底层移除指纹
	public native int RemoveCredential(int ID);
    
	
    
	static {
 	   try {
         	System.loadLibrary("slfpjni");
			//Log.d("[wq]","[wq]:loadLibrary\n");
         } catch (UnsatisfiedLinkError e) {
         }
			
 }
	
	public boolean  InitFPSystemFromCliend(){
		
		int result =InitFPSystem();
		Log.e("[wq]","[wq]:InitFPSystemFromCliend\n");
		if(INIT_FP_FAIL ==result){
			return false;
		}else{
			return true;
		}
		
	}
       
}
