package com.mcxtzhang.redbookeditphoto.pictag.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * 原本想用TextView draw出来，但是考虑各种边界判断，这里要用完全的自定义View去draw
 * Created by zhangxutong on 2018/6/21.
 */

public class TagView extends View {
    private static final String TAG = TagView.class.getSimpleName();
    public static final int ICON_WIDTH_DP = 13;
    public static final int STROKE_WIDTH_DP = 1;
    public static final int ICON_PADDING_TOP_OR_BOTTOM_DP = 5;

    private Context mContext;

    /**
     * 通过java 方法设置
     */
    //标签尖角朝向(1:向左，2:向右)
    private int mDirection;
    private Point mLocation;
    private String mText = "";
    /**
     * 可通过xml修改的配置
     */
    private final String ELLIPSIS_HINT = "...";
    private int MAX_TEXT_SHOW_COUNT = 13;
    private int MIN_TEXT_SHOW_COUNT = 3;
    //private int mTextShowCount = 0;

    /**
     * View内部使用
     */
    //宽高
    private int mWidth, mHeight;

    private int mTextStartX; //计算得出。小圆点呼吸半径（左边）+小圆点+横线的宽度
    private Paint mTextPaint;
    private final Rect mTextBounds = new Rect();
    private final Rect mEllipsisBounds = new Rect();
    private String mShowText;
    //View的最小宽度
    private int mMinWidth;

    private ViewGroup mParent;

    //圆点和圆环
    private int mCircleCentreX;
    private Paint mPointPaint;
    private int mPointRadius;
    private Paint mRingPaint;
    private int mRingRadius;
    private int mRingTextGap;

    //文字区域的边框
    private Paint mTextBorderPaint;
    private Path mTextBorderPath = new Path();
    private RectF mBorderRect;
    private int mTextBorderStartX;
    //边框线的宽度
    private int mStrokeWidth;
    //是标签三角形的两点水平距离(纯draw使用)
    private float mBgLeftOffset;
    //文字区域的背景
    private Paint mTextBgPaint;
    //private int mTextBgStartX;
    //文字区域边距属性
    //文字区域，距离竖线的padding
    private int mTextBorderPaddingVerticalLine;
    //距离箭头的padding
    private int mTextBorderPaddingArrow;

    //小icon
    private boolean isShowIcon;
    private int mIconWidth;
    private int mIconPaddingRight;
    private int mIconPaddingTop;
    private Bitmap mIconBitmap;
    private int mIconDrawStartX;


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

    private static final String COLOR_BG = "#66222222";

    private static final String COLOR_BORDER = "#4FFFFFFF";

    private void init(Context context) {
        mContext = context;
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();


        //mCircleCentreX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, metrics);
        mRingRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, metrics);
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(Color.parseColor("#66000000"));

        mPointRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.WHITE);

        mStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH_DP, metrics);
        mRingTextGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);

        mBorderRect = new RectF();
        mTextBorderPaint = new Paint();
        mTextBorderPaint.setAntiAlias(true);
        mTextBorderPaint.setColor(Color.parseColor("#88ffffff"));
        mTextBorderPaint.setStrokeWidth(mStrokeWidth);

        mTextBorderPaint.setStyle(Paint.Style.STROKE);
        mTextBorderPaint.setColor(Color.parseColor(COLOR_BORDER));
        CornerPathEffect pathEffect = new CornerPathEffect(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics));
        mTextBorderPaint.setPathEffect(pathEffect);
        //mTextBorderStartX = mLineStartX + mLineWidth;

        mTextBgPaint = new Paint();
        mTextBgPaint.setAntiAlias(true);
        mTextBgPaint.setColor(Color.parseColor(COLOR_BG));
        mTextBgPaint.setPathEffect(pathEffect);

        mBgLeftOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, metrics);
        mTextBorderPaddingVerticalLine = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, metrics);
        //mTextBgStartX = mTextBorderStartX + mStrokeWidth;

        mIconWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_WIDTH_DP, metrics);
        mTextBorderPaddingArrow = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, metrics);
        mIconPaddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, metrics);
        mIconPaddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_PADDING_TOP_OR_BOTTOM_DP, metrics);


        mTextPaint = mPointPaint;
        //传入像素
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, metrics));
        // TODO: 2018/7/24 maybe useless
        mTextPaint.getTextBounds(ELLIPSIS_HINT, 0, ELLIPSIS_HINT.length(), mEllipsisBounds);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure() called with: getLeft = [" + getLeft() + "], getTop = [" + getTop() + "]" + "], getRight = [" + getRight());
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
                int textWidth = getPaddingLeft() + getPaddingRight() + mTextBounds.width();
                if (isShowIcon) {
                    textWidth += (mIconWidth + mIconPaddingRight);
                }
                int computeSize = mRingRadius * 2 + mRingTextGap + mStrokeWidth + mTextBorderPaddingArrow + textWidth + mTextBorderPaddingVerticalLine + mStrokeWidth;

                //边界计算
                ViewParent parent = getParent();
                boolean resize = false;
                if (parent instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    int parentSpace = 0;

                    if (isLeft()) {
                        if (getLeft() == 0) {
                            parentSpace = viewGroup.getWidth() - viewGroup.getPaddingLeft() - viewGroup.getPaddingRight();
                        } else {
                            parentSpace = viewGroup.getWidth() - viewGroup.getPaddingRight() - getLeft();
                        }

                        //超出右边界，且能继续缩减
                        while (computeSize > parentSpace && textShowCount > MIN_TEXT_SHOW_COUNT) {
                            resize = true;
                            textShowCount--;
                            mShowText = mText.substring(0, textShowCount) + ELLIPSIS_HINT;
                            mTextPaint.getTextBounds(mShowText, 0, mShowText.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
                            textWidth = getPaddingLeft() + getPaddingRight() + mTextBounds.width();
                            if (isShowIcon) {
                                textWidth += (mIconWidth + mIconPaddingRight);
                            }
                            computeSize = mRingRadius * 2 + mRingTextGap + mStrokeWidth + mTextBorderPaddingArrow + textWidth + mTextBorderPaddingVerticalLine + mStrokeWidth;
                        }

                    } else {
                        if (getRight() == 0) {
                            parentSpace = viewGroup.getWidth() - viewGroup.getPaddingLeft() - viewGroup.getPaddingRight();
                        } else {
                            parentSpace = getRight() - viewGroup.getPaddingLeft();
                        }
                        //超出左边界，且能继续缩减
                        while (computeSize > parentSpace && textShowCount > MIN_TEXT_SHOW_COUNT) {
                            resize = true;
                            textShowCount--;
                            mShowText = mText.substring(0, textShowCount) + ELLIPSIS_HINT;
                            mTextPaint.getTextBounds(mShowText, 0, mShowText.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
                            textWidth = getPaddingLeft() + getPaddingRight() + mTextBounds.width();
                            if (isShowIcon) {
                                textWidth += (mIconWidth + mIconPaddingRight);
                            }
                            computeSize = mRingRadius * 2 + mRingTextGap + mStrokeWidth + mTextBorderPaddingArrow + textWidth + mTextBorderPaddingVerticalLine + mStrokeWidth;
                        }
                    }
                    //如果resize过，说明接近边缘
                    if (resize) {
                        wSize = parentSpace;
                    } else {
                        wSize = computeSize;
                    }
                } else {
                    wSize = computeSize;
                }

        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                int computeSize = (getPaddingTop() + getPaddingBottom() + mIconWidth + mStrokeWidth + mStrokeWidth + mIconPaddingTop + mIconPaddingTop);
                hSize = computeSize < hSize ? computeSize : hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                computeSize = (getPaddingTop() + getPaddingBottom() + mIconWidth + mStrokeWidth + mStrokeWidth + mIconPaddingTop + mIconPaddingTop);
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
        int verticalMiddle = mHeight / 2;
        //因为存在边缘限制，会挤压View的宽度，所以先确定不会挤压的部分，再利用宽度-这些部分 = 剩余文字区域的宽度

        //先画黑圆环，再用白色小圆点盖在黑色圆环上
        if (isLeft()) {
            //mLineStartX = mRingRadius + mPointRadius;
            mCircleCentreX = mRingRadius;
        } else {
            //mLineStartX = mWidth - mRingRadius - mPointRadius - mLineWidth;
            mCircleCentreX = mWidth - mRingRadius;
        }
        //canvas.drawLine(mLineStartX, verticalMiddle, mLineStartX + mLineWidth, verticalMiddle, mLinePaint);
        canvas.drawCircle(mCircleCentreX, verticalMiddle, mRingRadius, mRingPaint);
        canvas.drawCircle(mCircleCentreX, verticalMiddle, mPointRadius, mPointPaint);


        int baseX;
        if (isLeft()) {
            //文字背景
            mTextBorderStartX = mRingRadius * 2 + mRingTextGap;
            mTextBorderPath.reset();

            mTextBorderPath.moveTo(mTextBorderStartX, verticalMiddle);// 此点为多边形的起点
            mTextBorderPath.lineTo(mTextBorderStartX + mBgLeftOffset, 0 + mStrokeWidth);
            mTextBorderPath.lineTo(mWidth - mStrokeWidth, 0 + mStrokeWidth);
            mTextBorderPath.lineTo(mWidth - mStrokeWidth, mHeight - mStrokeWidth);
            mTextBorderPath.lineTo(mTextBorderStartX + mBgLeftOffset, mHeight - mStrokeWidth);
            mTextBorderPath.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(mTextBorderPath, mTextBgPaint);
            canvas.drawPath(mTextBorderPath, mTextBorderPaint);

            //小icon
            mIconDrawStartX = mTextBorderStartX + mStrokeWidth + mTextBorderPaddingArrow;

            if (isShowIcon) {
                mBorderRect.set(mIconDrawStartX, mStrokeWidth + getPaddingTop() + mIconPaddingTop,
                        mIconDrawStartX + mIconWidth, mStrokeWidth + getPaddingTop() + mIconPaddingTop + mIconWidth);
                canvas.drawBitmap(mIconBitmap,
                        null,
                        mBorderRect,
                        mTextPaint);
                baseX = mIconDrawStartX + mIconWidth + mIconPaddingRight;
            } else {
                baseX = mIconDrawStartX;
            }
        } else {
            //文字背景
            mTextBorderStartX = mWidth - mRingRadius * 2 - mRingTextGap;
            mTextBorderPath.reset();

            mTextBorderPath.moveTo(mTextBorderStartX, verticalMiddle);// 此点为多边形的起点
            mTextBorderPath.lineTo(mTextBorderStartX - mBgLeftOffset, mHeight - mStrokeWidth);
            mTextBorderPath.lineTo(0 + mStrokeWidth, mHeight - mStrokeWidth);
            mTextBorderPath.lineTo(0 + mStrokeWidth, 0 + mStrokeWidth);
            mTextBorderPath.lineTo(mTextBorderStartX - mBgLeftOffset, 0 + mStrokeWidth);
            mTextBorderPath.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(mTextBorderPath, mTextBgPaint);
            canvas.drawPath(mTextBorderPath, mTextBorderPaint);

            //小icon
            if (isShowIcon) {
                //mIconDrawStartX = (mLineStartX - mTextBounds.width() - mIconWidth - mIconPaddingRight) >> 1;
                mIconDrawStartX = mStrokeWidth + mTextBorderPaddingVerticalLine;
                mBorderRect.set(mIconDrawStartX, mStrokeWidth + getPaddingTop() + mIconPaddingTop,
                        mIconDrawStartX + mIconWidth, mStrokeWidth + getPaddingTop() + mIconPaddingTop + mIconWidth);
                canvas.drawBitmap(mIconBitmap,
                        null,
                        mBorderRect,
                        mTextPaint);
                baseX = mIconDrawStartX + mIconWidth + mIconPaddingRight;

            } else {
                baseX = mStrokeWidth + mTextBorderPaddingVerticalLine;
            }
        }
        // 计算Baseline绘制的Y坐标
        int baseY = (verticalMiddle - ((int) (mTextPaint.descent() + mTextPaint.ascent()) >> 1));
        canvas.drawText(mShowText, 0, mShowText.length(), baseX, baseY, mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public TagView setShowIcon(boolean showIcon) {
        isShowIcon = showIcon;
        //updateAttr();
        return this;
    }

    public TagView setIconBitmap(Bitmap iconBitmap) {
        mIconBitmap = iconBitmap;
        computeMinWidth();
        invalidate();
        return this;
    }

    public TagView setLocation(Point location) {
        mLocation = location;
        updateLocation();
        return this;
    }

    public void setDirectionAndPosition(int direction, Point point) {
        this.mDirection = direction;
        this.mLocation = point;
        updateLocation();
    }

    public void changeOrientation() {
        if (mDirection == 1) {
            mDirection = 2;
        } else {
            mDirection = 1;
        }

        fixLocation();
        updateLocation();
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    public void updatePosition(int distanceX, int distanceY) {
        mLocation.x += distanceX;
        mLocation.y += distanceY;
        fixLocation();
        updateLocation();
    }

    public void updatePositionWithoutFix(int distanceX, int distanceY) {
        mLocation.x += distanceX;
        mLocation.y += distanceY;
        updateLocation();
    }

    public TagView setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return this;
        }
        mText = text;
        computeMinWidth();
        requestLayout();
        invalidate();
        return this;
    }

    public TagView setParent(ViewGroup viewGroup) {
        mParent = viewGroup;
        return this;
    }

    public int getDirection() {
        return mDirection;
    }

    private boolean isLeft() {
        return mDirection == 1;
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

/*    private void updateAttr() {
        if (isShowIcon) {
            mIconDrawStartX = mTextBgStartX + getPaddingLeft();
            mTextStartX = mIconDrawStartX + mIconWidth + mIconPaddingRight;
        } else {
            mTextStartX = mTextBgStartX + getPaddingLeft();
        }
    }*/

    private void computeMinWidth() {
        //int paddingLeft = getPaddingLeft();
        //int paddingRight = getPaddingRight();
        int textLength = mText.length();

//        int minShowLength = textLength > MIN_TEXT_SHOW_COUNT ? MIN_TEXT_SHOW_COUNT : textLength;
//        mTextPaint.getTextBounds(mText.substring(0, minShowLength) + ELLIPSIS_HINT, 0, minShowLength + ELLIPSIS_HINT.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高

        if (textLength > MIN_TEXT_SHOW_COUNT) {
            mTextPaint.getTextBounds(mText.substring(0, MIN_TEXT_SHOW_COUNT) + ELLIPSIS_HINT, 0, MIN_TEXT_SHOW_COUNT + ELLIPSIS_HINT.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
        } else {
            mTextPaint.getTextBounds(mText, 0, textLength, mTextBounds);//测量计算文字所在矩形，可以得到宽高
        }


        mMinWidth = mRingRadius * 2 + mRingTextGap + +mStrokeWidth + mTextBorderPaddingArrow + mTextBounds.width() + mTextBorderPaddingVerticalLine + mStrokeWidth;
        if (isShowIcon) {
            mMinWidth += mIconWidth + mIconPaddingRight;
        }

//        if (textLength > MAX_TEXT_SHOW_COUNT) {
//            mTextPaint.getTextBounds(mText.substring(0, MAX_TEXT_SHOW_COUNT) + ELLIPSIS_HINT, 0, MAX_TEXT_SHOW_COUNT + ELLIPSIS_HINT.length(), mTextBounds);//测量计算文字所在矩形，可以得到宽高
//        } else {
//            mTextPaint.getTextBounds(mText, 0, textLength, mTextBounds);//测量计算文字所在矩形，可以得到宽高
//        }
//        mMaxWidth = paddingLeft + paddingRight + mTextBounds.width();
    }

    private void fixLocation() {
        int parentWidth = mParent.getWidth();
        int parentPaddingRight = mParent.getPaddingRight();
        int parentPaddingLeft = mParent.getPaddingLeft();
        int parentPaddingBottom = mParent.getPaddingBottom();
        int parentPaddingTop = mParent.getPaddingTop();

        int height = getMeasuredHeight();
        //边界修正
        int topMargin = mLocation.y - height / 2;
        //top
        int parentTopSpace = topMargin;
        if (parentTopSpace < 0) {
            mLocation.y -= parentTopSpace;
        }
        //bottom
        int parentBottomSpace = mParent.getHeight() - parentPaddingBottom - parentPaddingTop - topMargin - height;
        if (parentBottomSpace < 0) {
            mLocation.y += parentBottomSpace;
        }

        if (isLeft()) {
            //right
            int parentRightSpace = parentWidth - parentPaddingRight;
            parentRightSpace = parentRightSpace - mMinWidth - mLocation.x;
            if (parentRightSpace < 0) {
                mLocation.x += parentRightSpace;
            }
            //left
            if (mLocation.x < parentPaddingLeft) {
                mLocation.x = parentPaddingLeft;
            }

        } else {
            //left
            int parentLeftSpace = mLocation.x - mMinWidth - parentPaddingLeft;
            if (parentLeftSpace < 0) {
                mLocation.x -= parentLeftSpace;
            }
            //right
            int parentRightSpace = parentWidth - parentPaddingRight - mLocation.x;
            if (parentRightSpace < 0) {
                mLocation.x += parentRightSpace;
            }
        }
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
        if (isLeft()) {
            lp.gravity = Gravity.LEFT;
            lp.leftMargin = mLocation.x - mParent.getPaddingLeft();
        } else {
            lp.gravity = Gravity.RIGHT;
            lp.rightMargin = mParent.getWidth() - mParent.getPaddingRight() - mLocation.x;
        }
        lp.topMargin = mLocation.y - height / 2 - mParent.getPaddingTop();

        setLayoutParams(lp);
    }
}
