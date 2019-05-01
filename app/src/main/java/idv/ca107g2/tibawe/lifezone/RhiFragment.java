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
public class RhiFragment extends Fragment {
    private final static String TAG = "RhiRecyclerVIewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView rhiRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

        String[] rhiTitles = new String[RentingHouseInformation.rentingHouseInformations.length];
        for(int i = 0 ; i <rhiTitles.length; i++){
            rhiTitles[i] = RentingHouseInformation.rentingHouseInformations[i].getRhiTitle();
        }


        int[] rhiPics = new int[RentingHouseInformation.rentingHouseInformations.length];
        for(int i = 0; i<rhiPics.length; i++){
            rhiPics[i] = RentingHouseInformation.rentingHouseInformations[i].getRhiPicId();
        }

        RhiAdapter adapter = new RhiAdapter(rhiTitles, rhiPics);
        rhiRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rhiRecycler.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rhiRecycler);

        adapter.setListener(new RhiAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), RhiDetailActivity.class);
                intent.putExtra(RhiDetailActivity.EXTRA_INFO_ID, position);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        return rhiRecycler;

    }
}
