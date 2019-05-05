package idv.ca107g2.tibawe.campuszone;

import android.content.Intent;
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
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.lifezone.StoreDetailActivity;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.Latest_News_VO;


public class CampusNewsActivity extends AppCompatActivity {

    private static final String TAG = "CampusNewsActivity";
    private CommonTask getLatestNewsTask;
    private List<Latest_News_VO> latest_news_list;
    private RecyclerView newsRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_news);

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

        String url = Util.URL + "Latest_News_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getall");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getLatestNewsTask = new CommonTask(url, jsonOut);
            try {
                String result = getLatestNewsTask.execute().get();
                Type collectionType = new TypeToken<List<Latest_News_VO>>() {
                }.getType();
                latest_news_list = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (latest_news_list.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_CourseNotFound);
            } else {
                newsRecycler = findViewById(R.id.rvCampusNews);

                LatestNewsAdapter adapter = new LatestNewsAdapter(latest_news_list);
                newsRecycler.setAdapter(adapter);
                adapter.setListener(new LatestNewsAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(CampusNewsActivity.this, StoreDetailActivity.class);
                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                newsRecycler.setLayoutManager(layoutManager);
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
