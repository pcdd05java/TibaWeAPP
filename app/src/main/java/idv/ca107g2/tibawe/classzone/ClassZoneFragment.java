package idv.ca107g2.tibawe.classzone;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassZoneFragment extends Fragment {
    String memberType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView infoRecycler =
                (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);

        memberType = preferences.getString("memberType", "");


        if(memberType.equals("1")) {
            int[] infoTitles = new int[ClassZone.CLASS_ZONES.length];
            for(int i = 0 ; i <infoTitles.length; i++){
                infoTitles[i] = ClassZone.CLASS_ZONES[i].getInfoTitle();
        }

            int[] infoPics = new int[ClassZone.CLASS_ZONES.length];
            for(int i = 0; i<infoPics.length; i++){
                infoPics[i] = ClassZone.CLASS_ZONES[i].getInfoPicId();
        }

        ClassZoneAdapter adapter = new ClassZoneAdapter(infoTitles, infoPics);
        infoRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        infoRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new ClassZoneAdapter.Listener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(getActivity(), ClassInformationActivity.class);
                        getActivity().startActivity(intent0);
                        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), CourseQueryActivity.class);
                        getActivity().startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), PhoneBookActivity.class);
                        getActivity().startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), ClassSeatActivity.class);
                        getActivity().startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), CampusRuleActivity.class);
                        getActivity().startActivity(intent4);
                        break;
                }
            }
        });
        } else{
            int[] infoTitles = new int[ClassZoneTeacher.CLASS_ZONES.length];
            for(int i = 0 ; i <infoTitles.length; i++){
                infoTitles[i] = ClassZoneTeacher.CLASS_ZONES[i].getInfoTitle();
            }

            int[] infoPics = new int[ClassZoneTeacher.CLASS_ZONES.length];
            for(int i = 0; i<infoPics.length; i++){
                infoPics[i] = ClassZoneTeacher.CLASS_ZONES[i].getInfoPicId();
            }

            ClassZoneAdapter adapterTeacher = new ClassZoneAdapter(infoTitles, infoPics);
            infoRecycler.setAdapter(adapterTeacher);
            StaggeredGridLayoutManager layoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            infoRecycler.setLayoutManager(layoutManager);

            adapterTeacher.setListener(new ClassZoneAdapter.Listener() {
                @Override
                public void onClick(int position) {
                    switch (position) {
                        case 0:
                            Intent intent0 = new Intent(getActivity(), ClassInformationActivity.class);
                            getActivity().startActivity(intent0);
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            break;
                        case 1:
                            Intent intent1 = new Intent(getActivity(), CourseQueryActivity.class);
                            getActivity().startActivity(intent1);
                            break;
                        case 2:
                            Intent intent3 = new Intent(getActivity(), ClassSeatActivity.class);
                            getActivity().startActivity(intent3);
                            break;
                        case 3:
                            Intent intent4 = new Intent(getActivity(), PhoneBookActivity.class);
                            getActivity().startActivity(intent4);
                            break;
                    }
                }
            });
        }

        return infoRecycler;

    }


}
