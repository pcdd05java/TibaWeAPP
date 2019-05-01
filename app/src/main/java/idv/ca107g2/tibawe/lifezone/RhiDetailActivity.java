package idv.ca107g2.tibawe.lifezone;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import idv.ca107g2.tibawe.R;

public class RhiDetailActivity extends AppCompatActivity {
    public static final String EXTRA_INFO_ID = "infoId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        int infoId = (Integer)getIntent().getExtras().get(EXTRA_INFO_ID);
        String infoTitles = RentingHouseInformation.rentingHouseInformations[infoId].getRhiTitle();
        TextView textView = findViewById(R.id.tvInfoTitle);
        textView.setText(infoTitles);

        int infoPics = RentingHouseInformation.rentingHouseInformations[infoId].getRhiPicId();
        ImageView imageView = findViewById(R.id.ivInfoPic1);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, infoPics));

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
