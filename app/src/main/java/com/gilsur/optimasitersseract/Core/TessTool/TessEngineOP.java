package com.gilsur.optimasitersseract.Core.TessTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.WriteFile;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by Fadi on 6/11/2014.
 * Modified by Gilsur on 11/12/2016.
 */
public class TessEngineOP {

    static final String TAG = "DBG_" + TessEngineOP.class.getName();

    private Context context;
    private Bitmap Threshold;

    private TessEngineOP(Context context){
        this.context = context;
    }

    public static TessEngineOP Generate(Context context) {
        return new TessEngineOP(context);
    }

    public String detectText(Bitmap bitmap, String lang) {
        Log.d(TAG, "Initialization of TessBaseApi");
        TessDataManager.initTessTrainedData(context, lang);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.setDebug(true);
        tessBaseAPI.init(TessDataManager.DATA_PATH, lang);
        tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
        Log.d(TAG, "Ended initialization of TessEngine");
        Log.d(TAG, "Running inspection on bitmap");
        tessBaseAPI.setImage(bitmap);
        String inspection = tessBaseAPI.getUTF8Text();
        Log.d(TAG, "Got data: " + inspection);
        Pix threshold = tessBaseAPI.getThresholdedImage();
        Threshold = WriteFile.writeBitmap(threshold);
        tessBaseAPI.end();
        System.gc();
        return inspection;
    }

    public Bitmap getThreshold(){
        return this.Threshold;
    }

}
