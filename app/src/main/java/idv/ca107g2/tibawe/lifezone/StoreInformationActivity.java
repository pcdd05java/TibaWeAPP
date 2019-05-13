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
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.StoreInformationVO;

public class StoreInformationActivity  extends AppCompatActivity {

    private static final String TAG = "StoreInfoActivity";
    private CommonTask getStoreTask;
    private List<StoreInformationVO> storeInformationList;
    private RecyclerView storeRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

        findStore();
    }

    public void findStore() {


        String url = Util.URL + "StoreInformationServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getallStoreInfotext");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getStoreTask = new CommonTask(url, jsonOut);
            try {
                String result = getStoreTask.execute().get();
                Type collectionType = new TypeToken<List<StoreInformationVO>>() {
                }.getType();
                storeInformationList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (storeInformationList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            } else {
                storeRecycler = findViewById(R.id.rvStoreInfo);
                int imageSize = getResources().getDisplayMetrics().widthPixels;

                StoreInformationAdapter adapter = new StoreInformationAdapter(storeInformationList, imageSize);
                storeRecycler.setAdapter(adapter);
                adapter.setListener(new StoreInformationAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(StoreInformationActivity.this, StoreDetailActivity.class);
//                        intent.putExtra(StoreDetailActivity.EXTRA_DETAIL_ID, position);
                        Bundle bundle = new Bundle();
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                storeRecycler.setLayoutManager(layoutManager);
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
