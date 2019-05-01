package idv.ca107g2.tibawe.lifezone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import idv.ca107g2.tibawe.campuszone.LatestNews;
import idv.ca107g2.tibawe.campuszone.LatestNewsAdapter;
import idv.ca107g2.tibawe.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotArticleFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView hotRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

        String[] newsTitles = new String[LatestNews.latestNews.length];
        for(int i = 0 ; i <newsTitles.length; i++){
            newsTitles[i] = LatestNews.latestNews[i].getNewsTitle();
        }


        int[] newsPics = new int[LatestNews.latestNews.length];
        for(int i = 0; i<newsPics.length; i++){
            newsPics[i] = LatestNews.latestNews[i].getNewsPicId();
        }


        String[] newsContents = new String[LatestNews.latestNews.length];
        for(int i = 0 ; i <newsContents.length; i++){
            newsContents[i] = LatestNews.latestNews[i].getNewsContent();
        }

        LatestNewsAdapter adapter = new LatestNewsAdapter(newsTitles, newsPics, newsContents);
        hotRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        hotRecycler.setLayoutManager(layoutManager);

//        adapter.setListener(new LatestNewsAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                Intent intent = new Intent(getActivity(), RhiDetailActivity.class);
//                intent.putExtra(RhiDetailActivity.EXTRA_INFO_ID, position);
//                getActivity().startActivity(intent);
//            }
//        });
        return hotRecycler;

    }
}
