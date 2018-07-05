package net.archeryc.qiniushortvideodemo.ui.switchwheel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ArcherYc
 * @date on 2018/7/5  上午9:53
 * @mail 247067345@qq.com
 */
public class WheelViewPager extends ViewPager {

    public WheelViewPager(@NonNull Context context) {
        this(context, null);
    }

    public WheelViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClipChildren(false);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int width = Integer.MAX_VALUE / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            if (childHeight > height) {
                height = childHeight;
            }
            if (childWidth < width) {
                width = childWidth;
            }
        }
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }

}
