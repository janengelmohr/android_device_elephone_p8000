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
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

import com.jayway.android.robotium.solo.Solo;
import com.mediatek.fmradio.FmRadioActivity;
import com.mediatek.fmradio.FmRadioFavorite;
import com.mediatek.fmradio.FmRadioStation;
import com.mediatek.fmradio.R;


public class FmRadioFavoriteTest extends
        ActivityInstrumentationTestCase2<FmRadioFavorite> {
    private Solo mSolo = null;
    private TextView mTextViewFrequency = null;
    private FmRadioFavorite mFmRadioFavorite = null;
    private FmRadioActivity mFmRadioActivity = null;
    private Context mContext = null;
    private Instrumentation mInstrumentation = null;
    private static final int CONVERT_RATE = 10;
    private static final long SLEEP_TIME = 2000;

    public FmRadioFavoriteTest() {
        super("com.mediatek.fmradio", FmRadioFavorite.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mInstrumentation = getInstrumentation();
        mFmRadioFavorite = (FmRadioFavorite) getActivity();
        mContext = mFmRadioFavorite.getApplicationContext();
    }
    public void testCase01_AddToFavorite() {
        boolean find = false;
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
            if (!FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList)) {
                mSolo.clickLongOnText(frequencyStr);
                mSolo.clickOnText(FmRadioTestCaseUtil.getProjectString(mContext, R.string.add_to_favorite1, R.string.add_to_favorite));
                mInstrumentation.waitForIdleSync();
                InputMethodManager inputMethodManager = (InputMethodManager) mSolo.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, 0);
                mSolo.clickOnButton(mContext.getString(R.string.btn_ok));
                mInstrumentation.waitForIdleSync();
                sleep(SLEEP_TIME);
                assertTrue(FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList));
                return ;
            }
        }

    }
    public void testCase02_EditFavoriteChannel() {
        boolean find = false;
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
                mSolo.clickOnText(mContext.getString(R.string.contmenu_item_edit));
                EditText editText = (EditText) mSolo.getView(R.id.dlg_edit_station_name_text);
                mSolo.clearEditText(editText);
                mSolo.enterText(editText, "aaa");
                mInstrumentation.waitForIdleSync();
                InputMethodManager inputMethodManager = (InputMethodManager) mSolo.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, 0);
                mSolo.clickOnButton(mContext.getString(R.string.edit_frequency_overwrite_text));
                mInstrumentation.waitForIdleSync();
                sleep(SLEEP_TIME);
                assertEquals("aaa", FmRadioStation.getStationName(mFmRadioFavorite, stationInList, FmRadioStation.STATION_TYPE_FAVORITE));
                return ;
            }
        }
    }
    public void testCase03_DeleteFromFavorite() throws Throwable  {
        boolean find = false;
        float frequency = 0;
        int stationInList = 0;
        final ListView listView = (ListView) mFmRadioFavorite.findViewById(R.id.station_list);
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
                runTestOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(0);
                    }
                });
                mSolo.sleep(3000);
                mSolo.clickLongOnText(frequencyStr);
                mSolo.clickOnText(FmRadioTestCaseUtil.getProjectString(mContext, R.string.contmenu_item_delete1, R.string.contmenu_item_delete));
                mInstrumentation.waitForIdleSync();
                mSolo.clickOnButton(mContext.getString(R.string.btn_ok));
                mInstrumentation.waitForIdleSync();
                sleep(SLEEP_TIME);
                assertFalse(FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList));
                return ;
            }
        }
    }

    // edit a favorite to a favorite frequency
    public void testCase04_EditFavoriteToFavorite() throws Throwable {
        boolean find = false;
        float frequency = 0;
        int stationInList = 0;
        String preFavoritaFreq = "";
        ListView listView = (ListView) mFmRadioFavorite.findViewById(R.id.station_list);
        FmRadioTestCaseUtil.sleep(SLEEP_TIME);
        assertTrue((listView != null) && (listView.getCount() > 0));
        ListAdapter listAdapter = listView.getAdapter();
        int count = listView.getCount();
        for (int i = 0; i < count; i++) {
            int favoriateCount = 0;
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
                favoriateCount += 1;
                preFavoritaFreq = frequencyStr;
            }
            // add to favoriate
            if (!FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList)) {
                mSolo.clickLongOnText(frequencyStr);
                mSolo.clickOnText(FmRadioTestCaseUtil.getProjectString(mContext, R.string.add_to_favorite1, R.string.add_to_favorite));
                mInstrumentation.waitForIdleSync();
                InputMethodManager inputMethodManager = (InputMethodManager) mSolo.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, 0);
                mSolo.clickOnButton(mContext.getString(R.string.btn_ok));
                mInstrumentation.waitForIdleSync();
                sleep(SLEEP_TIME);
                assertTrue(FmRadioStation.isFavoriteStation(mFmRadioFavorite, stationInList));
                // edit favoriate to exist favorite
                if (favoriateCount > 0) {
                    mSolo.clickLongOnText(frequencyStr);
                    mSolo.clickOnText(mContext.getString(R.string.contmenu_item_edit));
                    EditText editText = (EditText) mSolo.getView(R.id.dlg_edit_station_name_text);
                    mSolo.clearEditText(editText);
                    mSolo.enterText(editText, preFavoritaFreq);
                    mInstrumentation.waitForIdleSync();
                    InputMethodManager inputMethodManager2 = (InputMethodManager) mSolo.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager2.toggleSoftInput(0, 0);
                    mSolo.clickOnButton(mContext.getString(R.string.btn_ok));
                    mInstrumentation.waitForIdleSync();
                    sleep(SLEEP_TIME);
                    assertEquals(preFavoritaFreq, FmRadioStation.getStationName(mFmRadioFavorite, stationInList, FmRadioStation.STATION_TYPE_FAVORITE));
                    break;
                }
            }
        }
        testCase03_DeleteFromFavorite();
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        FmRadioTestCaseUtil.sleep(SLEEP_TIME);
        try {
            mSolo.finalize();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.tearDown();
    }

}
