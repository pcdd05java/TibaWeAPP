package idv.ca107g2.tibawe.campuszone;


import android.content.SharedPreferences;
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

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.ClrrVO;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClrrQueryFragment extends Fragment {

    private static final String TAG = "ClrrQueryFragment";
    private CommonTask clrrQueryTask;
    private String memberaccount, membername, class_no;
    private RecyclerView rvCLRRquery;
    private List<ClrrVO> clrrVOList;
    private TextView clrr_result, clrr_records;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");
        membername = preferences.getString("membername", "");
        class_no = preferences.getString("class_no", "");


        View view = inflater.inflate(R.layout.fragment_clrr_query, container, false);

        rvCLRRquery = view.findViewById(R.id.rvCLRRquery);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvCLRRquery.setLayoutManager(layoutManager);
        clrr_result = view.findViewById(R.id.clrr_result);

        findClrrQuery(memberaccount);

        return view;

    }

    @Override
    public void onResume() {
        ((ClrrActivity)getActivity()).pager.getAdapter().getItemPosition(POSITION_NONE);

        super.onResume();
    }

    public void findClrrQuery(String memberaccount) {

        String url = Util.URL + "ClrrServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "queryCLRR");
        jsonObject.addProperty("memberaccount", memberaccount);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            clrrQueryTask = new CommonTask(url, jsonOut);
            try {
                String result = clrrQueryTask.execute().get();
                Type collectionType = new TypeToken<List<ClrrVO>>() {
                }.getType();
                clrrVOList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (clrrVOList.isEmpty()) {
                clrr_result.setText(R.string.msg_clrr_norecord);
            } else {
                rvCLRRquery.setAdapter(new ClrrAdapter(clrrVOList, membername));
            }

        } else {
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }

}
