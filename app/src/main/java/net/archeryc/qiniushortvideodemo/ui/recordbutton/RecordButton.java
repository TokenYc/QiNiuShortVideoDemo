package net.archeryc.qiniushortvideodemo.ui.recordbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;

import net.archeryc.qiniushortvideodemo.ui.DensityUtil;

/**
 * @author ArcherYc
 * @date on 2018/7/11  下午2:28
 * @mail 247067345@qq.com
 */
public class RecordButton extends View {
    private Context mContext;

    private Paint mRectPaint;

    private Paint mCirclePaint;

    private float corner;
    private float circleRadius;
    private float circleStrokeWidth;
    private float rectWidth;

    private float mMinCircleRadius;
    private float mMaxCircleRadius;
    private float mMinRectWidth;
    private float mMaxRectWidth;
    private float mMinCorner;
    private float mMaxCorner;
    private float mMinCircleStrokeWidth;
    private float mMaxCircleStrokeWidth;


    private RectF mRectF = new RectF();

    private RecordMode mRecordMode;

    private AnimatorSet mAnimatorSet;

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    public RecordButton(Context context) {
        this(context, null);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.WHITE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#33ffffff"));

        mMinCircleStrokeWidth = DensityUtil.dip2px(mContext, 3);
        mMaxCircleStrokeWidth = DensityUtil.dip2px(mContext, 12);
        circleStrokeWidth = mMinCircleStrokeWidth;
        mCirclePaint.setStrokeWidth(circleStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        mMaxRectWidth = width / 3;
        mMinRectWidth = mMaxRectWidth * 0.6f;

        mMinCircleRadius = mMaxRectWidth / 2 + mMinCircleStrokeWidth + DensityUtil.dip2px(mContext, 5);
        mMaxCircleRadius = width / 2 - mMaxCircleStrokeWidth;

        mMinCorner = DensityUtil.dip2px(mContext, 5);
        mMaxCorner = mMaxRectWidth / 2;

        if (rectWidth == 0) {
            rectWidth = mMaxRectWidth;
        }
        if (circleRadius == 0) {
            circleRadius = mMinCircleRadius;
        }
        if (corner == 0) {
            corner = rectWidth / 2;
        }


        mCirclePaint.setColor(Color.parseColor("#33ffffff"));
        canvas.drawCircle(centerX, centerY, circleRadius, mCirclePaint);
        mCirclePaint.setXfermode(mXfermode);

        mCirclePaint.setColor(Color.parseColor("#000000"));
        canvas.drawCircle(centerX, centerY, circleRadius - circleStrokeWidth, mCirclePaint);
        mCirclePaint.setXfermode(null);

        mRectF.left = centerX - rectWidth / 2;
        mRectF.right = centerX + rectWidth / 2;
        mRectF.top = centerY - rectWidth / 2;
        mRectF.bottom = centerY + rectWidth / 2;
        canvas.drawRoundRect(mRectF, corner, corner, mRectPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void startAnimation() {
        mAnimatorSet = new AnimatorSet();
        AnimatorSet startAnimatorSet = new AnimatorSet();
        ObjectAnimator cornerAnimator = ObjectAnimator.ofFloat(this, "corner",
                mMaxCorner, mMinCorner)
                .setDuration(500);
        ObjectAnimator rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
                mMaxRectWidth, mMinRectWidth)
                .setDuration(500);
        ObjectAnimator radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
                mMinCircleRadius, mMaxCircleRadius)
                .setDuration(500);
        startAnimatorSet.playTogether(cornerAnimator, rectSizeAnimator, radiusAnimator);

        ObjectAnimator circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
                mMinCircleStrokeWidth, mMaxCircleStrokeWidth, mMinCircleStrokeWidth)
                .setDuration(1500);
        circleWidthAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mAnimatorSet.playSequentially(startAnimatorSet, circleWidthAnimator);
        mAnimatorSet.start();
    }


    public void setCorner(float corner) {
        this.corner = corner;
        invalidate();
    }

    public void setCircleRadius(float circleRadius) {
        this.circleRadius = circleRadius;
    }

    public void setCircleStrokeWidth(float circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
        invalidate();
    }

    public void setRectWidth(float rectWidth) {
        this.rectWidth = rectWidth;
    }

    private enum RecordMode {
        /**
         * 单击录制模式
         */
        SINGLE_CLICK,
        /**
         * 长按录制模式
         */
        LONG_CLICK;

        RecordMode() {

        }
    }
}
