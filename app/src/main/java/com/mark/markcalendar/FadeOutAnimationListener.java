package com.mark.markcalendar;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

/*
 * フェードアウトアニメーションリスナー
 * 　フェードアウトアニメーションが終了したとき、フェードインアニメーションを開始する
 */
public class FadeOutAnimationListener implements Animation.AnimationListener {

    private final TextView      mTargetView;
    private final Animation     mInAnimation;
    private final String        mChangedStr;

    /*
     * コンストラクタ
     */
    public FadeOutAnimationListener(TextView targetView, String changedStr, int direction ){

        //アニメーション対象ビュー
        mTargetView = targetView;

        //フェードインアニメーション
        int in = ( (direction == MarkCountView.UP) ? R.anim.fade_in_up : R.anim.fade_in_down);
        mInAnimation = AnimationUtils.loadAnimation( targetView.getContext(), in );

        //変更後文字列
        mChangedStr = changedStr;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        //Context context = mTargetView.getContext();

        //ビューに反映
        mTargetView.setText( mChangedStr );
/*        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                mTargetView,
                (int)context.getResources().getDimension(R.dimen.autoSizeMinTextSize),
                (int)context.getResources().getDimension(R.dimen.autoSizeMaxTextSize),
                (int)context.getResources().getDimension(R.dimen.autoSizeStepGranularity),
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);*/

        Layout layout = mTargetView.getLayout();
        if( layout != null ){
            Context context = mTargetView.getContext();
            float size;
            if( layout.getEllipsisCount(0) > 0 ){
                //省略されていれば、最小サイズ
                size = context.getResources().getDimension(R.dimen.toolbar_selectedMarkTextSizeMin);
            } else {
                //省略されていなければ、通常サイズ
                size = context.getResources().getDimension(R.dimen.toolbar_selectedMarkTextSize);
            }

            //サイズを適用
            float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
            mTargetView.setTextSize( COMPLEX_UNIT_SP, size / scaledDensity );
        }

        //アニメーション開始
        mTargetView.startAnimation(mInAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }
    @Override
    public void onAnimationStart(Animation animation) { }


}
