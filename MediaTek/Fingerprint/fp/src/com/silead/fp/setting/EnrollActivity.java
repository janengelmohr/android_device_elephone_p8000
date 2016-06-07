package com.silead.fp.setting;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

import android.annotation.SuppressLint;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class EnrollActivity extends Activity {

    private ProgressBar circularProgress;
    private ImageView fingerprintBg;
    private ImageView fingerprintAnim;
    private TextView progressPerc;
    private TextSwitcher textSwitcher;

    public int enrollIndex;
    private int progressNum = 0;

    private boolean mIsEnrolling = false;

    private FpControllerNative mControllerNative;
    private EnrollHandler mEnrollHandler = new EnrollHandler();
    private SLFpsvcIndex fpsvcIndex;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_enroll);
        circularProgress = (ProgressBar) findViewById(R.id.progressBar);
        fingerprintBg = (ImageView) findViewById(R.id.fingerprint_bg);
        fingerprintAnim = (ImageView) findViewById(R.id.fingerprint_animator);
        progressPerc = (TextView) findViewById(R.id.progress_perc);
        progressPerc.setText("0%");

        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        textSwitcher.setFactory(new ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(EnrollActivity.this);
                myText.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);

                myText.setTextSize(20);
                myText.setTextColor(Color.BLACK);
                myText.setText(getString(R.string.enroll_finger_intro));
                return myText;
            }
        });


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

        ObjectAnimator fingerAnim = ObjectAnimator.ofFloat(fingerprintAnim, "alpha", 0.13f, 0.2f, 0.13f);
        fingerAnim.setDuration(250);
        fingerAnim.start();

        if (progressNum == 1) {
            textSwitcher.setText(getString(R.string.enroll_finger_repeat));
        } else if (progressNum == 2) {
            textSwitcher.setText(getString(R.string.enroll_finger_continue));
        }

        progressPerc.setText(Integer.toString(progress) + "%");

        if (progress >= 0 && progress < 100) {
            setDialogProgress(progress);
        } else {
            if (progress >= 100) {
                setDialogProgress(100);

                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(fingerprintBg, "alpha", 0f, 1f);
                alphaAnim.setDuration(750);
                alphaAnim.start();
            }
            textSwitcher.setText(getString(R.string.enroll_finger_success));

            final int ind = index;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enrollSuccess(ind);
                }
            }, 2000);
        }
    }

    private void enrollSuccess(final int index) {
        mIsEnrolling = false;
        progressNum = 0;
        Intent intent = getIntent();
        intent.putExtra("enrollIndex", index);
        EnrollActivity.this.setResult(0, intent);
        createNewSLFpsvcFPInfo(index);
        EnrollActivity.this.finish();
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
                textSwitcher.setText(getString(R.string.enroll_finger_remove));
            } else if (enrollResult == FpControllerNative.ENROLL_NOT_SUPPORT) {
                enrollnotsupport();
            } else if (enrollResult == FpControllerNative.ENROLL_FAIL) {
                enrollError();
            }
        }
    }

    private void identifyFinger(int identifyIndex, int identifyResult) {
        if (identifyResult == FpControllerNative.IDENTIFY_SUCCESS) {
            textSwitcher.setText(getString(R.string.identify_success));
        } else if (identifyResult == FpControllerNative.ENROLL_CANCLED) {
            textSwitcher.setText(getString(R.string.identify_errol));
            mControllerNative.destroyFPSystem();
        } else if (identifyResult == FpControllerNative.IDENTIFY_FAIL) {
            textSwitcher.setText(getString(R.string.identify_fail));
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

    private void setDialogProgress(int progress) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(circularProgress, "progress", circularProgress.getProgress(), progress * 100);
        progressAnimator.setDuration(250);
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.start();
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
}
