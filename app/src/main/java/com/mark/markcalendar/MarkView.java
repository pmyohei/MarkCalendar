package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class MarkView extends View {
    private final Paint paint;

    /*
     *　レイアウトから生成時用
     */
    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        //背景色を取得
        ColorDrawable colorDrawable = (ColorDrawable)this.getBackground();
        if( colorDrawable != null ){
            paint.setColor( colorDrawable.getColor() );
            this.setBackgroundColor( getResources().getColor( R.color.clear ) );
        }

    }

    /*
     *　コードから生成時用
     */
    public MarkView(Context context) {
        super(context);
        paint = new Paint();
    }

    public void setColorID(int colorID){
        paint.setColor(getResources().getColor(colorID));
        invalidate();
    }

    public int getColorHex(){
        return paint.getColor();
    }
    public void setColorHex(int colorHex){
        paint.setColor(colorHex);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();

        //paint.setColor(getResources().getColor(R.color.mark_5));
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, getHeight() / 2, (width / 5), paint);
    }
}
