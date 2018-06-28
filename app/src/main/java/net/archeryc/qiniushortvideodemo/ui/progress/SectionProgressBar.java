package net.archeryc.qiniushortvideodemo.ui.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import net.archeryc.qiniushortvideodemo.ui.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ArcherYc
 * @date on 2018/6/27  上午11:47
 * @mail 247067345@qq.com
 */
public class SectionProgressBar extends View implements Animator.AnimatorListener {

    private static final String TAG = "RecordProgressBar";


    private Paint mBgPaint;
    private RectF mBgRectF;
    private int mBgCorner;


    private Paint mProgressPaint;
    private Path mProgressRectPath;
    private RectF mProgressRectF;
    private float[] mProgressCorner;

    private Paint mSectionPaint;
    private int mSectionWidth;

    private float progress = 0.0f;

    private List<SectionInfo> sectionList = new ArrayList<>();

    private ObjectAnimator progressAnimator;

    private SectionProgressListener mProgressListener;

    public SectionProgressBar(Context context) {
        this(context, null);
    }

    public SectionProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultParams();
        init();
    }

    private void initDefaultParams() {
        mBgCorner = DensityUtil.dip2px(getContext(), 5);
        mSectionWidth = DensityUtil.dip2px(getContext(), 2);
        mProgressCorner = new float[8];
        mProgressCorner[0] = mBgCorner;
        mProgressCorner[1] = mBgCorner;
        mProgressCorner[2] = 0f;
        mProgressCorner[3] = 0f;
        mProgressCorner[4] = 0f;
        mProgressCorner[5] = 0f;
        mProgressCorner[6] = mBgCorner;
        mProgressCorner[7] = mBgCorner;
    }

    private void init() {
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.parseColor("#22000000"));
        mBgRectF = new RectF();

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(Color.parseColor("#15bFFF"));
        mProgressRectF = new RectF();
        mProgressRectPath = new Path();
        float[] round = {mBgCorner, mBgCorner, mBgCorner, mBgCorner, 0f, 0f, 0f, 0f};
        mProgressRectPath.addRoundRect(mProgressRectF, round, Path.Direction.CW);

        mSectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSectionPaint.setStyle(Paint.Style.FILL);
        mSectionPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "width----->" + getWidth() + "  height--->" + getHeight());

        drawBg(canvas);

        drawProgress(canvas);

        drawSection(canvas);
    }

    private void drawBg(Canvas canvas) {
        mBgRectF.left = 0;
        mBgRectF.top = 0;
        mBgRectF.right = getWidth();
        mBgRectF.bottom = getHeight();
        canvas.drawRoundRect(mBgRectF, mBgCorner, mBgCorner, mBgPaint);
    }

    private void drawProgress(Canvas canvas) {
        mProgressRectF.left = 0;
        mProgressRectF.top = 0;
        mProgressRectF.right = getWidth() * progress;
        mProgressRectF.bottom = getHeight();
        mProgressRectPath.reset();
        mProgressRectPath.addRoundRect(mProgressRectF, mProgressCorner, Path.Direction.CW);
        canvas.drawPath(mProgressRectPath, mProgressPaint);
    }

    private void drawSection(Canvas canvas) {
        if (!sectionList.isEmpty()) {
            for (SectionInfo sectionInfo : sectionList) {
                canvas.drawRect(getWidth() * sectionInfo.getEndProgress() - mSectionWidth, 0f,
                        getWidth() * sectionInfo.getEndProgress(), getHeight(), mSectionPaint);
            }
        }
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (progress < 1.0f) {
            invalidate();
        }
    }

    /**
     * 开始一个片段
     *
     * @param leftTime
     */
    public void beginSection(int leftTime) {
        if (progressAnimator == null || !progressAnimator.isRunning()) {
            SectionInfo sectionInfo = new SectionInfo();
            sectionInfo.setStartProgress(progress);
            sectionList.add(sectionInfo);

            progressAnimator = ObjectAnimator.ofFloat(this, "progress", progress, 1.0f)
                    .setDuration(leftTime);
            progressAnimator.setInterpolator(new LinearInterpolator());
            if (progressAnimator != null) {
                progressAnimator.removeAllListeners();
            }
            progressAnimator.addListener(this);
            progressAnimator.start();
        }
    }

    /**
     * 结束一个片段
     */
    public void endSection() {
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
            if (!sectionList.isEmpty()) {
                sectionList.get(sectionList.size() - 1).setEndProgress(progress);
            }
            invalidate();
        }
    }

    /**
     * 删除一个片段
     */
    public void deleteLastSection() {
        if (progressAnimator != null && !progressAnimator.isRunning()) {
            if (!sectionList.isEmpty()) {
                SectionInfo sectionInfo = sectionList.get(sectionList.size() - 1);
                progress = sectionInfo.getStartProgress();
                sectionList.remove(sectionInfo);
                if (progressAnimator != null) {
                    progressAnimator.removeAllListeners();
                }
                progressAnimator = ObjectAnimator.ofFloat(this, "progress",
                        sectionInfo.getEndProgress(), sectionInfo.getStartProgress())
                        .setDuration((long) (1000 * (sectionInfo.getEndProgress() - sectionInfo.getStartProgress())));
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();
            }
        }
    }

    /**
     * 是否动画还在运行
     *
     * @return
     */
    public boolean isRunning() {
        return progressAnimator != null && progressAnimator.isRunning();
    }

    /**
     * 重置
     */
    public void reset() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        progress = 0.0f;
        sectionList.clear();
        invalidate();
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (progress == 1.0f) {
            if (!sectionList.isEmpty()) {
                sectionList.get(sectionList.size() - 1).setEndProgress(progress);
                invalidate();
            }
            if (mProgressListener != null) {
                mProgressListener.onProgressEnd();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public interface SectionProgressListener {
        void onProgressEnd();
    }

    public void setSectionProgressListener(SectionProgressListener listener) {
        mProgressListener = listener;
    }

}

