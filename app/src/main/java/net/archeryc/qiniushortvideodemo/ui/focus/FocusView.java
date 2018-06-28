package net.archeryc.qiniushortvideodemo.ui.focus;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @author ArcherYc
 * @date on 2018/6/28  下午4:27
 * @mail 247067345@qq.com
 */
public class FocusView extends AppCompatImageView implements View.OnTouchListener, Animator.AnimatorListener {

    private GestureDetector mDetector;
    private AnimatorSet mAnimatorSet;

    public FocusView(Context context) {
        this(context, null);
    }

    public FocusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(GONE);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(600);
        mAnimatorSet.addListener(this);
        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (!mAnimatorSet.isRunning()) {
                    setX(e.getX() - (float) getWidth() / 2);
                    setY(e.getY() - (float) getHeight() / 2);

                    ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(FocusView.this, "scaleX",
                            2.0f, 1.0f);
                    ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(FocusView.this, "scaleY",
                            2.0f, 1.0f);
                    mAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator);
                    mAnimatorSet.start();
                }
                return super.onSingleTapUp(e);
            }
        });
    }

    public void attach(View view) {
        view.setOnTouchListener(this);
    }

    public boolean isFocusing() {
        return mAnimatorSet.isRunning();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        setVisibility(GONE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
