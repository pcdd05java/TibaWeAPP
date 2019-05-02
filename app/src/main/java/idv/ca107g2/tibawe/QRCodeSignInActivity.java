package idv.ca107g2.tibawe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

public class QRCodeSignInActivity extends AppCompatActivity{
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

    }


    @Override
    public void onBackPressed(){
        if(preferences.getBoolean("login", true)) {
            Intent intent = new Intent(QRCodeSignInActivity.this, ValidMainActivity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.swipeback_stack_to_front,
                    R.anim.swipeback_stack_right_out);
        }else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}
