package com.gilsur.optimasitersseract;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gilsur.optimasitersseract.Core.CameraEngine;
import com.gilsur.optimasitersseract.Core.ExtraViews.FocusBoxView;
import com.gilsur.optimasitersseract.Core.Imaging.Tools;
import com.gilsur.optimasitersseract.Core.TessTool.TessAsyncEngine;

/**
 * Created by Gilsur on 11/12/2016.
 */

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.ShutterCallback {

    static final String TAG = "DBG_" + MainActivity.class.getName();

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    TextView tv_lang;
    CameraEngine cameraEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "Surface Created - starting camera");

        if (cameraEngine != null && !cameraEngine.isOn()) {
            cameraEngine.start();
        }

        if (cameraEngine != null && cameraEngine.isOn()) {
            Log.d(TAG, "Camera engine already on");
            return;
        }

        cameraEngine = CameraEngine.New(holder);
        cameraEngine.start();

        Log.d(TAG, "Camera engine started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraFrame = (SurfaceView) findViewById(R.id.camera_frame);
        shutterButton = (Button) findViewById(R.id.shutter_button);
        focusBox = (FocusBoxView) findViewById(R.id.focus_box);
        focusButton = (Button) findViewById(R.id.focus_button);
        tv_lang = (TextView) findViewById(R.id.tv_lang);
        shutterButton.setOnClickListener(this);
        focusButton.setOnClickListener(this);

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        tv_lang.setOnClickListener(this);
        cameraFrame.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (cameraEngine != null && cameraEngine.isOn()) {
            cameraEngine.stop();
        }

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.removeCallback(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_lang){
            if (tv_lang.getText().toString().equals("ara")){
                tv_lang.setText("eng");
            }else{
                tv_lang.setText("ara");
            }
        }
        if(v == shutterButton){
            if(cameraEngine != null && cameraEngine.isOn()){
                cameraEngine.takeShot(this, this, this);
                View view = v.getRootView();

                view.setDrawingCacheEnabled(true);

                Bitmap bitmap = view.getDrawingCache();

            }
        }

        if(v == focusButton){
            if(cameraEngine!=null && cameraEngine.isOn()){
                cameraEngine.requestFocus();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        String langs = tv_lang.getText().toString().trim();

        Log.d(TAG, "Picture taken");

        if (data == null) {
            Log.d(TAG, "Got null data");
            return;
        }

        Bitmap bmp = Tools.getFocusedBitmap(this, camera, data, focusBox.getBox());

        Log.d(TAG, "Got bitmap");

        Log.d(TAG, "Initialization of TessBaseApi");

        new TessAsyncEngine(this, bmp, langs).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        Log.d(TAG, "TessBaseApi Finish");

        camera.startPreview();

    }

    @Override
    public void onShutter() {

    }

}