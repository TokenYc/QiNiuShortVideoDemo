package net.archeryc.qiniushortvideodemo.ui.focus;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import net.archeryc.qiniushortvideodemo.R;

public class FocusActivity extends AppCompatActivity {


    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        clRoot = findViewById(R.id.cl_root);

        final FocusView focusView=new FocusView(this);
        clRoot.addView(focusView);
        focusView.setImageResource(R.mipmap.icon_focus_video);
        focusView.attach(new FocusView.OnFocusListener() {
            @Override
            public void onFocus(int centerX, int centerY, int width, int height) {

            }
        });
        clRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                focusView.handleEvent(event);
                return true;
            }
        });
    }
}
