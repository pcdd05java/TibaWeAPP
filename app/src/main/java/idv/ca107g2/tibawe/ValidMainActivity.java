package idv.ca107g2.tibawe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qrcore.util.QRScannerHelper;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Map;

import idv.ca107g2.tibawe.classzone.CourseQueryFragment;
import idv.ca107g2.tibawe.lifezone.HotArticleFragment;
import idv.ca107g2.tibawe.task.CommonTask;

public class ValidMainActivity extends AppCompatActivity {
    Menu menu;
    SharedPreferences preferences;
    String qrtime_date, class_no, memberaccount, nowDateString;
    java.sql.Date nowDate;
    int msg_code;

    private static int hr,min;
    private static final String TAG = "CourseQueryFragment";
    private CommonTask havetoCheckTask, qrCheckTask;




    boolean login;

    private boolean hasCameraPermission = true;
    private QRScannerHelper mScannerHelper;
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

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
                        AlertFragment alertFragment = new AlertFragment();
                        FragmentManager fm = getSupportFragmentManager();
                        alertFragment.show(fm, "alert");
           }

                return true;
            }
        });


        ValidMainPagerAdapter pagerAdapter = new ValidMainPagerAdapter(getSupportFragmentManager());
        ViewPager pager =  findViewById(R.id.vpMain);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        TabLayout tabLayout = findViewById(R.id.tbMain);
        tabLayout.setupWithViewPager(pager);

        initQRScanner();

        isQRCode();

    }


    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                qrtime_date = result;
            }
        });
    }



    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

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
                    getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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

    public void isQRCode(){
        if(getIntent().getBooleanExtra("isQRCode", false)){
            havetoCheckNow();
        }
    }

    public void onClickQRCode(View view){
        havetoCheckNow();
    }

    public void havetoCheckNow(){
        Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        String url = Util.URL + "AttendanceServlet";
        class_no =  preferences.getString("class_no", "");
        memberaccount = preferences.getString("memberaccount","");
        nowDate= new java.sql.Date(System.currentTimeMillis());
        nowDateString = nowDate.toString();

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
                    openCamera();
                }else if(!havetoCheckNow){
                    msg_code = 2;
                }else if(isChecked){
                        msg_code = 5;
                }

                Intent intent = new Intent(this, QRCodeSignInActivity.class);
                intent.putExtra("msg_code", msg_code);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

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

            qrCheck();
            Intent intent = new Intent(this, QRCodeSignInActivity.class);
            intent.putExtra("msg_code", msg_code);
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
                    return new HotArticleFragment();
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
                    return getResources().getText(R.string.hot_article);
                case 1:
                    return getResources().getText(R.string.validmain_menu_tab);
                case 2:
                    return getResources().getText(R.string.course_query_quick);
            }
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        if(login){
            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);}


    }
}
