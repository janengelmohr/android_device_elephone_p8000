package www.sileadinc.com;


import java.util.Timer;
import java.util.TimerTask;

import pack.jni.silead_fp;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Silead_fpActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	TextView testTextView;
	TextView readTextView;
	TextView perFrTextView;
	TextView calibrationInfo;
	ImageView imageView;
	ImageView img;
	fpView fpImageFpView;
	SurfaceView sfvView;
	SurfaceHolder sHolder;
	Button start_draw_button;
	Button read_registerButton;
	Button write_registerButton;
	Handler fpHandler ;
	Handler fpHandler2;
	Handler fpHandler3;
	private Timer mTimer;
	private TimerTask fpTimerTask;
	Runnable runnable;
	Runnable runnable2;
	private int dealyTime = 1;
	int sizepixel = 1;
	private double frameCnt = 0;
	private double tmpCnt = 0;
	
	EditText addr_register_EditText;
	EditText data_register_EditText;
	
	
	
	private static boolean fp_flag = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        read_registerButton = (Button) findViewById(R.id.read_reg);
        write_registerButton = (Button) findViewById(R.id.write_reg);
        
        addr_register_EditText  = (EditText) findViewById(R.id.edit_addr);
        data_register_EditText = (EditText) findViewById(R.id.edit_data);
        
        start_draw_button = (Button) findViewById(R.id.start_draw);
        
        start_draw_button.setOnClickListener( this);
        read_registerButton.setOnClickListener(this);
        write_registerButton.setOnClickListener(this);
        
        testTextView = (TextView)findViewById(R.id.hw_init_text);
        readTextView = (TextView) findViewById(R.id.read_text);	
        perFrTextView = (TextView) findViewById(R.id.frame_in_second);
        calibrationInfo = (TextView) findViewById(R.id.calibration_info);
        calibrationInfo.setText(R.string.calibration_init);
        sfvView = (SurfaceView) findViewById(R.id.back_surface);
        sHolder = sfvView.getHolder();
        silead_fp.testString();
        if(silead_fp.silead_fp_hwInit())
        		testTextView.setText("Hardware Init OK");
        else {
				testTextView.setText("Hardware Init NG");
		}
        readTextView.setText("this is read text");
        perFrTextView.setText("show frequency when read frames in one second");

        //------------------------------------------------------------------        
        fpHandler = new Handler(){
        	@Override  
            public void handleMessage(Message msg) {  
                // TODO Auto-generated method stub  
                // do something you want
        		//fpDraw();
        		perFrTextView.setText(tmpCnt +":Frames"+"/"+1+"S");
        		tmpCnt = 0;
                super.handleMessage(msg);  
            }  
        };
        mTimer = new Timer();
        fpTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 1;
				fpHandler.sendMessage(message);
			}
		};
//--------------------------------------------------------------------		
	
		fpHandler2 = new Handler();
		runnable = new Runnable() {
			
			@Override
			synchronized public void run() {
				// TODO Auto-generated method stub
				System.out.println("before: fpDraw" );
				fpDraw();
				fpHandler2.postDelayed(this, dealyTime);
			}
		};
		fpHandler3 = new Handler();
		runnable2 = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				perFrTextView.setText(tmpCnt +":Frames"+"/"+1+"s");
        		tmpCnt = 0;
				fpHandler3.postDelayed(runnable2, 1000);
			}
		};
    }
    
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == start_draw_button) {
			if(fp_flag){
				//mTimer.schedule(fpTimerTask, 1000, 1000); 
				System.out.println("start: button event" );
				silead_fp.silead_fp_hwInit();
				fpHandler3.postDelayed(runnable2, 1000);
				fpHandler2.postDelayed(runnable, dealyTime);
				System.out.println("end: button event" );
				fp_flag = false;
				addr_register_EditText.setText("adjusting");
				start_draw_button.setText(R.string.end_fpshow_tips);
                                calibrationInfo.setText(R.string.calibration_start);
			} else {
				
//				silead_fp.silead_fp_DhwInit();
				start_draw_button.setText(R.string.st_fpshow_tips);
				//mTimer.cancel();
				silead_fp.clearIntFlag();
				fpHandler3.removeCallbacks(runnable2);
				fpHandler2.removeCallbacks(runnable);
				fp_flag = true;
			}
			
		}else if (v == read_registerButton) {
			
			synchronized (v) {
				String string = "";
				string = addr_register_EditText.getText().toString().trim();
				if (string != null) {
					
					data_register_EditText.setText(silead_fp.readAddr(string));
				}
			}
			
		}else if (v == write_registerButton) {
			synchronized (v) {
				String string_addr = "",string_data = "";
				string_data = data_register_EditText.getText().toString().trim();
				string_addr = addr_register_EditText.getText().toString().trim();
				
				if (string_addr != null && string_data != null) {
					silead_fp.writeAddr(string_addr, string_data);
				}	
			}
		}
	}
	
	
    private synchronized void fpDraw(){
    	System.out.println("start: fpDraw" );
		Canvas canvas = sHolder.lockCanvas(new Rect(0, 0
				,getWindowManager().getDefaultDisplay().getWidth()
				,getWindowManager().getDefaultDisplay().getHeight()));
		
		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(1);
		
		final long starttime = System.currentTimeMillis();
		System.out.println("start:" + starttime);
		silead_fp.writeAddr("0x88", "0x1");
		
		byte[] oneframe = silead_fp.GetOneFrame();		
		System.out.println("takes time:" + (System.currentTimeMillis() - starttime)); 
		
		
		tmpCnt ++;
		frameCnt +=1;
		readTextView.setText("one frame length in bytes:"+oneframe.length + ";the frame:"+ frameCnt+"th");
		int start_x = (getWindowManager().getDefaultDisplay().getWidth()/2-118*sizepixel/2)/2 - 80;
		int start_y = 50;
    	int temp = start_x;	
    
    	if(true)
    	for (int i = 0; i < oneframe.length; i++) {
   			if(i % 118 == 0 ){
    			start_x = temp;
    			start_y += sizepixel;
    		}else{
    			start_x += sizepixel;
    		}
   			
   			if(mPaint != null)
			mPaint.setColor(Color.rgb(oneframe[i]&0xff,
    				oneframe[i]&0xff, oneframe[i]&0xff));
    		
			if(canvas != null && mPaint != null)
    		canvas.drawRect(new RectF(start_x,start_y,start_x+sizepixel,start_y+sizepixel), mPaint);
	}
    	if(silead_fp.ReadReg(0) == 1) {
                calibrationInfo.setText(R.string.calibration_end);
                if (!"calibration done".equals(addr_register_EditText.getText().toString()))
		    Toast.makeText(this, R.string.calibration_done, Toast.LENGTH_SHORT).show();
    		addr_register_EditText.setText("calibration done");
        }
    	
    	if(canvas != null)
   		sHolder.unlockCanvasAndPost(canvas);
    }
    
    
    private void ClearDraw()
    {
    	Canvas canvas = sHolder.lockCanvas();
    	if(canvas != null)
    		canvas.drawColor(Color.BLUE);
    	sHolder.unlockCanvasAndPost(canvas);
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		//ClearDraw();
		if (fp_flag == false) {
			silead_fp.silead_fp_DhwInit();
			fpHandler2.removeCallbacks(runnable);
			fpHandler3.removeCallbacks(runnable2);
			silead_fp.clearIntFlag();
			fp_flag = true;
		}
		
		super.onDestroy();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		if (fp_flag == false) {
			silead_fp.silead_fp_DhwInit();
			fpHandler2.removeCallbacks(runnable);
			fpHandler3.removeCallbacks(runnable2);
			silead_fp.clearIntFlag();
			fp_flag = true;
		}
		super.onStop();
	}
 
}
