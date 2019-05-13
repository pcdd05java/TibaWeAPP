package idv.ca107g2.tibawe.lifezone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.StoreInformationVO;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInformationFragment extends Fragment {
    private static final String TAG = "StoreInfoFragment";
    private CommonTask getStoreTask;
    private List<StoreInformationVO> storeInformationList;
    private RecyclerView storeRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storeRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);


//        StoreInformationAdapter adapter = new StoreInformationAdapter(storeTitles, storePics);
//        storeRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        storeRecycler.setLayoutManager(layoutManager);

        findStore();

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(storeRecycler);


        return storeRecycler;

    }

    public void findStore() {


        String url = Util.URL + "StoreInformationServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getallStoreInfotext");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
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
                Util.showToast(getContext(), R.string.msg_nodata);
            } else {

                int imageSize = getResources().getDisplayMetrics().widthPixels;

                StoreInformationAdapter adapter = new StoreInformationAdapter(storeInformationList, imageSize);
                storeRecycler.setAdapter(adapter);
                adapter.setListener(new StoreInformationAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        StoreInformationVO storeInformationVO = storeInformationList.get(position);

                        Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("storeInformationVO", storeInformationVO);
                        intent.putExtras(bundle);
//                        intent.putExtra(StoreDetailActivity.EXTRA_DETAIL_POSITION, position);
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
