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

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.util.Log;

import java.util.Locale;

/**
 * This class provider interface to compute station and frequency, get project
 * string
 */
public class FmRadioUtils {
    private static final String TAG = "FmRx/Utils";

    // FM station variables
    public static final int DEFAULT_STATION = 1000;
    public static final float DEFAULT_STATION_FLOAT = computeFrequency(DEFAULT_STATION);
    // maximum station frequency
    private static final int HIGHEST_STATION = 1080;
    // minimum station frequency
    private static final int LOWEST_STATION = 875;
    // station step
    private static final int STEP = 1;
    // convert rate
    private static final int CONVERT_RATE = 10;

/* Vanzo:yujianpeng on: Fri, 30 Jan 2015 14:44:52 +0800
 * add FM FactoryMode
 */
    private static final String HEADSET_STATE_PATH = "/sys/class/switch/h2w/state";
// End of Vanzo:yujianpeng
    // minimum storage space for record
    public static final long LOW_SPACE_THRESHOLD = 512 * 1024;
    // StorageManager For FM record
    private static StorageManager sStorageManager = null;
    // short antenna
    private static final boolean IS_FM_SHORT_ANTENNA_SUPPORT = SystemProperties.getBoolean(
            "ro.mtk_fm_short_antenna_support", false);
    // FM suspend feature
    private static final boolean IS_FM_SUSPEND_SUPPORT = SystemProperties.getBoolean(
            "ro.mtk_tc1_fm_at_suspend", false);

    /**
     * Whether the frequency is valid.
     *
     * @param station The FM station
     *
     * @return true if the frequency is in the valid scale, otherwise return
     *         false
     */
    public static boolean isValidStation(int station) {
        boolean isValid = (station >= LOWEST_STATION && station <= HIGHEST_STATION);
        Log.v(TAG, "isValidStation: freq = " + station + ", valid = " + isValid);
        return isValid;
    }

    /**
     * Compute increase station frequency
     *
     * @param station The station frequency
     *
     * @return station The frequency after increased
     */
    public static int computeIncreaseStation(int station) {
        int result = station + STEP;
        if (result > HIGHEST_STATION) {
            result = LOWEST_STATION;
        }
        return result;
    }

    /**
     * Compute decrease station frequency
     *
     * @param station The station frequency
     *
     * @return station The frequency after decreased
     */
    public static int computeDecreaseStation(int station) {
        int result = station - STEP;
        if (result < LOWEST_STATION) {
            result = HIGHEST_STATION;
        }
        return result;
    }

    /**
     * Compute station value with given frequency
     *
     * @param frequency The station frequency
     *
     * @return station The result value
     */
    public static int computeStation(float frequency) {
        return (int) (frequency * CONVERT_RATE);
    }

    /**
     * Compute frequency value with given station
     *
     * @param station The station value
     *
     * @return station The frequency
     */
    public static float computeFrequency(int station) {
        return (float) station / CONVERT_RATE;
    }

    /**
     * According station to get frequency string
     *
     * @param station for 100KZ, range 875-1080
     *
     * @return string like 87.5
     */
    public static String formatStation(int station) {
        float frequency = (float) station / CONVERT_RATE;
        String result = String.format(Locale.ENGLISH, "%.1f",
                Float.valueOf(frequency));
        return result;
    }

    /**
     * Get the phone storage path
     *
     * @return The phone storage path
     */
    public static String getDefaultStoragePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * Get the default storage state
     *
     * @return The default storage state
     */
    public static String getDefaultStorageState(Context context) {
        ensureStorageManager(context);
        String state = sStorageManager.getVolumeState(getDefaultStoragePath());
        Log.d(TAG, "getDefaultStorageState() return state:" + state);
        return state;
    }

    private static void ensureStorageManager(Context context) {
        if (sStorageManager == null) {
            sStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        }
    }

    /**
     * Get the FM play list path
     *
     * @param context The context
     *
     * @return The FM play list path
     */
    public static String getPlaylistPath(Context context) {
        ensureStorageManager(context);
        String[] externalStoragePaths = sStorageManager.getVolumePaths();
        String path = externalStoragePaths[0] + "/Playlists/";
        return path;
    }

/* Vanzo:yujianpeng on: Fri, 30 Jan 2015 14:46:21 +0800
 * add FM FactoryMode
 */
    static boolean isWiredHeadsetReallyOn() {
        try {
            char[] buffer = new char[8];
            java.io.FileReader file = new java.io.FileReader(HEADSET_STATE_PATH);
            int len = file.read(buffer, 0, 8);
            int state = Integer.valueOf((new String(buffer, 0, len)).trim());
            file.close();
            return (state == 1 || state == 2);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
// End of Vanzo:yujianpeng
    /**
     * Check if has enough space for record
     *
     * @param recordingSdcard The recording sdcard path
     *
     * @return true if has enough space for record
     */
    public static boolean hasEnoughSpace(String recordingSdcard) {
        boolean ret = false;
        try {
            StatFs fs = new StatFs(recordingSdcard);
            long blocks = fs.getAvailableBlocks();
            long blockSize = fs.getBlockSize();
            long spaceLeft = blocks * blockSize;
            Log.d(TAG, "hasEnoughSpace: available space=" + spaceLeft);
            ret = spaceLeft > LOW_SPACE_THRESHOLD ? true : false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "sdcard may be unmounted:" + recordingSdcard);
        }
        return ret;
    }

    /**
     * Check if support short antenna. If true, can play FM without headset
     * @return true if support
     */
    public static boolean isFmShortAntennaSupport() {
        return IS_FM_SHORT_ANTENNA_SUPPORT;
    }

    /**
     * Check if support FM suspend feature. If true, will not aquire wakelock.
     * @return true if support
     */
    public static boolean isFmSuspendSupport() {
        return IS_FM_SUSPEND_SUPPORT;
    }

}
