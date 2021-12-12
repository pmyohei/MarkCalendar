package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MarkView extends View {
    private final Paint paint;

    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public MarkView(Context context) {
        super(context);
        paint = new Paint();
    }

    public void setColor(int color){
        paint.setColor(getResources().getColor(color));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();

        paint.setColor(getResources().getColor(R.color.accent));
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, getHeight() / 2, (width / 5), paint);
    }
}
