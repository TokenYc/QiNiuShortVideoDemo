package net.archeryc.qiniushortvideodemo.ui.switchwheel;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.archeryc.qiniushortvideodemo.R;

public class SwitchWheelActivity extends AppCompatActivity {
    private SwitchWheelView switchWheelView;
    private TextAdapter textAdapter = new TextAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_wheel);
        switchWheelView = findViewById(R.id.switchWheelView);
        switchWheelView.setAdapter(textAdapter);
        switchWheelView.getWheelViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switchWheelView.getWheelViewPager().setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                Log.d("transform", "position--->" + position);
                TextView textView = page.findViewById(R.id.tv_type);
                if (position == 0) {
                    if (Color.WHITE != textView.getCurrentTextColor()) {
                        textView.setTextColor(Color.WHITE);
                    }
                } else if (position == 1 || position == -1) {
                    if (Color.BLACK != textView.getCurrentTextColor()) {
                        textView.setTextColor(Color.BLACK);
                    }
                }
            }
        });
    }

    class TextAdapter extends PagerAdapter {

        private String[] titles = {"照片", "视频"};

        @Override
        public int getCount() {
            return titles.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(SwitchWheelActivity.this).inflate(
                    R.layout.item_text, container, false);
            TextView textView = view.findViewById(R.id.tv_type);
            textView.setText(titles[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            TextView textView = container.findViewById(R.id.tv_type);
        }
    }
}
