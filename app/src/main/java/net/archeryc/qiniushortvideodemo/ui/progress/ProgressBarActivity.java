package net.archeryc.qiniushortvideodemo.ui.progress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.archeryc.qiniushortvideodemo.R;

public class ProgressBarActivity extends AppCompatActivity {

    private SectionProgressBar progressBar;

    private int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        progressBar = findViewById(R.id.recordProgressBar);


    }

    public void start(View view) {
        progressBar.beginSection((int) (10 * 1000 /count * (1 - progressBar.getProgress())));
        count++;
    }

    public void pause(View view) {
        progressBar.endSection();
    }

    public void reset(View view) {
        progressBar.reset();
    }

    public void delete(View view) {
        progressBar.deleteLastSection();
    }
}
