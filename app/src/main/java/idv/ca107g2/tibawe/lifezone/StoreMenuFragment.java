package idv.ca107g2.tibawe.lifezone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import idv.ca107g2.tibawe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreMenuFragment extends Fragment {
    private final static String TAG = "RhiRecyclerVIewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView storeRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

        String[] storeTitles = new String[StoreMenu.storeMenus.length];
        for(int i = 0 ; i <storeTitles.length; i++){
            storeTitles[i] = StoreMenu.storeMenus[i].getStoreTitle();
        }


        int[] storePics = new int[StoreMenu.storeMenus.length];
        for(int i = 0; i<storePics.length; i++){
            storePics[i] = StoreMenu.storeMenus[i].getStorePicId();
        }

        StoreMenuAdapter adapter = new StoreMenuAdapter(storeTitles, storePics);
        storeRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        storeRecycler.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(storeRecycler);

        adapter.setListener(new StoreMenuAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), StoreMenuDetailActivity.class);
                intent.putExtra(StoreMenuDetailActivity.EXTRA_INFO_ID, position);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        return storeRecycler;

    }
}
