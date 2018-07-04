package net.archeryc.qiniushortvideodemo.filter;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author ArcherYc
 * @date on 2018/7/3  下午4:51
 * @mail 247067345@qq.com
 */
public class FilterDetector implements GestureDetector.OnGestureListener {

    private static final float LIMIT_SPEED = 800;

    private GestureDetector gestureDetector;

    private OnFilterChangeListener mOnFilterChangeListener;

    public FilterDetector(Context context) {
        gestureDetector = new GestureDetector(context, this);
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
        Log.d("detector","distanceX---->"+distanceX+"\tdistanceY---->"+distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > LIMIT_SPEED) {
                if (mOnFilterChangeListener != null) {
                    mOnFilterChangeListener.onFilterChange(1);
                }
            } else if (velocityX < -LIMIT_SPEED) {
                if (mOnFilterChangeListener != null) {
                    mOnFilterChangeListener.onFilterChange(-1);
                }
            }
        }
        return false;
    }

    public interface OnFilterChangeListener {
        void onFilterChange(int direction);
    }

    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.mOnFilterChangeListener = listener;
    }

    public void handleEvent(MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
    }
}
