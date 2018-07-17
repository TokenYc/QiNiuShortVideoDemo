package net.archeryc.qiniushortvideodemo.ui.cover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.archeryc.qiniushortvideodemo.ui.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ArcherYc
 * @date on 2018/7/17  下午5:14
 * @mail 247067345@qq.com
 */
public class CoverSelectView extends View {

    public static final int MAX_COVER = 9;

    private Context mContext;

    private List<Bitmap> mBitmapList = new ArrayList<>();
    private long mTotalTime;
    private long mSelectTime;

    private Paint mCursorPaint;
    private Paint mBitmapPaint;
    private Rect mBitmapSrcRect = new Rect();
    private Rect mBitmapDstRect = new Rect();
    private Rect mCursorRect = new Rect();

    private boolean mIsDrag = false;
    private RectF mDownRect = new RectF();
    private float mLastX = 0;

    public CoverSelectView(Context context) {
        this(context, null);
    }

    public CoverSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoverSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        mCursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCursorPaint.setColor(Color.WHITE);
        mCursorPaint.setStrokeWidth(DensityUtil.dip2px(mContext, 2));
        mCursorPaint.setStyle(Paint.Style.STROKE);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setCovers(List<Bitmap> bitmaps, long totalTime, long selectTime) {
        mBitmapList = bitmaps;
        mTotalTime = totalTime;
        mSelectTime = selectTime;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapList.size() >= MAX_COVER) {
            for (int i = 0; i < MAX_COVER; i++) {
                Bitmap bitmap = mBitmapList.get(0);
                float targetRatio = (float) getMeasuredHeight() / (getMeasuredWidth() / MAX_COVER);
                if ((float) bitmap.getHeight() / bitmap.getWidth() > targetRatio) {
                    mBitmapSrcRect.left = 0;
                    mBitmapSrcRect.right = bitmap.getWidth();
                    mBitmapSrcRect.top = (int) ((bitmap.getHeight() - bitmap.getWidth() * targetRatio) / 2);
                    mBitmapSrcRect.bottom = (int) (mBitmapSrcRect.top + bitmap.getWidth() * targetRatio);
                } else {
                    mBitmapSrcRect.top = 0;
                    mBitmapSrcRect.left = (int) ((bitmap.getWidth() - bitmap.getHeight() / targetRatio) / 2);
                    mBitmapSrcRect.right = (int) (mBitmapSrcRect.left + bitmap.getHeight() / targetRatio);
                    mBitmapSrcRect.bottom = bitmap.getHeight();
                }

                mBitmapDstRect.left = i * getMeasuredWidth() / MAX_COVER;
                mBitmapDstRect.top = 0;
                mBitmapDstRect.right = mBitmapDstRect.left + getMeasuredWidth() / MAX_COVER;
                mBitmapDstRect.bottom = getMeasuredHeight();
                canvas.drawBitmap(bitmap, mBitmapSrcRect, mBitmapDstRect, mBitmapPaint);
            }
        }

        mCursorRect.left = (int) ((int) ((float) mSelectTime / mTotalTime * getMeasuredWidth()) +
                mCursorPaint.getStrokeWidth() / 2);
        mCursorRect.right = (int) (mCursorRect.left + getMeasuredWidth() / MAX_COVER - mCursorPaint.getStrokeWidth() / 2);
        mCursorRect.top = (int) (mCursorPaint.getStrokeWidth() / 2);
        mCursorRect.bottom = (int) (getMeasuredHeight() - mCursorPaint.getStrokeWidth() / 2);
        canvas.drawRect(mCursorRect, mCursorPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownRect.left = getMeasuredWidth() * ((float) mSelectTime / mTotalTime);
                mDownRect.right = mDownRect.left + getMeasuredWidth() / MAX_COVER;
                mIsDrag = event.getX() >= mDownRect.left && event.getX() <= mDownRect.right;
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDrag && mLastX > 0) {
                    long mTargetSelectTime = mSelectTime + (long) (mTotalTime * ((event.getX() - mLastX)
                            / getMeasuredWidth()));
                    if (mTargetSelectTime >= 0 && mTargetSelectTime <= mTotalTime) {
                        mSelectTime = mTargetSelectTime;
                        mLastX = event.getX();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mIsDrag) {
                    mSelectTime = (long) (mTotalTime * event.getX() / getMeasuredWidth());
                    invalidate();
                }
                mIsDrag = false;
                mLastX = 0;
                break;
            default:
                break;

        }
        return true;
    }
}
