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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * This class provider interface to operator databases, use by activity and
 * service
 */
public class FmRadioStation {
    private static final String TAG = "FmRx/Station";
    // authority use composite content provider uri
    public static final String AUTHORITY = "com.mediatek.fmradio.FmRadioContentProvider";
    // use to composite content provider uri
    public static final String STATION = "station";
    // default current station name
    private static final String CURRENT_STATION_NAME = "FmDfltSttnNm";

    // Default RDS settings
    // PS RT whether open
    private static final boolean DEFAULT_PSRT_ENABLED = false;
    // AF whether open
    private static final boolean DEFAULT_AF_ENABLED = false;
    // TA whether open
    private static final boolean DEFAULT_TA_ENABLED = false;

    // Station types.
    public static final int STATION_TYPE_CURRENT = 1;
    public static final int STATION_TYPE_FAVORITE = 2;
    public static final int STATION_TYPE_SEARCHED = 3;
    // just use to save rds, not really station type
    private static final int STATION_TYPE_RDS_SETTING = 4;

    // RDS setting items
    // save PSRT set
    public static final int RDS_SETTING_FREQ_PSRT = 1;
    // save AF set
    public static final int RDS_SETTING_FREQ_AF = 2;
    // save TA set
    public static final int RDS_SETTING_FREQ_TA = 3;
    // RDS setting values for every item
    public static final String RDS_SETTING_VALUE_ENABLED = "ENABLED";
    public static final String RDS_SETTING_VALUE_DISABLED = "DISABLED";

    // stationList table in database columns
    static final String COLUMNS[] = new String[] {
            Station._ID,
            Station.COLUMN_STATION_NAME,
            Station.COLUMN_STATION_FREQ,
            // Use this type to identify different stations.
            Station.COLUMN_STATION_TYPE
    };

    /**
     * This class provider the columns of StationList table
     */
    public static final class Station implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + STATION);
        // Extra columns of the table: COLUMN_STATION_NAME COLUMN_STATION_FREQ
        // COLUMN_STATION_TYPE
        public static final String COLUMN_STATION_NAME = "COLUMN_STATION_NAME";
        public static final String COLUMN_STATION_FREQ = "COLUMN_STATION_FREQ";
        public static final String COLUMN_STATION_TYPE = "COLUMN_STATION_TYPE";
    }

    /**
     * Init FM database for current station and RDS setting
     *
     * @param context The context
     */
    public static void initFmDatabase(Context context) {
        // Init current station, if no current station in database, set to be
        // FIXED_STATION_FREQ
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                new String[] {
                    Station.COLUMN_STATION_FREQ
                },
                Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                    String.valueOf(STATION_TYPE_CURRENT)
                },
                null);
        if (null != cur) {
            try {
                if (!cur.moveToFirst()) {
                    // Can't find current station, insert FIXED_STATION_FREQ to db
                    final int size = 3;
                    ContentValues values = new ContentValues(size);
                    values.put(Station.COLUMN_STATION_NAME, CURRENT_STATION_NAME);
                    values.put(Station.COLUMN_STATION_FREQ, FmRadioUtils.DEFAULT_STATION);
                    values.put(Station.COLUMN_STATION_TYPE, STATION_TYPE_CURRENT);
                    context.getContentResolver().insert(Station.CONTENT_URI, values);
                }
            } finally {
                cur.close();
            }
        }

        // Init PSRT, AF and TA
        int[] types = new int[] {
                RDS_SETTING_FREQ_PSRT, RDS_SETTING_FREQ_AF, RDS_SETTING_FREQ_TA
        };
        boolean[] enables = new boolean[] {
                DEFAULT_PSRT_ENABLED,
                DEFAULT_AF_ENABLED, DEFAULT_TA_ENABLED
        };
        for (int i = 0; i < types.length; i++) {
            cur = context.getContentResolver().query(
                    Station.CONTENT_URI,
                    new String[] {
                        Station.COLUMN_STATION_NAME
                    },
                    Station.COLUMN_STATION_FREQ + "=" + String.valueOf(types[i]),
                    null,
                    null);
            if (null != cur) {
                try {
                    if (!cur.moveToFirst()) {
                        final int size = 3;
                        ContentValues values = new ContentValues(size);
                        values.put(Station.COLUMN_STATION_NAME, enables[i] ?
                                RDS_SETTING_VALUE_ENABLED : RDS_SETTING_VALUE_DISABLED);
                        values.put(Station.COLUMN_STATION_FREQ, types[i]);
                        values.put(Station.COLUMN_STATION_TYPE, STATION_TYPE_RDS_SETTING);
                        context.getContentResolver().insert(Station.CONTENT_URI, values);
                    }
                } finally {
                    cur.close();
                }
            }
        }
        Log.d(TAG, "FmRadioStation.initFmDatabase");
    }

    /**
     * Insert station information to database
     *
     * @param context The context
     * @param stationName The station name
     * @param stationFreq The station frequency
     * @param stationType The station type
     */
    public static void insertStationToDb(Context context, String stationName,
            int stationFreq, int stationType) {
        Log.d(TAG, "FmRadioStation.insertStationToDb start");
        final int size = 3;
        ContentValues values = new ContentValues(size);
        values.put(Station.COLUMN_STATION_NAME, stationName);
        values.put(Station.COLUMN_STATION_FREQ, stationFreq);
        values.put(Station.COLUMN_STATION_TYPE, stationType);
        context.getContentResolver().insert(Station.CONTENT_URI, values);
        Log.d(TAG, "FmRadioStation.insertStationToDb end");
    }

    /**
     * Update station name, station frequency according original station
     * frequency, station type
     *
     * @param context The context
     * @param stationName The new station's name
     * @param oldStationFreq The old station frequency
     * @param newStationFreq The new station frequency
     * @param stationType The old station type
     */
    public static void updateStationToDb(Context context, String stationName, int oldStationFreq,
            int newStationFreq, int stationType) {
        final int size = 3;
        ContentValues values = new ContentValues(size);
        values.put(Station.COLUMN_STATION_NAME, stationName);
        values.put(Station.COLUMN_STATION_FREQ, newStationFreq);
        values.put(Station.COLUMN_STATION_TYPE, stationType);
        context.getContentResolver().update(
                Station.CONTENT_URI,
                values,
                Station.COLUMN_STATION_FREQ + "=? AND " + Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                        String.valueOf(oldStationFreq), String.valueOf(stationType)
                });
        Log.d(TAG, "FmRadioStation.updateStationToDb: name = " + stationName +
                ", new freq = " + newStationFreq);
    }

    /**
     * Update station name and station type according station frequency
     *
     * @param context The context
     * @param newStationName The station new name
     * @param newStationType The station new type
     * @param stationFreq The original station frequency
     */
    public static void updateStationToDb(Context context, String newStationName,
            int newStationType, int stationFreq) {
        final int size = 3;
        ContentValues values = new ContentValues(size);
        values.put(Station.COLUMN_STATION_NAME, newStationName);
        values.put(Station.COLUMN_STATION_FREQ, stationFreq);
        values.put(Station.COLUMN_STATION_TYPE, newStationType);
        context.getContentResolver().update(
                Station.CONTENT_URI,
                values,
                Station.COLUMN_STATION_FREQ + "=? AND " +
                        Station.COLUMN_STATION_TYPE + "<>" + STATION_TYPE_CURRENT,
                new String[] {
                    String.valueOf(stationFreq)
                });
        Log.d(TAG, "FmRadioStation.updateStationToDb: new name = " + newStationName
                + ", new freq type = " + newStationType);
    }

    /**
     * Delete station according station frequency and station type
     *
     * @param context The context
     * @param stationFreq The station frequency
     * @param stationType The station type
     */
    public static void deleteStationInDb(Context context, int stationFreq, int stationType) {
        context.getContentResolver().delete(
                Station.CONTENT_URI,
                Station.COLUMN_STATION_FREQ + "=? AND " + Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                        String.valueOf(stationFreq), String.valueOf(stationType)
                });
        Log.d(TAG, "FmRadioStation.deleteStationInDb: freq = " + stationFreq +
                ", type = " + stationType);
    }

    /**
     * Judge a station whether exist according station frequency and station
     * type
     *
     * @param context The context
     * @param stationFreq The station frequency
     * @param stationType The station type
     *
     * @return true or false indicate whether station is exist
     */
    public static boolean isStationExist(Context context, int stationFreq, int stationType) {
        Log.d(TAG, ">>> isStationExist: stationFreq=" + stationFreq +
                ",stationType=" + stationType);
        boolean isExist = false;
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                new String[] {
                    Station.COLUMN_STATION_NAME
                },
                Station.COLUMN_STATION_FREQ + "=? AND " + Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                        String.valueOf(stationFreq), String.valueOf(stationType)
                },
                null);
        if (null != cur) {
            try {
                if (cur.moveToFirst()) {
                    // This station is exist
                    isExist = true;
                }
            } finally {
                cur.close();
            }
        }
        Log.d(TAG, "<<< isStationExist: " + isExist);
        return isExist;
    }

    /**
     * Judge a station whether exist according station frequency
     *
     * @param context The context
     * @param stationFreq The station frequency
     * @param stationType The station type
     *
     * @return true or false indicate whether station is exist
     */
    public static boolean isStationExistInChList(Context context, int stationFreq) {
        Log.d(TAG, ">>> isStationExist: stationFreq=" + stationFreq);
        boolean isExist = false;
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                new String[] {
                    Station.COLUMN_STATION_NAME
                },
                Station.COLUMN_STATION_FREQ + "=? AND " + Station.COLUMN_STATION_TYPE + "<>1",
                new String[] {
                    String.valueOf(stationFreq)
                },
                null);
        if (null != cur) {
            try {
                if (cur.moveToFirst()) {
                    // This station is exist
                    isExist = true;
                }
            } finally {
                cur.close();
            }
        }
        Log.d(TAG, "<<< isStationExist: " + isExist);
        return isExist;
    }

    /**
     * Get current station from database
     *
     * @param context The context
     *
     * @return the station which station type is current
     */
    public static int getCurrentStation(Context context) {
        int currentStation = FmRadioUtils.DEFAULT_STATION;
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                new String[] {
                    Station.COLUMN_STATION_FREQ
                },
                Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                    String.valueOf(STATION_TYPE_CURRENT)
                },
                null);
        if (null != cur) {
            try {
                if (cur.moveToFirst()) {
                    currentStation = cur.getInt(0);
                    if (!FmRadioUtils.isValidStation(currentStation)) {
                        // If current station is invalid, use default and update
                        // the database
                        currentStation = FmRadioUtils.DEFAULT_STATION;
                        setCurrentStation(context, currentStation);
                        Log.w(TAG, "current station is invalid, use default!");
                    }
                }
            } finally {
                cur.close();
            }
        }
        Log.d(TAG, "FmRadioStation.getCurrentStation: " + currentStation);
        return currentStation;
    }

    /**
     * Set current station
     *
     * @param context The context
     * @param station The station frequency
     */
    public static void setCurrentStation(Context context, int station) {
        Log.d(TAG, "FmRadioStation.setCurrentStation start");
        // Update current station to database.
        final int size = 3;
        ContentValues values = new ContentValues(size);
        values.put(Station.COLUMN_STATION_NAME, CURRENT_STATION_NAME);
        values.put(Station.COLUMN_STATION_FREQ, station);
        values.put(Station.COLUMN_STATION_TYPE, STATION_TYPE_CURRENT);
        context.getContentResolver().update(
                Station.CONTENT_URI,
                values,
                Station.COLUMN_STATION_NAME + "=? AND " + Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                        CURRENT_STATION_NAME, String.valueOf(STATION_TYPE_CURRENT)
                });
        Log.d(TAG, "FmRadioStation.setCurrentStation end");
    }

    /**
     * Clean all stations which station type is searched
     *
     * @param context The context
     */
    public static void cleanSearchedStations(Context context) {
        Log.d(TAG, "FmRadioStation.cleanSearchedStations start");
        context.getContentResolver().delete(
                Station.CONTENT_URI,
                Station.COLUMN_STATION_TYPE + "=" + String.valueOf(STATION_TYPE_SEARCHED),
                null);
        Log.d(TAG, "FmRadioStation.cleanSearchedStations end");
    }

    /**
     * Get station name according station frequency and station type
     *
     * @param context The context
     * @param stationFreq The station frequency
     * @param stationType The station type
     *
     * @return The station name
     */
    public static String getStationName(Context context, int stationFreq, int stationType) {
        Log.d(TAG, "FmRadioStation.getStationName: type = "
                + stationType + ", freq = " + stationFreq);
        // If can't find this station id database, return default station name
        String stationName = context.getString(R.string.default_station_name);
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                new String[] {
                    Station.COLUMN_STATION_NAME
                },
                Station.COLUMN_STATION_FREQ + "=? AND " + Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                        String.valueOf(stationFreq), String.valueOf(stationType)
                },
                null);
        if (null != cur) {
            try {
                if (cur.moveToFirst()) {
                    stationName = cur.getString(0);
                }
            } finally {
                cur.close();
            }
        }
        Log.d(TAG, "FmRadioStation.getStationName: stationName = " + stationName);
        return stationName;
    }

    /**
     * Judge whether station is a favorite station
     *
     * @param context The context
     * @param iStation The station frequency
     *
     * @return true or false indicate whether station type is favorite
     */
    public static boolean isFavoriteStation(Context context, int iStation) {
        return isStationExist(context, iStation, STATION_TYPE_FAVORITE);
    }

    /**
     * Get station count according station type
     *
     * @param context The context
     * @param stationType The station type
     *
     * @return The numbers of station according station type
     */
    public static int getStationCount(Context context, int stationType) {
        Log.d(TAG, "FmRadioStation.getStationCount Type: " + stationType);
        int stationNus = 0;
        Cursor cur = context.getContentResolver().query(
                Station.CONTENT_URI,
                COLUMNS,
                Station.COLUMN_STATION_TYPE + "=?",
                new String[] {
                    String.valueOf(stationType)
                },
                null);
        if (null != cur) {
            try {
                stationNus = cur.getCount();
            } finally {
                cur.close();
            }
        }
        Log.d(TAG, "FmRadioStation.getStationCount: " + stationNus);
        return stationNus;
    }

    /**
     * Clear all station of FMRadio database
     *
     * @param context The application context
     */
    public static void cleanAllStations(Context context) {
        Uri uri = Station.CONTENT_URI;
        Cursor cur = context.getContentResolver().query(uri, COLUMNS, null, null, null);
        if (null != cur) {
            try {
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    // Have find one station.
                    uri = ContentUris.appendId(Station.CONTENT_URI.buildUpon(),
                            cur.getInt(cur.getColumnIndex(Station._ID))).build();
                    context.getContentResolver().delete(uri, null, null);
                    cur.moveToNext();
                }
            } finally {
                cur.close();
            }
        }
    }
}
