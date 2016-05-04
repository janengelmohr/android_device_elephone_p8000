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

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * The dialog fragment for save recording file
 */
public class FmRecordDialogFragment extends DialogFragment {
    private static final String TAG = "FmRx/RecordDlg";

    // save recording file button
    private Button mButtonSave = null;
    // discard recording file button
    private Button mButtonDiscard = null;
    // rename recording file edit text
    private EditText mRecordingNameEditText = null;
    // recording file default name
    private String mDefaultRecordingName = null;
    // recording file to save name
    private String mRecordingNameToSave = null;
    // text view which show storage warning
    private TextView mStorageWarningTextView = null;
    private OnRecordingDialogClickListener mListener = null;

    // The default filename need't to check whether exist
    private boolean mIsNeedCheckFilenameExist = false;
    // record sd card path when start recording
    private String mRecordingSdcard = null;

    /**
     * FM record dialog fragment, because fragment manager need empty
     * constructor to instantiated this dialog fragment when configuration
     * change
     */
    public FmRecordDialogFragment() {
    }

    /**
     * FM record dialog fragment according name, should pass value recording
     * file name
     *
     * @param defaultName The default file name in FileSystem
     * @param recordingName The name in the dialog for show and save
     */
    public FmRecordDialogFragment(String sdcard, String defaultName, String recordingName) {
        mRecordingSdcard = sdcard;
        mDefaultRecordingName = defaultName;
        mRecordingNameToSave = recordingName;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRecordingDialogClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * When onCreate Set dialog style
     *
     * @param savedInstanceState The save instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, ">>onCreate() savedInstanceState:" + savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Log.d(TAG, "<<onCreate()");
    }

    /**
     * Inflate view and get the operation button
     *
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The save instance state
     *
     * @return The inflater view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_recorder_dialog, container, false);
        mButtonSave = (Button) view.findViewById(R.id.fm_recording_btn_save);
        mButtonSave.setOnClickListener(mButtonOnClickListener);

        mButtonDiscard = (Button) view.findViewById(R.id.fm_recording_btn_discard);
        mButtonDiscard.setOnClickListener(mButtonOnClickListener);

        mStorageWarningTextView = (TextView) view
                .findViewById(R.id.save_recording_storage_warning);

        // Set the recording edit text
        mRecordingNameEditText = (EditText) view.findViewById(R.id.fm_recording_text);
        return view;
    }

    /**
     * Set the dialog edit text and other attribute
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, ">>onResume()");

        // check if storage is OK, if not enough storage, make view visible
        if (!FmRadioUtils.hasEnoughSpace(mRecordingSdcard)) {
            mStorageWarningTextView.setVisibility(View.VISIBLE);
        }

        // have define in fm_recorder_dialog.xml length at most
        // 250(maxFileLength - suffixLength)
        mRecordingNameEditText.setSingleLine(true);

        if (mDefaultRecordingName != null) {
            if (null != mRecordingNameToSave) {
                // this case just for,fragment recreate
                mRecordingNameEditText.setText(mRecordingNameToSave);
                if ("".equals(mRecordingNameToSave)) {
                    mButtonSave.setEnabled(false);
                }
            } else {
                mRecordingNameEditText.setText(mDefaultRecordingName);
            }

            mRecordingNameEditText.selectAll();
        }
        mRecordingNameEditText.setHint(getActivity().getResources().getString(
                R.string.edit_recording_name_hint));
        mRecordingNameEditText.requestFocus();
        setTextChangedCallback();
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Log.d(TAG, "<<onResume()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, ">>onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, ">>onDestroy()");
        mDefaultRecordingName = null;
        mRecordingNameToSave = null;
        mListener = null;
        Log.d(TAG, "<<onDestroy()");
        super.onDestroy();
    }

    /**
     * This method register callback and set filter to Edit, in order to make
     * sure that user input is legal. The input can't be illegal filename and
     * can't be too long.
     */
    private void setTextChangedCallback() {
        mRecordingNameEditText.addTextChangedListener(new TextWatcher() {
            // not use, so don't need to implement it
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            // not use, so don't need to implement it
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * check user input whether include invalid character
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() s:" + s + ", start:" + start +
                        ", before:" + before + ", count:" + count);
                // Filename changed, so we should check the new filename is
                // whether exist.
                mIsNeedCheckFilenameExist = true;
                String recordName = s.toString().trim();
                // Characters not allowed by file system
                if (recordName.length() <= 0
                        || recordName.startsWith(".")
                        || recordName.matches(".*[/\\\\:*?\"<>|\t].*")) {
                    mButtonSave.setEnabled(false);
                } else {
                    mButtonSave.setEnabled(true);
                }

                mRecordingNameToSave = mRecordingNameEditText.getText().toString().trim();
                Log.d(TAG, "onTextChanged mRecordingNameToSave:" + mRecordingNameToSave);
            }
        });
    }

    private OnClickListener mButtonOnClickListener = new OnClickListener() {
        /**
         * Define the button operation
         */
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.fm_recording_btn_save:
                String msg = null;
                // Check the recording name whether exist
                mRecordingNameToSave = mRecordingNameEditText.getText().toString().trim();
                File recordingFolderPath = new File(mRecordingSdcard, "FM Recording");
                File recordingFileToSave =
                        new File(recordingFolderPath, mRecordingNameToSave
                                + FmRecorder.RECORDING_FILE_EXTENSION);

                // If the new name is same as default name ,need't to check!
                if (mDefaultRecordingName == null) {
                    Log.e(TAG, "Error:recording file is not exist!");
                    return;
                }
                if (mDefaultRecordingName.equals(mRecordingNameToSave)) {
                    mIsNeedCheckFilenameExist = false;
                } else {
                    mIsNeedCheckFilenameExist = true;
                }

                Log.d(TAG, "save:" + mDefaultRecordingName + "->" + mRecordingNameToSave +
                        ", " + mIsNeedCheckFilenameExist);
                if (recordingFileToSave.exists() && mIsNeedCheckFilenameExist) {
                    // show a toast notification if can't renaming a file/folder
                    // to the same name
                    msg = mRecordingNameEditText.getText().toString() + " "
                            + getActivity().getResources().getString(R.string.already_exists);
                    Log.d(TAG, "file " + mRecordingNameToSave + ".ogg is already exists!");
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } else {
                    if (null != mListener) {
                        mListener.onRecordingDialogClick(mRecordingNameToSave);
                    }
                    dismissAllowingStateLoss();
                }
                break;

                case R.id.fm_recording_btn_discard:
                    if (null != mListener) {
                        mListener.onRecordingDialogClick(null);
                    }
                    dismissAllowingStateLoss();
                    Log.d(TAG, "Discard FM recording file. ");
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * The listener for click Save or Discard
     */
    public interface OnRecordingDialogClickListener {
        /**
         * Record dialog click callback
         *
         * @param recordingName The user input recording name
         */
        void onRecordingDialogClick(String recordingName);
    }

    /**
     * Get the latest modified recording name
     *
     * @return The latest modified recording name
     */
    public String getRecordingNameToSave() {
        return mRecordingNameToSave;
    }
}
