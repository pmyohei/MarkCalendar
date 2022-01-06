package com.mark.markcalendar;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FadeUpDownAnimationListener implements Animation.AnimationListener {

    private final TextView      mTargetView;
    private final Animation     mInAnimation;
    private final String        mChangedStr;

    /*
     * コンストラクタ
     */
    public FadeUpDownAnimationListener( TextView targetView, String changedStr, int direction ){

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

        //ビューに反映
        mTargetView.setText( mChangedStr );

        //アニメーション開始
        mTargetView.startAnimation(mInAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) { }
    @Override
    public void onAnimationStart(Animation animation) { }


}
