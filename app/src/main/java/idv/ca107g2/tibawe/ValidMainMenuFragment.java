package idv.ca107g2.tibawe;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import idv.ca107g2.tibawe.tools.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ValidMainMenuFragment extends Fragment {

    private String class_no;
    SharedPreferences preferences;
    String memberType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validmain_menu, container, false);

        preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);

        memberType = preferences.getString("memberType", "");


        if(!memberType.equals("1")){
            FrameLayout frameCampusMenu = (FrameLayout) view.findViewById(R.id.frameCampusMenu);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameCampusMenu.getLayoutParams();
            params.setMargins(100, 0, 100, 0);
            frameCampusMenu.setLayoutParams(params);

            FrameLayout frameClassMenu = (FrameLayout) view.findViewById(R.id.frameClassMenu);
            frameClassMenu.setLayoutParams(params);

            FrameLayout frameLifeMenu = (FrameLayout) view.findViewById(R.id.frameLifeMenu);
            frameLifeMenu.setLayoutParams(params);
        }

        return view;

    }

}
