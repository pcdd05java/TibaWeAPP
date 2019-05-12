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
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsApplyQueryFragment extends Fragment {

    private static final String TAG = "AbsQueryFragment";
    private CommonTask absQueryTask;
    private String memberaccount, membername, class_no;
    private RecyclerView rvABSquery;
    private List<Map<String, String>> absList;
    private TextView abs_result, abs_records;



    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");
        membername = preferences.getString("membername", "");
        class_no = preferences.getString("class_no", "");


        View view = inflater.inflate(R.layout.fragment_abs_query, container, false);

        rvABSquery = view.findViewById(R.id.rvABSquery);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvABSquery.setLayoutManager(layoutManager);
        abs_result = view.findViewById(R.id.abs_result);

        findAbsQuery();

        return view;

    }

    @Override
    public void onResume() {
        ((AbsApplyActivity)getActivity()).pager.getAdapter().getItemPosition(POSITION_NONE);

        super.onResume();
    }

    public void findAbsQuery() {

        String url = Util.URL + "AbsenceServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "queryAbs");
        jsonObject.addProperty("memberaccount", memberaccount);
        jsonObject.addProperty("class_no", class_no);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            absQueryTask = new CommonTask(url, jsonOut);
            try {
                String result = absQueryTask.execute().get();
                Type collectionType = new TypeToken<List<Map<String, String>>>() {
                }.getType();
                absList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (absList.isEmpty()) {
                abs_result.setText(R.string.msg_clrr_norecord);
            } else {
                rvABSquery.setAdapter(new AbsApplyAdapter(absList));
            }

        } else {
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }

}
