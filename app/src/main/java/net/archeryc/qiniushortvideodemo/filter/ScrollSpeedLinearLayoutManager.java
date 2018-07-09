package net.archeryc.qiniushortvideodemo.filter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * @author ArcherYc
 * @date on 2018/7/3  下午5:16
 * @mail 247067345@qq.com
 */
public class ScrollSpeedLinearLayoutManager extends LinearLayoutManager {
    private static float MILLISECONDS_PER_INCH = 0.03f;
    private Context mContext;

    public ScrollSpeedLinearLayoutManager(Context context) {
        super(context);
        this.mContext = context;
    }

    public ScrollSpeedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return ScrollSpeedLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                        //返回滑动一个pixel需要多少毫秒
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    /**
     * 设置速度 默认慢速为 0.5f  0.03f为快速
     * @param speedFactor 速度因子
     */
    public void setSpeed(float speedFactor) {
        MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * speedFactor;
    }

    public void setSpeedSlow() {
        MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.5f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.03f;
    }
}

