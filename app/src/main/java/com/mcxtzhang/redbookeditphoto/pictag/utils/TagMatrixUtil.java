package com.mcxtzhang.redbookeditphoto.pictag.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.DrawableRes;

/**
 * Created by zhangxutong on 2018/6/19.
 */

public class TagMatrixUtil {

    private static Matrix sMatrix;
    private static float[] sMatrixValues = new float[9];

    /**
     * 获取在缩放前原图中的坐标
     *
     * @param matrixValues
     * @param x
     * @return
     */
    public static float getOriginX(float[] matrixValues, int x) {
        if (null == matrixValues) {
            return -1;
        }
        // 变化的倍数
        float mScaleX = matrixValues[Matrix.MSCALE_X];
        float mTransX = matrixValues[Matrix.MTRANS_X];
        return ((x - 1 * mTransX) / mScaleX);
    }

    public static float getOriginY(float[] matrixValues, int y) {
        if (null == matrixValues) {
            return -1;
        }
        // 变化的倍数
        float mScaleY = matrixValues[Matrix.MSCALE_Y];
        float mTransY = matrixValues[Matrix.MTRANS_Y];
        return ((y - 1 * mTransY) / mScaleY);
    }

    /**
     * 获取经过Matrix 变换后的坐标
     *
     * @param matrixValues
     * @param x
     * @return
     */
    public static int getMatrixX(float[] matrixValues, double x) {
        if (null == matrixValues) {
            return -1;
        }
        // 变化的倍数
        float mScaleX = matrixValues[Matrix.MSCALE_X];
        float mTransX = matrixValues[Matrix.MTRANS_X];
        return (int) (x * mScaleX + 1 * mTransX);
    }

    public static int getMatrixY(float[] matrixValues, double y) {
        if (null == matrixValues) {
            return -1;
        }
        // 变化的倍数
        float mScaleY = matrixValues[Matrix.MSCALE_Y];
        float mTransY = matrixValues[Matrix.MTRANS_Y];
        return (int) (y * mScaleY + 1 * mTransY);
    }

//    public static int getOriginX(Matrix imageMatrix, int x) {
//        imageMatrix.getValues(sMatrixValues);
//
//        // 变化的倍数
//        float mscale_x = sMatrixValues[Matrix.MSCALE_X];
//        float mtrans_x = sMatrixValues[Matrix.MTRANS_X];
//
//        x = (int) ((x - 1 * mtrans_x) / mscale_x);
//        return x;
//    }
//
//    public static int getOriginY(Matrix imageMatrix, int y) {
//        imageMatrix.getValues(sMatrixValues);
//
//        float mscale_y = sMatrixValues[Matrix.MSCALE_Y];
//        float mtrans_y = sMatrixValues[Matrix.MTRANS_Y];
//        y = (int) ((y - 1 * mtrans_y) / mscale_y);
//        return y;
//    }


    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }


    public static int[] getImageWidthHeight(Resources resources, @DrawableRes int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableId, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }
}
