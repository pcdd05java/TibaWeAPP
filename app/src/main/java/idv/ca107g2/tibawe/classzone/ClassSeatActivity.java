package idv.ca107g2.tibawe.classzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;

public class ClassSeatActivity extends AppCompatActivity {

    private static final String TAG = "ClassSeatActivity";
    private CommonTask classSeatTask;

    private TextView tvcsClassName, cvtvSeatName;
    private String memberaccount, class_no, className;
    private String[] seatArray;
    private Map classSeatData;

    private RecyclerView rvClassSeat;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_seat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = this.getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");
        class_no = preferences.getString("class_no", "");

        rvClassSeat = findViewById(R.id.rvClassSeat);

        tvcsClassName = findViewById(R.id.tvcsClassName);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.VERTICAL);
        rvClassSeat.setLayoutManager(layoutManager);

        findSeat(class_no);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);
    }

    public void findSeat(String class_no) {

        String url = Util.URL + "ClassaServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "querySeat");
        jsonObject.addProperty("memberAccount", memberaccount);
        jsonObject.addProperty("class_no", class_no);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            classSeatTask = new CommonTask(url, jsonOut);
            try {
                String result = classSeatTask.execute().get();
                Type collectionTypeMap = new TypeToken<Map>() {
                }.getType();
                classSeatData = gson.fromJson(result, collectionTypeMap);

                className = gson.fromJson(classSeatData.get("className").toString(), String.class);

                seatArray = gson.fromJson(classSeatData.get("seatArray").toString(), String[].class);

Log.d("className", className);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (seatArray.length==0) {
                Util.showToast(this, R.string.msg_nodata);

            } else {
                tvcsClassName.setText(className);
                ClassSeatAdapter classSeatadapter = new ClassSeatAdapter(seatArray);
                rvClassSeat.setAdapter(classSeatadapter);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

}
