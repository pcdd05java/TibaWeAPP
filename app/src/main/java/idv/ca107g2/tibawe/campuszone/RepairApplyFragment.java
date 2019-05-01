package idv.ca107g2.tibawe.campuszone;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepairApplyFragment extends Fragment {
    private static TextView tvRepairApplicant;
    private Spinner spRepairLoc, spRepairObject, spRepairDescribe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair_apply, container, false);

        spRepairLoc = view.findViewById(R.id.spRepairLoc);
        spRepairObject = view.findViewById(R.id.spRepairObject);
        spRepairDescribe = view.findViewById(R.id.spRepairDescribe);

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        tvRepairApplicant = view.findViewById(R.id.tvRepairApplicant);
        tvRepairApplicant.setText(preferences.getString("membername",""));

        spinner();

        return view;
    }


    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        String[] repairLoc = {"106會議室", "107會議室", "205教室", "206教室", "309教室"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, repairLoc);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepairLoc.setAdapter(adapterLoc);
        spRepairLoc.setSelection(0, true);
        spRepairLoc.setOnItemSelectedListener(listener);

        // 直接由程式碼動態產生Spinner做法
        String[] repairObj = {"主機", "螢幕", "滑鼠", "鍵盤", "硬碟機", "光碟機", "軟碟機", "USB", "RAM", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterObj = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, repairObj);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterObj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepairObject.setAdapter(adapterObj);
        spRepairObject.setSelection(0, true);
        spRepairObject.setOnItemSelectedListener(listener);

        // 直接由程式碼動態產生Spinner做法
        String[] repairDescribe = {"無法開機", "當機", "無法上網", "不正常顯示", "無法廣播", "無音效", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterDecribe = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, repairDescribe);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterDecribe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepairDescribe.setAdapter(adapterDecribe);
        spRepairDescribe.setSelection(0, true);
        spRepairDescribe.setOnItemSelectedListener(listener);

    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
//            TextView tvSelected = (TextView)adapter.getDropDownView(position, view, parent);
//            if(position == 0){
//                Util.showToast(getActivity(), parent.getItemAtPosition(position).toString());
//                tvSelected.setTextColor(getResources().getColor(R.color.colorDarkRed));
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Util.showToast(getActivity(), R.string.msg_selectRepairLoc);
        }
    };


}
