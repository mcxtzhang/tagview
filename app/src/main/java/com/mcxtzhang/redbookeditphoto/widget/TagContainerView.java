package com.mcxtzhang.redbookeditphoto.widget;

import android.content.Context;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

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

    private Button mDelButton;
    private GestureDetectorCompat mTagParentGestureDetector;
    private List<TagView> mTagViewList = new LinkedList<>();
    private ImageView mTargetImageView;

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

    public TagContainerView bindImageView(ImageView targetImageView) {
        mTargetImageView = targetImageView;
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (null == layoutParams) {
                    layoutParams = new ViewGroup.LayoutParams(mTargetImageView.getWidth(), mTargetImageView.getHeight());
                } else {
                    layoutParams.height = mTargetImageView.getHeight();
                    layoutParams.width = mTargetImageView.getWidth();
                }
                setLayoutParams(layoutParams);
            }
        });
        return this;
    }

    private void init(Context context) {
        mContext = context;
        setClickable(true);
        setBackgroundColor(Color.parseColor("#88000000"));
        createDelButton();
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
                    boolean isRight = false;
                    int containerWidth = getWidth();
                    if (containerWidth / 2 > x) {
                        isRight = true;
                    }
                    addTag(new Point(x, y), isRight);
                } else {
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
                return false;
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

    private void createDelButton() {
        if (mode == MODE_VIEW) {
            return;
        }
        mDelButton = new Button(mContext);
        mDelButton.setText("删除");
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        addView(mDelButton, lp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: event = [" + event + "]");
        return mTagParentGestureDetector.onTouchEvent(event);
    }

    private void addTag(Point point, boolean isRight) {
        TagView tagView = new TagView(mContext);
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        tagView.setPadding(padding, padding, padding, padding);
        tagView.setParent(this);
        tagView.setText("Gucci Gucci");
        tagView.setBackgroundColor(Color.BLUE);
        tagView.setClickable(true);
        tagView.setOrientationAndPosition(isRight, point);

        if (mode == MODE_EDIT) {
            final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mContext, new TagGestureListener(tagView));
            gestureDetectorCompat.setIsLongpressEnabled(false);
            tagView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    Log.i(TAG, "onTouch() called with: v = [" + v + "], ev = [" + ev + "]");
                    return gestureDetectorCompat.onTouchEvent(ev);
                }
            });

        }
        addView(tagView);
        mTagViewList.add(tagView);
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

    public void loadTags(final List<UploadPhotoTagData> tagPositions) {
        if (null == tagPositions) return;
        mTargetImageView.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = mTargetImageView.getDrawable().getBounds();
                int drawableWidth = bounds.right - bounds.left;
                int drawableHeight = bounds.bottom - bounds.top;

                float[] matrixValues = new float[9];
                mTargetImageView.getImageMatrix().getValues(matrixValues);

                Rect visibleRect = new Rect(0, 0, mTargetImageView.getWidth(), mTargetImageView.getHeight());

                //判断Tag是否在可见范围，仅仅是宽高比严重失调的图 才会出现部分tag不可见。大部分情况下，所有Tag都是在可见范围。
                //所以在遍历Tag时，还是将每一个Tag的在图片中的位置都计算一遍，以便后面计算margin

                for (UploadPhotoTagData tagPosition : tagPositions) {
                    //将百分比换算成具体的像素
                    double originX = tagPosition.xPosition * drawableWidth;
                    double originY = tagPosition.yPosition * drawableHeight;

                    int offsetX = TagMatrixUtil.getMatrixX(matrixValues, originX);
                    int offsetY = TagMatrixUtil.getMatrixY(matrixValues, originY);

                    boolean contains = visibleRect.contains(offsetX, offsetY);
                    if (contains) {
                        addTag(new Point(offsetX, offsetY), tagPosition.isRight);
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

            //删除
            Rect rect = new Rect();
            mDelButton.getGlobalVisibleRect(rect);
            Log.d(TAG, "onScroll() called with: rect = [" + rect + "]");
            if (rect.contains((int) rawX, (int) rawY)) {
                removeView(mView);
                mTagViewList.remove(mView);
                return false;
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
