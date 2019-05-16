package idv.ca107g2.tibawe.lifezone;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.DBDMemberVO;
import idv.ca107g2.tibawe.vo.DBDOderVO;
import idv.ca107g2.tibawe.vo.StoreInformationVO;
import idv.ca107g2.tibawe.vo.StoreMenuVO;

public class DBDQueryActivity extends AppCompatActivity {
    private static final String TAG = "DBDQueryActivity";
    private CommonTask dbdQueryTask;
    private Button btnChangeDBD;
//    private Dialog changeDBDDialog;
    private TextView cvtvQueryDBDDate, cvtvQueryDBDclass,cvtvQueryDBDhost,
        cvtvQueryDBDstore, cvtvQueryDBDItem, cvtvQueryDBDStatus, dbdQuery_result;
    private String memberaccount;
    private RecyclerView dbdQueryRecycler;

    private Map<String, List> dbdQueryMap;
    private List<DBDMemberVO> dbdMemberQuertlist;
    private List<Map<String, String>> dbdQueryhostDatalist;
    private List<DBDOderVO> dbdOderQueryVOlist;
    private List<StoreMenuVO> dbdStoreMenuQueryVOlist;
    private List<StoreInformationVO> dbdQuerystoreInformationVOlist;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbd_query);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = this.getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");

        dbdQueryRecycler = findViewById(R.id.rvDBDQuery);
        dbdQuery_result = findViewById(R.id.dbdQuery_result);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        dbdQueryRecycler.setLayoutManager(layoutManager);

        findDBDQuery(memberaccount);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);
    }

    public void findDBDQuery(String memberaccount) {

        String url = Util.URL + "DBDMemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "queryDBD");
        jsonObject.addProperty("memberAccount", memberaccount);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            dbdQueryTask = new CommonTask(url, jsonOut);
            try {
                String result = dbdQueryTask.execute().get();
                Type collectionTypeMap = new TypeToken<Map<String, List>>() {
                }.getType();
                dbdQueryMap = gson.fromJson(result, collectionTypeMap);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (dbdQueryMap.isEmpty()) {
                Util.showToast(this, R.string.msg_nodata);
                dbdQuery_result.setVisibility(View.VISIBLE);
                dbdQueryRecycler.setVisibility(View.GONE);
            } else {
                Type collectionTypeListDBDMemberVO = new TypeToken<List<DBDMemberVO>>() {
                }.getType();
                dbdMemberQuertlist = gson.fromJson(dbdQueryMap.get("dbdMemberQuertlist").toString(), collectionTypeListDBDMemberVO);

                Type collectionTypeListMap = new TypeToken<List<Map>>() {
                }.getType();
                dbdQueryhostDatalist = gson.fromJson(dbdQueryMap.get("dbdQueryhostDatalist").toString(), collectionTypeListMap);

                Type collectionTypeListDBDOderVO = new TypeToken<List<DBDOderVO>>() {
                }.getType();
                dbdOderQueryVOlist = gson.fromJson(dbdQueryMap.get("dbdOderQueryVOlist").toString(), collectionTypeListDBDOderVO);

                Type collectionTypeListStoreMenuVO = new TypeToken<List<StoreMenuVO>>() {
                }.getType();
                dbdStoreMenuQueryVOlist = gson.fromJson(dbdQueryMap.get("dbdStoreMenuQueryVOlist").toString(), collectionTypeListStoreMenuVO);

                Type collectionTypeListStoreInformationVO = new TypeToken<List<StoreInformationVO>>() {
                }.getType();
                dbdQuerystoreInformationVOlist = gson.fromJson(dbdQueryMap.get("dbdQuerystoreInformationVOlist").toString(), collectionTypeListStoreInformationVO);

                DBDQueryAdapter dbdQueryadapter = new DBDQueryAdapter(dbdMemberQuertlist, dbdQueryhostDatalist,
                        dbdOderQueryVOlist, dbdStoreMenuQueryVOlist, dbdQuerystoreInformationVOlist);
                dbdQueryRecycler.setAdapter(dbdQueryadapter);
                dbdQueryadapter.setListener(new DBDQueryAdapter.Listener() {

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(int position) {

                        Util.showToast(DBDQueryActivity.this, "功能開發中");
//                        openchangeDBDDialog(position);

                    }
                });
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }
}