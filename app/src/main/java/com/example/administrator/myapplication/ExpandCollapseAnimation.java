package com.example.administrator.myapplication;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandCollapseAnimation extends Animation {
    private final View mTargetView;
    private final int mStartHeight;
    private final int mEndHeight;
    private final int mAnimationDuration = 200;
    public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
        mTargetView = view;
        mStartHeight = startHeight;
        mEndHeight = endHeight;
        setDuration(mAnimationDuration);
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
       // mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
        mTargetView.getLayoutParams().height = newHeight;
        mTargetView.requestLayout();

    }
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
    @Override
    public boolean willChangeBounds() {
        return true;
    }
};