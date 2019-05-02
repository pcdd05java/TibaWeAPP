package com.journeyapps.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import idv.ca107g2.zxingandroid.R;


/**
 *
 */
public class CaptureActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    public static final int SPOT_SUCCESS = 0xeeff00;

    private CaptureManager mCapture;
    private DecoratedBarcodeView mBarcodeScannerView;

    private ImageView mSwitchLightView;


    private boolean isLightOn;

    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_capture);
        initView();
        initCaptureManager(savedInstanceState);
        initListener();
    }

    /**
     * 重要方法
     *
     * @param savedInstanceState
     */
    private void initCaptureManager(Bundle savedInstanceState) {
        //初始化配置扫码界面
        mCapture = new CaptureManager(this, mBarcodeScannerView);
        //intent中携带了通过IntentIntegrator设置的参数
        mCapture.initializeFromIntent(getIntent(), savedInstanceState);
        mCapture.decode();
    }

    private void initView() {
        mBarcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        mSwitchLightView = findViewById(R.id.btn_switch_light);
        mProgressBar = findViewById(R.id.progress);

        if (!hasFlash()) {
            mSwitchLightView.setVisibility(View.GONE);
        }

        mProgressBar.setVisibility(View.GONE);
    }

    private void initListener() {
        mBarcodeScannerView.setTorchListener(this);

        mSwitchLightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLightOn) {
                    mBarcodeScannerView.setTorchOff();
                } else {
                    mBarcodeScannerView.setTorchOn();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCapture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCapture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCapture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCapture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mCapture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        }
    }

