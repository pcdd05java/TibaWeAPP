package idv.ca107g2.tibawe.lifezone;

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
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.Renting_House_Information_VO;

public class RhiActivity extends AppCompatActivity {

    private static final String TAG = "RhiActivity";
    private CommonTask getRhiTask;
    private List<Renting_House_Information_VO> rhiList;
    private RecyclerView rhiRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

        findRhi();
    }

    public void findRhi() {


        String url = Util.URL + "Renting_House_Information_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getallRhitext");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getRhiTask = new CommonTask(url, jsonOut);
            try {
                String result = getRhiTask.execute().get();
                Type collectionType = new TypeToken<List<Renting_House_Information_VO>>() {
                }.getType();
                rhiList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (rhiList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            } else {
                rhiRecycler = findViewById(R.id.rvRhi);
                int imageSize = getResources().getDisplayMetrics().widthPixels;

                RhiAdapter adapter = new RhiAdapter(rhiList, imageSize);
                rhiRecycler.setAdapter(adapter);
                adapter.setListener(new RhiAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Renting_House_Information_VO rhiVO = rhiList.get(position);

                        Intent intent = new Intent(RhiActivity.this, RhiDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("rhiVO", rhiVO);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rhiRecycler.setLayoutManager(layoutManager);
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
