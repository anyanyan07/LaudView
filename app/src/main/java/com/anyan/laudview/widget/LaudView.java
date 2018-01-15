package com.anyan.laudview.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.anyan.laudview.R;

/**
 * Created by Administrator on 2018/1/15.
 */

public class LaudView extends LinearLayout implements View.OnClickListener {
    private LaudImageView laudImageView;
    private LaudCountView laudCountView;

    public LaudView(Context context) {
        super(context);
        init(context);
    }

    public LaudView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LaudView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LaudView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.laud_view_layout, this);
        laudImageView = view.findViewById(R.id.laudView);
        laudCountView = view.findViewById(R.id.laudCountView);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (laudImageView.getCurState() == 0 || laudImageView.getCurState() == 2) {
            //由正常状态变成点赞状态
            laudImageView.setCurState(1);
            laudCountView.setCurState(0);//数字加一
            ObjectAnimator laudImageViewAnimator = ObjectAnimator.ofInt(laudImageView, "radius", 0, laudImageView.getMAX_RADIUS());
            ObjectAnimator offYAnimator = ObjectAnimator.ofInt(laudCountView, "offY", 0, laudCountView.getMaxOffY());
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(800);
            animatorSet.play(laudImageViewAnimator).with(offYAnimator);
            animatorSet.start();
        } else if (laudImageView.getCurState() == 1) {
            //由点赞状态变为取消点赞状态
            laudImageView.setCurState(2);
            laudCountView.setCurState(1);
            ObjectAnimator laudImageViewAnimator = ObjectAnimator.ofInt(laudImageView, "radius", laudImageView.getMAX_RADIUS(),0);
            ObjectAnimator offYAnimator = ObjectAnimator.ofInt(laudCountView, "offY", 0, laudCountView.getMaxOffY());
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(800);
            animatorSet.play(laudImageViewAnimator).with(offYAnimator);
            animatorSet.start();
        }
    }
}
