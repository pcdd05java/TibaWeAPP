package idv.ca107g2.tibawe.campuszone;


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
import idv.ca107g2.tibawe.lifezone.StoreDetailActivity;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.Latest_News_VO;


/**
 * A simple {@link Fragment} subclass.
 */
public class LatestNewsFragment extends Fragment {

    private static final String TAG = "LatestNewsFragment";
    private CommonTask getLatestNewsTask;
    private List<Latest_News_VO> latest_news_list;
    private RecyclerView newsRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);


//        LatestNewsAdapter adapter = new LatestNewsAdapter(newsTitles, newsPics, newsContents);
//        newsRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        newsRecycler.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(newsRecycler);

        findNews();

//        adapter.setListener(new LatestNewsAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                Intent intent = new Intent(getActivity(), RhiDetailActivity.class);
//                intent.putExtra(RhiDetailActivity.EXTRA_INFO_ID, position);
//                getActivity().startActivity(intent);
//            }
//        });
        return newsRecycler;

    }

    public void findNews() {

        String url = Util.URL + "Latest_News_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getall");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
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
                Util.showToast(getContext(), R.string.msg_nodata);
            } else {

                LatestNewsAdapter adapter = new LatestNewsAdapter(latest_news_list);
                newsRecycler.setAdapter(adapter);
                adapter.setListener(new LatestNewsAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
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
