package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

        //※影の描画に必要な設定
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        paint = new Paint();

        //背景色が設定されているなら、マーク色として設定する
        ColorDrawable colorDrawable = (ColorDrawable)this.getBackground();
        if( colorDrawable != null ){
            //マーク色として設定
            paint.setColor( colorDrawable.getColor() );

            //背景色自体には、色なしを設定
            this.setBackgroundColor( Color.TRANSPARENT );

        } else {
            //色が未指定の場合、色なしを設定
            paint.setColor( Color.TRANSPARENT );
        }

    }

    /*
     *　コードから生成時用
     */
    public MarkView(Context context) {
        super(context);

        //※影の描画に必要な設定
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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

        float radius = (width / 5f);
        float shaodowRadius = (width / 10f);

        //影の設定
        paint.setShadowLayer( shaodowRadius, 0, 0 + 4f, getResources().getColor(R.color.shadowa));

        //paint.setColor(getResources().getColor(R.color.mark_5));
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2f, getHeight() / 2f, radius, paint);
    }
}
