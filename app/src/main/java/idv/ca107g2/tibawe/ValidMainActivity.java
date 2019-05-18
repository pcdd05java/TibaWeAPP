package idv.ca107g2.tibawe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qrcore.util.QRScannerHelper;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import idv.ca107g2.tibawe.classzone.CourseQueryFragment;
import idv.ca107g2.tibawe.lifezone.DBDFragment;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;

public class ValidMainActivity extends AppCompatActivity {
    Menu menu;
    SharedPreferences preferences;
    String qrtime_date, class_no, memberaccount, nowDateString, qrtime_interval;
    java.sql.Date nowDate;
    int msg_code;

    private static int hr, min;
    private static final String TAG = "ValidMainActivity";
    private CommonTask havetoCheckTask, qrCheckTask;

    boolean login;

    private boolean hasCameraPermission = true;
    private QRScannerHelper mScannerHelper;
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static final int MY_REQUEST_CODE = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private static Location location;
    private static double tibameLocLatitude = 24.967790;
    private double tibameLocLongitude = 121.191709;
    private String distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        login = preferences.getBoolean("login", false);

        setContentView(R.layout.activity_validmain);
        Toolbar toolbar = findViewById(R.id.toolbar_validmain);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // Menu item click的監聽事件一樣要設定在setSupportActionBar之後才有作用
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_logout:
                        LogoutAlertFragment alertFragment = new LogoutAlertFragment();
                        FragmentManager fm = getSupportFragmentManager();
                        alertFragment.show(fm, "alert");
                }

                return true;
            }
        });


        ValidMainPagerAdapter pagerAdapter = new ValidMainPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.vpMain);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        TabLayout tabLayout = findViewById(R.id.tbMain);
        tabLayout.setupWithViewPager(pager);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        askPermissions();

        initQRScanner();

        isQRCode();

    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = getString(R.string.text_noGrant);
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                break;
        }
    }

    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (result != null) {
                    qrtime_interval = String.valueOf(result.charAt(11));
                    qrtime_date = result.substring(0, 10);
                } else {
                    msg_code = 8;
                }

            }
        });
    }


    public static class LogoutAlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_export_24)
                    .setTitle(R.string.logout)
                    //設定訊息內容
                    .setMessage(R.string.logout_msg)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.logout, this)
                    //設定取消鍵 (negative用於取消)
                    .setNegativeButton(R.string.logout_cancel, this)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button posbtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    posbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                    Button negbtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE,
                            MODE_PRIVATE);
                    pref.edit().putBoolean("login", false).apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    getActivity().finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_top_valid, menu);
        this.menu = menu;
        menu.findItem(R.id.action_membername).setTitle(preferences.getString("membername", ""));
        return true;
    }

    public void isQRCode() {
        if (getIntent().getBooleanExtra("isQRCode", false)) {
            getLocation();
        }
    }

    public void onClickQRCode(View view) {
        getLocation();
    }


    public void getLocation() {
        if(ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            askPermissions();
        }
        else{
            if (location == null) {
                LocationAlertFragment locationAlertFragment = new LocationAlertFragment();
                FragmentManager fm1 = getSupportFragmentManager();
                locationAlertFragment.show(fm1, "locationalert");

                new CountDownTimer(3000, 100) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (location != null) {
                            cancel();
                        }
                    }

                    @Override
                    public void onFinish() {
                        PermissionAlertFragment permissionAlertFragment = new PermissionAlertFragment();
                        FragmentManager fm2 = getSupportFragmentManager();
                        permissionAlertFragment.show(fm2, "Permissionalert");
                    }
                }.start();
            }else{
                float[] results = new float[1];
                // 計算自己位置與使用者輸入地點，此2點間的距離(公尺)，結果會存入results[0]
                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        tibameLocLatitude, tibameLocLongitude, results);
                distance = NumberFormat.getInstance().format(results[0]);

                if (results[0] < 500f) {
                    havetoCheckNow();
                } else {
                    msg_code= 9;
                    Intent intent = new Intent(this, QRCodeSignInActivity.class);
                    intent.putExtra("msg_code", msg_code);
                    intent.putExtra("distance", distance);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            }
        }
    }

    public void havetoCheckNow(){
        Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        nowDate= new java.sql.Date(System.currentTimeMillis());
        nowDateString = nowDate.toString();

        int interval;
        if(7<=hr && hr<11) {
            interval=1;
        }else if (11<=hr && hr<17) {
            interval=2;
        }else {
            interval=3;
        }

        String url = Util.URL + "AttendanceServlet";
        class_no =  preferences.getString("class_no", "");
        memberaccount = preferences.getString("memberaccount","");


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "havetoCheckNow");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("memberaccount", memberaccount);
        jsonObject.addProperty("nowDateString", nowDateString);
        jsonObject.addProperty("hr", hr);

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            havetoCheckTask = new CommonTask(url, jsonOut);
            try {
                String result = havetoCheckTask.execute().get();

                Type collectionType = new TypeToken<Map>() {
                }.getType();
                Map haveCheckNow = gson.fromJson(result, collectionType);

                Boolean havetoCheckNow = Boolean.valueOf(haveCheckNow.get("havetoCheckNow").toString());
                Boolean isChecked = Boolean.valueOf(haveCheckNow.get("isChecked").toString());

                if(havetoCheckNow && !isChecked){
                    switch(interval) {
                        case 1:
                            if ((hr <= 8 && min <= 59) || (hr == 9 && min <= 30)) {
                                openCamera();
                            } else {
                                msg_code = 3;
                                Intent intent = new Intent(this, QRCodeSignInActivity.class);
                                intent.putExtra("msg_code", msg_code);
                                intent.putExtra("distance", distance);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }
                            break;
                        case 2:
                            if (!(hr <= 13 && min <= 59) || (hr == 14 && min == 0)) {
                                openCamera();
                            }else {
                                msg_code = 3;
                                Intent intent = new Intent(this, QRCodeSignInActivity.class);
                                intent.putExtra("msg_code", msg_code);
                                intent.putExtra("distance", distance);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }
                            break;
                        case 3:
                            if ((hr <= 18 && min <= 59) || (hr == 19 && min <= 30)) {
                                openCamera();
                            }else {
                                msg_code = 3;
                                Intent intent = new Intent(this, QRCodeSignInActivity.class);
                                intent.putExtra("msg_code", msg_code);
                                intent.putExtra("distance", distance);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }
                            break;
                    }
                }else if(!havetoCheckNow){
                    msg_code = 2;
                    Intent intent = new Intent(this, QRCodeSignInActivity.class);
                    intent.putExtra("msg_code", msg_code);
                    intent.putExtra("distance", distance);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }else if(isChecked){
                    msg_code = 5;
                    Intent intent = new Intent(this, QRCodeSignInActivity.class);
                    intent.putExtra("msg_code", msg_code);
                    intent.putExtra("distance", distance);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }



            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    public void openCamera(){
        if (!hasCameraPermission) {
            Toast.makeText(this, R.string.msg_pleaseopencamera, Toast.LENGTH_SHORT).show();
            return;
        }
        mScannerHelper.startScanner();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);

            int interval;
            if(7<=hr && hr<11) {
                interval=1;
            }else if (11<=hr && hr<17) {
                interval=2;
            }else {
                interval=3;
            }

            if(qrtime_date!=null) {
                if (!nowDateString.equals(qrtime_date)) {
                    msg_code = 1;
                } else if ((nowDateString.equals(qrtime_date)) && (!qrtime_interval.equals(String.valueOf(interval)))) {
                    msg_code = 6;
                } else if ((nowDateString.equals(qrtime_date)) && (qrtime_interval.equals(String.valueOf(interval)))) {
                    qrCheck();
                }
            }

            Intent intent = new Intent(this, QRCodeSignInActivity.class);
            intent.putExtra("msg_code", msg_code);
            intent.putExtra("distance", distance);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }


    public void qrCheck() {
        Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        String url = Util.URL + "AttendanceServlet";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "qrCheckIn");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("memberaccount", memberaccount);
        jsonObject.addProperty("qrtime_date", qrtime_date);
        jsonObject.addProperty("hr", hr);
        jsonObject.addProperty("min", min);
        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            qrCheckTask = new CommonTask(url, jsonOut);
            try {
                String result = qrCheckTask.execute().get();

                msg_code = Integer.valueOf(result);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private class ValidMainPagerAdapter extends FragmentPagerAdapter {

        public ValidMainPagerAdapter (FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new DBDFragment();
                case 1:
                    return new ValidMainMenuFragment();
                case 2:
                    return new CourseQueryFragment();
            }
            return null;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getResources().getText(R.string.DBD_today);
                case 1:
                    return getResources().getText(R.string.validmain_menu_tab);
                case 2:
                    return getResources().getText(R.string.course_query_quick);
            }
            return null;
        }
    }


    //for Location

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
//                if (location != null)
//                    onClickLocation();
        }
        };
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        // 10秒要一次位置資料 (但不一定, 有可能不到10秒, 也有可能超過10秒才要一次)
        locationRequest.setInterval(10000);
        // 若有其他app也使用了LocationServices, 就會以此時間為取得位置資料的依據
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.e(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(ValidMainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(ValidMainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Log.e(TAG, "Cancel location updates requested");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        if(login){
            LogoutAlertFragment alertFragment = new LogoutAlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);}


    }

    public static class LocationAlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog locationAlertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_gps_signal_24)
                    .setTitle(R.string.text_GPSwaiting)
                    .setMessage("")
//                    //設定取消鍵 (negative用於取消)
//                    .setNegativeButton(R.string.logout_cancel, this)
                    .create();
            locationAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    new CountDownTimer(3000, 100) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(location != null){
                                cancel();
                                locationAlertDialog.cancel();
                            }
                        }
                        @Override
                        public void onFinish() {
                            locationAlertDialog.dismiss();
                        }
                    }.start();
//                    Button posbtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                    posbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
//                    Button negbtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                    negbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return locationAlertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }


    public static class PermissionAlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog permissionAlertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_gps_disconnected_24)
                    .setTitle(R.string.text_GPSfaild)
                    .setMessage(R.string.text_ShouldGrant)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.abs_alert_return, this)
                    .create();
            permissionAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button posbtn = permissionAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    posbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return permissionAlertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dialog.dismiss();
                default:
                    break;

            }
        }
    }
}
