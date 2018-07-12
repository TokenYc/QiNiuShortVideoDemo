package net.archeryc.qiniushortvideodemo.ui.recordbutton;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import net.archeryc.qiniushortvideodemo.R;

/**
 * @author yc
 */
public class RecordButtonActivity extends AppCompatActivity {

    private RecordButton recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_button);

        ConstraintLayout clRoot = findViewById(R.id.cl_root);
        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnRecordStateChangedListener(new RecordButton.OnRecordStateChangedListener() {
            @Override
            public void onStart() {
                Toast.makeText(RecordButtonActivity.this, "开始录制", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStop() {
                Toast.makeText(RecordButtonActivity.this, "结束录制", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onZoom(float percentage) {
                Log.d("zoom", "zoom--->" + percentage);
            }
        });
    }
}
