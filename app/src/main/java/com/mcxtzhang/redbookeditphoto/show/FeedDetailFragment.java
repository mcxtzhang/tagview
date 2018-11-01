package com.mcxtzhang.redbookeditphoto.show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mcxtzhang.redbookeditphoto.R;
import com.mcxtzhang.redbookeditphoto.UploadPhotoTagData;
import com.mcxtzhang.redbookeditphoto.widget.TagContainerView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxutong on 2018/6/20.
 */

public class FeedDetailFragment extends Fragment {
    private static final List<UploadPhotoTagData> TAG_DATA_LIST = new LinkedList<>();

    static {
        TAG_DATA_LIST.add(new UploadPhotoTagData(1, 0.1, 0.01));
        TAG_DATA_LIST.add(new UploadPhotoTagData(1, 0.3, 0.3));
        TAG_DATA_LIST.add(new UploadPhotoTagData(1, 0.5, 0.5));
        TAG_DATA_LIST.add(new UploadPhotoTagData(1, 0.7, 0.7));
        TAG_DATA_LIST.add(new UploadPhotoTagData(1, 0.9, 0.9));
    }

    public static Map<Integer, List<UploadPhotoTagData>> sIntegerListMap = new HashMap<>();

    public static FeedDetailFragment newInstance(int position) {
        FeedDetailFragment photoEditFragment = new FeedDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("key-position", position);
        photoEditFragment.setArguments(arguments);
        return photoEditFragment;
    }

    TagContainerView mTagContainerView;
    ImageView mImageView;
    private int mPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            mPosition = arguments.getInt("key-position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed_detail, container, false);
//        int[] imageWidthHeight = TagMatrixUtil.getImageWidthHeight(getResources(), R.drawable.vertical);
//        Log.d("TAG", "vertical imageWidth:" + imageWidthHeight[0]);
//        Log.d("TAG", "imageHeight:" + imageWidthHeight[1]);
//
//        imageWidthHeight = TagMatrixUtil.getImageWidthHeight(getResources(), R.drawable.horizontal);
//        Log.d("TAG", "horizontal imageWidth:" + imageWidthHeight[0]);
//        Log.d("TAG", "imageHeight:" + imageWidthHeight[1]);
        mImageView = rootView.findViewById(R.id.imageView);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("TAG", "onTouch: " + event);
                return true;
            }
        });

        mTagContainerView = rootView.findViewById(R.id.tagContainer);

        //模拟延时加载图片
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {

                setImageViewSource();

                //图片加载完毕
                //开始加载标签
                List<UploadPhotoTagData> points = sIntegerListMap.get(mPosition);
                if (points == null /*&& mPosition == 0*/) {
                    points = TAG_DATA_LIST;
                }

                mTagContainerView
                        .bindImageView(mImageView)
                        .loadTags(points);
            }
        }, 2000);


        /*mImageView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = mImageView.getDrawable();
                Rect rectTemp = drawable.getBounds();

                Log.d("TAG", "drawable.bounds() :" + rectTemp);
//                Log.d("TAG", "drawable.getIntrinsicHeight:" + drawable.getIntrinsicHeight() + ",drawable.getIntrinsicWidth:" + drawable.getIntrinsicWidth());
//                Log.d("TAG", "drawable.getMinimumHeight:" + drawable.getMinimumHeight() + ",drawable.getMinimumWidth:" + drawable.getMinimumWidth());
                Matrix imageMatrix = mImageView.getImageMatrix();
                Log.d("TAG", "imageMatrix:" + imageMatrix);

                float[] values = new float[9];
                imageMatrix.getValues(values);

                // 存储Matrix矩阵的9个值
                float[] matrixValues = new float[9];
                // 变化的Matrix矩阵
                imageMatrix.getValues(matrixValues);

                // 图片原始点
                int x = 0;
                int y = 0;
                //相对于缩放前原图的点
                float originX = TagMatrixUtil.getOriginX(matrixValues, x);
                float originY = TagMatrixUtil.getOriginY(matrixValues, y);
                Log.d("TAG", "run() originX:" + originX);
                Log.d("TAG", "run() originY:" + originY);
                x = mImageView.getWidth();
                y = mImageView.getHeight();
                Log.d("TAG", "ImageView width:" + x);
                Log.d("TAG", "ImageView Height:" + y);
                //相对于缩放前原图的点
                originX = TagMatrixUtil.getOriginX(matrixValues, x);
                originY = TagMatrixUtil.getOriginY(matrixValues, y);
                Log.d("TAG", "run() originX:" + originX);
                Log.d("TAG", "run() originY:" + originY);

            }
        });*/


        return rootView;
    }

    private void setImageViewSource() {
        if ((mPosition & 1) == 0) {
            mImageView.setImageResource(R.drawable.vertical);
        } else {
            mImageView.setImageResource(R.drawable.horizontal);
        }
        if (mPosition == 0) {
            mImageView.setImageResource(R.drawable.vertical);
        }
        if (mPosition == 1) {
            mImageView.setImageResource(R.drawable.horizontal);
        }
        if (mPosition == 2) {
            mImageView.setImageResource(R.drawable.little);
        }
        if (mPosition == 3) {
            mImageView.setImageResource(R.drawable.tall);
        }
        if (mPosition == 4) {
            mImageView.setImageResource(R.drawable.normal);
        }
    }
}
