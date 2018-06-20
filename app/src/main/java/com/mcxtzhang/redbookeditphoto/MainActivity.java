package com.mcxtzhang.redbookeditphoto;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        int[] imageWidthHeight = TagMatrixUtil.getImageWidthHeight(getResources(), R.drawable.vertical);
//        Log.d("TAG", "vertical imageWidth:" + imageWidthHeight[0]);
//        Log.d("TAG", "imageHeight:" + imageWidthHeight[1]);
//
//        imageWidthHeight = TagMatrixUtil.getImageWidthHeight(getResources(), R.drawable.horizontal);
//        Log.d("TAG", "horizontal imageWidth:" + imageWidthHeight[0]);
//        Log.d("TAG", "imageHeight:" + imageWidthHeight[1]);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = imageView.getDrawable();
                Rect rectTemp = drawable.getBounds();

                Log.d("TAG", "drawable.bounds() :" + rectTemp);
//                Log.d("TAG", "drawable.getIntrinsicHeight:" + drawable.getIntrinsicHeight() + ",drawable.getIntrinsicWidth:" + drawable.getIntrinsicWidth());
//                Log.d("TAG", "drawable.getMinimumHeight:" + drawable.getMinimumHeight() + ",drawable.getMinimumWidth:" + drawable.getMinimumWidth());
                Matrix imageMatrix = imageView.getImageMatrix();
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
                x = TagMatrixUtil.getOriginX(matrixValues, x);
                y = TagMatrixUtil.getOriginY(matrixValues, y);
                Log.d("TAG", "run() x:" + x);
                Log.d("TAG", "run() y:" + y);
                x = imageView.getWidth();
                y = imageView.getHeight();
                Log.d("TAG", "ImageView width:" + x);
                Log.d("TAG", "ImageView Height:" + y);
                //相对于缩放前原图的点
                x = TagMatrixUtil.getOriginX(matrixValues, x);
                y = TagMatrixUtil.getOriginY(matrixValues, y);
                Log.d("TAG", "run() x:" + x);
                Log.d("TAG", "run() y:" + y);

            }
        });
    }

}
