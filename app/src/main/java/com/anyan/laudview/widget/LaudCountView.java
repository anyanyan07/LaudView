package com.anyan.laudview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.anyan.laudview.R;

/**
 * Created by Administrator on 2018/1/15.
 */

public class LaudCountView extends View {
    private Paint paint;
    private int textHeight;
    private int textSize;
    private int textColor;
    private String text;
    private int ADD_STATE = 0;
    private int DEC_STATE = 1;

    private int curState = -1;
    private int offY;
    private int maxOffY;
    private String[] result;

    public int getMaxOffY() {
        return maxOffY;
    }

    public int getCurState() {
        return curState;
    }

    public void setCurState(int curState) {
        this.curState = curState;
        if (curState == ADD_STATE) {
            //数字增加
            Integer curNum = Integer.valueOf(text);
            result = calculateCount(text, curNum + 1 + "");
            text = curNum + 1 + "";
        } else if (curState == DEC_STATE) {
            Integer curNum = Integer.valueOf(text);
            result = calculateCount(text, curNum - 1 + "");
            text = curNum - 1 + "";
        }
    }


    public int getOffY() {
        return offY;
    }

    public void setOffY(int offY) {
        this.offY = offY;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public LaudCountView(Context context) {
        super(context);
        init(null, context);
    }

    public LaudCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public LaudCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LaudCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LaudCountView);
        textSize = typedArray.getDimensionPixelSize(R.styleable.LaudCountView_textSize, 36);
        textColor = typedArray.getColor(R.styleable.LaudCountView_textColor, Color.GRAY);
        text = typedArray.getString(R.styleable.LaudCountView_text);
        Log.e("TAG", "=====init: " + text);
        typedArray.recycle();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (TextUtils.isEmpty(text)) {
            setMeasuredDimension(0, 0);
            return;
        }
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        textHeight = bounds.height();
        maxOffY = (int) (textHeight * 2.0);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = bounds.width();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = bounds.height() * 3;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(text))
            return;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (curState == -1) {
            canvas.drawText(text, paddingLeft, textHeight * 2 + paddingTop, paint);
        } else if (curState == ADD_STATE) {
            //数字增加
            canvas.drawText(result[0], paddingLeft, textHeight * 2 + paddingTop, paint);
            float changePartLeft = paddingLeft + paint.measureText(result[0], 0, result[0].length());
//            paint.reset();
//            paint.setTextSize((float) ((1-offY/(1.2*textHeight))*textSize));
//            paint.setAlpha((int) (offY/(1.2*textHeight)));
            canvas.drawText(result[1], changePartLeft, textHeight * 2 + paddingTop - offY, paint);
//            paint.reset();
//            paint.setTextSize((float) ((offY/(1.2*textHeight))*textSize));
//            paint.setAlpha((int) (offY/(1.2*textHeight)));
            canvas.drawText(result[2], changePartLeft, textHeight * 4.0f + paddingTop - offY, paint);
        } else if (curState == DEC_STATE) {
            //数字减小
            canvas.drawText(result[0], paddingLeft, textHeight * 2 + paddingTop, paint);
            float changePartLeft = paddingLeft + paint.measureText(result[0], 0, result[0].length());
            canvas.drawText(result[1], changePartLeft, textHeight * 2.0f + paddingTop + offY, paint);
            canvas.drawText(result[2], changePartLeft, paddingTop + offY, paint);
        }
    }

    //计算变动和保持不变的部分
    private String[] calculateCount(String oldText, String newText) {
        //0:不变部分，1：可变部分之old值，2：可变部分之new值
        String[] result = new String[3];
        if (oldText.length() != newText.length()) {
            result[0] = "";
            result[1] = oldText.toString();
            result[2] = newText.toString();
        } else {
            for (int i = 0; i < oldText.length(); i++) {
                if (!oldText.substring(i, i + 1).equals(newText
                        .substring(i, i + 1))) {
                    result[0] = oldText.substring(0, i);
                    result[1] = oldText.substring(i, oldText.length());
                    result[2] = newText.substring(i, newText.length());
                    break;
                }
            }
        }
        return result;
    }
}
