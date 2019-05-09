package idv.ca107g2.tibawe.campuszone;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.SpinnerTask;
import idv.ca107g2.tibawe.vo.CrVO;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepairApplyFragment extends Fragment {
    private static final String TAG = "RepairApplyFragment";
    private static TextView tvRepairApplicant, tvRepairClassNo;
    private Spinner spRepairLoc, spRepairObject, spRepairDescribe;
    private SpinnerTask spinnerTask;
    private CommonTask addRafTask;
    private EditText edRepairNote;
    ArrayAdapter<String> adapterLoc, adapterObj,adapterDecribe;
    private String memberaccount, raf_type, raf_con, cr_no, class_no;
    private Button btnRafApply, btnRafCancel;
    private List<CrVO> crList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair_apply, container, false);

        spRepairLoc = view.findViewById(R.id.spRepairLoc);
        spRepairObject = view.findViewById(R.id.spRepairObject);
        spRepairDescribe = view.findViewById(R.id.spRepairDescribe);
        edRepairNote = view.findViewById(R.id.edRepairNote);
        btnRafApply = view.findViewById(R.id.btnRafApply);
        btnRafCancel = view.findViewById(R.id.btnRafCancel);
        btnRafApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyRaf();
            }
        });
        btnRafCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRaf();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        tvRepairApplicant = view.findViewById(R.id.tvRepairApplicant);
        tvRepairApplicant.setText(preferences.getString("membername",""));
        tvRepairClassNo = view.findViewById(R.id.tvRepairClassNo);
        tvRepairClassNo.setText(preferences.getString("className",""));
        memberaccount = preferences.getString("memberaccount","");
        class_no = preferences.getString("class_no","");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(getActivity())) {
            spinnerTask = new SpinnerTask();
            try {
                crList = spinnerTask.execute().get();
                List<String> CrVO = new ArrayList<>();
                for (int i = 0; i < crList.size(); i++) {
                    CrVO.add(crList.get(i).getCr_name());
                }
                adapterLoc = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, CrVO);
                adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRepairLoc.setAdapter(adapterLoc);
                spRepairLoc.setSelection(0, true);
                spRepairLoc.setOnItemSelectedListener(listener);
                cr_no = crList.get(0).getCr_no();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        spinner();

    }
    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        String[] repairObj = {"主機", "螢幕", "滑鼠", "鍵盤", "硬碟機", "光碟機", "軟碟機", "USB", "RAM", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterObj = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, repairObj);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterObj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepairObject.setAdapter(adapterObj);
        spRepairObject.setSelection(0, true);
        spRepairObject.setOnItemSelectedListener(listener);
        raf_type = adapterObj.getItem(0);

        // 直接由程式碼動態產生Spinner做法
        String[] repairDescribe = {"無法開機", "當機", "無法上網", "不正常顯示", "無法廣播", "無音效", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterDecribe = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, repairDescribe);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterDecribe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepairDescribe.setAdapter(adapterDecribe);
        spRepairDescribe.setSelection(0, true);
        spRepairDescribe.setOnItemSelectedListener(listener);
        raf_con = adapterObj.getItem(0);
    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterLoc)){
                cr_no = crList.get(parent.getSelectedItemPosition()).getCr_no();
            }
            if(parent.getAdapter().equals(adapterObj)){
                raf_type = parent.getItemAtPosition(position).toString();
            }
            if(parent.getAdapter().equals(adapterDecribe)) {
                raf_con = parent.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void applyRaf(){

        if (Util.networkConnected(getActivity())) {
            String url = Util.URL + "RafServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "addRaf");
            jsonObject.addProperty("memberaccount", memberaccount);
            jsonObject.addProperty("class_no", class_no);
            jsonObject.addProperty("cr_no", cr_no);
            jsonObject.addProperty("raf_type", raf_type);
            jsonObject.addProperty("raf_con", raf_con);
            jsonObject.addProperty("raf_note", edRepairNote.getText().toString());

            String jsonOut = jsonObject.toString();
            addRafTask = new CommonTask(url, jsonOut);
            try {
                String result = addRafTask.execute().get();

                Util.showToast(getActivity(), result);
                resetRaf();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

    }

    public void resetRaf(){
        spRepairLoc.setSelection(0, true);
        spRepairObject.setSelection(0, true);
        spRepairDescribe.setSelection(0, true);
        edRepairNote.setText(null);
    }

}
