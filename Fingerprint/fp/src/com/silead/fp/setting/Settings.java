package com.silead.fp.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcFPInfo;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

import java.util.ArrayList;

public class Settings extends Activity {

    private static final String APPS_CALIBRATION = "www.sileadinc.com";
    private static final String APPS_CALIBRATION_ACTIVITY = "www.sileadinc.com.Silead_fpActivity";
    public SLFpsvcFPInfo fPInfos;
    private ListView mFingerListView;
    private LinearLayout mAddFingerprint;
    private TextView mMaxFingerInfo;
    private FingerAdapter mAdapter;
    private ArrayList<SLFpsvcFPInfo> mFingerList;
    private FpControllerNative mControllerNative;
    private SLFpsvcIndex fpsvcIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_setting);

        mControllerNative = FpControllerNative.getInstance();
        mControllerNative.initFPSystem();

        mFingerList = new ArrayList<>();

        fpsvcIndex = mControllerNative.GetFpInfo();

        if (fpsvcIndex.max > 5) {
            fpsvcIndex.max = 5;
        }

        for (int i = 0; i < fpsvcIndex.max; i++) {
            if (fpsvcIndex.FPInfo[i].slot == 1) {
                mFingerList.add(fpsvcIndex.FPInfo[i]);
            }
        }

        initView();

        initFpAdapter();
        mAdapter.notifyDataSetChanged();
    }

    private void initFpAdapter() {
        mAdapter = new FingerAdapter(Settings.this, mFingerList);
        mFingerListView.setAdapter(mAdapter);
        mFingerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                renameFinger(position);
            }
        });
    }

    private void initView() {
        mFingerListView = (ListView) findViewById(R.id.fp_finger_list);
        mAddFingerprint = (LinearLayout) findViewById(R.id.add_fingerprint);
        mMaxFingerInfo = (TextView) findViewById(R.id.max_finger_info);
        TextView mCalibrationInfo = (TextView) findViewById(R.id.calibration_info);

        if (mFingerList.size() >= 5) {
            setAddFingerButton(false);
        }

        mAddFingerprint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mFingerList.size() < 5) {
                    Intent intent = new Intent(Settings.this, EnrollActivity.class);
                    startActivity(intent);
                }
            }
        });

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent_manger = new Intent();
                intent_manger.setClassName(APPS_CALIBRATION, APPS_CALIBRATION_ACTIVITY);
                startActivity(intent_manger);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        String calText = getString(R.string.calibration_info);
        String calButton = getString(R.string.calibration);
        String finalString = calText + " " + calButton;

        Spannable fString = new SpannableString(finalString);
        fString.setSpan(new ForegroundColorSpan(0xFF009688), calText.length() + 1, finalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fString.setSpan(clickableSpan, calText.length() + 1, finalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mCalibrationInfo.setText(fString);
        mCalibrationInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void renameFinger(final int pos) {
        final SLFpsvcFPInfo mFinger = mFingerList.get(pos);

        View dialogView = View.inflate(Settings.this, R.layout.rename_dialog, null);
        AlertDialog.Builder mDialog = new Builder(Settings.this);

        mDialog.setView(dialogView);

        String ok = getString(android.R.string.ok);
        String delete = getString(R.string.delete);

        final EditText et = (EditText) dialogView.findViewById(R.id.fingerEditText);
        et.setText(mFinger.getFingerName());
        et.selectAll();

        final TextView lengthRem = (TextView) dialogView.findViewById(R.id.length_rem);
        lengthRem.setText(mFinger.getFingerName().length() + "/30");

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                lengthRem.setText(s.length() + "/30");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mDialog.setPositiveButton(ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fingerName = et.getText().toString().trim();
                if (fingerName.length() == 0) return;
                mFinger.setFingerName(fingerName);
                int enrollIndex2 = mFinger.enrollIndex;
                fpsvcIndex = mControllerNative.GetFpInfo();
                fpsvcIndex.FPInfo[enrollIndex2].setFingerName(fingerName);
                fpsvcIndex.setFPInfo(fpsvcIndex.FPInfo);
                mControllerNative.SetFpInfo(fpsvcIndex);
                mAdapter.notifyDataSetChanged();
            }
        });
        mDialog.setNegativeButton(delete, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                fPInfos = mFingerList.get(pos);
                fpsvcIndex = mControllerNative.GetFpInfo();
                int deleteIndex = fPInfos.enrollIndex;
                mControllerNative.removeCredential(deleteIndex);
                mFingerList.remove(pos);
                mAdapter.notifyDataSetChanged();
                if (mFingerList.size() < 5) {
                    setAddFingerButton(true);
                }
            }
        });

        AlertDialog dialog = mDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();

        if (mFingerList.size() < 5) {
            setAddFingerButton(true);
        } else {
            setAddFingerButton(false);
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
//		mControllerNative.destroyFPSystem();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mFingerList.clear();

        fpsvcIndex = mControllerNative.GetFpInfo();

        for (int i = 0; i < fpsvcIndex.max; i++) {
            if (fpsvcIndex.FPInfo[i].slot == 1) {
                mFingerList.add(fpsvcIndex.FPInfo[i]);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //mControllerNative.FpCancelOperation();
        mControllerNative.destroyFPSystem();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //mControllerNative.FpCancelOperation();
                return true;
            case KeyEvent.KEYCODE_HOME:
                //mControllerNative.FpCancelOperation();
                return true;
            case KeyEvent.KEYCODE_MENU:
                //mControllerNative.FpCancelOperation();
                return true;
            case KeyEvent.KEYCODE_POWER:
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.calibrate:
                Intent intent_manger = new Intent();
                intent_manger.setClassName(APPS_CALIBRATION, APPS_CALIBRATION_ACTIVITY);
                startActivity(intent_manger);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setAddFingerButton(Boolean enable) {
        if (!enable) {
            mMaxFingerInfo.setVisibility(View.VISIBLE);
            mAddFingerprint.setAlpha(0.3f);
            mAddFingerprint.setEnabled(false);
        } else {
            mMaxFingerInfo.setVisibility(View.GONE);
            mAddFingerprint.setAlpha(1f);
            mAddFingerprint.setEnabled(true);
        }
    }
}
