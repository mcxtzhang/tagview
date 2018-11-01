package com.mcxtzhang.redbookeditphoto.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxtzhang.redbookeditphoto.R;
import com.mcxtzhang.redbookeditphoto.TagMatrixUtil;
import com.mcxtzhang.redbookeditphoto.UploadPhotoTagData;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangxutong on 2018/6/14.
 */

public class TagContainerView extends FrameLayout {
    private static final String TAG = TagContainerView.class.getSimpleName();

    private Context mContext;
    public static final int MODE_EDIT = 0;
    public static final int MODE_VIEW = 1;
    private int mode = MODE_VIEW;

    private TextView mDelButton;
    private final Rect mDelButtonRect = new Rect();
    //private boolean isDeled;
    private GestureDetectorCompat mTagParentGestureDetector;
    private List<TagView> mTagViewList = new LinkedList<>();
    private ImageView mTargetImageView;
    private final Rect mImageRect = new Rect();

    private int mImageViewHeight;
    private int mImageViewWidth;
    private float mImageDrawableHeight;
    private float mImageDrawableWidth;
    private final Matrix mMatrix = new Matrix();
    private final float[] mMatrixValues = new float[9];


    public TagContainerView(@NonNull Context context) {
        this(context, null);
    }

    public TagContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TagContainerView openEditMode() {
        return setMode(MODE_EDIT);
    }

    public TagContainerView setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public int getMode() {
        return mode;
    }

    public TagContainerView bindImageView(ImageView targetImageView) {
        if (null == targetImageView) return this;
        mTargetImageView = targetImageView;
        if (mode != MODE_EDIT) {
            return this;
        }
        post(new Runnable() {
            @Override
            public void run() {
                mImageDrawableHeight = mTargetImageView.getDrawable().getBounds().height();
                mImageDrawableWidth = mTargetImageView.getDrawable().getBounds().width();
                //mImageViewHeight = mTargetImageView.getHeight();
                mImageViewWidth = mTargetImageView.getWidth();

                float rate = mImageViewWidth * 1.0f / mImageDrawableWidth;

                //scale image
                Matrix imageMatrix = mTargetImageView.getImageMatrix();
                imageMatrix.postScale(rate, rate);
                mTargetImageView.setImageMatrix(imageMatrix);

                //resize ImageView height
                mImageViewHeight = (int) (mImageDrawableHeight * rate);

                if (mode != MODE_EDIT || null == mDelButton) {
                    return;
                }
                //bounds fix
                ViewParent parent = mTargetImageView.getParent();
                int topMargin = 0;
                if (parent instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    int viewGroupHeight = viewGroup.getHeight();
                    int delButtonHeight = mDelButton.getHeight();
                    viewGroupHeight -= delButtonHeight;
                    if (mImageViewHeight > viewGroupHeight) {
                        mImageViewHeight = viewGroupHeight;
                    } else {
                        topMargin = (viewGroupHeight - mImageViewHeight) / 2;
                    }
                }

                FrameLayout.LayoutParams layoutParams = (LayoutParams) mTargetImageView.getLayoutParams();
                if (null == layoutParams) {
                    layoutParams = new FrameLayout.LayoutParams(mImageViewWidth, mImageViewHeight);
                } else {
                    layoutParams.width = mImageViewWidth;
                    layoutParams.height = mImageViewHeight;
                }
                layoutParams.topMargin = topMargin;
                mTargetImageView.setLayoutParams(layoutParams);
                post(new Runnable() {
                    @Override
                    public void run() {
                        mImageRect.set(mTargetImageView.getLeft(), mTargetImageView.getTop(), mTargetImageView.getRight(), mTargetImageView.getBottom());
                        mDelButton.getGlobalVisibleRect(mDelButtonRect);
                    }
                });
            }
        });
        return this;
    }

    public TagContainerView bindDelBtn(TextView delButton) {
        if (null == delButton) return this;
        mDelButton = delButton;
        return this;
    }


    private void init(Context context) {
        mContext = context;
        setClickable(true);
        setBackgroundColor(Color.parseColor("#88000000"));
        //createDelButton();
        mTagParentGestureDetector = new GestureDetectorCompat(mContext, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mode == MODE_EDIT) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();

                    if (mImageRect.contains(x, y)) {
                        boolean isRight = false;
                        int containerWidth = getWidth();
                        if (containerWidth / 2 > x) {
                            isRight = true;
                        }
                        addTag(new Point(x, y), isRight);
                    } else {

                    }


                } else {
                    //setVisibility(GONE);
//                    if (getVisibility() != View.INVISIBLE) {
//                        setVisibility(View.INVISIBLE);
//                    } else {
//                        setVisibility(View.VISIBLE);
//                    }
                }

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "mTagParentGestureDetector onScroll() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");

                //在当前基础上移动
                mMatrix.set(mTargetImageView.getImageMatrix());
                mMatrix.getValues(mMatrixValues);
/*
                if (mode == MODE_VIEW) {
                    //处于图片顶部，且 手指向下滑动
                    if (mMatrixValues[Matrix.MTRANS_Y] == 0 && distanceY < 0) {
                        dispatchTouchEvent2Parent(e1);
                        return false;
                    }
                }*/

                distanceY = checkDyBound(mMatrixValues, -distanceY);
                mMatrix.postTranslate(0, distanceY);
                mTargetImageView.setImageMatrix(mMatrix);

                //移动屏幕上的tag
                moveAllTags(Math.round(distanceY));
                return false;
            }

            /**
             *  和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
             *  @param values
             *  @param dy
             *  @return
             */
            private float checkDyBound(float[] values, float dy) {
                //Log.d(TAG, "checkDyBound() called with: values = [" + values + "], dy = [" + dy + "]");
                //Log.d(TAG, "checkDyBound() called with: mImageHeight = [" + mImageHeight + "], height = [" + height + "]");
                if (mImageDrawableHeight * values[Matrix.MSCALE_Y] < mImageViewHeight)
                    return 0;
                float translationY = values[Matrix.MTRANS_Y] + dy;
                //上边缘
                if (translationY > 0) {
                    dy = -values[Matrix.MTRANS_Y];
                }
                //下边缘
                else if (translationY < -(mImageDrawableHeight * values[Matrix.MSCALE_Y] - mImageViewHeight)) {
                    dy = -(mImageDrawableHeight * values[Matrix.MSCALE_Y] - mImageViewHeight) - values[Matrix.MTRANS_Y];
                }
                return dy;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });


    }
//
//    private void createDelButton() {
//        if (mode == MODE_VIEW) {
//            return;
//        }
//        mDelButton = new Button(mContext);
//        mDelButton.setText("删除");
//        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        addView(mDelButton, lp);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                isDeled = false;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: event = [" + event + "]");
        if (mode == MODE_VIEW) {
            Log.d(TAG, "onTouchEvent() called with: mode = [标签纯展示]，不处理触摸事件");
            return false;
        }

/*        mMatrix.set(mTargetImageView.getImageMatrix());
        mMatrix.getValues(mMatrixValues);
        Log.d(TAG, "onTouchEvent() called with: MTRANS_Y = [" + mMatrixValues[Matrix.MTRANS_Y] + "]");
        if (getVisibility() == GONE) {
            return false;
        }
        Log.d(TAG, "true onTouchEvent() called with: ");
        switch (event.getActionMasked()) {
*//*            case MotionEvent.ACTION_DOWN:
                if (mode == MODE_VIEW) {


                }
                break;*//*
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mode == MODE_VIEW) {
                    dispatchTouchEvent2Parent(event);
                    return false;
                }
                break;

        }*/
        return mTagParentGestureDetector.onTouchEvent(event);

//        if (isDeled) {
//            return true;
//        } else {
//            return mTagParentGestureDetector.onTouchEvent(event);
//
//        }
    }

/*    private void dispatchTouchEvent2Parent(MotionEvent event) {
        setVisibility(GONE);
        ((ViewGroup) getParent()).dispatchTouchEvent(MotionEvent.obtainNoHistory(event));
    }*/

    private void addTag(Point point, boolean isRight) {
        TagView tagView = new TagView(mContext);
/*        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int paddingHorizontal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        int paddingVertical = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
        tagView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);*/
        tagView.setParent(this);
        tagView.setText(Math.random() > 0.5 ? "地点啊地点六我是很多个字啊" : "正");
        tagView.setBackgroundColor(Color.BLUE);
        tagView.setClickable(true);
        tagView.setShowIcon(Math.random() > 0.5 ? true : false)
                .setIconBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.little));
        tagView.setOrientationAndPosition(isRight, point);

        if (mode == MODE_EDIT) {
            final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mContext, new TagGestureListener(tagView));
            gestureDetectorCompat.setIsLongpressEnabled(false);
            tagView.setOnTouchListener(new OnTouchListener() {
                private Point mDownLocation;

                @Override
                public boolean onTouch(final View v, MotionEvent ev) {
                    Log.i(TAG, "onTouch() called with: v = [" + v + "], ev = [" + ev + "]");
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mDownLocation = new Point(((TagView) v).getLocation());
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            if (null != mDelButton) {
                                mDelButton.setText("添加标签");
                            }
                            if (mDelButtonRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                                removeView(v);
                                mTagViewList.remove(v);
                                return true;
                            } else if (!mImageRect.contains((int) ev.getX(), (int) ev.getY())) {
                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                                        .setDuration(100);
                                final Point startPosition = ((TagView) v).getLocation();

                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    Point tempPoint = new Point();

                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        float animatedValue = (float) animation.getAnimatedValue();

                                        float x = startPosition.x - mDownLocation.x;
                                        float y = startPosition.y - mDownLocation.y;
                                        tempPoint.set((int) (startPosition.x - x * animatedValue),
                                                (int) (startPosition.y - y * animatedValue));

                                        ((TagView) v).setLocation(tempPoint);
                                    }
                                });
                                valueAnimator.start();

                            }

                            break;

                    }
                    return gestureDetectorCompat.onTouchEvent(ev);
                }
            });

        }
        addView(tagView);
        mTagViewList.add(tagView);
    }

    private void moveTouchView(TagView v, int gapX, int gapY) {
        v.updatePosition(gapX, gapY);


        //v.layout(v.getLeft() + gapX, v.getTop() + gapY, v.getLeft() + gapX + v.getWidth(), v.getTop() + gapY + getHeight());
//            FrameLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
//
//            lp.leftMargin = lp.leftMargin + gapX;
//            lp.topMargin = lp.topMargin + gapY;
//
//
//            v.setLayoutParams(lp);
    }

    private void moveAllTags(int distanceY) {
        for (TagView tagView : mTagViewList) {
            tagView.updatePositionWithoutFix(0, distanceY);
        }
    }

    public List<UploadPhotoTagData> saveTags() {
        Rect bounds = mTargetImageView.getDrawable().getBounds();
        int width = bounds.right - bounds.left;
        int heigth = bounds.bottom - bounds.top;

        float[] matrixValues = new float[9];
        mTargetImageView.getImageMatrix().getValues(matrixValues);

        List<UploadPhotoTagData> result = new LinkedList<>();
        for (TagView view : mTagViewList) {
            Point location = view.getLocation();

            float originX = TagMatrixUtil.getOriginX(matrixValues, location.x);
            float originY = TagMatrixUtil.getOriginY(matrixValues, location.y);

            UploadPhotoTagData uploadPhotoTagData = new UploadPhotoTagData(1,
                    originX / (width),
                    originY / (heigth),
                    view.isRight());
            result.add(uploadPhotoTagData);
        }
        return result;
    }

    /**
     * 编辑侧、展示侧都会用到。
     * 但编辑时，显示所有的标签。
     * 展示时，只显示可见区域的标签
     *
     * @param tagPositions
     */
    public void loadTags(final List<UploadPhotoTagData> tagPositions) {
        if (null == tagPositions) return;
        post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = mTargetImageView.getDrawable().getBounds();
                int drawableWidth = bounds.right - bounds.left;
                int drawableHeight = bounds.bottom - bounds.top;

                float[] matrixValues = new float[9];
                mTargetImageView.getImageMatrix().getValues(matrixValues);


                Rect visibleRect = null;
                if (mode == MODE_VIEW) {
                    DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
                    // mIconWidth + mStrokeWidth + mStrokeWidth + mIconPaddingTop + mIconPaddingTop;
                    int tagMeasuredHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TagView.STROKE_WIDTH_DP + TagView.ICON_PADDING_TOP_OR_BOTTOM_DP + TagView.ICON_WIDTH_DP + TagView.ICON_PADDING_TOP_OR_BOTTOM_DP + TagView.STROKE_WIDTH_DP, metrics);

                    int topLimitInView = tagMeasuredHeight;
                    int bottomLimitInView = mTargetImageView.getHeight() - tagMeasuredHeight;
                    visibleRect = new Rect(0, topLimitInView, mTargetImageView.getWidth(), bottomLimitInView);
                }


                //判断Tag是否在可见范围，仅仅是宽高比严重失调的图 才会出现部分tag不可见。大部分情况下，所有Tag都是在可见范围。
                //所以在遍历Tag时，还是将每一个Tag的在图片中的位置都计算一遍，以便后面计算margin

                for (UploadPhotoTagData tagPosition : tagPositions) {
                    //将百分比换算成具体的像素
                    double originX = tagPosition.xPosition * drawableWidth;
                    double originY = tagPosition.yPosition * drawableHeight;

                    int offsetXInView = TagMatrixUtil.getMatrixX(matrixValues, originX);
                    int offsetYInView = TagMatrixUtil.getMatrixY(matrixValues, originY);

                    if (mode == MODE_VIEW) {
                        if (visibleRect.contains(offsetXInView, offsetYInView)) {
                            addTag(new Point(offsetXInView, offsetYInView), tagPosition.isRight);
                        }
                    } else {
                        addTag(new Point(offsetXInView, offsetYInView), tagPosition.isRight);
                    }

                }
            }
        });

    }


    private class TagGestureListener implements GestureDetector.OnGestureListener {
        private TagView mView;
        private PointF mLastPointF = new PointF();

        public TagGestureListener(TagView view) {
            mView = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mLastPointF.x = e.getRawX();
            mLastPointF.y = e.getRawY();
            Log.d(TAG, "onDown() called with: e = [" + e + "]");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mView.changeOrientation();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            requestDisallowInterceptTouchEvent(true);
            float rawX = e2.getRawX();
            float rawY = e2.getRawY();
            float x = rawX - mLastPointF.x;
            float y = rawY - mLastPointF.y;
            Log.d(TAG, "onScroll() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
            Log.d(TAG, "onScroll() called with: x = [" + x + "], y = [" + y + "], rawX = [" + rawX + "], rawY = [" + rawY + "]");
            moveTouchView(mView, Math.round(x), Math.round(y));
            mLastPointF.x = rawX;
            mLastPointF.y = rawY;

            if (null == mDelButton) {
                return true;
            }
            //删除
            mDelButton.setText("拖移到此处删除");

            if (mDelButtonRect.contains((int) rawX, (int) rawY)) {
//                isDeled = true;
//                removeView(mView);
//                mTagViewList.remove(mView);
//                return false;
                mDelButton.setText("松手即可删除");
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        private void moveTouchView(TagView v, int gapX, int gapY) {
            v.updatePosition(gapX, gapY);


            //v.layout(v.getLeft() + gapX, v.getTop() + gapY, v.getLeft() + gapX + v.getWidth(), v.getTop() + gapY + getHeight());
//            FrameLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
//
//            lp.leftMargin = lp.leftMargin + gapX;
//            lp.topMargin = lp.topMargin + gapY;
//
//
//            v.setLayoutParams(lp);
        }
    }


}
