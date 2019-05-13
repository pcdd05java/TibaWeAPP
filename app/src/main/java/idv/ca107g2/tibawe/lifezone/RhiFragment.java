package idv.ca107g2.tibawe.lifezone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import idv.ca107g2.tibawe.tools.CirclePagerIndicatorDecoration;
import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.Renting_House_Information_VO;

/**
 * A simple {@link Fragment} subclass.
 */
public class RhiFragment extends Fragment {
    private static final String TAG = "RhiFragment";
    private CommonTask getRhiTask;
    private List<Renting_House_Information_VO> rhiList;
    private RecyclerView rhiRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rhiRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rhiRecycler.setLayoutManager(layoutManager);
        rhiRecycler.addItemDecoration(new CirclePagerIndicatorDecoration());

        findRhi();

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rhiRecycler);


        return rhiRecycler;

    }

    public void findRhi() {


        String url = Util.URL + "Renting_House_Information_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getallRhitext");
        String jsonOut = jsonObject.toString();
//
        if (Util.networkConnected(getActivity())) {
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
                Util.showToast(getContext(), R.string.msg_nodata);
            } else {

                int imageSize = getResources().getDisplayMetrics().widthPixels;

                RhiAdapter adapter = new RhiAdapter(rhiList, imageSize);
                rhiRecycler.setAdapter(adapter);
                adapter.setListener(new RhiAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Renting_House_Information_VO rhiVO = rhiList.get(position);

                        Intent intent = new Intent(getActivity(), RhiDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("rhiVO", rhiVO);
                        intent.putExtras(bundle);
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
