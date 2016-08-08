package ru.irafa.conversation.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;

/**
 * Fix for wrap_content in multiple line TextView having not correct width. Based on the answer
 * from stackoverflow.
 * http://stackoverflow.com/questions/7439748/why-is-wrap-content-in-multiple-line-textview-filling-parent
 * Created on 08.08.16.
 */

public class WrapWidthTextView extends AppCompatTextView{

    public WrapWidthTextView(Context context) {
        super(context);
    }

    public WrapWidthTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapWidthTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int width = (int) Math.ceil(getMaxLineWidth(layout))
                    + getCompoundPaddingLeft() + getCompoundPaddingRight();
            int height = getMeasuredHeight();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineWidth(Layout layout) {
        float max_width = 0.0f;
        int lines = layout.getLineCount();
        for (int i = 0; i < lines; i++) {
            if (layout.getLineWidth(i) > max_width) {
                max_width = layout.getLineWidth(i);
            }
        }
        return max_width;
    }
}
