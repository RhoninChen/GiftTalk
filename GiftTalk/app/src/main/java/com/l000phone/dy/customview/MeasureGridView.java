package com.l000phone.dy.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by HP on 2016/7/10.
 */
public class MeasureGridView extends GridView{
    public MeasureGridView(Context context) {
        this(context, null);
    }

    public MeasureGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureHeight);
    }
}
