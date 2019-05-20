package idv.ca107g2.tibawe;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.MemberVO;

public class MainActivity extends AppCompatActivity {
    private Dialog loginDialog;
    private TextView tvOK, tvCancel;
    private Button btnOK, btnCancel;
    private static final String TAG = "MainActivity";
    private TextView tvMessage;
    private EditText edAccount, edPassword;
    private CommonTask isMemberTask;
    private MemberVO memberVO;
    private Boolean isQRCode;
    private String className;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        // Menu item click的監聽事件一樣要設定在setSupportActionBar之後才有作用
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_login:
                        isQRCode = false;
                        loginCheck(isQRCode);
                        break;
                }
                return true;
            }
        });

    }

    private Dialog loginCheck(final Boolean isQRCode){

        loginDialog = new Dialog(MainActivity.this);
        loginDialog.setTitle(getString(R.string.login_tab));
        // 使用者無法自行取消對話視窗，需要進行操作才行
        loginDialog.setCancelable(true);
        loginDialog.setContentView(R.layout.dialog_login);
        Toolbar toolbar = loginDialog.findViewById(R.id.dialog_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login_tab);
        edAccount = loginDialog.findViewById(R.id.edAccount);
        edPassword = loginDialog.findViewById(R.id.edPassword);

        //神奇小按鈕
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_magic:
                        edAccount.setText("M000004");
                        edPassword.setText("123456");
                        break;
                }
                return true;
            }
        });

        tvMessage = loginDialog.findViewById(R.id.tvMessage);


        // 透過myDialog.getWindow()取得這個對話視窗的Window物件
        Window dialogWindow = loginDialog.getWindow();
                /*
                    設定對話視窗位置：
                    當参數值包含Gravity.LEFT時，對話視窗出現在左邊
                    當参數值包含Gravity.RIGHT時，對話視窗出現在右邊
                    當参數值包含Gravity.TOP時，對話視窗出現在上邊,
                    當参數值包含Gravity.BOTTOM時，對話視窗出現在下邊
                    當参數值包含Gravity.CENTER_HORIZONTAL時，對話視窗水平居中
                    當参數值包含Gravity.CENTER_VERTICAL時，對話視窗垂直居中
                    gravity的默認值為Gravity.CENTER，即Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
                 */
        dialogWindow.setGravity(Gravity.CENTER);

                 /*
                    設定對話視窗大小：
                    呼叫getAttributes()，取得LayoutParams物件即可進行屬性設定
                    相關屬性：
                    x：X座標
                    y：Y座標
                    width：寬度
                    height：高度
                    alpha：透明度 (0.0 ～ 1.0)
                 */
//                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                        lp.width = 1000;
//                        lp.alpha = 1.0f;
//                        dialogWindow.setAttributes(lp);

                /*
                    將對話視窗的大小依螢幕大小的百分比設置
                 */

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        p.height = (int) (d.getHeight() * 0.6); // 高度設置為螢幕的0.6 (60%)
        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
        dialogWindow.setAttributes(p);


        // 取得自訂對話視窗上的所有元件都需透過myDialog才能findViewById
        btnOK = loginDialog.findViewById(R.id.btnOK);
//        tvOK.setShadowLayer(2, 0, 0, Color.Gr);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memberaccount = edAccount.getText().toString().trim();
                String memberpass = edPassword.getText().toString().trim();
                if (memberaccount.length() <= 0 || memberpass.length() <= 0) {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                    return;
                }

                if (isMember(memberaccount, memberpass)) {
                    SharedPreferences preferences = getSharedPreferences(
                            Util.PREF_FILE, MODE_PRIVATE);
                    preferences.edit().putBoolean("login", true)
                            .putString("memberaccount", memberaccount)
                            .putString("membername", memberVO.getMemberName())
                            .putString("class_no", memberVO.getClass_no())
                            .putString("className", className)
                            .putString("memberaccount", memberVO.getMemberAccount())
                            .putString("memberType", String.valueOf(memberVO.getMemberType()))
                            .putString("memberpass", memberpass).apply();

                    setResult(RESULT_OK);
                    if(!isQRCode){
                        Toast.makeText(MainActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ValidMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                        loginDialog.cancel();
                    } else{
                        Intent intent = new Intent(MainActivity.this, ValidMainActivity.class);
                        intent.putExtra("isQRCode", isQRCode);
                        startActivity(intent);
                        finish();
                        loginDialog.cancel();
                    }

                } else {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                }
            }
        });

        btnCancel = loginDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.logout_cancel_text, Toast.LENGTH_SHORT).show();
                loginDialog.cancel();
            }
        });
        // 小心！一定要記得show()
        loginDialog.show();

        return loginDialog;
    }
    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_top_main, menu);
        return true;
    }


    public void onClickQRCode(View view){
        isQRCode = true;
        Toast.makeText(this, R.string.msg_loginbeforeQRCode, Toast.LENGTH_SHORT).show();
        loginCheck(isQRCode);
    }


      private boolean isMember(final String memberaccount, final String memberpass) {
        boolean isMember = false;

        if (Util.networkConnected(this)) {
            String url = Util.URL + "MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("memberaccount", memberaccount);
            jsonObject.addProperty("memberpass", memberpass);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();

                Type collectionType = new TypeToken<Map>() {
                }.getType();
                Map data = gson.fromJson(result, collectionType);

                memberVO = gson.fromJson(data.get("memberVO").toString(), MemberVO.class);
                className = data.get("className").toString();
                isMember = Boolean.valueOf(data.get("isMember").toString());
//                Util.showToast(this, String.valueOf(isMember));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isMember = false;
            }
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
        return isMember;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
