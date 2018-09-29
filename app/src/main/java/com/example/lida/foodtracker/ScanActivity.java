package com.example.lida.foodtracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    private ImageView scanImageView;
    private Bitmap scanBitmap;
    private BarcodeDetector detector;
    private static final String TAG = "SCAN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanImageView = (ImageView) findViewById(R.id.imgview);
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = false;
        Drawable d = getResources().getDrawable(R.drawable.ic_launcher_background);
        scanBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher_background);
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if(!detector.isOperational()){
            Log.d(TAG, "Could not set up the detector");
            // /txtView.setText("Could not set up the detector!");
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(scanBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        Barcode thisCode = barcodes.valueAt(0);
        TextView txtView = (TextView) findViewById(R.id.txtContent);
        txtView.setText(thisCode.rawValue);

    }

}
