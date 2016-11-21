package com.fintech.ternaku.Main.NavBar.CalendarToDoList;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;

import com.fintech.ternaku.R;
import com.p_v.flexiblecalendar.view.CircularEventCellView;

public class CalendarCellView extends CircularEventCellView {

    public CalendarCellView(Context context) {
        super(context);
    }

    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (7*width)/8;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}
