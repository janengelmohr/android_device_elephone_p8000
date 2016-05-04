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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class provider interface to operator FM database table StationList
 */
public class FmRadioContentProvider extends ContentProvider {
    private static final String TAG = "FmRx/Provider";

    // database instance use to operate the database
    private SQLiteDatabase mSqlDb = null;
    // database helper use to get database instance
    private DatabaseHelper mDbHelper = null;
    // database name
    private static final String DATABASE_NAME = "FmRadio.db";
    // database version
    private static final int DATABASE_VERSION = 1;
    // table name
    private static final String TABLE_NAME = "StationList";

    // URI match code
    private static final int STATION_FREQ = 1;
    // URI match code
    private static final int STATION_FREQ_ID = 2;
    // use to match URI
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    // match URI with station frequency or station frequency id
    static {
        URI_MATCHER.addURI(FmRadioStation.AUTHORITY, FmRadioStation.STATION, STATION_FREQ);
        URI_MATCHER.addURI(FmRadioStation.AUTHORITY, FmRadioStation.STATION + "/#",
                STATION_FREQ_ID);
    }

    /**
     * Helper to operate database
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * initial database name and database version
         *
         * @param context application context
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Create database table
         *
         * @param db The database
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create the table
            Log.d(TAG, "DatabaseHelper.onCreate");
            db.execSQL(
                    "Create table "
                            + TABLE_NAME
                            + "("
                            + FmRadioStation.Station._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + FmRadioStation.Station.COLUMN_STATION_NAME + " TEXT,"
                            + FmRadioStation.Station.COLUMN_STATION_FREQ + " INTEGER,"
                            + FmRadioStation.Station.COLUMN_STATION_TYPE + " INTEGER"
                            + ");"
                    );
        }

        /**
         * Upgrade database
         *
         * @param db database
         * @param oldVersion The old database version
         * @param newVersion The new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Delete database table rows with condition
     *
     * @param uri The uri to delete
     * @param selection The where cause to apply, if null will delete all rows
     * @param selectionArgs The select value
     *
     * @return The rows number has be deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "FmRadioContentProvider.delete");
        int rows = 0;
        mSqlDb = mDbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case STATION_FREQ:
                rows = mSqlDb.delete(TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            case STATION_FREQ_ID:
                String stationID = uri.getPathSegments().get(1);
                rows = mSqlDb.delete(TABLE_NAME,
                        FmRadioStation.Station._ID
                                + "="
                                + stationID
                                + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"),
                        selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                Log.e(TAG, "Error: Unkown URI to delete: " + uri);
                break;
        }
        return rows;
    }

    /**
     * Insert values to database with uri
     *
     * @param uri The insert uri
     * @param values The insert values
     *
     * @return The uri after inserted
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "FmRadioContentProvider.insert");
        Uri rowUri = null;
        mSqlDb = mDbHelper.getWritableDatabase();
        ContentValues v = new ContentValues(values);
        // Do not insert invalid values.
        if (!v.containsKey(FmRadioStation.Station.COLUMN_STATION_NAME)
                || !v.containsKey(FmRadioStation.Station.COLUMN_STATION_FREQ)
                || !v.containsKey(FmRadioStation.Station.COLUMN_STATION_TYPE)) {
            Log.e(TAG, "Error: Invalid values.");
            return rowUri;
        }

        long rowId = mSqlDb.insert(TABLE_NAME, null, v);
        if (rowId <= 0) {
            Log.e(TAG, "Error: Failed to insert row into " + uri);
        }
        rowUri = ContentUris.appendId(FmRadioStation.Station.CONTENT_URI.buildUpon(), rowId)
                .build();
        getContext().getContentResolver().notifyChange(rowUri, null);
        return rowUri;
    }

    /**
     * Create database helper
     *
     * @return true if create database helper success
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return (null == mDbHelper) ? false : true;
    }

    /**
     * Query the database with current settings and add information
     *
     * @param uri The database uri
     * @param projection The columns need to query
     * @param selection The where clause
     * @param selectionArgs The where value
     * @param sortOrder The column to sort
     *
     * @return The query result cursor
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        qb.setTables(TABLE_NAME);

        int match = URI_MATCHER.match(uri);

        if (STATION_FREQ_ID == match) {
            qb.appendWhere("_id = " + uri.getPathSegments().get(1));
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        if (null != c) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    /**
     * Update the database content use content values with current settings and
     * add information
     *
     * @param uri The database uri
     * @param values The values need to update
     * @param selection The where clause
     * @param selectionArgs The where value
     *
     * @return The row numbers have changed
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "FmRadioContentProvider.update");
        int rows = 0;
        mSqlDb = mDbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case STATION_FREQ:
                rows = mSqlDb.update(TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case STATION_FREQ_ID:
                String stationID = uri.getPathSegments().get(1);
                rows = mSqlDb.update(TABLE_NAME,
                        values,
                        FmRadioStation.Station._ID
                                + "="
                                + stationID
                                + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"),
                        selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                Log.e(TAG, "Error: Unkown URI to update: " + uri);
                break;
        }
        return rows;
    }

    /**
     * Get uri type
     *
     * @param uri The the uri
     *
     * @return The type
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
