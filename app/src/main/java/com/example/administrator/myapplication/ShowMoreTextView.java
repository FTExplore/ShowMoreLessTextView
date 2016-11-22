package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ShowMoreTextView extends RelativeLayout {

    private test mContent;
    private TextView mBtnShowMore;
    private TextView mBtnShowLess;
    private float lineSpace;
    private RelativeLayout.LayoutParams paramsAlignBottom;
    private RelativeLayout.LayoutParams paramsBelow;

    public ShowMoreTextView(Context context) {
        super(context);
        init(context);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initTextView(context);
        initShowMore(context);
        initShowLess(context);
        addView(mContent);
        addView(mBtnShowMore);
        addView(mBtnShowLess);
    }

    public void setText(String text) {
        mContent.setText(text);
    }


    private void initTextView(Context context) {
        mContent = new test(context);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContent.setLayoutParams(params);
        lineSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
        mContent.setLineSpacing(lineSpace, 1.0f);
        mContent.setMaxLines(1);
        mContent.setEllipsize(TextUtils.TruncateAt.END);
        mContent.setId(R.id.ShowMoreContent);
    }

    private void initShowMore(Context context) {
        mBtnShowMore = new TextView(context);
        mBtnShowMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mBtnShowMore.setTextColor(Color.parseColor("#69BE87"));
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mBtnShowMore.setLayoutParams(params);
        mBtnShowMore.setText("更多");
        mBtnShowMore.setOnClickListener(mShorMore);
    }

    private void initShowLess(Context context) {
        mBtnShowLess = new TextView(context);
        mBtnShowLess.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mBtnShowLess.setTextColor(Color.parseColor("#69BE87"));
        paramsAlignBottom = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsAlignBottom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsAlignBottom.addRule(ALIGN_BASELINE, R.id.ShowMoreContent);
        mBtnShowLess.setLayoutParams(paramsAlignBottom);
        mBtnShowLess.setText("收起");
        mBtnShowLess.setVisibility(GONE);
        mBtnShowLess.setOnClickListener(mShowLess);
        // another params
        paramsBelow = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsBelow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsBelow.addRule(BELOW, R.id.ShowMoreContent);
    }

    private OnClickListener mShorMore = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent.setMaxLines(Integer.MAX_VALUE);
            // TODO 在这里，判断一下，使用哪个LayoutParams
            mBtnShowLess.setLayoutParams(paramsBelow);
            mBtnShowLess.setVisibility(VISIBLE);
            mBtnShowMore.setVisibility(GONE);
        }
    };

    private OnClickListener mShowLess = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent.setMaxLines(1);
            mBtnShowLess.setVisibility(GONE);
            mBtnShowMore.setVisibility(VISIBLE);
        }
    };
}
