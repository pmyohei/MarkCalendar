package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class MarkView extends View {
    private Paint mPaint;

    /*
     *　レイアウトから生成時用
     */
    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

        //背景色が設定されているなら、マーク色として設定する
        ColorDrawable colorDrawable = (ColorDrawable)this.getBackground();
        if( colorDrawable != null ){
            //マーク色として設定
            mPaint.setColor( colorDrawable.getColor() );

            //背景色自体には、色なしを設定
            this.setBackgroundColor( Color.TRANSPARENT );

        } else {
            //色が未指定の場合、色なしを設定
            mPaint.setColor( Color.TRANSPARENT );
        }

    }

    /*
     *　コードから生成時用
     */
    public MarkView(Context context) {
        super(context);

        init();
    }

    /*
     * 初期化
     */
    public void init(){

        if( Build.VERSION.SDK_INT <= Build.VERSION_CODES.P ){
            //ピクチャノード以外で、API28以下なら、レイヤータイプを設定
            //※API28以下は、影の描画に必要な処理
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //ペイント生成
        mPaint = new Paint();
    }

    public int getColorHex(){
        return mPaint.getColor();
    }
    public void setColorHex(int colorHex){
        mPaint.setColor(colorHex);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();

        float markRadius = (width / 5f);
        float shadowRadius = (width / 10f);

        //影の設定
        mPaint.setShadowLayer( shadowRadius, 0, 0 + 4f, getResources().getColor(R.color.shadowa));

        //マークの描画
        mPaint.setAntiAlias(true);
        canvas.drawCircle(width / 2f, getHeight() / 2f, markRadius, mPaint);
    }
}
