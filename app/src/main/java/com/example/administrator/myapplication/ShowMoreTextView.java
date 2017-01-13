package com.example.administrator.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

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

    private int MaxLine = 1; // 默认是1
    private TextPaint textPaint;
    private final String ShowMoreString = " 更多";
    private final String ShowLessString = " 收起";
    private final String EllString = "...";
    private float LengthShowMore;
    private float LengthShowLess;
    private float LengthEll;

    public ShowMoreTextView(Context context) {
        this(context, null);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new TextPaint();
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        LengthShowMore = textPaint.measureText(ShowMoreString);
        LengthShowLess = textPaint.measureText(ShowLessString);
        LengthEll = textPaint.measureText(EllString);
        initAttr(context, attrs);
    }

    /**
     * 根据自定义属性设置
     */
    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ShowMoreTextView,
                    0, 0);
            int color = a.getColor(R.styleable.ShowMoreTextView_TextColor, Color.parseColor("#333333"));
            if (mContent != null) {
                mContent.setTextColor(color);
            }
            MaxLine = a.getInt(R.styleable.ShowMoreTextView_ShrinkMaxLine, 1);
        }
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
        mBtnShowLess.setVisibility(GONE);
        mContent.setText(mContentText);
        mContent.post(mShrinkRunnable);
    }

    /**
     * 当TextView是收缩状态时，是否需要显示省略号和"更多"按钮
     */
    private Runnable mShrinkRunnable = new Runnable() {
        @Override
        public void run() {

            if (mContent.getLayout().getLineCount() > MaxLine) {
                mBtnShowMore.setVisibility(VISIBLE);
            } else {
                mBtnShowMore.setVisibility(GONE);
                return;
            }

            if (TextUtils.isEmpty(mContentText)){
                mBtnShowMore.setVisibility(GONE);
                return;
            }


            int firstCharIndex = mContent.getLayout().getLineStart(MaxLine - 1);
            int LastCharIndex = mContent.getLayout().getLineEnd(MaxLine - 1);
            String lastline = mContentText.substring(firstCharIndex,LastCharIndex);

            float totalWidth = mContent.getWidth();
            float lastLineWidth = textPaint.measureText(lastline);
            float maxLineWidh = totalWidth - LengthEll - LengthShowMore;

            if (lastLineWidth > maxLineWidh){
                // 最后一行需要裁剪
                int index = 0;
                StringBuilder sb = new StringBuilder(lastline);
                do {
                    index++;
                    sb.deleteCharAt(sb.length() - 1);
                    lastLineWidth = textPaint.measureText(sb.toString());
                }while (lastLineWidth > maxLineWidh);
                String result = mContentText.substring(0,(LastCharIndex - index))+EllString;
                mContent.setText(result);
            }else{
                String result = mContentText.substring(0, LastCharIndex) + EllString;
                mContent.setText(result);
            }
        }
    };

    private int calLastIndex(String line) {
        String f = "[\u4e00-\u9fa5]+"; // 判断是否是中文
        Pattern HanyuPattern = Pattern.compile(f);
        int target = 6; // 目标是6个英文字母的位置
        int index = 0;
        for (int i = line.length() - 1; i > 0; i--) {
            char temp = line.charAt(i);
            if (HanyuPattern.matcher(String.valueOf(temp)).matches()) {
                index += 2;
            } else {
                index += 1;
            }
            if (index >= target) {
                return i;
            }
        }
        return 3;
    }

    private void initTextView(Context context) {
        mContent = new CustomBaseLineTextView(context);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (MaxLine == 1) {
            params.addRule(LEFT_OF, R.id.ShowMoreBtn);
        }
        mContent.setLayoutParams(params);
        lineSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
        mContent.setLineSpacing(lineSpace, 1.0f);
        mContent.setEllipsize(TextUtils.TruncateAt.END);
        mContent.setId(R.id.ShowMoreContent);
    }

    private void initShowMore(Context context) {
        mBtnShowMore = new TextView(context);
        mBtnShowMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mBtnShowMore.setTextColor(Color.parseColor("#69BE87"));
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_BASELINE, R.id.ShowMoreContent);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mBtnShowMore.setLayoutParams(params);
        mBtnShowMore.setId(R.id.ShowMoreBtn);
        mBtnShowMore.setText(ShowMoreString);
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
        mBtnShowLess.setText(ShowLessString);
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
