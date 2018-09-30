package com.example.lida.foodtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;

import java.io.IOException;
import java.lang.reflect.Parameter;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder myHolder;
    private Camera myCamera;
    private Camera.Parameters myParameters;
    private CameraSource myCameraSource;
    private static final int IMAGE_WIDTH = 1024;
    private static final int IMAGE_HEIGHT = 600;

    public CameraPreview(Context context, Camera camera, CameraSource cameraSource, Camera.Parameters parameters) {
        super(context);
        myCamera = camera;
        myParameters = parameters;
        myCameraSource = cameraSource;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        myHolder = getHolder();
        myHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            myCamera.setPreviewDisplay(holder);
            myCamera.startPreview();
            myCameraSource.start(myHolder);
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (myHolder.getSurface() == null){
            // preview surface does not exist
            myCameraSource.stop();
            return;
        }

        // stop preview before making changes
        try {
            myCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here


        for (Camera.Size previewSize: myParameters.getSupportedPreviewSizes()) {
            if (previewSize.width >= IMAGE_WIDTH && previewSize.height >= IMAGE_HEIGHT) {
                myParameters.setPreviewSize(previewSize.width, previewSize.height);
                break;
            }
        }

        // start preview with new settings
        try {
            myCamera.setParameters(myParameters); //Set Parameters My job
            myCamera.setPreviewDisplay(myHolder);
            myCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}
