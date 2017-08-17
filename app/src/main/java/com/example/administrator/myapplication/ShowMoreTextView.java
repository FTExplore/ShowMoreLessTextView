package com.example.administrator.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 项目需要，封装一个。
 * QQ 512378645
 * Created by 张宏祚 on 2016/11/22.
 */

public class ShowMoreTextView extends RelativeLayout {

    private CustomBaseLineTextView mContent;
    private int SET_TEXT_ID = 0; // 为了避免多次重复调用setText的情况
    private int SET_TEXT_POST_ID = 0;
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
    private final int distanceOfNewLine = 0;
    private MShrinkRunnable mShrinkRunnable = new MShrinkRunnable();

    public ShowMoreTextView(Context context) {
        this(context, null);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews(context);
        initAttr(context, attrs);
    }

    private void createViews(Context context) {
        textPaint = new TextPaint();
        mContent = new CustomBaseLineTextView(context);
        mBtnShowMore = new TextView(context);
        mBtnShowLess = new TextView(context);
    }

    /**
     * 根据自定义属性设置
     */
    private void initAttr(Context context, AttributeSet attrs) {

        int colorContent = 0x333333;
        int colorMoreBtnBackground = 0xffffff;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ShowMoreTextView,
                    0, 0);
            colorContent = a.getColor(R.styleable.ShowMoreTextView_TextColor, Color.parseColor("#333333"));
            colorMoreBtnBackground = a.getColor(R.styleable.ShowMoreTextView_MoreBtnBackground, Color.parseColor("#ffffff"));
            MaxLine = a.getInt(R.styleable.ShowMoreTextView_ShrinkMaxLine, 1);
            float size = a.getDimensionPixelSize(R.styleable.ShowMoreTextView_TextSize, -1);
            if (size != -1) {
                textPaint.setTextSize(size);
                mContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                mBtnShowMore.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                mBtnShowLess.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            } else {
                textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
                mContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            }
            a.recycle();
        } else {
            textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
            mContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        }
        init(context);

        mContent.setTextColor(colorContent);
        mBtnShowMore.setBackground(new ColorDrawable(colorMoreBtnBackground));
        mBtnShowLess.setBackground(new ColorDrawable(colorMoreBtnBackground));
        //
        LengthShowMore = textPaint.measureText(ShowMoreString);
        LengthShowLess = textPaint.measureText(ShowLessString);
        LengthEll = textPaint.measureText(EllString);
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
        SET_TEXT_ID++;
        this.mContentText = text;
        //Log.e("ZHZ","原始 ： " + text);
        mBtnShowLess.setVisibility(GONE);
        mContent.setText(mContentText);
        mContent.setMaxLines(3);
        //mContent.setText(Html.fromHtml(mContentText));
        mContent.post(mShrinkRunnable);
        //刷新重置动画状态
        ViewGroup.LayoutParams layout = getLayoutParams();
        layout.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layout.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        if(mContent.getLayout()!=null){
//            try {
//                Animation animation = new ExpandCollapseAnimation(ShowMoreTextView.this,mContent.getLayout().getLineTop(MaxLine),mContent.getLayout().getLineTop(MaxLine));
//                ShowMoreTextView.this.startAnimation(animation);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }

    private class MShrinkRunnable implements Runnable {

        @Override
        public void run() {
            SET_TEXT_POST_ID++;
            if (SET_TEXT_ID > SET_TEXT_POST_ID) {
                return;
            }
            if (mContent == null || mContent.getLayout() == null) {
                mBtnShowMore.setVisibility(GONE);
                mBtnShowLess.setVisibility(GONE);
                return;
            }
            int line = mContent.getLayout().getLineCount();
            try {
                //防闪动
                Animation animation = new ExpandCollapseAnimation(ShowMoreTextView.this, mContent.getLayout().getLineTop(line >= MaxLine ? MaxLine : line), mContent.getLayout().getLineTop(line >= MaxLine ? MaxLine : line));
                ShowMoreTextView.this.startAnimation(animation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (line > MaxLine) {
                mBtnShowMore.setVisibility(VISIBLE);
                mContent.setBaseLine(MaxLine - 1);
            } else {
                mContent.setVisibility(VISIBLE);
                mBtnShowMore.setVisibility(GONE);
                return;
            }
            if (TextUtils.isEmpty(mContentText)) {
                mBtnShowMore.setVisibility(GONE);
                return;
            }
            int firstCharIndex = mContent.getLayout().getLineStart(MaxLine - 1);
            int LastCharIndex = mContent.getLayout().getLineEnd(MaxLine - 1);
            String lastline = mContentText.substring(firstCharIndex, LastCharIndex);

            float totalWidth = mContent.getWidth();
            float lastLineWidth = textPaint.measureText(lastline);
            float maxLineWidh = totalWidth - LengthEll - LengthShowMore;

            if (lastLineWidth > maxLineWidh) {
                // 最后一行需要裁剪
                int index = 0;
                StringBuilder sb = new StringBuilder(lastline);
                do {
                    index++;
                    sb.deleteCharAt(sb.length() - 1);
                    lastLineWidth = textPaint.measureText(sb.toString());
                } while (lastLineWidth > maxLineWidh);
                String result = mContentText.substring(0, (LastCharIndex - index)) + EllString;
                mContent.setText(result);
            } else {
                String result = mContentText.substring(0, LastCharIndex).trim() + EllString;
                mContent.setText(result);
            }
            mContent.setVisibility(VISIBLE);
        }
    }


    private void initTextView(Context context) {
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContent.setLayoutParams(params);
        lineSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics());
        mContent.setLineSpacing(lineSpace, 1.0f);
        mContent.setId(R.id.ShowMoreContent);
        mContent.setTextColor(Color.parseColor("#333333"));
    }

    private void initShowMore(Context context) {
        mBtnShowMore.setId(R.id.ShowMoreBtn);
        mBtnShowMore.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mBtnShowMore.setTextColor(Color.parseColor("#6699CC"));
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_BASELINE, R.id.ShowMoreContent);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mBtnShowMore.setLayoutParams(params);
        mBtnShowMore.setText(ShowMoreString);
        mBtnShowMore.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mBtnShowMore.setVisibility(GONE);
        mBtnShowMore.setOnClickListener(mShorMore);
    }

    private void initShowLess(Context context) {
        mBtnShowLess.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mBtnShowLess.setTextColor(Color.parseColor("#6699CC"));
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
            mContent.setMaxLines(100);
            mContent.setInsurance(true);
            mContent.setText(mContentText);
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
            mBtnShowLess.setVisibility(VISIBLE);
            mBtnShowMore.setVisibility(GONE);

            Animation animation = new ExpandCollapseAnimation(ShowMoreTextView.this, mContent.getLayout().getLineTop(MaxLine), mContent.getLayout().getLineTop(mContent.getLineCount()) + dipToPX(getContext(), distanceOfNewLine));
            ShowMoreTextView.this.startAnimation(animation);
        }
    };

    private OnClickListener mShowLess = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent.setMaxLines(3);
            Animation animation = new ExpandCollapseAnimation(ShowMoreTextView.this, mContent.getLayout().getLineTop(mContent.getLineCount()) + dipToPX(getContext(), distanceOfNewLine), mContent.getLayout().getLineTop(MaxLine));
            ShowMoreTextView.this.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mContent.setInsurance(false);
                    mContent.setBaseLine(MaxLine - 1);
                    mContent.post(mShrinkRunnable);
                    mBtnShowLess.setVisibility(GONE);
                    mBtnShowMore.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    };

    /**
     * 仅用于判断“收回”按钮是否要单独起一行
     */
    private boolean isNeedNewLine() {

        if (TextUtils.isEmpty(mContentText)) {
            return true;
        }

        int startIndex = mContent.getLayout().getLineStart(mContent.getLineCount() - 1);
        String lastLine = mContentText.substring(startIndex);
        float totalWidth = mContent.getWidth();
        float lastlineWidth = textPaint.measureText(lastLine);
        float maxWidth = totalWidth - LengthShowLess - LengthEll;
        return lastlineWidth > maxWidth;
    }


    /**
     * px = dp * (dpi / 160)
     *
     * @param ctx
     * @param dip
     * @return
     */
    private int dipToPX(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

}
