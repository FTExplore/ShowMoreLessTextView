package com.example.administrator.myapplication;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class CustomBaseLineTextView extends TextView {
    private int baseLine;

    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
    }
    private boolean insurance = false;

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public CustomBaseLineTextView(Context context) {
        super(context);
    }

    public CustomBaseLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBaseLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getBaseline() {
        Layout layout = getLayout();
        if (layout == null) {
            return super.getBaseline();
        }

        if (insurance){
            return layout.getLineBaseline(layout.getLineCount() -1 );
        }

//        int baselineOffset = super.getBaseline() - layout.getLineBaseline(0);
//        return baselineOffset + layout.getLineBaseline(layout.getLineCount() - 1);
        return layout.getLineBaseline(baseLine>=layout.getLineCount()?layout.getLineCount()-1:baseLine);
    }

    public int getOneBaseLine() {
        return super.getBaseline();
    }
}
