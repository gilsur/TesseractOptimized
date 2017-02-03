package com.gilsur.optimasitersseract.Core.TessTool;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Fadi on 6/11/2014.
 * Modified by Gilsur on 11/12/2016.
 */
public class TessDataManager {

    static final String TAG = "DBG_" + TessDataManager.class.getName();
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/OptimasiTesseract/";
    // You should have the trained data file in assets folder
    // You can get them at:
    // http://code.google.com/p/tesseract-ocr/downloads/list
    // public static final String lang = "eng";

//    private static final String tessdir = "tesseract";
//    private static final String subdir = "tessdata";
//    private static final String filename = lang + ".traineddata";
//    private static String trainedDataPath;
//    private static String tesseractFolder;

//    public static String getTesseractFolder() {
//        return tesseractFolder;
//    }
//
//    public static String getTrainedDataPath(){
//        return initiated ? trainedDataPath : null;
//    }
//
//    private static boolean initiated;

    public static void initTessTrainedData(Context context, String lang){
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }
        // lang.traineddata file with the app (in assets folder)
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/"+ lang +".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".traineddata " + e.toString());
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.bigrams")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.bigrams");
                //GZIPInputStream gin = new GZIPInputStream(in);
                Log.v(TAG, in.toString());

                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.bigrams");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.bigrams");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.bigrams " + e.toString());
            }
        }


        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.fold")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.fold");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.fold");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.fold");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.fold " + e.toString());
            }
        }


        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.lm")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.lm");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.lm");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.lm");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.lm " + e.toString());
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.nn")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.nn");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.nn");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.nn");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.nn " + e.toString());
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.params")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.params");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.params");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.params");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.params " + e.toString());
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.size")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.size");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.size");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.size");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.size " + e.toString());
            }
        }


        if (!(new File(DATA_PATH + "tessdata/" + lang + ".cube.word-freq")).exists()) {
            try {

                AssetManager assetManager = context.getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".cube.word-freq");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".cube.word-freq");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".cube.word-freq");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + ".cube.word-freq " + e.toString());
            }
        }

    }

//    public static void initTessTrainedData(Context context){
//
//        if(initiated)
//            return;
//
//        File appFolder = context.getFilesDir();
//        File folder = new File(appFolder, tessdir);
//        if(!folder.exists())
//            folder.mkdir();
//        tesseractFolder = folder.getAbsolutePath();
//
//        File subfolder = new File(folder, subdir);
//        if(!subfolder.exists())
//            subfolder.mkdir();
//
//        File file = new File(subfolder, filename);
//        trainedDataPath = file.getAbsolutePath();
//        Log.d(TAG, "Trained data filepath: " + trainedDataPath);
//
//        if(!file.exists()) {
//
//            try {
//                FileOutputStream fileOutputStream;
//                byte[] bytes = readRawTrainingData(context);
//                if (bytes == null)
//                    return;
//                fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(bytes);
//                fileOutputStream.close();
//                initiated = true;
//                Log.d(TAG, "Prepared training data file");
//            } catch (FileNotFoundException e) {
//                Log.e(TAG, "Error opening training data file\n" + e.getMessage());
//            } catch (IOException e) {
//                Log.e(TAG, "Error opening training data file\n" + e.getMessage());
//            }
//        }
//        else{
//            initiated = true;
//        }
//    }
//
//    private static byte[] readRawTrainingData(Context context){
//
//        try {
//            InputStream fileInputStream = context.getResources()
//                    .openRawResource(R.raw.eng_traineddata);
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//            byte[] b = new byte[1024];
//
//            int bytesRead;
//
//            while (( bytesRead = fileInputStream.read(b))!=-1){
//                bos.write(b, 0, bytesRead);
//            }
//
//            fileInputStream.close();
//
//            return bos.toByteArray();
//
//        } catch (FileNotFoundException e) {
//            Log.e(TAG, "Error reading raw training data file\n"+e.getMessage());
//            return null;
//        } catch (IOException e) {
//            Log.e(TAG, "Error reading raw training data file\n" + e.getMessage());
//        }
//
//        return null;
//    }

}
