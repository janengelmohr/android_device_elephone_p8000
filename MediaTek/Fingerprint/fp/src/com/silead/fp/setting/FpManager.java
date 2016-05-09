package com.silead.fp.setting;


import android.content.Context;

import com.android.silead.FpService;
public class FpManager{
	
	private FpService mjni;// = new fpservice();
	private Context context;
	
	final int SL_SUCCESS = 0;
	final int SL_HW_NOREADY = -1001;
	final int SL_MEM_ERR  = -1002;
	private final String TAG = "FpManager";
	
	//final int SL_ENROLL   = 0x8001;
	//final int SL_IDENTIFY = 0x8004;
	//final int Max_Enroll_Touch_Count = 8;
	//final int Max_Identify_Touch_Count = 3;
	
//	#define SL_SUCCESS  0
//	#define SL_HW_NOREADY -1001
//	#define SL_MEM_ERR  -1002
//	#define SL_ALG_INITERR -1003
//	#define SL_ENROLL   0x8001
//	#define SL_STORE    0x8002
//	#define SL_CANCEL   0x8003
//	#define SL_IDENTIFY 0x8004
//
//	#define SL_ENROLL_NOT_ENOUGH -1004
//	#define SL_WRONG_PARAMETERS  -1005
//	#define SL_ENROLL_ERROR      -1006
//
//	#define SL_NOT_MATCH         -1007
//	#define SL_IDENTIFY_CANCELED -1008
//	#define SL_TOUCH_ERROR       -1009
//	#define SL_IDENTIFY_ERROR    -1010
//
//	#define SL_NOT_EXIST         -1011
//	#define SL_OTHER_ERROR       -1012
	
	
	//private int Enroll_count;
	//private int Identify_count;
	
	//private int enrollResult = -1;
	//private int identifyResult = -1;
	//EnrollThread mEnrollThread = new EnrollThread();
	//IdentifyThread mIdentifyThread = new IdentifyThread();
//	final int MSG_UPDATE_ENROLL = 201;
//	final int MSG_STRAT_ENROLL  = 202;
//    final int KILL_ACTIVITY = 102;
//    final int MSG_ENROLL_FINISH = 203;
	//private Handler mHandler;
//    static {
//    	System.loadLibrary("SL_fp");
//    }
//	private CustomAdapter mCustomAdapter;
	
	public  FpManager(Context cn){
		this.context = cn;
		 mjni = new FpService();
	}
	
//	public FpManager(Context cn){
//		this.context = cn;
//		//this.mHandler = handler;
//	}
	
//	 private final int MAX_FINGER_RECORD = 5;
//	 private final String FPINDEX  = "fpIndex";
//	 private static final String FILENAME = "fpfile";
//	 private SharedPreferences sharedPrefrences;
//	 public int findFpDataMinIndex(){
//    	 int index;
//         sharedPrefrences = this.context.getSharedPreferences(FILENAME, Context.MODE_WORLD_READABLE);
//		 for( index = 0; index < MAX_FINGER_RECORD; index++){//5 items at most
//        	if(!sharedPrefrences.contains(FPINDEX + index)){//judge preferences are exsited 
//        		break;
//        	}
//    	 }
//		 return index;
//    }
	 
//	 void EnrollFpLoop(){
//		 
//		//first get fpIndex
//		int fpIndex = findFpDataMinIndex();
//		Log.d(TAG,"fpIndex = "+ fpIndex);
//    	while(true){
//    		if(Enroll_count < Max_Enroll_Touch_Count){
//    			
//    			//default enable value: true
//    		    enrollResult = SL_EnrollNewFPCredential(fpIndex, true, SL_ENROLL);
//    			if(enrollResult < 0){
//    				Enroll_count++;
//    			}
//    			//because enrollResult is from 0 to 100
//    			enrollResult = enrollResult * 36;
//    			enrollResult = enrollResult / 10;
//    			if((enrollResult > 0) && (enrollResult < 360)){
//    				Log.d(TAG, "enrollResult = %d" + enrollResult);
//    				Message msg = new Message();
//    				msg.what = MSG_UPDATE_ENROLL;
//    				msg.arg1  = enrollResult;
//    				mHandler.sendMessage(msg);
//    			}
//    			if(enrollResult >= 360){//if(enrollResult >= 100){//enrollResult is rationValue
//    				Log.d(TAG,"enroll success, enroll times = %d" + Enroll_count);
//    				Message msg = new Message();
//    				msg.what = MSG_ENROLL_FINISH;
//    				msg.arg1  = enrollResult;
//    				mHandler.sendMessage(msg);
//       			
//       				
//       				break;
//       			}
//    		}else{
//    			//count is MaxCount
//    			Log.e(TAG,"Enroll times surpass Max_Enroll_Touch_Count");
//    			break;
//    		}
//    	}
//	 }
	    
//    void IdentifyUserLoop(){
//    	while(true){
//    		if(Identify_count < Max_Identify_Touch_Count){
//    			identifyResult = SL_IdentifyUser(3, SL_IDENTIFY); //SL_EnrollNewFPCredential(1, true, SL_IDENTIFY);
//    			Log.d(TAG, "identifyResult = " + identifyResult );
//    			if(identifyResult == SL_SUCCESS){
//    				
//    				Identify_count++;
//    			}else{
//    				//failure process
//    				break;
//    			}
//    			
//    		}else{
//    			//count is MaxCount
//    			break;
//    		}
//    	}
//    }
	    
//    class EnrollThread extends Thread {  
//        public void run() {  
//        	EnrollFpLoop();
//        }  
//    }  
//    
//    class IdentifyThread extends Thread{
//    	public void run(){
//    		IdentifyUserLoop();
//    	}
//    }
    
//    public void setHandler(Handler handler){
//    	this.mHandler = handler;
//    }
//    public Handler getHandler(){
//    	return this.mHandler;
//    }
    
//	private Handler mHandler = new Handler(){
//		public void handleMessage(Message msg) {
//			
//			switch(msg.what) {
//				case MSG_STRAT_ENROLL: {
//					mEnrollThread.start();
//					Log.d(TAG,"enroll thread start");
//					break;
//				}
//				default:
//					break;
//			}
//		}
//		
//	};

    
	public int SL_InitFPService(){
		//return mjni.InitFPService();
		return mjni.InitService();
	}
	
	public void SL_DeinitFPSerice(){
		//mjni.DeinitFPService();
		mjni.DeinitService();
	}
	
	public int SL_EnrollNewFPCredential(int fpIndex, Boolean isEnabled, int cmd){
		return mjni.EnrollNewFPCredential(fpIndex, isEnabled, cmd);
	}
	
	public int SL_IdentifyUser(int fpIndex, int cmd){
		return mjni.IdentifyUser(fpIndex, cmd);
	}
	
	public int SL_RemoveCredential(int fpIndex){
		return mjni.RemoveCredential(fpIndex);
	}
	
	public int Sl_EnalbeCredential(int fpIndex, Boolean isEnabled){
	    return mjni.EnalbeCredential(fpIndex, isEnabled);
	}
	
	public int SL_FpCancelOperation(){
		return mjni.FpCancelOperation();
	}
	
	public boolean SL_GetEnableCredential(int fpIndex){
		return mjni.GetEnableCredential(fpIndex);
	}
	
}
