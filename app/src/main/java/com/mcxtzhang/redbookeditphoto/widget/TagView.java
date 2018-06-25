package com.mcxtzhang.redbookeditphoto.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by zhangxutong on 2018/6/21.
 */

public class TagView extends View {
    private static final String TAG = TagView.class.getSimpleName();
    private Context mContext;

    /**
     * 通过java 方法设置
     */
    private boolean isRight;
    private Point mLocation;
    private String mText;
    /**
     * 可通过xml修改的配置
     */
    private final String ELLIPSIS_HINT = "...";
    private int MAX_TEXT_SHOW_COUNT = 10;
    private int MIN_TEXT_SHOW_COUNT = 2;
    //private int mTextShowCount = 0;

    /**
     * View内部使用
     */
    //宽高
    private int mWidth, mHeight;

    private Paint mTextPaint;
    private final Rect mTextBounds = new Rect();
    private final Rect mEllipsisBounds = new Rect();
    private String mShowText;
    //View的最小宽度
    private int mMinWidth;


    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mTextPaint = new Paint();
        //传入像素
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, metrics));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(ELLIPSIS_HINT, 0, ELLIPSIS_HINT.length(), mEllipsisBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: getLeft = [" + getLeft() + "], getTop = [" + getTop() + "]");
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int textShowCount = mText.length();
        if (textShowCount > MAX_TEXT_SHOW_COUNT) {
            textShowCount = MAX_TEXT_SHOW_COUNT;
            mShowText = mText.substring(0, textShowCount) + ELLIPSIS_HINT;
        } else {
            mShowText = mText;
        }
        mTextPaint.getTextBounds(mShowText, 0, mShowText.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高

        switch (wMode) {
            case MeasureSpec.EXACTLY:
                break;
            default:
                int computeSize = (getPaddingLeft() + getPaddingRight() + mTextBounds.width());


                //边界计算
                ViewParent parent = getParent();
                boolean resize = false;
                int parentRightSpace = 0;
                if (parent instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    parentRightSpace = viewGroup.getWidth() - viewGroup.getPaddingRight() - getLeft();
                    //超出右边界，且能继续缩减
                    while (computeSize > parentRightSpace && textShowCount > MIN_TEXT_SHOW_COUNT) {
                        resize = true;
                        textShowCount--;
                        mShowText = mText.substring(0, textShowCount) + ELLIPSIS_HINT;
                        mTextPaint.getTextBounds(mShowText, 0, mShowText.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
                        computeSize = (getPaddingLeft() + getPaddingRight() + mTextBounds.width());
                    }
                }
                if (resize) {
                    wSize = parentRightSpace;
                } else {
                    wSize = computeSize;
                }

        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                int computeSize = (int) (getPaddingTop() + getPaddingBottom() + mTextBounds.height());
                hSize = computeSize < hSize ? computeSize : hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                computeSize = (int) (getPaddingTop() + getPaddingBottom() + mTextBounds.height());
                hSize = computeSize;
                break;
        }


        setMeasuredDimension(wSize, hSize);


        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure() called with: getMeasuredHeight = [" + getMeasuredHeight() + "], getMeasuredWidth = [" + getMeasuredWidth() + "]");

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout() called with: changed = [" + changed + "], left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "]");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        canvas.drawColor(Color.GREEN);
        // 计算Baseline绘制的起点X轴坐标
        int baseX = getPaddingLeft();
        // 计算Baseline绘制的Y坐标
        int baseY = (int) ((mHeight / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));

        canvas.drawText(mShowText, 0, mShowText.length(), baseX, baseY, mTextPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void setOrientationAndPosition(boolean isRight, Point point) {
        this.isRight = isRight;
        this.mLocation = point;
        updateLocation();
    }

    public void changeOrientation() {
        this.isRight = !this.isRight;
        updateLocation();
    }

    public void updatePosition(int distanceX, int distanceY) {
        mLocation.x += distanceX;
        mLocation.y += distanceY;
        updateLocation();
    }

    public TagView setText(String text) {
        mText = text;
        computeMinWidth();
        requestLayout();
        invalidate();
        return this;
    }

    public boolean isRight() {
        return isRight;
    }

    public Point getLocation() {
        return mLocation;
    }

    public String getText() {
        return mText;
    }

    public int getMinWidth() {
        return mMinWidth;
    }

    private void computeMinWidth() {
        mTextPaint.getTextBounds(mText.substring(0, MIN_TEXT_SHOW_COUNT) + ELLIPSIS_HINT, 0, MIN_TEXT_SHOW_COUNT + ELLIPSIS_HINT.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
        mMinWidth = getPaddingLeft() + getPaddingRight() + mTextBounds.width();
    }

    private void updateLocation() {
        int height = getMeasuredHeight();
        if (height == 0) {
            measure(View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height = getMeasuredHeight();
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (isRight) {
            lp.leftMargin = mLocation.x;
        } else {
            lp.leftMargin = mLocation.x - getMeasuredWidth();
        }
        lp.topMargin = mLocation.y - height / 2;
        setLayoutParams(lp);
        //requestLayout();

    }
}
