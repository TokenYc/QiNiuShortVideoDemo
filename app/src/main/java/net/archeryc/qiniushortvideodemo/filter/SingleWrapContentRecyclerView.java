package net.archeryc.qiniushortvideodemo.filter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 类似于ViewPager的展示单个item的RecyclerView，自动适应内部item的高度。
 * @author ArcherYc
 * @date on 2018/7/9  下午5:07
 * @mail 247067345@qq.com
 */
public class SingleWrapContentRecyclerView extends RecyclerView {

    public SingleWrapContentRecyclerView(Context context) {
        super(context);
    }

    public SingleWrapContentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleWrapContentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = 0;
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            int childWidth = getChildAt(i).getMeasuredWidth();
            if (childWidth > width) {
                width = childWidth;
            }
            int childHeight = getChildAt(i).getMeasuredHeight();
            if (childHeight > height) {
                height = childHeight;
            }
        }

        int wrapWithSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int wrapHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        setMeasuredDimension(wrapWithSpec, wrapHeightSpec);
    }
}
