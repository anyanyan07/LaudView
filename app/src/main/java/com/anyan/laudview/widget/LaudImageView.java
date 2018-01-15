package com.anyan.laudview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.anyan.laudview.R;

/**
 * Created by Administrator on 2018/1/15.
 */

public class LaudImageView extends View {

    private Paint paint;
    //点赞的红色小手
    private Bitmap LAUD_SELECTED_BITMAP;
    //未点赞的灰色小手
    private Bitmap LAUD_UNSELECTED_BITMAP;
    //点赞周围的光芒
    private Bitmap BLUR_BITMAP;
    private int radius;
    //直接设值状态
    private int NORMAL_STATE = 0;
    //点赞状态
    private int LAUD_STATE = 1;
    //取消点赞状态
    private int CANCEL_STATE = 2;
    //默认当前状态为直接设值状态
    private int curState = NORMAL_STATE;
    private int MAX_RADIUS;

    public int getMAX_RADIUS() {
        return MAX_RADIUS;
    }

    public int getCurState() {
        return curState;
    }

    public void setCurState(int curState) {
        this.curState = curState;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public LaudImageView(Context context) {
        super(context);
        init(null,context);
    }

    public LaudImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,context);
    }

    public LaudImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LaudImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs,context);
    }

    private void init(AttributeSet attrs,Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.LaudImageView);
        int laudId = typedArray.getResourceId(R.styleable.LaudImageView_laudSrc, R.mipmap.ic_messages_like_selected);
        LAUD_SELECTED_BITMAP = BitmapFactory.decodeResource(getResources(), laudId);
        int unlaudId = typedArray.getResourceId(R.styleable.LaudImageView_unlaudSrc, R.mipmap.ic_messages_like_unselected);
        LAUD_UNSELECTED_BITMAP = BitmapFactory.decodeResource(getResources(), unlaudId);
        int blurId = typedArray.getResourceId(R.styleable.LaudImageView_blurSrc, R.mipmap.ic_messages_like_selected_shining);
        BLUR_BITMAP = BitmapFactory.decodeResource(getResources(), blurId);
        typedArray.recycle();
        MAX_RADIUS = Math.max(LAUD_SELECTED_BITMAP.getWidth(), BLUR_BITMAP.getHeight()) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //最小宽度为小手的宽度，最小高度为小手的高度加Blue高度的一半
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (withMode == MeasureSpec.AT_MOST) {
            //当指定宽为Wrap_content时
            withSize = LAUD_SELECTED_BITMAP.getWidth() + 20;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            //当指定高为Wrap_content时
            heightSize = LAUD_SELECTED_BITMAP.getHeight() + BLUR_BITMAP.getHeight() / 2 + 20;
        }
        setMeasuredDimension(withSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = getPaddingLeft() + 10;
        int top = getPaddingTop() + 10;
        if (curState == NORMAL_STATE) {
            //只显示一个灰色小手，无动画
            canvas.drawBitmap(LAUD_UNSELECTED_BITMAP, left, top + BLUR_BITMAP.getHeight() / 2, paint);
        } else if (curState == LAUD_STATE) {
            //点赞,动画：灰色小手变小消失，红色小手从灰色消失的大小先变大，再变小到正常大小，blue图片逐渐显示完整
            if (radius < MAX_RADIUS / 3) {
                //灰色小手消失部分
                Matrix matrix = new Matrix();
                float scaleX = (MAX_RADIUS - radius) * 1.0F / MAX_RADIUS;
                matrix.postScale(scaleX, scaleX);
                Bitmap laudUnselected = Bitmap.createBitmap(LAUD_UNSELECTED_BITMAP, 0, 0, LAUD_UNSELECTED_BITMAP.getWidth(), LAUD_UNSELECTED_BITMAP.getHeight(), matrix, true);
                canvas.drawBitmap(laudUnselected, left, top + BLUR_BITMAP.getHeight() / 2, paint);
            } else {
                //红色小手显示部分
                Matrix matrix = new Matrix();
                float scaleX = radius * 1.0F / MAX_RADIUS + 1f / 3f;
                scaleX = scaleX > 1.0 ? 1.0F : scaleX;
                matrix.postScale(scaleX, scaleX);
                Bitmap laudselected = Bitmap.createBitmap(LAUD_SELECTED_BITMAP, 0, 0, LAUD_SELECTED_BITMAP.getWidth(), LAUD_SELECTED_BITMAP.getHeight(), matrix, true);
                canvas.drawBitmap(laudselected, left, top + BLUR_BITMAP.getHeight() / 2, paint);
                //红色闪光部分开始显示
                canvas.save();
                Path path = new Path();
                path.addCircle(left + LAUD_SELECTED_BITMAP.getWidth() / 2, top + BLUR_BITMAP.getHeight() / 2, radius, Path.Direction.CCW);
                canvas.clipPath(path);
                canvas.drawBitmap(BLUR_BITMAP, left + (LAUD_SELECTED_BITMAP.getWidth() - BLUR_BITMAP.getWidth()) / 2, top, paint);
                canvas.restore();
            }
        } else if (curState == CANCEL_STATE) {
            //取消点赞,动画：红色小手变小，光辉慢慢消失，灰色小手从红色小手消失时大小变大到正常大小。
            if (radius > MAX_RADIUS * 2 / 3) {
                //红色小手慢慢消失过程
                Matrix matrix = new Matrix();
                float scale = radius * 1.0f / MAX_RADIUS;
                matrix.postScale(scale, scale);
                Bitmap laudBitmap = Bitmap.createBitmap(LAUD_SELECTED_BITMAP, 0, 0, LAUD_SELECTED_BITMAP.getWidth(), LAUD_SELECTED_BITMAP.getHeight(), matrix, true);
                canvas.drawBitmap(laudBitmap, left, top + BLUR_BITMAP.getHeight() / 2, paint);
            } else {
                //灰色小手变大过程
                Matrix matrix = new Matrix();
                float scale = (MAX_RADIUS - radius) * 1.0f / MAX_RADIUS + 1 * 1.0f / 3;
                scale = scale > 1.0 ? 1.0F : scale;
                matrix.postScale(scale, scale);
                Bitmap unlaudBitmap = Bitmap.createBitmap(LAUD_UNSELECTED_BITMAP, 0, 0, LAUD_UNSELECTED_BITMAP.getWidth(), LAUD_UNSELECTED_BITMAP.getHeight(), matrix, true);
                canvas.drawBitmap(unlaudBitmap, left, top + BLUR_BITMAP.getHeight() / 2, paint);
            }
        }
    }
}
