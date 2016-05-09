package com.android.silead;

import android.util.Log;
public class FpService{
	
	private  boolean  serviceInited; 
	final int SL_SUCCESS = 0;
	private final String TAG = "Settings_fpservice";
	
    public FpService() {
    	serviceInited = false;
    	//Log.d(TAG, "settings fpservice() construction fun");
    }
    
    public native int InitFPService();
	public native int DeinitFPService();
    public int  InitService() {
    	int ret = -1;
		if(serviceInited == false ){
			//Log.d(TAG, "InitService()");
			ret = InitFPService();
			if(ret == SL_SUCCESS){
				serviceInited = true;
			}
		}
		return ret;
    }

    public int DeinitService() {
    	int ret = -1;
		if( serviceInited == true ){
		  //Log.d(TAG, "DeinitService()");
	      ret = DeinitFPService();
	      if(ret == SL_SUCCESS){
	    	  serviceInited = false;
	      }
		}
		return ret;
    }
	
	public native int EnrollCredential(int fpIndex);
	
	public  int EnrollNewFPCredential(int fpIndex, boolean isEnabled, int cmd){
		
		return EnrollCredential( fpIndex);
	}

	public native int IdentifyCredential(int fpIndex);
	public int IdentifyUser(int fpIndex, int cmd){
		
		return IdentifyCredential(fpIndex);
	
	}
	
	public native int RemoveCredential(int fpIndex);
	
	public native int EnalbeCredential(int fpIndex, boolean isEnabled);
	
	public native int FpCancelOperation();
	
	public native boolean GetEnableCredential(int fpIndex);
	
	static {
    	   try {
            	System.loadLibrary("SL_fp");
            } catch (UnsatisfiedLinkError e) {
                //Log.d("FPservice", "SL_fp-hardware library not found!");
            }
    }
	
	
	
}
