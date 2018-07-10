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

    public BrightnessDetector(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mDetector = new GestureDetector(mContext, this);
        maxLength = mContext.getResources().getDisplayMetrics().heightPixels / 2;
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
            Log.d("isConsume", "result---->" + Math.abs((e2.getY() - e1.getY()) / (e2.getX() - e1.getX())));
            mIsConsume = Math.abs((e2.getY() - e1.getY()) / (e2.getX() - e1.getX())) > 1;
        }
        mIsFirstScroll = false;

        if (mIsConsume) {
            float percentage = (e1.getY() - e2.getY()) / (maxLength / 2);
            Log.d("onScroll", "percentage---->" + percentage);
        }
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
                mIsFirstScroll = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        mDetector.onTouchEvent(event);
    }

    public interface OnBrightnessChangedListener {
        /**
         * 亮度变化百分比 -100% ~ 100%
         *
         * @param percent
         */
        void onBrightnessChanged(float percent);
    }

}
