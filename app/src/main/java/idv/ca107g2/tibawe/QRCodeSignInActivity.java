package idv.ca107g2.tibawe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import idv.ca107g2.tibawe.vo.AttendanceVO;
import idv.ca107g2.tibawe.vo.ScheduleVO;

public class QRCodeSignInActivity extends AppCompatActivity{

    SharedPreferences preferences;
    AttendanceVO attendanceVO;
    ScheduleVO scheduleVO;
    int msg_code;

    private static final String TAG = "QRCodeSignInActivity";
    private CommonTask lastQRCheckTask;


//
//    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    TextView qr_result, tvQRDate, tvQRInterval, tvQRCourse, tvQRTime;

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

        msg_code = getIntent().getIntExtra("msg_code", 0);

        switch (msg_code){
            case 0:
                qr_result.setText(R.string.msg_qrfailed);
                break;
            case 1:
                qr_result.setText(R.string.msg_qr_invalid_date);
                break;
            case 2:
                qr_result.setText(R.string.msg_qr_no_need);
                break;
            case 3:
                qr_result.setText(R.string.msg_qr_invalid_delayed);
                break;
            case 4:
                qr_result.setText(R.string.msg_qrsuccess);
                break;
            case 5:
                qr_result.setText(R.string.msg_qralready);
                break;
            case 6:
                qr_result.setText(R.string.msg_qr_invalid_interval);
                break;
            case 7:
                qr_result.setText(R.string.msg_qr_norecord);
                break;
        }
    }


    public void lastQRCheck() {
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
                Type collectionType = new TypeToken<Map>() {
                }.getType();
                Map lastCourse = gson.fromJson(result, collectionType);

//                JSONObject jsonObj = new JSONObject(lastCourse.get("attendanceVO").toString());
//		          String qrecord = jsonObj.getString("qrecord");
//                Util.showToast(this, qrecord);

                attendanceVO = gson.fromJson(lastCourse.get("attendanceVO").toString(), AttendanceVO.class);
//                Util.showToast(this, attendanceVO.getQrecord().toString());

                scheduleVO = gson.fromJson(lastCourse.get("scheduleVO").toString(), ScheduleVO.class);
//                Util.showToast(this, scheduleVO.getSdate().toString());


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

        if (attendanceVO == null || scheduleVO == null) {
            msg_code = 7;
            qr_result.setText(R.string.msg_qr_norecord);
        } else {
            tvQRDate.setText(scheduleVO.getSdate().toString());
            switch ((scheduleVO.getInterval())) {
                case 1:
                    tvQRInterval.setText("上午");
                    break;
                case 2:
                    tvQRInterval.setText("下午");
                    break;
                case 3:
                    tvQRInterval.setText("夜間");
                    break;
            }
            tvQRCourse.setText(scheduleVO.getSubjectName());
            tvQRTime.setText(attendanceVO.getQrecord().toString());
        }
        Util.showToast(this, Util.msgCode(msg_code));
    }




    @Override
    public void onBackPressed(){
       super.onBackPressed();
       overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);
    }
}
