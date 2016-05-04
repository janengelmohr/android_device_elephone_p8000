/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2011-2014. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.fmradio;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mediatek.fmradio.dialogs.NoAntennaDialog;
import com.mediatek.fmradio.dialogs.SearchChannelsDialog;
import com.mediatek.fmradio.ext.IProjectStringExt;

import java.io.File;
/* Vanzo:libing on: Thu, 15 May 2014 19:34:51 +0800
 */
import java.io.FileReader;
import java.io.IOException;
import android.app.ActivityManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemProperties;
//import com.android.featureoption.FeatureOption;
// End of Vanzo:libing

/**
 * This class interact with user, provider FM basic function and FM recording
 * function
 */
public class FmRadioActivity extends Activity implements
/* Vanzo:libing on: Thu, 15 May 2014 19:36:32 +0800
 */
        SensorEventListener,
// End of Vanzo:libing
        OnMenuItemClickListener, OnDismissListener,
        NoAntennaDialog.NoAntennaListener,
        SearchChannelsDialog.CancelSearchListener,
        FmRecordDialogFragment.OnRecordingDialogClickListener {
    // Logging
    private static final String TAG = "FmRx/Activity";

    // Dialog tags
    private static final String TAG_SEARCH = "Search";
    private static final String TAG_SAVE_RECORDINGD = "SaveRecording";
    private static final String TAG_NO_ANTENNA = "NoAntenna";

    // Use shared preference to store record or play time
    private static final String REFS_NAME = "FMRecord";
    private static final String START_RECORD_TIME = "startRecordTime";
    private static final String START_PLAY_TIME = "startPlayTime";

    // Request code
    private static final int REQUEST_CODE_FAVORITE = 1;

    // FM
    private static final String FM = "FM";
    // Short antenna support
    private static final boolean SHORT_ANNTENNA_SUPPORT = FmRadioUtils.isFmShortAntennaSupport();

    // UI views
    private TextView mTextStationName = null;
    private TextView mTextStationValue = null;
    // RDS text view
    private TextView mTextRds = null;
    // Text view display "FM"
    private TextView mTextFm = null;
    // Text View display "MHZ"
    private TextView mTextMHz = null;
    private TextView mTxtRecInfoLeft = null;
    private TextView mTxtRecInfoRight = null;
    private ImageButton mButtonDecrease = null;
    private ImageButton mButtonPrevStation = null;
    private ImageButton mButtonNextStation = null;
    private ImageButton mButtonIncrease = null;
    private ImageButton mButtonAddToFavorite = null;
    private ImageButton mButtonRecord = null;
    private ImageButton mButtonStop = null;
    private ImageButton mButtonPlayback = null;
    private Animation mAnimation = null;
    private ImageView mAnimImage = null;

    // Layout display recording file information
    private RelativeLayout mRLRecordInfo = null;

    // Menu items
    private MenuItem mMenuItemChannelList = null;
    private MenuItem mMenuItemOverflow = null;
    private MenuItem mMenuItemPower = null;
    private PopupMenu mPopupMenu = null;

    // State variables
    private boolean mIsServiceStarted = false;
    private boolean mIsServiceBinded = false;
    private boolean mNeedTuneto = false;
    private boolean mIsNeedDisablePower = false;
    private boolean mIsPlaying = false;
    private boolean mIsInRecordingMode = false;
    private boolean mIsNeedShowRecordDlg = false;
    private boolean mIsNeedShowNoAntennaDlg = false;
    private boolean mIsNeedShowSearchDlg = true;
    private boolean mIsActivityForeground = true;

    // Record variables
    private long mRecordStartTime = 0;
    private long mPlayStartTime = 0;
    private int mPrevRecorderState = FmRecorder.STATE_INVALID;
    private int mCurrentStation = FmRadioUtils.DEFAULT_STATION;
    private int mRecordState = 0;

    // Instance variables
    private FmRadioService mService = null;
    private Context mContext = null;
    private Toast mToast = null;
    private FragmentManager mFragmentManager = null;
    private AudioManager mAudioManager = null;

    // Plugin
    private IProjectStringExt mProjectStringExt = null;
/* Vanzo:libing on: Fri, 02 Aug 2013 15:31:08 +0800
 */
    private static final String GESTURE_SWITCH_FLAG = "persist.sys.gesture_switch";
    private static final String GESTURE_FMRADIO_FLAG = "persist.sys.gesture_fmradio";

    // 1.first psensor value large than the GESTURE_FMRADIO_MAX_DISTANCE
    private static int GESTURE_FMRADIO_MAX_DISTANCE = 350;
    // 2.second psensor value small than the GESTURE_FMRADIO_MIN_DISTANCE
    private static int GESTURE_FMRADIO_MIN_DISTANCE = 150;
    // delay time of the gesture run hander,200 is 0.2s
    private static final int GESTURE_RUN_POST_DELAY_TIME = 200;
    private SensorManager mSensorManager;
    private Sensor mSensor = null;
    private Handler myHandler;
    private int mPrePsensor = 0;

    private boolean mGestureEnabled;
    private boolean mFMRadioEnabled;
    private boolean mHavePsensor = true;
    private boolean mHadGesture = false;
// end of vanzo:libing
/* Vanzo:lupei on: Fri, 28 Aug 2015 14:50:36 +0800
 * add default gesture distance max min value
 */
    //private String DEFAULT_GESTURE_DIST_MAX_MIN = com.android.featureoption.FeatureOption.VANZO_FEATURE_DEFAULT_GESTURE_DISTANCE_MAX_MIN_VALUE;
// End of Vanzo:lupei

    // Service listener
    private FmRadioListener mFmRadioListener = new FmRadioListener() {
        @Override
        public void onCallBack(Bundle bundle) {
            int flag = bundle.getInt(FmRadioListener.CALLBACK_FLAG);
            Log.d(TAG, "call back method flag:" + flag);

            if (flag == FmRadioListener.MSGID_FM_EXIT) {
                mHandler.removeCallbacksAndMessages(null);
            }

            // remove tag message first, avoid too many same messages in queue.
            Message msg = mHandler.obtainMessage(flag);
            msg.setData(bundle);
            mHandler.removeMessages(flag);
            mHandler.sendMessage(msg);
        }
    };

    // Handle sdcard unmount event
    private final BroadcastReceiver mSdcardListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                Log.d(TAG, "Sd card mounted");
                return;
            }

            // If not unmount recording sd card, do nothing;
            if (!isRecordingCardUnmount(intent)) {
                return;
            }

            if (Intent.ACTION_MEDIA_EJECT.equals(action)
                    || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                FmRecordDialogFragment df = (FmRecordDialogFragment) mFragmentManager
                        .findFragmentByTag(TAG_SAVE_RECORDINGD);
                if ((null != df && df.getShowsDialog()) || mIsNeedShowRecordDlg) {
                    Log.d(TAG, "recording sd card unmounted,dismiss save dialog");
                    dismissSaveRecordingDialog();
                    mIsNeedShowRecordDlg = false;
                    showToast(getString(R.string.toast_recording_lost_warning));
                }
                Log.d(TAG, "Sd card Eject or unmounted");
            }
        }
    };

    // Button click listeners on UI
    private final View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_record:
                // if power up, record FM, else toast prompt user to power up
                Log.d(TAG, "btn record: CLICK!!");
                if (null == mService) {
                    Log.d(TAG, "mService is null");
                    return;
                }
                if (!mIsPlaying) {
                    showToast(getString(R.string.toast_powerup_before_record_warning));
                    return;
                }
                refreshRecordNotIdle();
                mRecordStartTime = SystemClock.elapsedRealtime();
                editSharedPreferences(START_RECORD_TIME, mRecordStartTime);
                mService.startRecordingAsync();
                mService.setModifiedRecordingName(null);
                break;

                case R.id.btn_stop:
                    // stop record and stop play record file
                    Log.d(TAG, "btn stop: CLICK!!");
                    if (null == mService) {
                        Log.d(TAG, "mService is null");
                        return;
                    }
                    refreshRecordNotIdle();
                    mService.stopRecordingAsync();
                    mService.stopPlaybackAsync();
                    break;

                case R.id.btn_playback:
                    Log.d(TAG, "btn playback: CLICK!!");
                    if (null == mService) {
                        Log.d(TAG, "mService is null");
                        return;
                    }
                    refreshRecordNotIdle();
                    mPlayStartTime = SystemClock.elapsedRealtime();
                    editSharedPreferences(START_PLAY_TIME, mPlayStartTime);
                    mService.startPlaybackAsync();
                    break;

                case R.id.button_add_to_favorite:
                    Log.d(TAG, "onClick AddToFavorite start");
                    updateFavoriteStation();
                    Log.d(TAG, "onClick AddToFavorite end");
                    break;

                case R.id.button_decrease:
                    tuneToStation(FmRadioUtils.computeDecreaseStation(mCurrentStation));
                    break;

                case R.id.button_increase:
                    tuneToStation(FmRadioUtils.computeIncreaseStation(mCurrentStation));
                    break;

                case R.id.button_prevstation:
                    Log.d(TAG, "onClick PrevStation");
                    // Search for the previous station.
                    seekStation(mCurrentStation, false); // false: previous station
                    // true: next station
                    break;

                case R.id.button_nextstation:
                    // Search for the next station.
                    seekStation(mCurrentStation, true); // false: previous station
                    // true: next station
                    break;

                default:
                    Log.d(TAG, "invalid view id");
                    break;
            }
        }
    };

    /**
     * Main thread handler to update UI
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "mHandler.handleMessage: what = " + msg.what +
                    ",hashcode:" + mHandler.hashCode());
            Bundle bundle;
            switch (msg.what) {
                case FmRadioListener.MSGID_REFRESH:
                refreshTimeText();
                break;

                case FmRadioListener.MSGID_POWERUP_FINISHED:
                    bundle = msg.getData();
                    boolean isPowerup = bundle
                            .getBoolean(FmRadioListener.KEY_IS_POWER_UP);
                    mIsPlaying = isPowerup;
                    Log.d(TAG, "updateFMState: FMRadio is powerup = " + isPowerup);
                    stopAnimation();
                    if (isPowerup) {
                        refreshImageButton(true);
                        refreshPopupMenuItem(true);
                        refreshActionMenuItem(true);
                    } else {
                        showToast(getString(R.string.not_available));
                    }
                    // if not powerup success, refresh power to enable.
                    refreshActionMenuPower(true);
                    break;

                case FmRadioListener.MSGID_SWITCH_ANNTENNA:
                    bundle = msg.getData();
                    boolean isSwitch = bundle
                            .getBoolean(FmRadioListener.KEY_IS_SWITCH_ANNTENNA);
                    Log.d(TAG, "[FmRadioActivity.mHandler] swtich antenna: " + isSwitch);
                    if (!isSwitch) {
                        if (mIsActivityForeground) {
                            dismissNoAntennaDialog();
                            showNoAntennaDialog();
                        } else {
                            Log.d(TAG, "need show no antenna dialog after onResume:");
                            mIsNeedShowNoAntennaDlg = true;
                        }
                        stopAnimation();
                        // if not powerup success, refresh power to enable.
                        refreshActionMenuPower(true);
                    } else {
                        mIsNeedShowNoAntennaDlg = false;
                        dismissNoAntennaDialog();
                    }
                    break;

                case FmRadioListener.MSGID_POWERDOWN_FINISHED:
                    bundle = msg.getData();
                    boolean isPowerdown = bundle
                            .getBoolean(FmRadioListener.KEY_IS_POWER_DOWN);
                    mIsPlaying = !isPowerdown;
                    refreshImageButton(false);
                    refreshActionMenuItem(false);
                    refreshPopupMenuItem(false);
                    refreshActionMenuPower(true);
                    break;

                case FmRadioListener.MSGID_TUNE_FINISHED:
                    bundle = msg.getData();
                    boolean tuneFinish = bundle.getBoolean(FmRadioListener.KEY_IS_TUNE);
                    boolean isPowerUp = bundle.getBoolean(FmRadioListener.KEY_IS_POWER_UP);
                    // when power down state, tune from channel list,
                    // will call back send mIsPowerup state.
                    mIsPlaying = mIsPlaying ? mIsPlaying : isPowerUp;

                    stopAnimation();
                    // tune finished, should make power enable
                    mIsNeedDisablePower = false;
                    float frequency = bundle.getFloat(FmRadioListener.KEY_TUNE_TO_STATION);
                    mCurrentStation = FmRadioUtils.computeStation(frequency);
                    // After tune to station finished, refresh favorite button and
                    // other button status.
                    refreshStationUI(mCurrentStation);
                    // tune fail,should resume button status
                    if (!tuneFinish) {
                        Log.d(TAG, "mHandler.tune: " + tuneFinish);
                        refreshActionMenuItem(mIsPlaying);
                        refreshImageButton(mIsPlaying);
                        refreshPopupMenuItem(mIsPlaying);
                        refreshActionMenuPower(true);
                        return;
                    }
                    refreshImageButton(true);
                    refreshActionMenuItem(true);
                    refreshPopupMenuItem(true);
                    refreshActionMenuPower(true);
                    break;

                case FmRadioListener.MSGID_SCAN_FINISHED:
                    bundle = msg.getData();
                    // cancel scan happen
                    boolean isScan = bundle.getBoolean(FmRadioListener.KEY_IS_SCAN);
                    int tuneToStation = bundle.getInt(FmRadioListener.KEY_TUNE_TO_STATION);
                    int searchedNum = bundle.getInt(FmRadioListener.KEY_STATION_NUM);
                    refreshActionMenuItem(mIsPlaying);
                    refreshImageButton(mIsPlaying);
                    refreshPopupMenuItem(mIsPlaying);
                    // ebable action menu power items
                    refreshActionMenuPower(true);

                    if (!isScan) {
                        dismissSearchDialog();
                        Log.d(TAG, "mHandler.scan canceled. not enter to channel list.");
                        return;
                    }

                    mCurrentStation = tuneToStation;
                    // After tune to station finished, refresh favorite button and
                    // other button status.
                    refreshStationUI(mCurrentStation);
                    dismissSearchDialog();

                    if (searchedNum == 0) {
                        showToast(getString(R.string.toast_cannot_search));
                        return;
                    }

                    enterChannelList();
                    // Show toast to tell user how many stations have been searched
                    showToast(getString(R.string.toast_channel_searched) + " " +
                            String.valueOf(searchedNum));
                    break;

                case FmRadioListener.MSGID_FM_EXIT:
                    finish();
                    break;

                case FmRadioListener.LISTEN_RDSSTATION_CHANGED:
                    bundle = msg.getData();
                    int rdsStation = bundle.getInt(FmRadioListener.KEY_RDS_STATION);
                    refreshStationUI(rdsStation);
                    break;

                case FmRadioListener.LISTEN_PS_CHANGED:
                case FmRadioListener.LISTEN_RT_CHANGED:
                    bundle = msg.getData();
                    String text = "";
                    String psString = bundle.getString(FmRadioListener.KEY_PS_INFO);
                    String rtString = bundle.getString(FmRadioListener.KEY_RT_INFO);
                    if ((null != psString) && (psString.length() > 0)) {
                        text += psString;
                    }
                    if ((null != rtString) && (rtString.length() > 0)) {
                        if (text.length() > 0) {
                            text += "  ";
                        }
                        text += rtString;
                    }
                    showRds(text);
                    break;

                case FmRadioListener.LISTEN_RECORDSTATE_CHANGED:
                    bundle = msg.getData();
                    int recorderState = bundle
                            .getInt(FmRadioListener.KEY_RECORDING_STATE);
                    Log.d(TAG, "FmRadioActivity.mHandler: recorderState = "
                            + recorderState);
                    updateRecordingState(recorderState);
                    break;

                case FmRadioListener.LISTEN_RECORDERROR:
                    bundle = msg.getData();
                    int errorState = bundle
                            .getInt(FmRadioListener.KEY_RECORDING_ERROR_TYPE);
                    updateRecorderError(errorState);
                    break;

                case FmRadioListener.LISTEN_RECORDMODE_CHANGED:
                    bundle = msg.getData();
                    boolean isInRecordingMode = bundle
                            .getBoolean(FmRadioListener.KEY_IS_RECORDING_MODE);
                    exitRecordingMode(isInRecordingMode);
                    break;

                case FmRadioListener.LISTEN_SPEAKER_MODE_CHANGED:
                    bundle = msg.getData();
                    boolean isSpeakerMode = bundle
                            .getBoolean(FmRadioListener.KEY_IS_SPEAKER_MODE);
                    if (null != mPopupMenu) {
                        Menu menu = mPopupMenu.getMenu();
                        menu.findItem(R.id.fm_sound_mode).setTitle(isSpeakerMode ?
                                R.string.optmenu_earphone : R.string.optmenu_speaker);
                        refreshSoundModeVisiable();
                    }
                    break;

                default:
                    Log.d(TAG, "invalid message");
                    break;
            }
            Log.d(TAG, "handleMessage");
        }
    };

    // When call bind service, it will call service connect. register call back
    // listener and initial device
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        /**
         * called by system when bind service
         *
         * @param className component name
         * @param service service binder
         */
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "FmRadioActivity.onServiceConnected start");
            mService = ((FmRadioService.ServiceBinder) service).getService();
            if (null == mService) {
                Log.e(TAG, "ServiceConnection: Error: can't get Service");
                finish();
                return;
            }

            mService.registerFmRadioListener(mFmRadioListener);
            if (!mService.isServiceInited()) {
                Log.d(TAG, "ServiceConnection: FM service is not init");
                mService.initService(mCurrentStation);
                powerUpFm();
            } else {
                Log.d(TAG, "ServiceConnection: FM service is already init");
                if (mService.isDeviceOpen()) {

                    // ALPS01768123 Need to power up for this case
                    // Without earphone->Start FM->Click Home->Plug in earphone->Enter FM
                    // -> Power Menu will be power down status and disabled
                    // ALPS01811383 Cannot power up when in call, because cannot get AudioFocus
                    if (!mService.isPowerUp() && mService.isModeNormal()) {
                        Log.d(TAG, "Need to power up auto for this case");
                        powerUpFm();
                    } else if (!mService.isPowerUp() && !mService.isModeNormal()) {
                        // if onStop() has been called before resuming this activity during
                        // phone call, need check whether show no-antenna dialog.
                        if (!mIsNeedShowNoAntennaDlg && !mService.isAntennaAvailable()
                                && !FmRadioUtils.isFmShortAntennaSupport()) {
                            Log.w(TAG, "Need to show no antenna dialog for plug out earphone in onPause state");
                            dismissNoAntennaDialog();
                            showNoAntennaDialog();
                        }
                    }

                    // tunetostation during changing language,we need to tune
                    // again when service bind success
                    if (mNeedTuneto) {
                        tuneToStation(mCurrentStation);
                        mNeedTuneto = false;
                    }
                    updateCurrentStation();
                    boolean isPlaying = mService.isPowerUp();
                    // back key destroy activity, mIsPlaying will be the default
                    // false.
                    // but it may be true. so the power button will be in wrong
                    // state.
                    mIsPlaying = isPlaying;
                    updateMenuStatus();
                    updateDialogStatus();

                    // check whether set play back button enable
                    if (!isRecordFileExist()) {
                        mButtonPlayback.setEnabled(false);
                    }
                    updateRds();
                    restoreRecorderState();
                } else {
                    // Normal case will not come here
                    // Need to exit FM for this case
                    Log.e(TAG, "ServiceConnection: service is exiting while start FM again");
                    exitService();
                    finish();
                }
            }
            Log.d(TAG, "FmRadioActivity.onServiceConnected end");
        }

        /**
         * When unbind service will call this method
         *
         * @param className The component name
         */
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "FmRadioActivity.onServiceDisconnected");
        }
    };

    /**
     * Update FM recording state with given state from FM service
     *
     * @param recorderState The recorder state
     */
    private void updateRecordingState(int recorderState) {
        mRecordState = recorderState;
        refreshRecordingStatus(recorderState);

        switch (recorderState) {
            case FmRecorder.STATE_RECORDING:
                showToast(getString(R.string.toast_start_recording));
                Log.d(TAG, "updateRecordingState:startRecording");
                mHandler.sendEmptyMessage(FmRadioListener.MSGID_REFRESH);
                break;

            case FmRecorder.STATE_PLAYBACK:
                mHandler.sendEmptyMessage(FmRadioListener.MSGID_REFRESH);
                break;

            case FmRecorder.STATE_IDLE:
                Log.d(TAG, "updateRecordingState:remove message");
                mHandler.removeMessages(FmRadioListener.MSGID_REFRESH);
                break;

            default:
                mHandler.removeMessages(FmRadioListener.MSGID_REFRESH);
                break;
        }
    }

    /**
     * Update FM recorder error with given error from FM service
     *
     * @param errorType The record error type
     */
    private void updateRecorderError(int errorType) {
        Log.d(TAG, "updateRecorderError.errorType: " + errorType);
        String showString = null;
        // In FMRecorder.startRecording() error occurs,then we should set
        // mButtonRecord enable, because we set mButtonRecord disable ago
        refreshRecordIdle();
        switch (errorType) {
            case FmRecorder.ERROR_SDCARD_NOT_PRESENT:
                showString = getString(R.string.toast_sdcard_missing);
                break;

            case FmRecorder.ERROR_SDCARD_INSUFFICIENT_SPACE:
                showString = getString(R.string.toast_sdcard_insufficient_space);
                break;

            case FmRecorder.ERROR_RECORDER_INTERNAL:
                showString = getString(R.string.toast_recorder_internal_error);
                break;

            case FmRecorder.ERROR_PLAYER_INTERNAL:
                showString = getString(R.string.toast_player_internal_error);
                break;

            case FmRadioListener.NOT_AUDIO_FOCUS:
                showString = getString(R.string.not_available);
                if (isRecordFileExist()) {
                    refreshPlaybackIdle(true);
                }
                break;
            case FmRecorder.ERROR_RECORDER_INVALID_STATE:
                showString = getString(R.string.toast_powerup_before_record_warning);
                break;
            default:
                Log.d(TAG, "invalid recorder error");
                break;
        }

        showToast(showString);
    }

    /**
     * Update FM recorder mode with given mode from FM service
     *
     * @param isInRecordingMode The current mode, true if current is in
     *            recording mode
     */
    private void exitRecordingMode(boolean isInRecordingMode) {
        refreshImageButton(mIsPlaying);
        refreshActionMenuItem(mIsPlaying);
        refreshPopupMenuItem(mIsPlaying);
        refreshActionMenuPower(true);
        if (!isInRecordingMode) {
            // Service has already set recording mode to false, need to modify
            // UI here
            mIsInRecordingMode = false;
            switchRecordLayout(isInRecordingMode);
        }
    }

    /**
     * Format the given time to be string by hour:minute:second
     *
     * @param time The time to be formated
     *
     * @return string The formated time
     */
    private String getTimeString(int time) {
        final int oneHour = 3600;
        int hour = time / oneHour;
        final int minuteSecond = 60;
        int minute = (time / minuteSecond) % minuteSecond;
        int second = time % minuteSecond;
        String timeString = null;

        if (hour > 0) {
            final String timeFormatLong = "%02d:%02d:%02d";
            timeString = String.format(timeFormatLong, hour, minute, second);
        } else {
            final String timeFormatShort = "%02d:%02d";
            timeString = String.format(timeFormatShort, minute, second);
        }

        return timeString;
    }

    /**
     * Update the favorite UI state
     */
    private void updateFavoriteStation() {
        String showString = null;
        // Judge the current output and switch between the devices.
        if (FmRadioStation.isFavoriteStation(mContext, mCurrentStation)) {
            // Need to delete this favorite channel.
            String stationName = FmRadioStation.getStationName(mContext,
                    mCurrentStation, FmRadioStation.STATION_TYPE_FAVORITE);
            FmRadioStation.updateStationToDb(mContext, stationName,
                    FmRadioStation.STATION_TYPE_SEARCHED, mCurrentStation);
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_off_selector);
            mTextStationName.setText("");
            showString = mProjectStringExt.getProjectString(mContext,
                    R.string.toast_channel_deleted, R.string.toast_channel_deleted1);
        } else {
            // Add the station to favorite
            String stationName = FmRadioStation.getStationName(mContext,
                    mCurrentStation, FmRadioStation.STATION_TYPE_SEARCHED);
            if (FmRadioStation.isStationExist(mContext, mCurrentStation,
                    FmRadioStation.STATION_TYPE_SEARCHED)) {
                FmRadioStation.updateStationToDb(mContext, stationName,
                        FmRadioStation.STATION_TYPE_FAVORITE,
                        mCurrentStation);
            } else {
                FmRadioStation.insertStationToDb(mContext, stationName,
                        mCurrentStation, FmRadioStation.STATION_TYPE_FAVORITE);
            }
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_on_selector);
            mTextStationName.setText(stationName);
            showString = mProjectStringExt.getProjectString(mContext,
                    R.string.toast_channel_added, R.string.toast_channel_added1);
        }
        showToast(showString);
    }

    /**
     * Edit value which saved in shared preference
     *
     * @param key The preferences editor key
     * @param time The preferences editor value
     */
    private void editSharedPreferences(String key, long time) {
        SharedPreferences sharedPreferences = getSharedPreferences(REFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, time);
        editor.commit();
    }

    /**
     * Called when the activity is first created, initial variables
     *
     * @param savedInstanceState The saved bundle in onSaveInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/* Vanzo:libing on: Thu, 15 May 2014 18:13:02 +0800
 * add for gesture
 */
        Log.d(TAG, "FMRadio onCreate initPsensorManager()");
        if (isFmRadioGestureSupport()) {
            initPsensorManager();
        }
// End of Vanzo:libing
        Log.i(TAG, "FmRadioActivity.onCreate start");
        // Bind the activity to FM audio stream.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.main);
        mFragmentManager = getFragmentManager();
        mContext = getApplicationContext();

        FmRadioStation.initFmDatabase(mContext);
        mProjectStringExt = ExtensionUtils.getExtension(mContext);
        initUiComponent();
        registerButtonClickListener();
        registerSdcardReceiver();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Log.d(TAG, "FmRadioActivity.onCreate end");
    }
/* Vanzo:libing on: Thu, 15 May 2014 18:14:21 +0800
 * add for gesture
 */
    public void initPsensorManager() {
	return;
        /*File file = new File("/sys/bus/platform/drivers/als_ps/ps");
        if (!file.exists()) {
            mHavePsensor = false;
        }
        // has no psensor return
        if (mHavePsensor == false) {
            Log.w(TAG, "initPsensorManager() has no psensor !!!");
            return;
        }
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
*/
/* Vanzo:lupei on: Fri, 28 Aug 2015 14:51:36 +0800
 * add default gesture distance max min value
 */
        /*android.util.Log.d(TAG, "DEFAULT_GESTURE_DIST_MAX_MIN = "+DEFAULT_GESTURE_DIST_MAX_MIN);
        if (!("".equals(DEFAULT_GESTURE_DIST_MAX_MIN))) {
            String[] value = DEFAULT_GESTURE_DIST_MAX_MIN.split(",");
            if (value[0]!= "" && value[1] != "") {
                GESTURE_FMRADIO_MAX_DISTANCE = Integer.parseInt(value[0]);
                GESTURE_FMRADIO_MIN_DISTANCE = Integer.parseInt(value[1]);
            }
        }*/
// End of Vanzo:lupei

    }

    public Runnable myRunnable = new Runnable() {
        @Override
            public void run() {
                File file = new File("/sys/bus/platform/drivers/als_ps/ps");
                if (!file.exists()) {
                    file = new File("/sys/bus/platform/drivers/als_ps_cm3628/ps");
                }
                if (file.exists()) {
                    String pSensorValues2 = readFile(file);
                    if (pSensorValues2.trim().contains("-")) {
                        return;
                    }
                    if (pSensorValues2.trim().contains("0x")) {
                        mPrePsensor = Integer.parseInt(pSensorValues2.trim().substring(2), 16);
                    } else {
                        mPrePsensor = Integer.parseInt(pSensorValues2.trim());
                    }
                    android.util.Log.d(TAG, "fmradio isGestureOpen " + isGestureOpen());
                    if (isGestureOpen() && isTopActivity()) {
                        if (mPrePsensor > GESTURE_FMRADIO_MAX_DISTANCE) {
                            mHadGesture = true;
                        } else if (mHadGesture && mPrePsensor < GESTURE_FMRADIO_MIN_DISTANCE) {
                            mHadGesture = false;
                            seekStation(mCurrentStation, true); // false: previous station
                        }
                    }
                }
                myHandler.postDelayed(myRunnable, GESTURE_RUN_POST_DELAY_TIME);
            }
    };

    private boolean isTopActivity() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
        String topClassName = cn.getClassName();
        String myClassName = FmRadioActivity.this.getComponentName().getClassName();

        return topClassName.equalsIgnoreCase(myClassName);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private static String readFile(File fn) {
        FileReader f;
        int len;

        f = null;
        try {
            f = new FileReader(fn);
            String s = "";
            char[] cbuf = new char[200];
            while ((len = f.read(cbuf, 0, cbuf.length)) >= 0) {
                s += String.valueOf(cbuf, 0, len);
            }
            return s;
        } catch (IOException ex) {
            return "0";
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ex) {
                    return "0";
                }
            }
        }
    }

    public void onSensorChanged(SensorEvent event) {
    }

    private boolean isGestureOpen() {
        mGestureEnabled = SystemProperties.getInt(GESTURE_SWITCH_FLAG , 0) == 1 ? true : false;
        mFMRadioEnabled = SystemProperties.getInt(GESTURE_FMRADIO_FLAG , 0) == 1 ? true : false;

        return mGestureEnabled && mFMRadioEnabled;
    }

    private void stopPsensor() {
        mSensorManager.unregisterListener(this);
        if (myHandler != null) {
            myHandler.removeCallbacks(myRunnable);
        }
    }

    private void resumePsensor() {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        myHandler = new Handler();
        myHandler.post(myRunnable);
    }

    public static boolean isFmRadioGestureSupport() {
        boolean enable = false; //FeatureOption.VANZO_FEATURE_FM_RADIO_GESTURE;
        android.util.Log.i(TAG, "isFmRadioGestureSupport enable = " + enable);
        return enable;
    }
// End of Vanzo:libing
    /**
     * Go to channel list activity
     */
    private void enterChannelList() {
        Log.d(TAG, "enterChannelList");
        if (mService != null) {
            // AMS change the design for background start
            // activity. need check app is background in app code
            if (mIsActivityForeground) {
                Intent intent = new Intent();
                intent.setClass(FmRadioActivity.this, FmRadioFavorite.class);
                startActivityForResult(intent, REQUEST_CODE_FAVORITE);
            } else {
                Log.d(TAG, "enterChannelList. activity is background, not enter channel list.");
            }
        }
    }

    /**
     * Refresh the favorite button with the given station, if the station is
     * favorite station, show favorite icon, else show non-favorite icon.
     *
     * @param station The station frequency
     */
    private void refreshStationUI(int station) {
        // Change the station frequency displayed.
        mTextStationValue.setText(FmRadioUtils.formatStation(station));
        // Show or hide the favorite icon
        if (FmRadioStation.isFavoriteStation(mContext, station)) {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_on_selector);
            mTextStationName.setText(FmRadioStation.getStationName(mContext,
                    station, FmRadioStation.STATION_TYPE_FAVORITE));
        } else {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_off_selector);
            mTextStationName.setText("");
        }
    }

    @SuppressWarnings("deprecation")
    private void restoreConfiguration() {
        // after configuration change, need to reduction else the UI is abnormal
        if (null != getLastNonConfigurationInstance()) {
            Log.d(TAG,
                    "Configration changes,activity restart,need to reset UI!");
            Bundle bundle = (Bundle) getLastNonConfigurationInstance();
            if (null == bundle) {
                return;
            }
            mPrevRecorderState = bundle.getInt("mPrevRecorderState");
            mRecordState = bundle.getInt("mRecordState");
            mIsNeedShowRecordDlg = bundle.getBoolean("mIsFreshRecordingStatus");
            // mIsNeedShowNoAntennaDlg = bundle.getBoolean("mIsNeedShowNoAntennaDlg");
            mIsNeedShowSearchDlg = bundle.getBoolean("mIsNeedShowSearchDlg");
            // we doesn't get it from service because the service may be
            // null because not bind
            mIsInRecordingMode = bundle.getBoolean("isInRecordingMode");
            mIsPlaying = bundle.getBoolean("mIsPlaying");
            Log.d(TAG, "bundle = " + bundle);
        }
    }

    /**
     * Start and bind service, reduction variable values if configuration
     * changed
     */
    @Override
    public void onStart() {
        super.onStart();
        FmRadioService.setActivityIsOnStop(false);
        Log.d(TAG, "FmRadioActivity.onStart start");
        // Should start FM service first.
        if (null == startService(new Intent(FmRadioActivity.this,
                FmRadioService.class))) {
            Log.e(TAG, "Error: Cannot start FM service");
            return;
        }

        mIsServiceStarted = true;
        mIsServiceBinded = bindService(new Intent(FmRadioActivity.this,
                FmRadioService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        if (!mIsServiceBinded) {
            Log.e(TAG, "Error: Cannot bind FM service");
            finish();
            return;
        }
        restoreConfiguration();
        Log.d(TAG, "FmRadioActivity.onStart end");
    }

    /**
     * Refresh UI, when stop search, dismiss search dialog, pop up recording
     * dialog if FM stopped when recording in background
     */
    @Override
    public void onResume() {
        super.onResume();
/* Vanzo:libing on: Thu, 15 May 2014 18:17:38 +0800
 * add for gesture
 */
        Log.d(TAG, "FMRadio onResume isGestureOpen = " + isGestureOpen());
        if (isFmRadioGestureSupport() && isGestureOpen()) {
            resumePsensor();
        }
// End of Vanzo:libing
        Log.d(TAG, "FmRadioActivity.onResume start");
        mIsActivityForeground = true;
        if (null == mService) {
            Log.d(TAG, "service has not bind finished");
            mIsNeedShowNoAntennaDlg = false;
            return;
        }
        updateMenuStatus();
        updateDialogStatus();
        if (!isRecordFileExist()) {
            mButtonPlayback.setEnabled(false);
        }
        checkNoAntennaDialogInOnResume();
        Log.d(TAG, "FmRadioActivity.onResume end");
    }

    /**
     * In call and plug out earphone(in onPause state), need to show no antenna dialog but
     * use mIsNeedShowNoAntennaDlg to save the state, because there is no where to show this dialog after onResume() for this case
     */
    private void checkNoAntennaDialogInOnResume() {
        if (mIsNeedShowNoAntennaDlg && mService != null && !mService.isAntennaAvailable()
                && !FmRadioUtils.isFmShortAntennaSupport()) {
            Log.w(TAG, "Need to show no antenna dialog for plug out earphone in onPause state");
            dismissNoAntennaDialog();
            showNoAntennaDialog();
        }
    }

    /**
     * When activity is paused call this method, indicate activity enter
     * background if press exit, power down FM
     */
    @Override
    public void onPause() {
        Log.d(TAG, "start FmRadioActivity.onPause");
        mIsActivityForeground = false;
        /**
         * Should dismiss before call onSaveInstance, or it will resume
         * automatic
         */
        mIsNeedShowSearchDlg = true;
        dismissSearchDialog();

        /**
         * should dismiss before call onSaveInstance, or it will resume
         * automatic.
         */
        FmRecordDialogFragment df = (FmRecordDialogFragment) mFragmentManager
                .findFragmentByTag(TAG_SAVE_RECORDINGD);
        if (null != df && df.getShowsDialog()) {
            Log.d(TAG, "onPause.dismissSaveRecordingDialog()");
            if (mService != null) {
                mService.setModifiedRecordingName(df.getRecordingNameToSave());
            }
            dismissSaveRecordingDialog();
            mIsNeedShowRecordDlg = true;
        }

        // Need to dismiss avoid AMS popup this dialog again for power up will show this dialog
        if (dismissNoAntennaDialog()) {
            mIsNeedShowNoAntennaDlg = true;
        }

        Log.d(TAG, "end FmRadioActivity.onPause");
        super.onPause();
    }

    /**
     * Called when activity enter stopped state, unbind service, if exit
     * pressed, stop service
     */
    @Override
    public void onStop() {
        FmRadioService.setActivityIsOnStop(true);
        Log.d(TAG, "start FmRadioActivity.onStop");
        if (mIsServiceBinded) {
            unbindService(mServiceConnection);
            mIsServiceBinded = false;
        }
        mIsNeedShowNoAntennaDlg = false;
        Log.d(TAG, "end FmRadioActivity.onStop");
        super.onStop();
/* Vanzo:libing on: Thu, 15 May 2014 18:15:55 +0800
 * add for gesture
 */
        Log.d(TAG, "FMRadio onStop stopPsensor()");
        if (isFmRadioGestureSupport()) {
            stopPsensor();
        }
// End of Vanzo:libing
    }

    /**
     * W activity destroy, unregister broadcast receiver and remove handler
     * message
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "start FmRadioActivity.onDestroy");
        unregisterSdcardReceiver();
        // need to call this function because if doesn't do this,after
        // configuration change will have many instance and recording time
        // or playing time will not refresh
        // Remove all the handle message
        mHandler.removeCallbacksAndMessages(null);
        if (mService != null) {
            mService.unregisterFmRadioListener(mFmRadioListener);
        }
        mFmRadioListener = null;
        if (null != mPopupMenu) {
            mPopupMenu.dismiss();
            mPopupMenu = null;
        }
        Log.d(TAG, "end FmRadioActivity.onDestroy");
        super.onDestroy();
    }

    /**
     * Create options menu
     *
     * @param menu The option menu
     *
     * @return true or false indicate need to handle other menu item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "start FmRadioActivity.onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fm_action_bar, menu);
        mMenuItemChannelList = menu.findItem(R.id.fm_channel_list);
        mMenuItemOverflow = menu.findItem(R.id.fm_menu);
        mMenuItemPower = menu.findItem(R.id.fm_power);
        Log.d(TAG, "end FmRadioActivity.onCreateOptionsMenu");
        return true;
    }

    /**
     * Prepare options menu
     *
     * @param menu The option menu
     *
     * @return true or false indicate need to handle other menu item
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "start FmRadioActivity.onPrepareOptionsMenu");
        mMenuItemChannelList.setVisible(!mIsInRecordingMode);
        mMenuItemOverflow.setVisible(!mIsInRecordingMode);
        mMenuItemPower.setVisible(!mIsInRecordingMode);
        if (null == mService) {
            Log.d(TAG, "mService is null");
            return !mIsInRecordingMode;
        }

        boolean isPlaying = mService.isPowerUp();
        boolean isMakePowerdown = mService.isMakePowerDown();
        boolean isSeeking = mService.isSeeking();
        boolean isScan = mService.isScanning();
        // if fm power down by other app, should enable power menu, make it to
        // powerup.
        refreshActionMenuItem((isSeeking || isScan) ? false : isPlaying);
        refreshActionMenuPower((isScan || isSeeking)
                ? false
                : (isPlaying || (isMakePowerdown && !mIsNeedDisablePower)));

        // check whether show animation
        if (isSeeking) {
            Log.d(TAG, "onPrepareOptionsMenu: it is seeking");
            startAnimation();
        }

        Log.d(TAG, "end FmRadioActivity.onPrepareOptionsMenu");
        return !mIsInRecordingMode;
    }

    /**
     * Handle event when option item selected
     *
     * @param item The clicked item
     *
     * @return true or false indicate need to handle other menu item or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "start FmRadioActivity.onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.fm_channel_list:
                refreshImageButton(false);
                refreshActionMenuItem(false);
                refreshPopupMenuItem(false);
                refreshActionMenuPower(false);
                // Show favorite activity.
                enterChannelList();
                break;

            case R.id.fm_power:
                Log.d(TAG, "click fm_power menu");
                if (mIsPlaying) {
                    refreshImageButton(false);
                    refreshActionMenuItem(false);
                    refreshPopupMenuItem(false);
                    refreshActionMenuPower(false);
                    exitService();
                    break;
                }
                powerUpFm();
                break;

            case R.id.fm_menu:
                item.setEnabled(false);

                mPopupMenu = new PopupMenu(mContext, findViewById(R.id.fm_menu));
                Menu menu = mPopupMenu.getMenu();
                mPopupMenu.getMenuInflater().inflate(R.menu.fm_menu, menu);
                mPopupMenu.setOnMenuItemClickListener(this);
                mPopupMenu.setOnDismissListener(this);

                boolean isPlaying = mService.isPowerUp();
                if (isPlaying) {
                    menu.findItem(R.id.fm_record).setEnabled(true);
                    menu.findItem(R.id.fm_sound_mode).setEnabled(!mService.isBtConnected());
                    menu.findItem(R.id.fm_sound_mode).setTitle(
                            mService.isSpeakerUsed() ? R.string.optmenu_earphone
                                    : R.string.optmenu_speaker);
                    menu.findItem(R.id.fm_search).setEnabled(true);
                }
                refreshSoundModeVisiable();
                mPopupMenu.show();
                break;

            default:
                Log.e(TAG, "Error: Invalid options menu item.");
                break;
        }
        Log.d(TAG, "end FmRadioActivity.onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check whether antenna is available
     *
     * @return true or false indicate antenna available or not
     */
    private boolean isAntennaAvailable() {
        return mAudioManager.isWiredHeadsetOn();
    }

    /**
     * When on activity result, tune to station which is from channel list
     *
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The intent from channel list
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            if (REQUEST_CODE_FAVORITE != requestCode) {
                Log.e(TAG, "Error: Invalid requestcode.");
                return;
            }
            int iStation = data.getIntExtra(
                    FmRadioFavorite.ACTIVITY_RESULT, mCurrentStation);
            // Tune to this station.
            mCurrentStation = iStation;
            // if tune from channel list, we should disable power menu,
            // especially for
            // power down state
            mIsNeedDisablePower = true;
            Log.d(TAG, "onActivityForReult:" + mIsNeedDisablePower);
            if (null == mService) {
                Log.d(TAG, "activity.onActivityResult mService is null");
                mNeedTuneto = true;
                return;
            }
            tuneToStation(iStation);
            return;
        }

        if (FmRadioStation.isFavoriteStation(mContext, mCurrentStation)) {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_on_selector);
            mTextStationName.setText(FmRadioStation.getStationName(
                    mContext, mCurrentStation,
                    FmRadioStation.STATION_TYPE_FAVORITE));
        } else {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_off_selector);
            mTextStationName.setText("");
        }

        // Do not handle other result.
        Log.v(TAG, "The activity for requestcode " + requestCode
                + " does not return any data.");
    }

    /**
     * Start animation
     */
    private void startAnimation() {
        mAnimImage.setAnimation(mAnimation);
        mAnimImage.setVisibility(View.VISIBLE);
        Log.d(TAG, "FmRadioActivity.startAnimation end");
    }

    /**
     * Stop animation
     */
    private void stopAnimation() {
        mAnimImage.setVisibility(View.INVISIBLE);
        mAnimImage.setAnimation(null);
    }

    /**
     * Restore recorder state from shared preference
     */
    private void restoreRecorderState() {
        // here should do some recorder related.
        mIsInRecordingMode = mService.getRecordingMode();
        mRecordState = mService.getRecorderState();
        RelativeLayout recInfoBar = (RelativeLayout) findViewById(R.id.rl_recinfo);
        // if recording or play backing state, should send message trigger
        // refresh.
        if ((FmRecorder.STATE_RECORDING == mRecordState)
                || (FmRecorder.STATE_PLAYBACK == mRecordState)) {
            SharedPreferences sharedPreferences = getSharedPreferences(REFS_NAME, 0);
            mRecordStartTime = sharedPreferences.getLong(START_RECORD_TIME, 0);
            mPlayStartTime = sharedPreferences.getLong(START_PLAY_TIME, 0);
            recInfoBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "&&&sendemptyMessage:mRecoderStart:" + mRecordStartTime);
            mHandler.sendEmptyMessage(FmRadioListener.MSGID_REFRESH);
        } else {
            recInfoBar.setVisibility(View.GONE);
        }
        // if remove from app list, it will make recorder ui confused.
        switchRecordLayout(mIsInRecordingMode);
        changeRecordingMode(mIsInRecordingMode);
        if (mIsInRecordingMode) {
            refreshRecordingStatus(FmRecorder.STATE_INVALID);
        }
    }

    /**
     * Power up FM
     */
    private void powerUpFm() {
        Log.v(TAG, "start powerUpFm");
        refreshImageButton(false);
        refreshActionMenuItem(false);
        refreshPopupMenuItem(false);
        refreshActionMenuPower(false);
        startAnimation();
        mService.powerUpAsync(FmRadioUtils.computeFrequency(mCurrentStation));
        Log.v(TAG, "end powerUpFm");
    }

    private void setSpeakerPhoneOn(boolean isSpeaker) {
        if (isSpeaker) {
            Log.v(TAG, "UseSpeaker");
            mService.setSpeakerPhoneOn(true);
        } else {
            Log.v(TAG, "UseEarphone");
            mService.setSpeakerPhoneOn(false);
        }
        if (null != mPopupMenu) {
            Menu menu = mPopupMenu.getMenu();
            menu.findItem(R.id.fm_sound_mode).setTitle(mService.isSpeakerUsed() ?
                    R.string.optmenu_earphone : R.string.optmenu_speaker);
        }
    }

    /**
     * Tune to a station
     *
     * @param station The tune station
     */
    private void tuneToStation(final int station) {
        refreshImageButton(false);
        refreshActionMenuItem(false);
        refreshPopupMenuItem(false);
        refreshActionMenuPower(false);
        mService.tuneStationAsync(FmRadioUtils.computeFrequency(station));
        if (!mIsPlaying) {
            startAnimation();
        }
    }

    /**
     * Seek station according current frequency and direction
     *
     * @param station The seek start station
     * @param direction The seek direction
     */
    private void seekStation(final int station, boolean direction) {
        // If the seek AsyncTask has been executed and not canceled, cancel it
        // before start new.
        startAnimation();
        refreshImageButton(false);
        refreshActionMenuItem(false);
        refreshPopupMenuItem(false);
        refreshActionMenuPower(false);
        mService.seekStationAsync(FmRadioUtils.computeFrequency(station),
                direction);
    }

    private void refreshImageButton(boolean enabled) {
        mButtonDecrease.setEnabled(enabled);
        mButtonPrevStation.setEnabled(enabled);
        mButtonNextStation.setEnabled(enabled);
        mButtonIncrease.setEnabled(enabled);
    }

    // Refresh action menu except power menu
    private void refreshActionMenuItem(boolean enabled) {
        // action menu
        if (null != mMenuItemChannelList) {
            // if power down by other app, should disable channelist list, over
            // menu
            mMenuItemChannelList.setEnabled(enabled);
            mMenuItemOverflow.setEnabled(enabled);
        }
    }

    // Refresh action menu only power menu
    private void refreshActionMenuPower(boolean enabled) {
        Log.d(TAG, "refreshActionMenuPower enabled:" + enabled + ", mIsPlaying:" + mIsPlaying);
        // action menu
        if (null != mMenuItemChannelList) {
            // if fm power down by other app, should enable this button to
            // powerup.
            mMenuItemPower.setEnabled(enabled);
            mMenuItemPower.setIcon(mIsPlaying ? R.drawable.btn_fm_powerup_selector
                    : R.drawable.btn_fm_powerdown_selector);
        }
    }

    private void refreshPopupMenuItem(boolean enabled) {
        if (null != mPopupMenu) {
            Menu menu = mPopupMenu.getMenu();
            menu.findItem(R.id.fm_record).setEnabled(enabled);
            menu.findItem(R.id.fm_search).setEnabled(enabled);
            menu.findItem(R.id.fm_sound_mode).setEnabled(enabled && !mService.isBtConnected());
            refreshSoundModeVisiable();
        }
    }

    private void refreshSoundModeVisiable() {
        if (null != mPopupMenu) {
            Menu menu = mPopupMenu.getMenu();
            // Need hide only short antenna support and not plug in earphone
            boolean hideSoundMode = SHORT_ANNTENNA_SUPPORT && !mService.isAntennaAvailable();
            boolean showSoundMode = !hideSoundMode;
            menu.findItem(R.id.fm_sound_mode).setVisible(showSoundMode);
        }
    }

    private void refreshRecordNotIdle() {
        mButtonRecord.setEnabled(false);
        mButtonPlayback.setEnabled(false);
        mButtonStop.setEnabled(false);
    }

    private void refreshRecordIdle() {
        mButtonRecord.setEnabled(true);
        mButtonPlayback.setEnabled(false);
        mButtonStop.setEnabled(false);
    }

    private void refreshPlaybackIdle(boolean btnPlayBack) {
        mButtonRecord.setEnabled(true);
        mButtonPlayback.setEnabled(btnPlayBack);
        mButtonStop.setEnabled(false);
    }

    private void refreshRecording() {
        mButtonRecord.setEnabled(false);
        mButtonPlayback.setEnabled(false);
        mButtonStop.setEnabled(true);
    }

    private void refreshPlaybacking() {
        mButtonRecord.setEnabled(false);
        mButtonPlayback.setEnabled(false);
        mButtonStop.setEnabled(true);
    }

    /**
     * Called when back pressed
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "begin FmRadioActivity.onBackPressed");

        if (mIsInRecordingMode) {
            changeRecordingMode(false);
            if (null == mService) {
                Log.d(TAG, "mService is null");
                return;
            }
            // no need consider power down and other situation
            boolean isPlaying = mService.isPowerUp();
            refreshImageButton(isPlaying);
            refreshPopupMenuItem(isPlaying);
            refreshActionMenuItem(isPlaying);
            refreshActionMenuPower(true);
            if (mService != null && !mService.isAntennaAvailable()
                    && !FmRadioUtils.isFmShortAntennaSupport()) {
                Log.w(TAG, "Need to show no antenna dialog for plug out earphone in onPause state");
                dismissNoAntennaDialog();
                showNoAntennaDialog();
            }
            return;
        }

        // exit fm, disable all button
        if (!mIsPlaying && (null != mService) && !mService.isPowerUping()) {
            refreshImageButton(false);
            refreshActionMenuItem(false);
            refreshPopupMenuItem(false);
            refreshActionMenuPower(false);
            exitService();
            return;
        }

        super.onBackPressed();
        Log.d(TAG, "end FmRadioActivity.onBackPressed");
    }

    private void showToast(CharSequence text) {
        if (null == mToast) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
        Log.v(TAG, "FmRadioActivity.showToast: toast = " + text);
    }

    private void showRds(String text) {
        mTextRds.setText(text);
        mTextRds.setSelected(true);
        Log.v(TAG, "FmRadioActivity.showRds: RDS = " + text);
    }

    /**
     * Change recording mode
     *
     * @param recordingMode The current recording mode
     */
    private void changeRecordingMode(boolean recordingMode) {
        Log.d(TAG, "changeRecordingMode: " + recordingMode);
        if (mIsInRecordingMode == recordingMode) {
            Log.e(TAG, "FM already " + (recordingMode ? "in" : "NOT in")
                    + "recording mode!");
            return;
        }
        mIsInRecordingMode = recordingMode;
        mService.setRecordingModeAsync(recordingMode);
        switchRecordLayout(recordingMode);
    }

    /**
     * Switch to record layout, if in recorder mode.
     *
     * @param recordingMode true in recorder mode, false not in recorder mode
     */
    private void switchRecordLayout(boolean recordingMode) {
        ActionBar actionBar = getActionBar();
        invalidateOptionsMenu();

        // Set the action bar on the right to be up navigation
        actionBar.setDisplayHomeAsUpEnabled(recordingMode);
        actionBar.setHomeButtonEnabled(recordingMode);
        actionBar.setTitle(recordingMode ? R.string.fm_recorder_name
                : R.string.app_name);

        LinearLayout recBar = (LinearLayout) findViewById(R.id.bottom_bar_recorder);
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);

        bottomBar.setVisibility(recordingMode ? View.GONE : View.VISIBLE);
        recBar.setVisibility(recordingMode ? View.VISIBLE : View.GONE);
        mButtonAddToFavorite.setVisibility(recordingMode ? View.GONE
                : View.VISIBLE);
    }

    /**
     * Update recording UI according record state
     *
     * @param stateOverride The recording state
     */
    private void refreshRecordingStatus(int stateOverride) {
        int recorderState = FmRecorder.STATE_INVALID;

        recorderState = (stateOverride == FmRecorder.STATE_INVALID ? mService
                .getRecorderState() : stateOverride);

        Log.d(TAG, "refreshRecordingStatus: state=" + recorderState);
        switch (recorderState) {
            case FmRecorder.STATE_IDLE:
                long recordTime = mService.getRecordTime();
                if (recordTime > 0) {
                    if (isRecordFileExist()) {
                        mButtonPlayback.setEnabled(true);
                    }

                    if (FmRecorder.STATE_RECORDING == mPrevRecorderState) {
                        Log.d(TAG, "need show recorder dialog.mPrevRecorderState:" +
                                mPrevRecorderState);
                        if (mIsActivityForeground) {
                            showSaveRecordingDialog();
                        } else {
                            mIsNeedShowRecordDlg = true;
                        }
                    }
                } else {
                    mButtonPlayback.setEnabled(false);
                }

                refreshPlaybackIdle((recordTime > 0) && isRecordFileExist());
                mRLRecordInfo.setVisibility(View.GONE);
                break;

            case FmRecorder.STATE_RECORDING:
                mTxtRecInfoLeft.setText("");
                mTxtRecInfoRight.setText("");
                mTxtRecInfoLeft.setSelected(false);
                refreshRecording();
                mRLRecordInfo.setVisibility(View.VISIBLE);
                break;

            case FmRecorder.STATE_PLAYBACK:
                String recordingName = mService.getRecordingName();
                if (null == recordingName) {
                    recordingName = "";
                }
                mTxtRecInfoLeft.setText(recordingName);
                mTxtRecInfoRight.setText("");
                mTxtRecInfoLeft.setSelected(true);
                refreshPlaybacking();
                mRLRecordInfo.setVisibility(View.VISIBLE);
                break;

            case FmRecorder.STATE_INVALID:
                refreshRecordIdle();
                mRLRecordInfo.setVisibility(View.GONE);
                break;

            default:
                Log.d(TAG, "invalid record status");
                break;
        }
        mPrevRecorderState = recorderState;
        Log.d(TAG, "refreshRecordingStatus.mPrevRecorderState:" + mPrevRecorderState);
    }

    /**
     * Check whether FM recording temporary file exist
     *
     * @return true if FM recording temporary file exist, false not exist FM
     *         recording temporary file
     */
    private boolean isRecordFileExist() {
        String fileName = null;
        fileName = mService.getRecordingNameWithPath();
        // if recording file is delete by user, play button disabled
        File recordingFileToSave = new File(fileName + FmRecorder.RECORDING_FILE_EXTENSION);
        return recordingFileToSave.exists();
    }

    /**
     * use onRetainNonConfigurationInstance because after configuration change,
     * activity will destroy and create need use this function to save some
     * important variables
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        final int size = 5;
        Bundle bundle = new Bundle(size);
        bundle.putBoolean("isInRecordingMode", mIsInRecordingMode);
        bundle.putInt("mPrevRecorderState", mPrevRecorderState);
        bundle.putBoolean("mIsFreshRecordingStatus", mIsNeedShowRecordDlg);
        //bundle.putBoolean("mIsNeedShowNoAntennaDlg", mIsNeedShowNoAntennaDlg);
        bundle.putBoolean("mIsNeedShowSearchDlg", mIsNeedShowSearchDlg);
        bundle.putInt("mRecordState", mRecordState);
        bundle.putBoolean("mIsPlaying", mIsPlaying);
        Log.d(TAG, "onRetainNonConfigurationInstance() bundle:" + bundle);
        return bundle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    /**
     * Handle event about pop up menu clicked
     *
     * @param item The pop up menu item
     *
     * @return true or false indicate need to handle other menu item or not
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.d(TAG, "onMenuItemClick:" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.fm_search:
                // Don't show search dialog when activity is background
                if (!mIsActivityForeground) {
                    Log.w(TAG, "click searh menu in background, don't show search dialog");
                    return false;
                }
                mIsNeedShowSearchDlg = true;
                refreshImageButton(false);
                refreshActionMenuItem(false);
                refreshPopupMenuItem(false);
                refreshActionMenuPower(false);
                showSearchDialog();
                FmRadioStation.cleanSearchedStations(mContext);
                mService.startScanAsync();
                break;

            case R.id.fm_sound_mode:
                setSpeakerPhoneOn(!mService.isSpeakerUsed());
                break;

            case R.id.fm_record:
                changeRecordingMode(true);
                refreshRecordingStatus(FmRecorder.STATE_INVALID);
                break;

            default:
                Log.d(TAG, "invalid menu item");
                break;
        }
        return false;
    }

    /**
     * Called when PopUp menu dismissed
     *
     * @param PopUp The menu which dismiss
     */
    @Override
    public void onDismiss(PopupMenu menu) {
        Log.d(TAG, "popmenu dismiss listener:" + menu);
        invalidateOptionsMenu();
    }

    /**
     * Exit FM service
     */
    private void exitService() {
        Log.i(TAG, "exitService");
        if (mIsServiceBinded) {
            unbindService(mServiceConnection);
            mIsServiceBinded = false;
        }

        if (mIsServiceStarted) {
            boolean isSuccess = stopService(new Intent(
                    FmRadioActivity.this, FmRadioService.class));
            if (!isSuccess) {
                Log.e(TAG, "Error: Cannot stop the FM service.");
            }
            mIsServiceStarted = false;
        }
    }

    /**
     * Show no antenna dialog
     */
    public void showNoAntennaDialog() {
        NoAntennaDialog newFragment = NoAntennaDialog.newInstance();
        newFragment.show(mFragmentManager, TAG_NO_ANTENNA);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Show save recording dialog
     *
     * @param name The recording file name
     */
    public void showSaveRecordingDialog() {
        String sdcard = FmRadioService.getRecordingSdcard();
        String defaultName = mService.getRecordingName();
        String recordingName = mService.getModifiedRecordingName();
        FmRecordDialogFragment newFragment =
                new FmRecordDialogFragment(sdcard, defaultName, recordingName);
        newFragment.show(mFragmentManager, TAG_SAVE_RECORDINGD);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Show search dialog
     */
    private void showSearchDialog() {
        SearchChannelsDialog newFragment = SearchChannelsDialog.newInstance();
        newFragment.show(mFragmentManager, TAG_SEARCH);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Dismiss search dialog
     */
    private void dismissSearchDialog() {
        SearchChannelsDialog newFragment = (SearchChannelsDialog) mFragmentManager
                .findFragmentByTag(TAG_SEARCH);
        if (null != newFragment) {
            newFragment.dismissAllowingStateLoss();
        }
    }

    /**
     * Dismiss save recording dialog
     */
    private void dismissSaveRecordingDialog() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_SAVE_RECORDINGD);
        if (null != fragment) {
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * Check whether recording card is unmounted
     *
     * @param intent The intent about sdcard
     *
     * @return true or false indicate recording card unmount or not
     */
    private boolean isRecordingCardUnmount(Intent intent) {
        String sdcard = FmRadioService.getRecordingSdcard();
        String unmountSDCard = intent.getData().toString();
        Log.d(TAG, "unmount sd card file path: " + unmountSDCard);
        return unmountSDCard.equalsIgnoreCase("file://" + sdcard) ? true
                : false;
    }

    /**
     * Dismiss no antenna dialog
     */
    private boolean dismissNoAntennaDialog() {
        NoAntennaDialog newFragment = (NoAntennaDialog) mFragmentManager
                .findFragmentByTag(TAG_NO_ANTENNA);
        if (null != newFragment) {
            newFragment.dismissAllowingStateLoss();
            return true;
        }
        return false;
    }

    /**
     * Cancel search progress
     */
    public void cancelSearch() {
        Log.d(TAG, "FmRadioActivity.cancelSearch");
        mService.stopScan();
    }

    /**
     * No antenna continue to operate
     */
    @Override
    public void noAntennaContinue() {
        // We let user use the app if no antenna.
        // But we do not automatically start FM.
        Log.d(TAG, " noAntennaContinue.onClick ok to continue");
        if (isAntennaAvailable()) {
            powerUpFm();
        } else {
            Log.d(TAG, "noAntennaContinue.earphone is not ready");
            mService.switchAntennaAsync(1);
        }
    }

    /**
     * No antenna cancel to operate
     */
    @Override
    public void noAntennaCancel() {
        Log.d(TAG, " onClick Negative");
        if (mService != null && !mService.isInLockTaskMode()) {
            exitService();
        } else {
            Log.d(TAG, "No need exit Service and Activity cause current is lock mode");
        }
    }

    /**
     * Recording dialog click
     *
     * @param recordingName The new recording name
     */
    @Override
    public void onRecordingDialogClick(String recordingName) {
        mService.saveRecordingAsync(recordingName);
        mService.setModifiedRecordingName(null);
    }

    /**
     * Update rds information
     */
    private void updateRds() {
        if (mIsPlaying) {
            Bundle bundle = new Bundle(2);
            bundle.putString(FmRadioListener.KEY_PS_INFO, mService.getPS());
            bundle.putString(FmRadioListener.KEY_RT_INFO, mService.getLRText());
            Message msg = mHandler
                    .obtainMessage(FmRadioListener.LISTEN_PS_CHANGED);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }

    /**
     * Update current station according service station
     */
    private void updateCurrentStation() {
        // get the frequency from service, set frequency in activity, UI,
        // database
        // same as the frequency in service
        int freq = mService.getFrequency();
        if (FmRadioUtils.isValidStation(freq)) {
            if (mCurrentStation != freq) {
                Log.d(TAG, "frequency in service isn't same as in database");
                mCurrentStation = freq;
                FmRadioStation.setCurrentStation(mContext, mCurrentStation);
                refreshStationUI(mCurrentStation);
            }
        }
    }

    /**
     * Update button status, and dialog status
     */
    private void updateDialogStatus() {
        Log.d(TAG, "updateDialogStatus.mIsNeedShowSearchDlg:" + mIsNeedShowSearchDlg);
        boolean isScan = mService.isScanning();
        // check whether show search dialog, because it may be dismissed
        // onSaveInstance
        if (isScan && mIsNeedShowSearchDlg) {
            Log.d(TAG, "updateDialogStatus: show search dialog. isScan is " + isScan);
            mIsNeedShowSearchDlg = false;
            showSearchDialog();
        }

        // check whether show recorder dialog, when activity is foreground
        if (mIsNeedShowRecordDlg) {
            Log.d(TAG, "updateDialogStatus.resume recordDlg.mPrevRecorderState:" +
                    mPrevRecorderState);
            showSaveRecordingDialog();
            mIsNeedShowRecordDlg = false;
        }

        /*
        // check whether show no antenna dialog, when activity is foreground
        if (mIsNeedShowNoAntennaDlg) {
            Log.d(TAG, "updateDialogStatus.resume noAntennaDlg:");
            showNoAntennaDialog();
            refreshActionMenuPower(true);
            mIsNeedShowNoAntennaDlg = false;
        }
        */
    }

    /**
     * Update menu status, and animation
     */
    private void updateMenuStatus() {
        boolean isPlaying = mService.isPowerUp();
        boolean isPoweruping = mService.isPowerUping();
        boolean isSeeking = mService.isSeeking();
        boolean isScan = mService.isScanning();
        boolean isMakePowerdown = mService.isMakePowerDown();
        Log.d(TAG, "updateMenuStatus.isSeeking:" + isSeeking);
        boolean fmStatus = (isScan || isSeeking || isPoweruping);
        // when seeking, all button should disabled,
        // else should update as origin status
        refreshImageButton(fmStatus ? false : isPlaying);
        refreshPopupMenuItem(fmStatus ? false : isPlaying);
        refreshActionMenuItem(fmStatus ? false : isPlaying);
        // if fm power down by other app, should enable power button
        // to powerup.
        Log.d(TAG, "updateMenuStatus.mIsNeedDisablePower: " + mIsNeedDisablePower);
        refreshActionMenuPower(fmStatus ? false :
                (isPlaying || (isMakePowerdown && !mIsNeedDisablePower)));

        // check whether show animation
        if (isSeeking || isPoweruping) {
            Log.d(TAG, "updateMenuStatus. it is seeking or poweruping");
            startAnimation();
        }
    }

    private void initUiComponent() {
        Log.i(TAG, "initUIComponent");
        mTextRds = (TextView) findViewById(R.id.text_rds);
        mTextRds.setText("");
        mTextFm = (TextView) findViewById(R.id.text_fm);
        mTextFm.setText(FM);
        mTextMHz = (TextView) findViewById(R.id.text_mhz);
        mTextMHz.setText(R.string.fm_unit);

        mTextStationValue = (TextView) findViewById(R.id.station_value);
        mTxtRecInfoLeft = (TextView) findViewById(R.id.txtRecInfoLeft);
        mTxtRecInfoRight = (TextView) findViewById(R.id.txtRecInfoRight);
        mRLRecordInfo = (RelativeLayout) findViewById(R.id.rl_recinfo);
        mButtonRecord = (ImageButton) findViewById(R.id.btn_record);
        mButtonStop = (ImageButton) findViewById(R.id.btn_stop);
        mButtonPlayback = (ImageButton) findViewById(R.id.btn_playback);
        mButtonAddToFavorite = (ImageButton) findViewById(R.id.button_add_to_favorite);
        mTextStationName = (TextView) findViewById(R.id.station_name);
        mButtonDecrease = (ImageButton) findViewById(R.id.button_decrease);
        mButtonIncrease = (ImageButton) findViewById(R.id.button_increase);
        mButtonPrevStation = (ImageButton) findViewById(R.id.button_prevstation);
        mButtonNextStation = (ImageButton) findViewById(R.id.button_nextstation);

        // initial mPopupMenu
        mPopupMenu = new PopupMenu(mContext, findViewById(R.id.fm_menu));
        Menu menu = mPopupMenu.getMenu();
        mPopupMenu.getMenuInflater().inflate(R.menu.fm_menu, menu);

        // put favorite button here since it might be used very early in
        // changing recording mode
        mCurrentStation = FmRadioStation.getCurrentStation(mContext);
        boolean isFavoriteStation = FmRadioStation.isFavoriteStation(mContext,
                mCurrentStation);
        // If the current station is in favorite, set its icon to favorite icon;
        // else, set to none favorite icon.
        if (isFavoriteStation) {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_on_selector);
            mTextStationName.setText(FmRadioStation.getStationName(mContext,
                    mCurrentStation, FmRadioStation.STATION_TYPE_FAVORITE));
        } else {
            mButtonAddToFavorite
                    .setImageResource(R.drawable.btn_fm_favorite_off_selector);
        }

        mTextStationValue.setText(FmRadioUtils.formatStation(mCurrentStation));
        mAnimation = (Animation) AnimationUtils.loadAnimation(this,
                R.drawable.anim);
        mAnimImage = (ImageView) findViewById(R.id.iv_anim);
        mAnimImage.setVisibility(View.INVISIBLE);
    }

    private void registerButtonClickListener() {
        mButtonRecord.setOnClickListener(mButtonClickListener);
        mButtonStop.setOnClickListener(mButtonClickListener);
        mButtonPlayback.setOnClickListener(mButtonClickListener);
        mButtonAddToFavorite.setOnClickListener(mButtonClickListener);
        mButtonDecrease.setOnClickListener(mButtonClickListener);
        mButtonIncrease.setOnClickListener(mButtonClickListener);
        mButtonPrevStation.setOnClickListener(mButtonClickListener);
        mButtonNextStation.setOnClickListener(mButtonClickListener);
    }

    private void registerSdcardReceiver() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        iFilter.addDataScheme("file");
        registerReceiver(mSdcardListener, iFilter);
    }

    private void unregisterSdcardReceiver() {
        unregisterReceiver(mSdcardListener);
    }

    private void refreshTimeText() {
        Log.d(TAG, "refreshTimeText:mRecordState:" + mRecordState);
        if (!mIsInRecordingMode) {
            Log.d(TAG, "refreshTimeText:mIsInRecordingMode:" + mIsInRecordingMode);
            if (mRecordState == FmRecorder.STATE_RECORDING) {
                mService.stopRecordingAsync();
            } else if (mRecordState == FmRecorder.STATE_PLAYBACK) {
                mService.stopPlaybackAsync();
            }
            return;
        }

        final int oneSecond = 1000;
        switch (mRecordState) {
            case FmRecorder.STATE_RECORDING:
                int recordTime = (int) ((SystemClock.elapsedRealtime() - mRecordStartTime) /
                        oneSecond);
                mTxtRecInfoLeft.setText(getTimeString(recordTime));
                Log.d(TAG, "Recording time = " + mTxtRecInfoLeft.getText());
                String recordingSdcard = FmRadioService.getRecordingSdcard();
                if (!FmRadioUtils.hasEnoughSpace(recordingSdcard)) {
                    // recordTime 1s to avoid start() then quickly stop() native exception
                    if (recordTime > 1) {
                        // Insufficient storage
                        mService.stopRecordingAsync();
                    }
                }
                break;

            case FmRecorder.STATE_PLAYBACK:
                int playTime = (int) ((SystemClock.elapsedRealtime() - mPlayStartTime) / oneSecond);
                mTxtRecInfoRight.setText(getTimeString(playTime));
                Log.d(TAG, "Playing time = " + mTxtRecInfoRight.getText());
                break;

            default:
                break;
        }
        mHandler.sendEmptyMessageDelayed(FmRadioListener.MSGID_REFRESH, oneSecond);
    }
}
