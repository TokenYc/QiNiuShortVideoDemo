package net.archeryc.qiniushortvideodemo.ui.focus;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.archeryc.qiniushortvideodemo.R;

public class FocusActivity extends AppCompatActivity {


    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        clRoot = findViewById(R.id.cl_root);

        FocusView focusView=new FocusView(this);
        clRoot.addView(focusView);
        focusView.setImageResource(R.mipmap.icon_focus_video);
        focusView.attach(clRoot);
    }
}
