package idv.ca107g2.tibawe.classzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.ClassInformationVO;

public class ClassInformationActivity extends AppCompatActivity {

    private static final String TAG = "CampusNewsActivity";
    private CommonTask getClassNewsTask;
    private List<ClassInformationVO> classInformationList;
    private RecyclerView infoRecycler;
    private String class_no;
    SharedPreferences preferences;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        class_no = preferences.getString("class_no", "");

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

        findInfos();


    }

    public void findInfos() {

        String url = Util.URL + "ClassInformationServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getclass");
        jsonObject.addProperty("class_no", class_no);

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getClassNewsTask = new CommonTask(url, jsonOut);
            try {
                String result = getClassNewsTask.execute().get();
                Type collectionType = new TypeToken<List<ClassInformationVO>>() {
                }.getType();
                classInformationList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (classInformationList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            } else {

                infoRecycler = findViewById(R.id.rvClassNews);

                ClassInformationAdapter adapter = new ClassInformationAdapter(classInformationList);
                infoRecycler.setAdapter(adapter);
//                adapter.setListener(new ClassInformationAdapter.Listener() {
//                    @Override
//                    public void onClick(int position) {
//                        Intent intent = new Intent(ClassInformationActivity.this, StoreDetailActivity.class);
////                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//                    }
//                });

                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                infoRecycler.setLayoutManager(layoutManager);
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
