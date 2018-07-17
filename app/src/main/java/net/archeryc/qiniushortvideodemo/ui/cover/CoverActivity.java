package net.archeryc.qiniushortvideodemo.ui.cover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.archeryc.qiniushortvideodemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yc
 */
public class CoverActivity extends AppCompatActivity {

    private CoverSelectView coverSelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        coverSelectView = findViewById(R.id.coverSelectView);

        final List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_cover);
        for (int i = 0; i < CoverSelectView.MAX_COVER; i++) {
            bitmaps.add(bitmap);
        }
        coverSelectView.setCovers(bitmaps, 10 * 1000, 0);
    }
}
