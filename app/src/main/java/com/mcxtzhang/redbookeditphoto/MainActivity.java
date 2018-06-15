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
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = imageView.getDrawable();
                Rect rectTemp = drawable.getBounds();

                Log.d("TAG", "run() called:" + rectTemp);
                Log.d("TAG", "run() called:" + drawable.getIntrinsicHeight() + ",    :" + drawable.getIntrinsicWidth());
                Log.d("TAG", "run() called:" + drawable.getMinimumHeight() + ",width:" + drawable.getMinimumWidth());
                Matrix imageMatrix = imageView.getImageMatrix();
                Log.d("TAG", "run() called:" + imageMatrix);

                float[] values = new float[9];
                imageMatrix.getValues(values);

                Log.d("TAG", "run() called:" + values[0]);

                Log.d("TAG", "run() called:" + values[0] * rectTemp.width());
                Log.d("TAG", "run() called:" + values[0] * rectTemp.height());

                float leftX = values[2] + rectTemp.width() * values[0];
                float leftY = values[5] + rectTemp.height() * values[4];
                Log.d("TAG", "run() leftX:" + leftX);
                Log.d("TAG", "run() leftY:" + leftY);


                // 存储Matrix矩阵的9个值
                float[] matrixValues = new float[9];
                // 变化的Matrix矩阵

                imageMatrix.getValues(matrixValues);

                // 变化的倍数
                float mscale_x = matrixValues[Matrix.MSCALE_X];
                float mtrans_x = matrixValues[Matrix.MTRANS_X];
                float mscale_y = matrixValues[Matrix.MSCALE_Y];
                float mtrans_y = matrixValues[Matrix.MTRANS_Y];

                // 图片原始点
                float x = 0;
                float y = 0;

                // 变化后的点
//                x = x * mscale_x + 1 * mtrans_x;
//                y = y * mscale_y + 1 * mtrans_y;
                //相对于缩放前原图的点
                x = (x - 1 * mtrans_x) / mscale_x;
                y = (y - 1 * mtrans_y) / mscale_y;
                Log.d("TAG", "run() x:" + x);
                Log.d("TAG", "run() y:" + y);
                x = imageView.getWidth();
                y = imageView.getHeight();
                Log.d("TAG", "run() x:" + x);
                Log.d("TAG", "run() y:" + y);
                x = (x - 1 * mtrans_x) / mscale_x;
                y = (y - 1 * mtrans_y) / mscale_y;
                Log.d("TAG", "run() x:" + x);
                Log.d("TAG", "run() y:" + y);

            }
        });
    }
}
