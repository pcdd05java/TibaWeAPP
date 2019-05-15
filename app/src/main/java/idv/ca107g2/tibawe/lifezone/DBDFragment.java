package idv.ca107g2.tibawe.lifezone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.DBDMemberVO;
import idv.ca107g2.tibawe.vo.DBDOderVO;
import idv.ca107g2.tibawe.vo.StoreInformationVO;


/**
 * A simple {@link Fragment} subclass.
 */
public class DBDFragment extends Fragment {

    private static final String TAG = "DBDFragment";
    private CommonTask getDBDTask;
    private List<DBDOderVO> dbdOderVOlist;
    private List<StoreInformationVO> storeInformationVOlist;
    private List<List<DBDMemberVO>> dbdMemberTop3list;
    private List<String> dbdMemberCountlist;
    private List<Map<String, String>> hostDatalist;
    private List<Long> fnl_timelist;
    private RecyclerView rvDBDlist;
    private TextView lastdbd_result;
    private Map<String, List> dbdMap;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dbd, container, false);

        rvDBDlist = view.findViewById(R.id.rvDBDlist);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvDBDlist.setLayoutManager(layoutManager);
        lastdbd_result = view.findViewById(R.id.lastdbd_result);


        findDBD();

        return view;

    }

    public void findDBD() {

        String url = Util.URL + "DBDOderServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getNowDBD");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getDBDTask = new CommonTask(url, jsonOut);
            try {
                String result = getDBDTask.execute().get();
                Type collectionTypeMap = new TypeToken<Map<String, List>>() {
                }.getType();
                dbdMap = gson.fromJson(result, collectionTypeMap);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (dbdMap.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(getContext(), R.string.msg_nodata);
                lastdbd_result.setVisibility(View.VISIBLE);
                rvDBDlist.setVisibility(View.GONE);
            } else {
                Type collectionTypeListDBDOderVO = new TypeToken<List<DBDOderVO>>() {
                }.getType();
                dbdOderVOlist = gson.fromJson(dbdMap.get("dbdOderVOlist").toString(), collectionTypeListDBDOderVO);

                Type collectionTypeListStoreInformationVO = new TypeToken<List<StoreInformationVO>>() {
                }.getType();
                storeInformationVOlist = gson.fromJson(dbdMap.get("storeInformationVOlist").toString(), collectionTypeListStoreInformationVO);

                Type collectionTypeListListDBDMemberVO = new TypeToken<List<List<DBDMemberVO>>>() {
                }.getType();
                dbdMemberTop3list = gson.fromJson(dbdMap.get("dbdMemberTop3list").toString(), collectionTypeListListDBDMemberVO);

                Type collectionTypeListMap = new TypeToken<List<Map>>() {
                }.getType();
                hostDatalist = gson.fromJson(dbdMap.get("hostDatalist").toString(), collectionTypeListMap);


                Type collectionTypeListString = new TypeToken<List<String>>() {
                }.getType();
                dbdMemberCountlist = gson.fromJson(dbdMap.get("dbdMemberCountlist").toString(), collectionTypeListString);

                Type collectionTypeListLong = new TypeToken<List<Long>>() {
                }.getType();
                fnl_timelist = gson.fromJson(dbdMap.get("fnl_timelist").toString(), collectionTypeListLong);

                DBDAdapter adapter = new DBDAdapter(dbdOderVOlist, storeInformationVOlist,
                        dbdMemberTop3list, hostDatalist, dbdMemberCountlist, fnl_timelist);
                rvDBDlist.setAdapter(adapter);
                adapter.setListener(new DBDAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), DBDOrderActivity.class);
//                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }

}
