package com.silead.fp.setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

;

public class EnrollActivity extends Activity {
    
    public int enrollIndex;
    private ProgressBar mProgressBar;
	private ImageView mFingerprintAnimator;
	private TextView mStartMessage;
	private TextView mRepeatMessage;
	private Interpolator mFastOutSlowInInterpolator;
	private Interpolator mLinearOutSlowInInterpolator;
	private AnimatedVectorDrawable mIconAnimationDrawable;
	private int mIndicatorBackgroundRestingColor;
	private int mIndicatorBackgroundActivatedColor;
    private int progressNum = 0;
    private Handler mHandler;
    Runnable mAnimationStarter = new Runnable() {
		@Override
		public void run() {
			try {
				if(!mIconAnimationDrawable.isRunning()) {
					mIconAnimationDrawable.start();
				}
			} finally {
				mHandler.postDelayed(mAnimationStarter, 1000);
			}	
		}
	};
    private boolean mIsEnrolling = false;
    private FpControllerNative mControllerNative;
    private EnrollHandler mEnrollHandler = new EnrollHandler();
    private SLFpsvcIndex fpsvcIndex;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_next_in, R.anim.slide_next_out);
        setContentView(R.layout.fp_enroll);
        
        getWindow().getDecorView().setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			
		getWindow().setStatusBarColor(Color.TRANSPARENT);

		mStartMessage = (TextView) findViewById(R.id.start_message);
        mRepeatMessage = (TextView) findViewById(R.id.repeat_message);
        mProgressBar = (ProgressBar) findViewById(R.id.fingerprint_progress_bar);
        mFingerprintAnimator = (ImageView) findViewById(R.id.fingerprint_animator);
        mIconAnimationDrawable = (AnimatedVectorDrawable) mFingerprintAnimator.getDrawable();
        mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(
                this, android.R.interpolator.fast_out_slow_in);
        mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(
                this, android.R.interpolator.linear_out_slow_in);
                
		mIndicatorBackgroundRestingColor
			= getResources().getColor(R.color.fingerprint_indicator_background_resting);
        mIndicatorBackgroundActivatedColor
			= getResources().getColor(R.color.fingerprint_indicator_background_activated);

		mHandler = new Handler();

        initHandler();
        fpsvcIndex = mControllerNative.GetFpInfo();

        if (fpsvcIndex.max > 5) {
            fpsvcIndex.max = 5;
        }
        enrollStart();
    }
    
    private void initHandler() {
        mControllerNative = FpControllerNative.getInstance();
        mControllerNative.setHandler(mEnrollHandler);
        mControllerNative.initFPSystem();
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        startIconAnimation();
    }
    
    private void startIconAnimation() {
        mAnimationStarter.run(); 
    }
    
    private void stopIconAnimation() {
        mHandler.removeCallbacks(mAnimationStarter);
        mIconAnimationDrawable.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopIconAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        enrollCancel();
        mControllerNative.destroyFPSystem();
    }

    private int enrollStart() {
        mIsEnrolling = true;
        int index = findEnrollMinIndex();
        if (index < 5 && index >= 0) {
            mControllerNative.enrollCredentialREQ(index);
            return 0;
        } else {
            return -1;
        }
    }

    public int findEnrollMinIndex() {
        int index;
        for (index = 0; index < fpsvcIndex.max; index++) {// 5 items at most
            if (fpsvcIndex.FPInfo[index].slot == 0) {// judge
                break;
            }
        }
        return index;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
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
                enrollCancel();
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void enrolling(int index, int progress, int area) {
        progressNum++;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(40);
        
        animateFlash();

        if (progressNum == 1) {
			TextView headerTextView = (TextView) findViewById(R.id.headerTextView);
			headerTextView.setText(getString(R.string.enroll_repeat_title));
			
			mStartMessage.setVisibility(View.INVISIBLE);
			mRepeatMessage.setVisibility(View.VISIBLE);
        }

        if (progress >= 0 && progress < 100) {
            animateProgress(progress);
        } else {
            if (progress >= 100) {
                animateProgress(100);
            }
            final int ind = index;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enrollSuccess(ind);
                }
            }, 500);
        }
    }

    private void enrollSuccess(final int index) {
        mIsEnrolling = false;
        progressNum = 0;
        createNewSLFpsvcFPInfo(index);

        Intent intentFinish = new Intent(EnrollActivity.this, EnrollFinish.class);
        startActivity(intentFinish);
  
        finish();
    }

    private void enrollError() {
        finish();
        Mytoast("surpass Max_Enroll_Touch_Count");
        //enrollFailed();
    }

    private void enrollnotsupport() {
        finish();
    }

    private void enrollRSP(int enrollIndex, int progress, int enrollResult,
                           int area) {
        if (enrollResult == FpControllerNative.ENROLLING) {
            enrolling(enrollIndex, progress, area);
        } else {
            if (enrollResult == FpControllerNative.ENROLL_CANCLED) {
                //textSwitcher.setText(getString(R.string.enroll_finger_remove));
            } else if (enrollResult == FpControllerNative.ENROLL_NOT_SUPPORT) {
                enrollnotsupport();
            } else if (enrollResult == FpControllerNative.ENROLL_FAIL) {
                enrollError();
            }
        }
    }

    private void identifyFinger(int identifyIndex, int identifyResult) {
        if (identifyResult == FpControllerNative.IDENTIFY_SUCCESS) {
            //textSwitcher.setText(getString(R.string.identify_success));
        } else if (identifyResult == FpControllerNative.ENROLL_CANCLED) {
            //textSwitcher.setText(getString(R.string.identify_errol));
            mControllerNative.destroyFPSystem();
        } else if (identifyResult == FpControllerNative.IDENTIFY_FAIL) {
            //textSwitcher.setText(getString(R.string.identify_fail));
            mControllerNative.destroyFPSystem();
        }
    }

    private void enrollCancel() {
        if (!mIsEnrolling) {
            return;
        }
        mIsEnrolling = false;
        mControllerNative.FpCancelOperation();
    }

    private void animateProgress(int progress) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mProgressBar.getProgress(), progress * 100);
        progressAnimator.setDuration(250);
        progressAnimator.setInterpolator(mFastOutSlowInInterpolator);
        progressAnimator.start();
    }
    
    private void animateFlash() {
        ValueAnimator anim = ValueAnimator.ofArgb(mIndicatorBackgroundRestingColor,
                mIndicatorBackgroundActivatedColor);
        final ValueAnimator.AnimatorUpdateListener listener =
                new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFingerprintAnimator.setBackgroundTintList(ColorStateList.valueOf(
                        (Integer) animation.getAnimatedValue()));
            }
        };
        anim.addUpdateListener(listener);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator anim = ValueAnimator.ofArgb(mIndicatorBackgroundActivatedColor,
                        mIndicatorBackgroundRestingColor);
                anim.addUpdateListener(listener);
                anim.setDuration(300);
                anim.setInterpolator(mLinearOutSlowInInterpolator);
                anim.start();
            }
        });
        anim.setInterpolator(mFastOutSlowInInterpolator);
        anim.setDuration(300);
        anim.start();
    }
    
    public void Mytoast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void createNewSLFpsvcFPInfo(int index) {
        String default_string = getString(R.string.finger_name_defualt);
        String fingerName = default_string + " " + (index + 1);
        fpsvcIndex = mControllerNative.GetFpInfo();
        fpsvcIndex.FPInfo[index].setFingerName(fingerName);
        fpsvcIndex.FPInfo[index].setEnrollIndex(index);
        fpsvcIndex.setFPInfo(fpsvcIndex.FPInfo);
        mControllerNative.SetFpInfo(fpsvcIndex);
    }

    @SuppressLint("HandlerLeak")
    class EnrollHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int msgwt = msg.what;
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
                    int progress = intArray[1];
                    int enrollResult = intArray[2];
                    int area = intArray[3];
                    enrollRSP(enrollIndex, progress, enrollResult, area);
                    break;
                case FpControllerNative.INIT_FP_FAIL:
                    break;
                case FpControllerNative.FP_GENERIC_CB:
                    break;
                default:
                    break;
            }
        }
    }
}
