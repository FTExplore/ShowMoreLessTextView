package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 项目需要，封装一个
 * Created by zhanghongzuo on 2016/11/22.
 */

public class ShowMoreTextView extends RelativeLayout {

    private CustomBaseLineTextView mContent;
    private TextView mBtnShowMore;
    private TextView mBtnShowLess;
    private float lineSpace;
    private RelativeLayout.LayoutParams paramsAlignBottom;
    private RelativeLayout.LayoutParams paramsBelow;
    private String mContentText = ""; // textview的主显示内容

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
        this.mContentText = text;
        mContent.setText(mContentText);
        mContent.post(mShrinkRunnable);
    }

    /**
     * 当TextView是收缩状态时，是否需要显示省略号和"更多"按钮
     */
    private Runnable mShrinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mContent.getLayout().getLineCount() == 0) {
                mBtnShowMore.setVisibility(GONE);
                return;
            }
            int FirstLineCount = mContent.getLayout().getLineEnd(0) - mContent.getLayout().getLineStart(0);
            if (mContent.length() > FirstLineCount) {
                // 需要省略号，显示更多
                String subString = mContentText.substring(0, mContent.getLayout().getLineEnd(0) - 3) + "...";
                mContent.setText(subString);
                mBtnShowMore.setVisibility(VISIBLE);
            } else {
                mBtnShowMore.setVisibility(GONE);
            }
        }
    };

    private void initTextView(Context context) {
        mContent = new CustomBaseLineTextView(context);
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
            mContent.post(new Runnable() {
                @Override
                public void run() {
                    if (isNeedNewLine()) {
                        mBtnShowLess.setLayoutParams(paramsBelow);
                    } else {
                        mBtnShowLess.setLayoutParams(paramsAlignBottom);
                    }
                }
            });
            mContent.setText(mContentText);
            mBtnShowLess.setVisibility(VISIBLE);
            mBtnShowMore.setVisibility(GONE);
        }
    };

    private OnClickListener mShowLess = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent.setMaxLines(1);
            mContent.post(mShrinkRunnable);
            mBtnShowLess.setVisibility(GONE);
            mBtnShowMore.setVisibility(VISIBLE);
        }
    };

    /**
     * 判断TextView最后一行剩余的空缺
     * 是否满三个字符.
     *
     * @return true 不满三个字符, false 超过三个字符
     */
    private boolean isNeedNewLine() {
        if (mContent == null || mContent.getLayout().getLineCount() < 1) {
            return true;
        }
        Layout layout = mContent.getLayout();
        int lineCount = layout.getLineCount();
        int FirstLineCount = layout.getLineEnd(0) - layout.getLineStart(0);
        int LastLineCount = layout.getLineEnd(lineCount - 1) - layout.getLineStart(lineCount - 1);
        if (FirstLineCount - LastLineCount > 3) {
            return false;
        }

        return true;
    }
}
