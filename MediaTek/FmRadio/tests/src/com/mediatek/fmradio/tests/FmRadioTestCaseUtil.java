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
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.Assert;
//import com.mediatek.storage.StorageManagerEx;
import com.mediatek.fmradio.FmRadioActivity;
import com.mediatek.fmradio.R;

public class FmRadioTestCaseUtil {
    // FM station variables
    public static final int DEFAULT_STATION = 1000;
    // maximum station frequency
    public static final int HIGHEST_STATION = 1080;
    // minimum station frequency
    public static final int LOWEST_STATION = 875;

    // minimum storage space for record
    public static final long LOW_SPACE_THRESHOLD = 512 * 1024;

    public static final int CONVERT_RATE = 10;
    public static final long CHECK_TIME = 200;
    public static final String OP = android.os.SystemProperties.get("ro.operator.optr");
    public static final boolean IS_CMCC = ("OP01").equals(OP); // whether is CMCC project
    private static final String TAG = "FmRadioTestCaseUtil";

    // check station is in channel list.
    public static boolean isExistInChannelList(Activity activity, int station) {

        boolean find = false;
        float frequency = 0;
        int stationInList = 0;
        ListView listView = (ListView) activity.findViewById(R.id.station_list);
        sleep(CHECK_TIME);
        Assert.assertTrue((listView != null) && (listView.getCount() > 0));
        ListAdapter listAdapter = listView.getAdapter();
        int stationCount = listView.getCount();
        Log.d(TAG, "isExistInChannelList stationCount:" + stationCount);
        for (int i = 0; i < stationCount; i++) {
            View view = listAdapter.getView(i, null, listView);
            TextView textView = (TextView) view.findViewById(R.id.lv_station_freq);
            String frequencyStr = textView.getText().toString();
            try {
                frequency = Float.parseFloat(frequencyStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stationInList = (int) (frequency * CONVERT_RATE);
            Log.d(TAG, "isExistInChannelList i:" + i + ", frequencyStr:" + frequencyStr +
                    ", stationInList:" + stationInList);
            if (station == stationInList) {
                return true;
            }
        }
        return false;
    }

    // get station fraom UI.
    public static int getStationFromUI(TextView textView) {
        int station = 0;
        float frequency = 0;
        String frequencyStr = textView.getText().toString();
        try {
            frequency = Float.parseFloat(frequencyStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        station = (int) (frequency * CONVERT_RATE);
        return station;
    }

    // sleep
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // get variable from method.
    public static Object getVariableFromMethod(Activity activity, String method) {
        Object value = false;
        Class c = activity.getClass();
        try {
            Method m = (Method) c.getDeclaredMethod(method, new Class[] {});
            m.setAccessible(true);
            value = m.invoke(activity, new Object[] {});
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

    // get variable from activity.
    public static Object getVariableFromActivity(Activity activity, String variable) {
        Field field = null;
        Object value = null;
        try {
            field = FmRadioActivity.class.getDeclaredField(variable);
            field.setAccessible(true);
            value = field.get(activity);
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

    public static String getProjectString(Context context, int cmccResId, int normalResId) {
        return IS_CMCC ? context.getString(cmccResId) : context.getString(normalResId);
    }

//    public static void mountSDCard(Context context) {
//        try {
//            getMountService().mountVolume(StorageManagerEx.getDefaultPath());
//        } catch (RemoteException e) {
//        }
//    }

//    public static void unmountSDCard(Context context) {
//        try {
//            getMountService().unmountVolume(StorageManagerEx.getDefaultPath(),
//                    true, false);
//        } catch (RemoteException e) {
//        }
//    }
//
//    private static IMountService getMountService() {
//        IMountService mountService = null;
//        IBinder service = ServiceManager.getService("mount");
//        Assert.assertNotNull(service);
//        mountService = IMountService.Stub.asInterface(service);
//        return mountService;
//    }

    private static OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "request audio focus loss");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d(TAG, "request audio focus loss transient");
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.d(TAG, "request audio focus gain");
                break;
            default :
                break;
            }
        }
    };
    public static void requestFocusGain(AudioManager audioManager) {
        audioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }
}
