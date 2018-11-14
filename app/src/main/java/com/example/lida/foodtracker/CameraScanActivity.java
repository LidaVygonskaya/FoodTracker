package com.example.lida.foodtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.example.lida.foodtracker.Utils.ProductComparator;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CameraScanActivity extends AppCompatActivity {
    private ListView barcodeInfo;
    private Button buttonSave;
    private TextView buttonSaveEmpty;
    private BarcodeDetector barcodeDetector;
    private ImageButton exitButton;
    private Button manualInput;

    private ProductAdapter arrayAdapter;
    private List<Product> products;
    private static final String TAG = "CAMERA_SCAN_ACTIVITY";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Camera myCamera;
    private CameraPreview myPreview;
    private CameraSource myCameraSource;
    private Camera.Parameters myParameters;
    private boolean isAbleToScan = true;
    private SharedPreferences sPref;
    private FrameLayout preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        preview = (FrameLayout) findViewById(R.id.camera_view);

        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(saveListener);

        buttonSaveEmpty = (TextView) findViewById(R.id.button_save_empty);

        exitButton = (ImageButton) findViewById(R.id.exit);
        exitButton.setOnClickListener(exitListener);

        products = new ArrayList<Product>();

        barcodeInfo = (ListView) findViewById(R.id.barcodeTextView);
        arrayAdapter = new ProductAdapter(this, R.layout.product_list_item, products);

        barcodeInfo.setAdapter(arrayAdapter);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13)
                .build();
        myCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
        requestCameraPermission();

        manualInput = (Button) findViewById(R.id.manual_input);
        manualInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  LayoutInflater inflater = CameraScanActivity.this.getLayoutInflater();
                v = inflater.inflate(R.layout.add_product, null);

                //Dialog for
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraScanActivity.this);

                mBuilder.setTitle("Добавление продукта");

                final EditText productNameView = (EditText) v.findViewById(R.id.add_product);
                final DatePicker date = (DatePicker) v.findViewById(R.id.datePicker);
                final EditText numberPicker = (EditText) v.findViewById(R.id.numberPicker);
                numberPicker.setOnFocusChangeListener(onFocusChangeListener);
                numberPicker.setOnKeyListener(onEditTextClickListener);
                final Spinner spinner = (Spinner) v.findViewById(R.id.spinner_quantity_choise);
                ArrayAdapter<?> adapterSpinner = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.quantity_choise, android.R.layout.simple_spinner_item);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) view).setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                mBuilder.setView(v);

                mBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createProduct(date, numberPicker, productNameView, spinner.getSelectedItem().toString(), "noBarcode", null);
                        dialog.dismiss();
                        isAbleToScan = true;
                        checkButtonVisibility();
                    }
                });

                mBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isAbleToScan = true;
                    }
                });

                AlertDialog dialog = mBuilder.create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                dialog.show();
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
                if (response.code() == 200) {
                    final Product product = response.body();

                    final LayoutInflater inflater = CameraScanActivity.this.getLayoutInflater();

                    View v = inflater.inflate(R.layout.add_product, null);

                    //Dialog for
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraScanActivity.this);

                    mBuilder.setTitle("Добавление продукта");

                    final EditText productNameView = (EditText) v.findViewById(R.id.add_product);
                    productNameView.setText(product.getName());
                    final DatePicker date = (DatePicker) v.findViewById(R.id.datePicker);
                    final EditText numberPicker = (EditText) v.findViewById(R.id.numberPicker);
                    numberPicker.setOnFocusChangeListener(onFocusChangeListener);
                    numberPicker.setOnKeyListener(onEditTextClickListener);
                    final Spinner spinner = (Spinner) v.findViewById(R.id.spinner_quantity_choise);
                    ArrayAdapter<?> adapterSpinner = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.quantity_choise, android.R.layout.simple_spinner_item);
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                    spinner.setAdapter(adapterSpinner);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) view).setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    mBuilder.setView(v);

                    mBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createProduct(date, numberPicker, productNameView, spinner.getSelectedItem().toString(), barcode, product.getComposition());
                            dialog.dismiss();
                            isAbleToScan = true;
                            checkButtonVisibility();
                        }
                    });

                    mBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isAbleToScan = true;
                        }
                    });

                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {}
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    EditText.OnKeyListener onEditTextClickListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard(v);
                return true;
            }
            return false;
        }
    };

    EditText.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
                return;
            }
        }
    };


    //Check camera permissions
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Требуется разрешение к камере")
                    .setMessage("Пожалуйста, дайте доступ к камере, чтобы мы смогли считывать штрих-код продуктов")
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
        Log.d(TAG, "adad");
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            Log.d(TAG, "adad");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission GRANDED", Toast.LENGTH_SHORT).show();
                myCamera = getCameraInstance();
                myParameters = myCamera.getParameters();
                myPreview = new CameraPreview(this, myCamera, myCameraSource, myParameters);
                preview.addView(myPreview);
                barcodeDetector.setProcessor(barcodeProcessor);

            } else {
                //Toast.makeText(this, "Permission was not GRANDED", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onrequestpermmsion");

            }
            return;
        }
    }

    //Can try to request camera permissions here
    public Camera getCameraInstance(){
        Camera c = null;
        Camera.Parameters p = null;
        try {
            if (ContextCompat.checkSelfPermission(CameraScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(CameraScanActivity.this, "You have this permission", Toast.LENGTH_LONG).show();
            } else {
                requestCameraPermission();
            }
            c = Camera.open(); // attempt to get a Camera instance
            p = c.getParameters();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "not abvi");
        }
        return c; // returns null if camera is unavailable
    }

    public void checkButtonVisibility() {
        if (buttonSave.getVisibility() == View.INVISIBLE && !products.isEmpty()) {
            buttonSave.setVisibility(View.VISIBLE);
            buttonSaveEmpty.setVisibility(View.INVISIBLE);
        } else if (products.isEmpty()) {
            buttonSave.setVisibility(View.INVISIBLE);
            buttonSaveEmpty.setVisibility(View.VISIBLE);
        }
    }

    Detector.Processor<Barcode> barcodeProcessor = new Detector.Processor<Barcode>() {
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
                        barcodes.clear();
                        getProductInformation(thisBarcode.rawValue);
                    }
                });
            }
        }
    };

    public void createProduct(DatePicker date, EditText quantity, EditText name, String spin, String barCode, String composition) {
        if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty()) {
            return;
        }
        Product product = new Product();
        product.setDateEnd(new Date(date.getYear(), date.getMonth(), date.getDayOfMonth()));
        product.setQuantity(Double.parseDouble(quantity.getText().toString()));
        product.setName(name.getText().toString());
        product.setDescription("smth description");
        product.setImgId(R.drawable.carrot);
        product.setQuantityChoise(spin);
        product.setBarCode(barCode);
        //TODO: set composition!! Сейчас она не выставляется. Надо найти проблемное место скорее всего это здесь
        product.setComposition(composition);
        products.add(product);
        arrayAdapter.sort(new ProductComparator());
        arrayAdapter.notifyDataSetChanged();
    }

    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("PRODUCT_LIST", (Serializable) products);//Список продуктов
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    View.OnClickListener exitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (myPreview != null) {
            myCameraSource.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
