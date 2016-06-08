package com.silead.fp.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcFPInfo;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings extends Activity {

    private static final String APPS_CALIBRATION = "www.sileadinc.com";
    private static final String APPS_CALIBRATION_ACTIVITY = "www.sileadinc.com.Silead_fpActivity";

    private ListView mFingerListView;
    private MenuItem addFingerprintItem;
    private LinearLayout mFingerPrompt;
    private FingerAdapter mAdapter;
    private HashMap<Integer, Boolean> check;

    private ArrayList<SLFpsvcFPInfo> mFingerList;
    private FpControllerNative mControllerNative;
    private SLFpsvcIndex fpsvcIndex;
    public SLFpsvcFPInfo fPInfos;

    public final static String ACTION_NONSUPPORT_LOCKSCREEN_ACTIVE = "com.silead.fp.Settings.NONSUPPORT_LOCKSCREEN_ACTIVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_setting);

        mControllerNative = FpControllerNative.getInstance();
        mControllerNative.initFPSystem();
        initView();

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

        if (mFingerList.size() > 0) {
            mFingerPrompt.setVisibility(View.GONE);
        } else {
            mFingerPrompt.setVisibility(View.VISIBLE);
        }

        check = FingerAdapter.getCheck();
        for (int j = 0; j < fpsvcIndex.max; j++) {
            if (fpsvcIndex.FPInfo[j].enable == 0) {
                check.put(j, false);
            } else if (fpsvcIndex.FPInfo[j].enable == 1) {
                check.put(j, true);
            }
        }
        initFpAdapter();
        mAdapter.notifyDataSetChanged();
    }

    private void initFpAdapter() {
        mAdapter = new FingerAdapter(Settings.this, mFingerList);
        mFingerListView.setAdapter(mAdapter);
        mFingerListView.setOnItemLongClickListener(new FingerListItemOnItemLongClick());
    }

    class FingerListItemOnItemLongClick implements OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int pos1, long pos2) {
            SLFpsvcFPInfoonLongClick(pos1);
            return true;
        }
    }

    public static void DisableFp() {
        FpControllerNative controllerNative = FpControllerNative.getInstance();
        controllerNative.initFPSystem();
        SLFpsvcIndex fpIndex = controllerNative.GetFpInfo();
        for (int i = 0; i < fpIndex.max; i++) {
            if (fpIndex.FPInfo[i].enable == 1) {
                fpIndex.FPInfo[i].setEnable(0);
            }
        }
        controllerNative.SetFpInfo(fpIndex);

    }

    @SuppressLint("NewApi")
    private void SLFpsvcFPInfoonLongClick(final int pos) {

        final String[] mItems = {getString(R.string.rename), getString(R.string.delete)};
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setTitle(fpsvcIndex.FPInfo[pos].fingerName);
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        renameSLFpsvcFPInfo(pos);
                        break;
                    case 1:
                        fPInfos = mFingerList.get(pos);
                        fpsvcIndex = mControllerNative.GetFpInfo();
                        int deleteIndex = fPInfos.enrollIndex;
                        mControllerNative.removeCredential(deleteIndex);
                        mFingerList.remove(pos);
                        check.put(deleteIndex, false);
                        mAdapter.notifyDataSetChanged();
                        if (mFingerList.size() < 5) {
                            addFingerprintItem.setEnabled(true);
                            addFingerprintItem.getIcon().setAlpha(255);
                        } else {
                            addFingerprintItem.setEnabled(false);
                            addFingerprintItem.getIcon().setAlpha(110);
                        }
                        if (mFingerList.size() == 0) {
                            mFingerPrompt.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0 && data != null) {
            Bundle extras = data.getExtras();
            int enrollIndex = extras.getInt("enrollIndex");
            fpsvcIndex = mControllerNative.GetFpInfo();
            mFingerList.add(fpsvcIndex.FPInfo[enrollIndex]);
            check.put(enrollIndex, true);
        }
        if (mFingerList.size() > 0) {
            mFingerPrompt.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mFingerListView = (ListView) findViewById(R.id.fp_finger_list);
        mFingerPrompt = (LinearLayout) findViewById(R.id.fp_prompt);

        final Button calibrationButton = (Button) findViewById(R.id.calibration_button);
        calibrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent_manger = new Intent();
                intent_manger.setClassName(APPS_CALIBRATION, APPS_CALIBRATION_ACTIVITY);
                startActivity(intent_manger);
            }
        });
    }

    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
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

    private void renameSLFpsvcFPInfo(final int index) {

        final SLFpsvcFPInfo mfinger = mFingerList.get(index);
        AlertDialog.Builder mDialog = new Builder(Settings.this);

        String title = getString(R.string.entry_finger_name);
        String ok = getString(android.R.string.ok);
        String cancel = getString(android.R.string.cancel);
        mDialog.setTitle(title);

        LinearLayout layout = new LinearLayout(Settings.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        float scale = getResources().getDisplayMetrics().density;
        int margin = (int) (20 * scale + 0.5f);

        params.setMargins(margin, 0, margin, 0);

        final EditText et = new EditText(Settings.this);
        // et.setHint(mfinger.getFingerName());
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        et.setMaxLines(1);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        layout.addView(et, params);

        final TextView lengthRem = new TextView(Settings.this);
        lengthRem.setText("0/15");
        lengthRem.setGravity(Gravity.RIGHT);
        lengthRem.setPadding(0, 0, (int) (4 * scale + 0.5f), 0);
        layout.addView(lengthRem, params);

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                lengthRem.setText(s.length() + "/15");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mDialog.setView(layout);

        mDialog.setPositiveButton(ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String fingerName = et.getText().toString().trim();
                if (fingerName.length() == 0) return;
                mfinger.setFingerName(fingerName);
                int enrollIndex2 = mfinger.enrollIndex;
                fpsvcIndex = mControllerNative.GetFpInfo();
                fpsvcIndex.FPInfo[enrollIndex2].setFingerName(fingerName);
                fpsvcIndex.setFPInfo(fpsvcIndex.FPInfo);
                mControllerNative.SetFpInfo(fpsvcIndex);
                mAdapter.notifyDataSetChanged();
            }
        });
        mDialog.setNegativeButton(cancel, null);

        AlertDialog dialog = mDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_fingerprint:
                if (mFingerList.size() >= 5) {
                    Toast.makeText(this, getString(R.string.max_fingerprint_info), Toast.LENGTH_SHORT).show();
                    addFingerprintItem.setEnabled(false);
                    addFingerprintItem.getIcon().setAlpha(110);
                    return true;
                }
                Intent intent = new Intent(Settings.this, EnrollActivity.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.calibrate:
                Intent intent_manger = new Intent();
                intent_manger.setClassName(APPS_CALIBRATION, APPS_CALIBRATION_ACTIVITY);
                startActivity(intent_manger);
                return true;

            case R.id.disable_all:
                for (int i = 0; i < mFingerList.size(); i++) {
                    if (mFingerList.get(i).getEnable() == 1) {
                        mFingerList.get(i).setEnable(0);
                        mControllerNative.EnalbeCredential(mFingerList.get(i).enrollIndex, 0);
                        check.put(mFingerList.get(i).enrollIndex, false);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return true;

            case R.id.enable_all:
                for (int i = 0; i < mFingerList.size(); i++) {
                    if (mFingerList.get(i).getEnable() == 0) {
                        mFingerList.get(i).setEnable(1);
                        mControllerNative.EnalbeCredential(mFingerList.get(i).enrollIndex, 1);
                        check.put(mFingerList.get(i).enrollIndex, true);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        addFingerprintItem = menu.findItem(R.id.add_fingerprint);

        if (mFingerList.size() >= 5) {
            addFingerprintItem.setEnabled(false);
            addFingerprintItem.getIcon().setAlpha(110);
        } else {
            addFingerprintItem.setEnabled(true);
            addFingerprintItem.getIcon().setAlpha(255);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
