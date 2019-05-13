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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.vo.Latest_News_VO;


/**
 * A simple {@link Fragment} subclass.
 */
public class DBDFragment extends Fragment {

    private static final String TAG = "DBDFragment";
    private CommonTask getHotArticleTask;
    private List<Latest_News_VO> latest_news_list;
    private RecyclerView hotRecycler;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hotRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(hotRecycler);

        findNews();

//        adapter.setListener(new LatestNewsAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                Intent intent = new Intent(getActivity(), zRhiDetailActivity.class);
//                intent.putExtra(zRhiDetailActivity.EXTRA_INFO_ID, position);
//                getActivity().startActivity(intent);
//            }
//        });
        return hotRecycler;

    }

    public void findNews() {

        String url = Util.URL + "Latest_News_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getHotArticleTask = new CommonTask(url, jsonOut);
            try {
                String result = getHotArticleTask.execute().get();
                Type collectionType = new TypeToken<List<Latest_News_VO>>() {
                }.getType();
                latest_news_list = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (latest_news_list.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(getContext(), R.string.msg_CourseNotFound);
            } else {

                DBDAdapter adapter = new DBDAdapter(latest_news_list);
                hotRecycler.setAdapter(adapter);
                adapter.setListener(new DBDAdapter.Listener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
//                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                hotRecycler.setLayoutManager(layoutManager);

            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }
}
