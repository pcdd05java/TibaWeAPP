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
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.RafVO;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RafQueryFragment extends Fragment {


    private static final String TAG = "RafQueryFragment";
    private CommonTask rafQueryTask;
    private String memberaccount, membername, class_no;
    private RecyclerView rvRafquery;
    private List<RafVO> rafVOList;
    private TextView raf_result, raf_records;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");
        membername = preferences.getString("membername", "");
        class_no = preferences.getString("class_no", "");


        View view = inflater.inflate(R.layout.fragment_raf_query, container, false);

        rvRafquery = view.findViewById(R.id.rvRafquery);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvRafquery.setLayoutManager(layoutManager);
        raf_result = view.findViewById(R.id.raf_result);

        findRafQuery(memberaccount);

        return view;

    }

    @Override
    public void onResume() {
        ((RafActivity)getActivity()).pager.getAdapter().getItemPosition(POSITION_NONE);

        super.onResume();
    }

    public void findRafQuery(String memberaccount) {

        String url = Util.URL + "RafServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "queryRAF");
        jsonObject.addProperty("memberaccount", memberaccount);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            rafQueryTask = new CommonTask(url, jsonOut);
            try {
                String result = rafQueryTask.execute().get();
                Type collectionType = new TypeToken<List<RafVO>>() {
                }.getType();
                rafVOList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (rafVOList.isEmpty()) {
                raf_result.setText(R.string.msg_raf_norecord);
            } else {
                rvRafquery.setAdapter(new RafAdapter(rafVOList, membername));
            }

        } else {
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }


}
