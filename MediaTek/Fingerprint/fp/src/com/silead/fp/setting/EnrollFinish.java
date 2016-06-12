package com.silead.fp.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.silead.fp.R;
import com.silead.fp.utils.FpControllerNative;
import com.silead.fp.utils.FpControllerNative.SLFpsvcIndex;

public class EnrollFinish extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_next_in, R.anim.slide_next_out);
        setContentView(R.layout.fp_enroll_finish);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Button addAnotherButton = (Button) findViewById(R.id.add_another_button);
        Button doneButton = (Button) findViewById(R.id.done_button);

        addAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnrollFinish.this, EnrollActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (getFingerCount() >= 5) {
            addAnotherButton.setVisibility(View.GONE);
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private int getFingerCount() {
        SLFpsvcIndex fpsvcIndex;
        int fpCount = 0;

        fpsvcIndex = FpControllerNative.getInstance().GetFpInfo();

        for (int i = 0; i < fpsvcIndex.max; i++) {
            if (fpsvcIndex.FPInfo[i].slot == 1) {
                fpCount++;
            }
        }

        return fpCount;
    }
}
