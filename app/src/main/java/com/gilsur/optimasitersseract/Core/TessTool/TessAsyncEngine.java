package com.gilsur.optimasitersseract.Core.TessTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.gilsur.optimasitersseract.Core.Dialogs.ImageDialog;
import com.gilsur.optimasitersseract.Core.Imaging.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fadi on 6/11/2014.
 * Modified by Gilsur on 11/12/2016.
 */
public class TessAsyncEngine extends AsyncTask<Object, Integer, String> {

    static final String TAG = "DBG_" + TessAsyncEngine.class.getName();

    private Bitmap bmp;
    private String langs;

    private Bitmap bitmap;
    private Bitmap bmpThres;
    private Bitmap bmpOp;
    private Bitmap bmpOpLum;
    private Bitmap bmpOpThres;

    private Activity context;

    private ProgressDialog pDialog;

    public TessAsyncEngine(Activity activity, Bitmap bitmap, String langs){
        this.context = activity;
        this.bitmap = bitmap;
        this.langs = langs;

    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Memproses Gambar");
        pDialog.setMessage("Harap Tunggu");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setProgress(0);
        pDialog.show();
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Object... params) {

        try {

//            if(params.length < 2) {
//                Log.e(TAG, "Error passing parameter to execute - missing params");
//                return null;
//            }
//
//            if(!(params[0] instanceof Activity) || !(params[1] instanceof Bitmap)) {
//                Log.e(TAG, "Error passing parameter to execute(context, bitmap)");
//                return null;
//            }
//
//            context = (Activity)params[0];

            bmp = bitmap;

            int rotate = 0;

            if(params.length == 3 && params[2]!= null && params[2] instanceof Integer){
                rotate = (Integer) params[2];
            }

            if(rotate >= -180 && rotate <= 180 && rotate != 0)
            {
                bmp = Tools.preRotateBitmap(bmp, rotate);
                Log.d(TAG, "Rotated OCR bitmap " + rotate + " degrees");
            }
            final long startTime = SystemClock.uptimeMillis();
            publishProgress(12);
            TessEngine tessEngine =  TessEngine.Generate(context);
            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
            publishProgress(25);
            String result = tessEngine.detectText(bmp, langs);
            // Calculate performance statistics
            final long stopTime = SystemClock.uptimeMillis();
            publishProgress(37);
            bmpThres = tessEngine.getThreshold();

            final long startTimeOP = SystemClock.uptimeMillis();
            publishProgress(50);
            bmpOp = Tools.NearestNeighbor(bmp);
            Bitmap bmpOpz = bmpOp.copy(Bitmap.Config.ARGB_8888, true);
            publishProgress(62);
            bmpOpLum = Tools.getLuminosity(bmpOpz);
            Bitmap bmpOpLumc = bmpOpLum.copy(Bitmap.Config.ARGB_8888, true);
            publishProgress(75);
            TessEngineOP tessEngineOp = TessEngineOP.Generate(context);
            publishProgress(87);
            String resultOP = tessEngineOp.detectText(bmpOpLumc, langs);
            // Calculate performance statistics
            final long stopTimeOP = SystemClock.uptimeMillis();
            publishProgress(100);
            bmpOpThres = tessEngineOp.getThreshold();

            String resultz = "Hasil Original\n-----------------\nHasil OCR: " + result + "\nResolusi: " +
                    bmp.getWidth() + "x" + bmp.getHeight() + "\nWaktu Proses: "+ (stopTime - startTime) +
                    "ms\n\nHasil Modifikasi\n-----------------\nHasil OCR: " + resultOP + "\nResolusi: " +
                    bmpOp.getWidth() + "x" + bmpOp.getHeight() + " \nWaktu Proses: "+ (stopTimeOP - startTimeOP) +"ms";
            
            Log.d(TAG, resultz);

            return resultz;

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex + "\n" + ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pDialog.dismiss();
        if(s == null || bmp == null || context == null)
            return;

        ImageDialog.New()
                .addBitmap(bmp)
                .addTitle(s)
                .show(context.getFragmentManager(), TAG);
        Tools.storeImage(bmp, "1_Original");
        Tools.storeImage(bmpThres, "2_OriginalThreshold");
        Tools.storeImage(bmpOp, "3_NearestNeighbor");
        Tools.storeImage(bmpOpLum, "4_Luminosity");
        Tools.storeImage(bmpOpThres, "5_OptimizedFinal");


        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.v(TAG, "Progress " + progress[0] +"%");
        pDialog.setProgress(progress[0]);
        if (progress[0] == 12) pDialog.setMessage("Inisialisasi Tesseract Engine");
        if (progress[0] == 25) pDialog.setMessage("Dekteksi Text Original");
        if (progress[0] == 37) pDialog.setMessage("Dekteksi Text Selesai");
        if (progress[0] == 50) pDialog.setMessage("Memperbesar Gambar dengan Nearest Neighbor");
        if (progress[0] == 62) pDialog.setMessage("Luminosity Gambar");
        if (progress[0] == 75) pDialog.setMessage("Inisialisasi Tesseract Engine");
        if (progress[0] == 87) pDialog.setMessage("Dekteksi Text Modifikasi");
        if (progress[0] == 100) pDialog.setMessage("Dekteksi Text Selesai Harap Tunggu");
    }
}
