package com.anyan.laudview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/1/15.
 */

public class LaudCountView extends View {
    public LaudCountView(Context context) {
        super(context);
        init();
    }

    public LaudCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LaudCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LaudCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }


}
