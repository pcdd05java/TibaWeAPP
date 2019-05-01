package idv.ca107g2.tibawe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import idv.ca107g2.tibawe.task.CommonTask;

public class BeforeMainActivity extends AppCompatActivity {
    private CommonTask isMemberTask;
    private static final String TAG = "BeforMainActivity";
    private ImageView ivLogoIcon, ivLogoTiba, ivLogoWe, ivLogoTibaweCH, ivLogoTop, ivLogoRight, ivLogoRightBottom, ivLogoLeftBottom, ivLogoLeft;
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_main);
        ivLogoIcon = findViewById(R.id.ivLogoIcon);
        ivLogoTiba = findViewById(R.id.ivLogoTiba);
        ivLogoWe = findViewById(R.id.ivLogoWe);
        ivLogoTibaweCH = findViewById(R.id.ivLogoTibaweCH);
        ivLogoTop = findViewById(R.id.ivLogoTop);
        ivLogoRight = findViewById(R.id.ivLogoRight);
        ivLogoRightBottom = findViewById(R.id.ivLogoRightBottom);
        ivLogoLeftBottom = findViewById(R.id.ivLogoLeftBottom);
        ivLogoLeft = findViewById(R.id.ivLogoLeft);
        ViewGroup container = findViewById(R.id.container);

        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;
            if (view.getId() == R.id.ivLogoIcon){
                viewAnimator = ViewCompat.animate(view)
                        .alpha(0.5f)
                        .setDuration(1500);
            }else if (view.getId() == R.id.ivLogoTiba){
                viewAnimator = ViewCompat.animate(view)
                        .alpha(1)
                        .setDuration(1500);
            }else if (view.getId() == R.id.ivLogoTibaweCH){
                viewAnimator = ViewCompat.animate(view)
                        .translationY(+80).alpha(1)
                        .setStartDelay(300)
                        .setDuration(1000);
            }else if (view.getId() == R.id.ivLogoWe){
                viewAnimator = ViewCompat.animate(view)
                        .alpha(1)
                        .setStartDelay(600)
                        .setDuration(1500);
            }else if (view.getId() == R.id.ivLogoTop){
                viewAnimator = ViewCompat.animate(view)
                        .setStartDelay(700)
                        .alpha(1);
            }else if (view.getId() == R.id.ivLogoRight){
                viewAnimator = ViewCompat.animate(view)
                        .setStartDelay(1100)
                        .alpha(1);
            }else if (view.getId() == R.id.ivLogoRightBottom){
                viewAnimator = ViewCompat.animate(view)
                        .setStartDelay(1400)
                        .alpha(1);
            }else if (view.getId() == R.id.ivLogoLeftBottom){
                viewAnimator = ViewCompat.animate(view)
                        .setStartDelay(1700)
                        .alpha(1);
            }else{
                viewAnimator = ViewCompat.animate(view)
                        .setStartDelay(2000)
                        .setDuration(100)
                        .alpha(1);
                ViewCompat.animate(view).setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        checkLogin();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
            }
            viewAnimator.setInterpolator(new DecelerateInterpolator(1.5f)).start();
        }
    }

    private void checkLogin(){

        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);

        boolean login = preferences.getBoolean("login", false);

        if (login) {
            String member_ID = preferences.getString("member_ID", "");
            String memberpass = preferences.getString("memberpass", "");
            if (isMember(member_ID, memberpass)) {
                Util.showToast(this, R.string.login_auto);
                setResult(RESULT_OK);
                Intent intent = new Intent(BeforeMainActivity.this, ValidMainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }

        }else{
            Intent intent = new Intent(BeforeMainActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();

        }
    }

    private boolean isMember(final String member_ID, final String memberpass) {
        boolean isMember = false;
        if (Util.networkConnected(this)) {
            String url = Util.URL + "MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("member_ID", member_ID);
            jsonObject.addProperty("memberpass", memberpass);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();
                Type collectionType = new TypeToken<Map>() {
                }.getType();
                Map data = gson.fromJson(result, collectionType);
                isMember = Boolean.valueOf(data.get("isMember").toString());
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
    protected void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }
}
