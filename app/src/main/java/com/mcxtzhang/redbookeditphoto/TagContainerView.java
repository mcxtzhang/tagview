package com.mcxtzhang.redbookeditphoto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangxutong on 2018/6/14.
 */

public class TagContainerView extends FrameLayout {
    private static final String TAG = TagContainerView.class.getSimpleName();

    private Context mContext;
    private Button mDelButton;
    private GestureDetectorCompat mTagParentGestureDetector;
    private List<View> mTagViewList = new LinkedList<>();

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
                Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                addTag(new Point((int) e.getX(), (int) e.getY()));
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

    private void addTag(Point point) {
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = point.x;
        lp.topMargin = point.y;
        TextView tagView = new TextView(mContext);
        tagView.setText("Gucci");
        tagView.setBackgroundColor(Color.BLUE);
        tagView.setClickable(true);

        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(mContext, new TagGestureListener(tagView));
        gestureDetectorCompat.setIsLongpressEnabled(false);
        tagView.setOnTouchListener(new OnTouchListener() {
            private PointF downPoint = new PointF();
            private PointF lastPoint = new PointF();

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                Log.i(TAG, "onTouch() called with: v = [" + v + "], ev = [" + ev + "]");
                return gestureDetectorCompat.onTouchEvent(ev);
            }
        });

        addView(tagView, lp);
        mTagViewList.add(tagView);
    }

    public List<Point> saveTags() {
        List<Point> result = new LinkedList<>();
        for (View view : mTagViewList) {
            FrameLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            result.add(new Point(lp.leftMargin, lp.topMargin));
        }
        return result;
    }

    public void loadTags(List<Point> tagPositions) {
        if (null == tagPositions) return;
        for (Point tagPosition : tagPositions) {
            addTag(tagPosition);
        }
    }

    private class TagGestureListener implements GestureDetector.OnGestureListener {
        private View mView;
        private PointF mLastPointF = new PointF();

        public TagGestureListener(View view) {
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
            Toast.makeText(mView.getContext(), "旋转", Toast.LENGTH_SHORT).show();
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

        private void moveTouchView(View v, int gapX, int gapY) {
            //v.layout(v.getLeft() + gapX, v.getTop() + gapY, v.getLeft() + gapX + v.getWidth(), v.getTop() + gapY + getHeight());
            FrameLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
            lp.leftMargin = lp.leftMargin + gapX;
            lp.topMargin = lp.topMargin + gapY;
            v.setLayoutParams(lp);
        }
    }


}
