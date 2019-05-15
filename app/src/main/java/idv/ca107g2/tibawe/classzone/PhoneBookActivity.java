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
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.MemberVO;

public class PhoneBookActivity extends AppCompatActivity {

    private static final String TAG = "PhoneBookActivity";
    private CommonTask getPhoneBookTask;
    private List<MemberVO> memberVOlist;
    private RecyclerView rvPhonebook;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

        findNews();
    }

    public void findNews() {
        SharedPreferences preferences =this.getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);

        String url = Util.URL + "MemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getPhoneBook");
        jsonObject.addProperty("class_no", preferences.getString("class_no", ""));

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getPhoneBookTask = new CommonTask(url, jsonOut);
            try {
                String result = getPhoneBookTask.execute().get();
                Type collectionType = new TypeToken<List<MemberVO>>() {
                }.getType();
                memberVOlist = gson.fromJson(result, collectionType);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (memberVOlist.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            } else {
                MemberVO memberVO = memberVOlist.get(0);
                TextView tvpbClass = findViewById(R.id.tvpbClass);
                if(!memberVO.getClass_no().isEmpty()) {
                    tvpbClass.setText(memberVO.getClass_no()); }else{
                    tvpbClass.setText("---");}

                rvPhonebook = findViewById(R.id.rvPhonebook);

                PhoneBookAdapter adapter = new PhoneBookAdapter(memberVOlist);
                rvPhonebook.setAdapter(adapter);
//                adapter.setListener(new LatestNewsAdapter.Listener() {
//                    @Override
//                    public void onClick(int position) {
//                        Intent intent = new Intent(CampusNewsActivity.this, StoreDetailActivity.class);
////                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//                    }
//                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rvPhonebook.setLayoutManager(layoutManager);
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
