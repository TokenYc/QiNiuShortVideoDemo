package net.archeryc.qiniushortvideodemo.ui.switchwheel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;


/**
 * @author ArcherYc
 * @date on 2018/7/4  下午5:05
 * @mail 247067345@qq.com
 */
public class SwitchWheelView extends RelativeLayout {

    private WheelViewPager wheelViewPager;

    public SwitchWheelView(@NonNull Context context) {
        this(context, null);
    }

    public SwitchWheelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClipChildren(false);
        wheelViewPager = new WheelViewPager(getContext());
        addView(wheelViewPager);
        RelativeLayout.LayoutParams lp = (LayoutParams) wheelViewPager.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public void setAdapter(PagerAdapter adapter) {
        wheelViewPager.setAdapter(adapter);
    }

    public WheelViewPager getWheelViewPager() {
        return wheelViewPager;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return wheelViewPager.dispatchTouchEvent(event);
    }

}
