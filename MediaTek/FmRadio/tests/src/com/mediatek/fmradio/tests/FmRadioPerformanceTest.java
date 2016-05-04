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
 * MediaTek Inc. (C) 2010. All rights reserved.
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

package com.mediatek.fmradio.tests;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.media.AudioManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.app.Instrumentation.ActivityMonitor;
import com.jayway.android.robotium.solo.Solo;

import com.mediatek.fmradio.FmRadioActivity;
import com.mediatek.fmradio.FmRadioFavorite;
import com.mediatek.fmradio.FmRadioStation;
import com.mediatek.fmradio.R;

public class FmRadioPerformanceTest extends ActivityInstrumentationTestCase2<FmRadioActivity> {

    public FmRadioPerformanceTest(Class<FmRadioActivity> activityClass) {
        super(activityClass);
    }

    public FmRadioPerformanceTest() {
        super(FmRadioActivity.class);
    }

    private AudioManager mAudioManager = null;

    // bottom bar
    private ImageButton mButtonDecrease = null;
    private ImageButton mButtonPrevStation = null;
    private ImageButton mButtonNextStation = null;
    private ImageButton mButtonIncrease = null;
//    private ImageButton mButtonPlayStop = null;

    private ImageButton mButtonAddToFavorite = null;

    private TextView mTextViewFrequency = null;

    private static final long TIMEOUT = 5000;
    private static final long CHECK_TIME = 100;
    private static final long SHORT_TIME = 3000;
    private static final long WAIT_UI_STATE_CHANGE = 10000;
    private static final long WAIT_SCAN_FINISH = 20000;
    private static final long WAIT_TEAR_DOWN = 3000;
    private static final long SLEEP_TIME = 2000;
    private static final long RDS_TIME_OUT = 10 * 60 * 1000;
    private static final int CONVERT_RATE = 10;
    private static final int EXECUTE_TIME = 500;
    private static final String TAG = "FmRadioPerformanceTest";

    private FmRadioActivity mFmRadioActivity = null;
    private Instrumentation mInstrumentation = null;
    private ActivityMonitor mActivityMonitor = null;
    private FmRadioFavorite mFmRadioFavorite = null;
    private Solo mSolo = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Log.i(TAG, "setUp");
        setActivityInitialTouchMode(false);
        mFmRadioActivity = getActivity();
        assertNotNull(mFmRadioActivity);
        mInstrumentation = getInstrumentation();
        assertNotNull(mInstrumentation);
        mSolo = new Solo(getInstrumentation(), mFmRadioActivity);
        Context mContext = mFmRadioActivity.getApplicationContext();
        mAudioManager = (AudioManager) mFmRadioActivity.getSystemService(mContext.AUDIO_SERVICE);
        // Makesure FmRadio is playing and initialed
        waitForPowerupWithTimeout(TIMEOUT);
        boolean isPlaying = getBooleanFromVariable(mFmRadioActivity, "mIsPlaying");
        assertTrue(isPlaying);
        waitForInitedWithTimeout(TIMEOUT);
        boolean isInited = getBooleanFromVariable(mFmRadioActivity, "mIsServiceBinded");
        assertTrue(isInited);
        mButtonDecrease = (ImageButton) mFmRadioActivity.findViewById(R.id.button_decrease);
        mButtonPrevStation = (ImageButton) mFmRadioActivity.findViewById(R.id.button_prevstation);
        mButtonNextStation = (ImageButton) mFmRadioActivity.findViewById(R.id.button_nextstation);
        mButtonIncrease = (ImageButton) mFmRadioActivity.findViewById(R.id.button_increase);
//        mButtonPlayStop = (ImageButton) mFmRadioActivity.findViewById(R.id.button_play_stop);

        mButtonAddToFavorite = (ImageButton) mFmRadioActivity.findViewById(R.id.button_add_to_favorite);
        mTextViewFrequency = (TextView) mFmRadioActivity.findViewById(R.id.station_value);
    }


    private void waitForInitedWithTimeout(long timeOut) {
        Log.i(TAG, ">>>waitForInitedWithTimeout");

        long startTime = System.currentTimeMillis();
        while (!getBooleanFromVariable(mFmRadioActivity, "mIsServiceBinded")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            sleep(CHECK_TIME);
        }
        Log.i(TAG, "<<<waitForInitedWithTimeout");

    }

    private void waitForPowerupWithTimeout(long timeOut) {
        Log.i(TAG, ">>>waitForPowerupWithTimeout");

        long startTime = System.currentTimeMillis();
        while (!getBooleanFromVariable(mFmRadioActivity, "mIsPlaying")) {
            if (System.currentTimeMillis() - startTime > timeOut) {
                break;
            }
            sleep(CHECK_TIME);
        }
        Log.i(TAG, "<<<waitForPowerupWithTimeout");
    }

    /**
     * Test scan channel performance.
     */
    public void testCase00_ScanChannelPerformance() throws Exception {
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        mInstrumentation.waitForIdleSync();
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
        mInstrumentation.waitForIdleSync();
       long startTime = System.currentTimeMillis();
       Log.i(TAG, "[Performance test][FmRadio] scan channel start [" + startTime + "]");
       mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
       sleep(WAIT_SCAN_FINISH);
       mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
       sleep(SHORT_TIME);
    }

    /**
     * Test open channel performance.
     */
    /*public void testCase01_ChannelOpenPerformance() throws Exception {
        int station = getStationFromUI();
        addChannelAsFavorite(station);
        clickView(mButtonIncrease);
        sleep(SHORT_TIME);
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_channel_list, 0);
        mActivityMonitor = new ActivityMonitor("com.mediatek.fmradio.FmRadioFavorite", null, false);
        mInstrumentation.addMonitor(mActivityMonitor);
        mFmRadioFavorite = (FmRadioFavorite) mActivityMonitor.waitForActivityWithTimeout(TIMEOUT);
        assertNotNull(mFmRadioFavorite);
        ListView listView = (ListView) mFmRadioFavorite.findViewById(R.id.station_list);
        mSolo.clickOnView(listView.getChildAt(0));
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] open channel start [" + startTime + "]");
        sleep(WAIT_UI_STATE_CHANGE);

    }*/

    /**
     * Test increase and decrease 0.1 MHz / 0.05 MHZ performance.
     */
    public void testCase02_FrequencyChangePerformance() throws Exception {

        // test decrease frequency performance
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] decrease frequency start [" + startTime + "]");
        clickView(mButtonDecrease);
        sleep(WAIT_UI_STATE_CHANGE);

        // test increase frequency performance
        startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] increase frequency start [" + startTime + "]");
        clickView(mButtonIncrease);
        sleep(WAIT_UI_STATE_CHANGE);

    }

    /**
     * Test seek channel performance.
     */
    public void testCase03_SeekChannelPerformance() throws Exception {
        // test seek previous station performance
        int stationGap = 0;
        int currentStation = FmRadioStation.getCurrentStation(mFmRadioActivity);
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] seek previous channel start [" + startTime + "]");
        clickView(mButtonPrevStation);
        sleep(WAIT_UI_STATE_CHANGE);
        int searchStation = FmRadioStation.getCurrentStation(mFmRadioActivity);
        if (searchStation > currentStation) {
            stationGap = searchStation - currentStation;
            Log.i(TAG, "[Performance test][FmRadio] Test FmRadio seek time stationStep [" + (float) stationGap / CONVERT_RATE  + "]");
        } else if (searchStation < currentStation) {
            stationGap = FmRadioTestCaseUtil.HIGHEST_STATION - currentStation + searchStation - FmRadioTestCaseUtil.LOWEST_STATION;
            Log.i(TAG, "[Performance test][FmRadio] Test FmRadio seek time stationStep [" + (float) stationGap / CONVERT_RATE  + "]");
        } else {
            Log.e(TAG, "SearchStation Unchanged");
        }
        // test seek next station frequency performance
        currentStation = FmRadioStation.getCurrentStation(mFmRadioActivity);
        startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] seek next channel start [" + startTime + "]");
        clickView(mButtonNextStation);
        sleep(WAIT_UI_STATE_CHANGE);
        if (searchStation > currentStation) {
            stationGap = searchStation - currentStation;
            Log.i(TAG, "[Performance test][FmRadio] Test FmRadio seek time stationStep [" + (float) stationGap / CONVERT_RATE + "]");
        } else if (searchStation < currentStation) {
            stationGap = FmRadioTestCaseUtil.HIGHEST_STATION - currentStation + searchStation - FmRadioTestCaseUtil.LOWEST_STATION;
            Log.i(TAG, "[Performance test][FmRadio] Test FmRadio seek time stationStep [" + (float) stationGap / CONVERT_RATE + "]");
        } else {
            Log.e(TAG, "SearchStation Unchanged");
        }
    }

    /**
     * Test switch earphone and speaker performance.
     */
    public void testCase04_SwitchSpeakerEarphone() throws Exception {
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        String earphone = mFmRadioActivity.getString(R.string.optmenu_earphone);
        String speaker = mFmRadioActivity.getString(R.string.optmenu_speaker);

        sleep(SHORT_TIME);
        // test switch speaker performance
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        mSolo.clickOnText(speaker);
        sleep(SHORT_TIME);
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        assertTrue(mSolo.searchText(earphone));
        mInstrumentation.waitForIdleSync();

        // test switch earphone performance
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        mSolo.clickOnText(earphone);
        sleep(SHORT_TIME);
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        assertTrue(mSolo.searchText(speaker));
    }

    /**
     * Test power up performance.
     */
    public void testCase05_PowerUpPerformance() throws Exception {
        if (mButtonDecrease.isEnabled()) {
            makeFMPowerDown();
        }
        // test power up performance
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] power up start [" + startTime + "]");
//        clickView(mButtonPlayStop);
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity, R.id.fm_power, 0);
        sleep(WAIT_UI_STATE_CHANGE);
    }

    /**
     * Test get rds performance
     * start time: tune to 91.4 finished
     * end time: rds information show
     */
    public void testCase06_RDSPerformance() {
        int station = 0;
        // test add channel as favorite
        station = FmRadioTestCaseUtil.getStationFromUI(mTextViewFrequency);
        if (!FmRadioStation.isFavoriteStation(mFmRadioActivity, station)) {
            clickView(mButtonAddToFavorite);
            mInstrumentation.waitForIdleSync();
            FmRadioTestCaseUtil.sleep(SHORT_TIME);
        }

        // enter channel list
        mInstrumentation.invokeMenuActionSync(mFmRadioActivity,
                R.id.fm_channel_list, 0);
        mActivityMonitor = new ActivityMonitor(
                "com.mediatek.fmradio.FmRadioFavorite", null, false);
        mInstrumentation.addMonitor(mActivityMonitor);
        mFmRadioFavorite = (FmRadioFavorite) mActivityMonitor
                .waitForActivityWithTimeout(TIMEOUT);
        assertNotNull(mFmRadioFavorite);
        // edit this station to rds station91.4
        float frequency = 0;
        int stationInList = 0;
        ListView listView = (ListView) mFmRadioFavorite.findViewById(R.id.station_list);
        FmRadioTestCaseUtil.sleep(SLEEP_TIME);
        assertTrue((listView != null) && (listView.getCount() > 0));
        ListAdapter listAdapter = listView.getAdapter();
        for (int i = 0; i < listView.getCount(); i++) {
            View view = listAdapter.getView(i, null, listView);
            TextView textView = (TextView) view.findViewById(R.id.lv_station_freq);
            String frequencyStr = textView.getText().toString();
            try {
                frequency = Float.parseFloat(frequencyStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stationInList = (int) (frequency * CONVERT_RATE);
            if (FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList)) {
                mSolo.clickLongOnText(frequencyStr);
                mSolo.clickOnText(mFmRadioFavorite.getString(R.string.contmenu_item_edit));
                EditText editText = (EditText) mSolo.getView(R.id.dlg_edit_station_freq_text);
                mSolo.clearEditText(editText);
                mSolo.enterText(editText, "91.4");
                mInstrumentation.waitForIdleSync();
                InputMethodManager inputMethodManager = (InputMethodManager) mSolo.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, 0);
                if (FmRadioStation.isStationExistInChList(mFmRadioFavorite, 914)) {
                    mSolo.clickOnText(mFmRadioFavorite.getString(R.string.edit_frequency_overwrite_text));
                } else {
                    mSolo.clickOnText(mFmRadioFavorite.getString(R.string.btn_ok));
                }
                mInstrumentation.waitForIdleSync();
                sleep(SLEEP_TIME);
//                assertEquals("aaa", FmRadioStation.getStationName(mFmRadioFavorite, stationInList, FmRadioStation.STATION_TYPE_FAVORITE));
                break;
            }
        }

        // tune to RDS station 91.4
        mSolo.clickOnText("91.4");
        long startTuneTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] open channel start [" + startTuneTime + "]");
        mInstrumentation.waitForIdleSync();
        // test increase frequency performance
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] receive RDS start [" + startTime + "]");

        // check RDS info is change
        final TextView rdsTextView = (TextView) FmRadioTestCaseUtil.getVariableFromActivity(mFmRadioActivity, "mTextRDS");
        long scapeTime = 0;
        while (true) {
            String rdsInfo = rdsTextView.getText().toString();
            if (!"".equals(rdsInfo)) {
                long endTime = System.currentTimeMillis();
                Log.i(TAG, "[Performance test][FmRadio] receive RDS end [" + endTime + "]");
                break;
            }
            try {
                scapeTime = System.currentTimeMillis() - startTime;
                sleep(CHECK_TIME);
            } catch (Exception e) {
            }
            // if time > 10min, end time will be 10min
            if (scapeTime > RDS_TIME_OUT) {
                long endTime = System.currentTimeMillis();
                Log.i(TAG, "[Performance test][FmRadio] receive RDS end [" + endTime + "]");
                break;
            }
        }
    }

    public void testCase07_PowerDownPerformance() throws Exception {
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        // test power down performance
        long startTime = System.currentTimeMillis();
        Log.i(TAG, "[Performance test][FmRadio] power down start [" + startTime + "]");
        FmRadioTestCaseUtil.requestFocusGain(mAudioManager);
        sleep(WAIT_UI_STATE_CHANGE);
    }

    private void makeFMPowerUp() {
        boolean isPlaying = false;
        isPlaying = getBooleanFromVariable(mFmRadioActivity, "mIsPlaying");
        if (!isPlaying) {
            mInstrumentation
            .invokeMenuActionSync(mFmRadioActivity, R.id.fm_power, 0);
        }
        sleep(WAIT_UI_STATE_CHANGE);

    }

    private void makeFMPowerDown() {
        boolean isPlaying = true;
        isPlaying = getBooleanFromVariable(mFmRadioActivity, "mIsPlaying");
        if (isPlaying) {
            FmRadioTestCaseUtil.requestFocusGain(mAudioManager);
        }
        sleep(WAIT_UI_STATE_CHANGE);

    }

    private void switchEarphone() {
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        mInstrumentation
                .invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        String earphone = mFmRadioActivity.getString(R.string.optmenu_earphone);
        mSolo.clickOnText(earphone);
        mInstrumentation.waitForIdleSync();
    }

    private void switchSpeaker() {
        if (!mButtonDecrease.isEnabled()) {
            makeFMPowerUp();
        }
        mInstrumentation
                .invokeMenuActionSync(mFmRadioActivity, R.id.fm_menu, 0);
        String speaker = mFmRadioActivity.getString(R.string.optmenu_speaker);
        mSolo.clickOnText(speaker);
        mInstrumentation.waitForIdleSync();

    }

    private boolean getBooleanFromVariable(Activity activity, String variable) {
        Field field = null;
        boolean value = false;
        try {
            field = FmRadioActivity.class.getDeclaredField(variable);
            field.setAccessible(true);
            value = ((Boolean) field.get(activity)).booleanValue();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private boolean getBooleanFromMethod(Activity activity, String method) {

        boolean value = false;
        Class c = mFmRadioActivity.getClass();
        try {
            Method m = (Method) c.getDeclaredMethod(method, new Class[] {});
            m.setAccessible(true);
            value = (Boolean) m.invoke(activity, new Object[] {});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void clickView(final View view) {
        try {
            runTestOnUiThread(new Runnable() {
                public void run() {
                    view.performClick();

                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addChannelAsFavorite(int station) {
        if (!FmRadioStation.isFavoriteStation(mFmRadioActivity, station)) {
            clickView(mButtonAddToFavorite);
            mInstrumentation.waitForIdleSync();
            sleep(SHORT_TIME);
        }

    }

    private int getStationFromUI() {
        int station = 0;
        float frequency = 0;
        mTextViewFrequency = (TextView) mFmRadioActivity.findViewById(R.id.station_value);
        String frequencyStr = mTextViewFrequency.getText().toString();
        try {
            frequency = Float.parseFloat(frequencyStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        station = (int) (frequency * CONVERT_RATE);
        return station;

    }

    @Override
    public void tearDown() throws Exception {
        Log.i(TAG, "tearDown");
        try {
            mSolo.finalize();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        mSolo.finishOpenedActivities();
        sleep(WAIT_TEAR_DOWN);
        super.tearDown();
    }


}
