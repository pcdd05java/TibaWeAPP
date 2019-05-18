package idv.ca107g2.tibawe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.Map;

import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;

public class QRCodeSignInActivity extends AppCompatActivity{

    SharedPreferences preferences;

    int msg_code;
    String distance;

    private static final String TAG = "QRCodeSignInActivity";
    private CommonTask lastQRCheckTask;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    TextView qr_result, tvQRDate, tvQRInterval, tvQRCourse, tvQRTime, lastcheck_result, qr_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        preferences =this.getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);

        qrResult();

        lastQRCheck();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

    }


    public void qrResult(){

        qr_result = findViewById(R.id.qr_result);
        qr_distance = findViewById(R.id.qr_distance);

        msg_code = getIntent().getIntExtra("msg_code", 0);
        distance = getIntent().getStringExtra("distance");

        switch (msg_code){
            case 0:
                qr_result.setText(R.string.msg_qr_0_failed);
                break;
            case 1:
                qr_result.setText(R.string.msg_qr_1_invalid_date);
                break;
            case 2:
                qr_result.setText(R.string.msg_qr_2_no_need);
                break;
            case 3:
                qr_result.setText(R.string.msg_qr_3_invalid_delayed);
                break;
            case 4:
                qr_result.setText(R.string.msg_qr_4_success);
                qr_distance.setText("您目前距學校定位"+ distance+"公尺");
                qr_distance.setVisibility(View.VISIBLE);
                break;
            case 5:
                qr_result.setText(R.string.msg_qr_5_already);
                break;
            case 6:
                qr_result.setText(R.string.msg_qr_6_invalid_interval);
                break;
            case 7:
                qr_result.setText(R.string.msg_qr_7_norecord);
                break;
            case 8:
                qr_result.setText(R.string.msg_qr_8_cancel);
                break;
            case 9:
                qr_result.setText(R.string.msg_qr_9_toofar);
                qr_distance.setText("您目前距學校定位"+ distance+"公尺");
                qr_distance.setVisibility(View.VISIBLE);
        }
//        Util.showToast(this, Util.msgCode(msg_code));
    }

    public void lastQRCheck() {
        Map lastCourse = null;
        lastcheck_result = findViewById(R.id.lastcheck_result);
        tvQRDate = findViewById(R.id.tvQRDate);
        tvQRInterval = findViewById(R.id.tvQRInterval);
        tvQRCourse = findViewById(R.id.tvQRCourse);
        tvQRTime = findViewById(R.id.tvQRTime);

        String url = Util.URL + "AttendanceServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "lastQRCheckTask");
        jsonObject.addProperty("memberaccount", preferences.getString("memberaccount", ""));

        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            lastQRCheckTask = new CommonTask(url, jsonOut);
            try {
                String result = lastQRCheckTask.execute().get();
                Log.e("time", result);
                Type collectionType = new TypeToken<Map>() {
                }.getType();
                lastCourse = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

        if (lastCourse.get("qrecord").toString().isEmpty()) {
            msg_code = 7;
            lastcheck_result.setText(R.string.msg_qr_7_norecord);
            lastcheck_result.setVisibility(View.VISIBLE);
        } else {
//            tvQRDate.setText(lastCourse.get("sdate").toString());
            tvQRDate.setText((String)lastCourse.get("sdate"));
            String interval = (String)lastCourse.get("interval");
            switch (interval) {
                case "1":
                    tvQRInterval.setText("上午");
                    break;
                case "2":
                    tvQRInterval.setText("下午");
                    break;
                case "3":
                    tvQRInterval.setText("夜間");
                    break;
            }
            tvQRCourse.setText((String)lastCourse.get("subjectName"));
            tvQRTime.setText((String)lastCourse.get("qrecord"));
        }
    }

    @Override
    public void onBackPressed(){
       super.onBackPressed();
       overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);
    }
}
