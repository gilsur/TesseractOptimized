package com.gilsur.optimasitersseract.Core.Imaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import com.gilsur.optimasitersseract.Core.ExtraViews.FocusBoxUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fadi on 5/11/2014.
 * Modified by Gilsur on 11/12/2016.
 */
public class Tools {

    public static Bitmap NearestNeighbor(Bitmap bitmap){
        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int scale = 2;
        int imgWidth = bmp.getWidth();
        int imgHeight = bmp.getHeight();
        int scaleImgWidth = bmp.getWidth()*scale;
        int scaleImgHeight = bmp.getHeight()*scale;
        //Initialize the intArray with the same size as the number of pixels on the image
        int[] intArray = new int[imgWidth*imgHeight];

        //copy pixel data from the Bitmap into the 'intArray' array
        bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, imgWidth, imgHeight);

        // int[] resize = resizePixels(intArray, imgWidth, imgHeight, (imgWidth*2), (imgHeight*2));

        // Nearest Neighbor
        int[] resize = new int[scaleImgWidth*scaleImgHeight];
        double x_ratio = imgWidth/(double)scaleImgWidth ;
        double y_ratio = imgHeight/(double)scaleImgHeight ;
        double px, py ;
        for (int i=0;i<scaleImgHeight;i++) {
            for (int j=0;j<scaleImgWidth;j++) {
                px = Math.floor(j*x_ratio) ;
                py = Math.floor(i*y_ratio) ;
                resize[(i*scaleImgWidth)+j] = intArray[(int)((py*imgWidth)+px)] ;
            }
        }

        return Bitmap.createBitmap(resize, scaleImgWidth, scaleImgHeight, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap preRotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    public static enum ScalingLogic {
        CROP, FIT
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                          ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    public static Bitmap decodeByteArray(byte[] bytes, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        return unscaledBitmap;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                            ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static Bitmap getFocusedBitmap(Context context, Camera camera, byte[] data, Rect box){
        Point CamRes = FocusBoxUtils.getCameraResolution(context, camera);
        Point ScrRes = FocusBoxUtils.getScreenResolution(context);

        int SW = ScrRes.x; //SCREEN WIDTH - HEIGHT
        int SH = ScrRes.y;

        int RW = box.width(); // FOCUS BOX RECT WIDTH - HEIGHT - TOP - LEFT
        int RH = box.height();
        int RL = box.left;
        int RT = box.top;

        float RSW = (float) (RW * Math.pow(SW, -1)); // DIMENSION RATIO OF FOCUSBOX OVER SCREEN
        float RSH = (float) (RH * Math.pow(SH, -1));

        float RSL = (float) (RL * Math.pow(SW, -1));
        float RST = (float) (RT * Math.pow(SH, -1));

        // 0.5f
        float k = 0.5f;

        int CW = CamRes.x;
        int CH = CamRes.y;

        int X = (int) (k * CW); //SCALED BITMAP FROM CAMERA
        int Y = (int) (k * CH);

        Bitmap unscaledBitmap = Tools.decodeByteArray(data, X, Y, Tools.ScalingLogic.CROP);
        Bitmap bmp = Tools.createScaledBitmap(unscaledBitmap, X, Y, Tools.ScalingLogic.CROP);
        unscaledBitmap.recycle();

        if (CW > CH)
            bmp = Tools.rotateBitmap(bmp, 90);

        int BW = bmp.getWidth();
        int BH = bmp.getHeight();

        int RBL = (int) (RSL * BW);
        int RBT = (int) (RST * BH);

        int RBW = (int) (RSW * BW);
        int RBH = (int) (RSH * BH);

        Bitmap res = Bitmap.createBitmap(bmp, RBL, RBT, RBW, RBH);
        bmp.recycle();

        return res;
    }

    // Luminosity
    public static Bitmap getLuminosity(Bitmap img)
    {

        Bitmap bmap = img.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < bmap.getWidth(); i++)
        {
            for (int j = 0; j < bmap.getHeight(); j++)
            {
                // get one pixel color
                int pixel = img.getPixel(i, j);
                // retrieve color of all channels
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(0.299 * R + 0.587 * G + 0.114 * B);
                // set new pixel color to output bitmap
                bmap.setPixel(i, j, Color.argb(A, R, G, B));

            }
        }
        return bmap.copy(Bitmap.Config.ARGB_8888, true);

    }

    public static void storeImage(Bitmap imageData, String filename) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/HasilOCR/");
        myDir.mkdirs();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();

        String filenames = formatter.format(now) + "-" + filename + ".PNG";
        File file = new File (myDir, filenames);
        if (file.exists ()) file.delete ();
        try {
            Log.w("Save", "Saving into " + root + "/HasilOCR/" + filenames);
            FileOutputStream out = new FileOutputStream(file);
            imageData.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.w("Save", "Saving into " + root + "/HasilOCR/" + filenames + " complete!");
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
//            MediaScannerConnection.scanFile(context.getApplicationContext(), new String[] { file.getAbsolutePath() }, null, new MediaScannerConnection.OnScanCompletedListener() {
//
//                @Override
//                public void onScanCompleted(String path, Uri uri) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
