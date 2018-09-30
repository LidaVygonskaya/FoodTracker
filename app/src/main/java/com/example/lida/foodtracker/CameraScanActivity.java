package com.example.lida.foodtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Product;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraScanActivity extends AppCompatActivity {
    private SurfaceView cameraView;
    private ListView barcodeInfo;
    private BarcodeDetector barcodeDetector;

    private ArrayAdapter<String> arrayAdapter;
    private List<String> productList = new ArrayList<String>();
    private static final String TAG = "CAMERA_SCAN_ACTIVITY";
    private ProgressDialog progressDialog;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Camera myCamera;
    private CameraPreview myPreview;
    private CameraSource myCameraSource;
    private Camera.Parameters myParameters;
    private boolean isAbleToScan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);
        productList.add("Начните сканировать и добавлять продукт");
        barcodeInfo = (ListView) findViewById(R.id.barcodeTextView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, productList);

        barcodeInfo.setAdapter(arrayAdapter);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13)
                .build();
        myCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .build();

        myCamera = getCameraInstance();
        myParameters = myCamera.getParameters();
        myPreview = new CameraPreview(this, myCamera, myCameraSource, myParameters);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(myPreview);


/*
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(CameraScanActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(CameraScanActivity.this, "You have this permission", Toast.LENGTH_LONG).show();
                    } else {
                        requestCameraPermission();
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    Log.d(TAG, "Cant start CameraSource");
                    e.printStackTrace();
                }
            }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });*/

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && isAbleToScan) {
                    barcodeInfo.post(new Runnable() {
                        @Override
                        public void run() {
                            isAbleToScan = false;
                            Barcode thisBarcode = (Barcode) barcodes.valueAt(0);
                            Log.d(TAG, thisBarcode.rawValue);
                            //Intent intent = new Intent();
                            //intent.putExtra("barcode", thisBarcode.rawValue);
                            //productList.add(thisBarcode.rawValue);
                            //arrayAdapter.notifyDataSetChanged();
                            barcodes.clear();
                            getProductInformation("1234567890123");
                            //setResult(RESULT_OK, intent);
                            //finish();

                        }
                    });
                }
            }

        });
    }

    public void getProductInformation(final String barcode) {
        Log.d(TAG, "getInformationAboutProfuct");

        Map<String, String> data = new HashMap<>();
        data.put("bar", barcode);
        Call<Product> call = App.getApi().getProduct(data);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.d(TAG, response.toString());
                final Product product = response.body();


                final LayoutInflater inflater = getLayoutInflater();
                final View chooseAmountDialogView = inflater.inflate(R.layout.choose_amount_dialog, null);
                //NumberPicker
                final NumberPicker numberPicker = (NumberPicker) chooseAmountDialogView.findViewById(R.id.dialog_number_picker);
                numberPicker.setMaxValue(100);
                numberPicker.setMinValue(1);
                numberPicker.setWrapSelectorWheel(false);

                //Да нет? Alert dialog
                AlertDialog alertDialog = new AlertDialog.Builder(CameraScanActivity.this)
                        .setTitle("Выберите количество")
                        .setMessage(product.getName())
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productList.add(product.getName());
                                arrayAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                isAbleToScan = true;
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isAbleToScan = true;
                            }
                        })
                        .setView(chooseAmountDialogView)
                        .create();
                alertDialog.show();

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d(TAG, "Failed response");

            }
        });

    }


    //Check camera permissions
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permittion Needed")
                    .setMessage("Дайте доступ к камере, чтобы мы смогли считывать штрих-код продуктов")
                    .setPositiveButton("Разрешить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CameraScanActivity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Запретить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANDED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission was not GRANDED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Can try to request camera permissions here
    public static Camera getCameraInstance(){
        Camera c = null;
        Camera.Parameters p = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
            p = c.getParameters();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myPreview != null) {
            myCameraSource.stop();
        }
        //myCameraSource.stop();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (myPreview == null) {
            try {
                myCameraSource.start(myPreview.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
