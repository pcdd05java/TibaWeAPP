package idv.ca107g2.tibawe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
    private static final String CHANNEL_ID = "id";
    private static final String CHANNEL_NAME = "name";
    private NotificationManager manager;

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

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


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
                createNotification();
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


    private void createNotification() {
        Intent intent = new Intent(QRCodeSignInActivity.this, ValidMainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("title", "從通知訊息切換過來的");
//        bundle.putString("content", "老師在你背後，他很火！");
//        intent.putExtras(bundle);

        /*
            Intent指定好要幹嘛後，就去做了，如startActivity(intent);
            而PendingIntent則是先把某個Intent包好，以後再去執行Intent要幹嘛
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(QRCodeSignInActivity.this
                , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Uri uri = Uri.parse(URL);
//        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(
//                MainActivity.this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_icon_transparent);

//        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
//                android.R.drawable.ic_menu_share, "Go!", pendingIntent2
//        ).build();

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        Notification notification = builder
                // 訊息面板的標題
                .setContentTitle("簽到成功")
                // 訊息面板的內容文字
                .setContentText("今天也很準時哪，又是活力滿滿的一天！")
//                // 訊息的小圖示
                .setSmallIcon(R.drawable.icons8_checked_24)
                // 訊息的大圖示
                .setLargeIcon(bitmap)
                // 使用者點了之後才會執行指定的Intent
                .setContentIntent(pendingIntent)
                // 加入音效
                .setSound(soundUri)
                // 點擊後會自動移除狀態列上的通知訊息
                .setAutoCancel(true)
//                // 加入狀態列下拉後的進一步操作
//                .addAction(action)
                .build();

        manager.notify(1, notification);
    }

    @Override
    public void onBackPressed(){
       super.onBackPressed();
       overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);
    }
}
