package net.archeryc.qiniushortvideodemo.bright;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author ArcherYc
 * @date on 2018/7/10  下午7:08
 * @mail 247067345@qq.com
 */
public class BrightnessDetector implements GestureDetector.OnGestureListener {

    private GestureDetector mDetector;

    private Context mContext;

    private int maxLength;

    private boolean mIsFirstScroll = true;

    private boolean mIsConsume = false;

    private OnBrightnessChangedListener mOnBrightnessChangedListener;

    private int mMinExposureCompensation;

    private int mMaxExposureCompensation;

    private int mExposureCompensation = 0;

    private int mStartExposureCompensation = 0;

    private float mDownY;

    public BrightnessDetector(Context context, int minExposureCompensation, int maxExposureCompensation) {
        this.mContext = context;
        this.mMinExposureCompensation = minExposureCompensation;
        this.mMaxExposureCompensation = maxExposureCompensation;
        init();
    }

    private void init() {
        mDetector = new GestureDetector(mContext, this);
        maxLength = mContext.getResources().getDisplayMetrics().heightPixels / 4;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.d("onScroll", "e1 y:" + e1.getY() + " e2 y:" + e2.getY());
        if (mIsFirstScroll) {
            mIsConsume = Math.abs((e2.getY() - e1.getY()) / (e2.getX() - e1.getX())) > 1;
        }
        mIsFirstScroll = false;

//        if (mIsConsume) {
//            float percentage = (e1.getY() - e2.getY()) / (maxLength / 2);
//            Log.d("onScroll", "percentage---->" + percentage);
//            if (percentage >= -1 && percentage <= 1) {
//                if (mOnBrightnessChangedListener != null) {
//                    mOnBrightnessChangedListener.onBrightnessChanged(percentage);
//                }
//            }
//        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void handleEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                mIsFirstScroll = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsConsume) {
                    mStartExposureCompensation = mExposureCompensation;
                    mIsConsume = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsConsume && mMinExposureCompensation != 0 && mMaxExposureCompensation != 0) {
                    float percentage = (mDownY - event.getY()) / ((float) maxLength / 2);
                    int result = (int) (mStartExposureCompensation + percentage * (float) (mMaxExposureCompensation - mMinExposureCompensation));
                    if (result < mMinExposureCompensation) {
                        result = mMinExposureCompensation;
                    }
                    if (result > mMaxExposureCompensation) {
                        result = mMaxExposureCompensation;
                    }
                    if (result >= mMinExposureCompensation && result <= mMaxExposureCompensation) {
                        mExposureCompensation = result;
                        if (mOnBrightnessChangedListener != null) {
                            mOnBrightnessChangedListener.onBrightnessChanged(result);
                        }
                    }

                }
                break;
            default:
                break;
        }
        mDetector.onTouchEvent(event);
    }

    public void setOnBrightnessChangedListener(OnBrightnessChangedListener listener) {
        this.mOnBrightnessChangedListener = listener;
    }

    public interface OnBrightnessChangedListener {
        /**
         * 亮度变化百分比
         *
         * @param exposureCompensation 曝光度
         */
        void onBrightnessChanged(int exposureCompensation);

    }

}
