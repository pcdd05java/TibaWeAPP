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
public class RafFragment extends Fragment {
    private static final String TAG = "RafFragment";
    private static TextView tvRafApplicant, tvRafClassNo;
    private Spinner spRafLoc, spRafType, spRafCon;
    private SpinnerTask spinnerTask;
    private CommonTask addRafTask;
    private EditText edRafNote;
    ArrayAdapter<String> adapterLoc, adapterType,adapterCon;
    private String memberaccount, raf_type, raf_con, cr_no, class_no;
    private Button btnRafApply, btnRafCancel;
    private List<CrVO> crList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raf, container, false);

        spRafLoc = view.findViewById(R.id.spRafLoc);
        spRafType = view.findViewById(R.id.spRafType);
        spRafCon = view.findViewById(R.id.spRafCon);
        edRafNote = view.findViewById(R.id.edRafNote);
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
        tvRafApplicant = view.findViewById(R.id.tvRafApplicant);
        tvRafApplicant.setText(preferences.getString("membername",""));
        tvRafClassNo = view.findViewById(R.id.tvRafClassNo);
        tvRafClassNo.setText(preferences.getString("className",""));
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
                spRafLoc.setAdapter(adapterLoc);
                spRafLoc.setSelection(0, true);
                spRafLoc.setOnItemSelectedListener(listener);
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
        String[] rafType = {"主機", "螢幕", "滑鼠", "鍵盤", "硬碟機", "光碟機", "軟碟機", "USB", "RAM", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterType = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, rafType);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRafType.setAdapter(adapterType);
        spRafType.setSelection(0, true);
        spRafType.setOnItemSelectedListener(listener);
        raf_type = adapterType.getItem(0);

        // 直接由程式碼動態產生Spinner做法
        String[] rafCon = {"無法開機", "當機", "無法上網", "不正常顯示", "無法廣播", "無音效", "其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterCon = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, rafCon);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterCon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRafCon.setAdapter(adapterCon);
        spRafCon.setSelection(0, true);
        spRafCon.setOnItemSelectedListener(listener);
        raf_con = adapterCon.getItem(0);
    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterLoc)){
                cr_no = crList.get(parent.getSelectedItemPosition()).getCr_no();
            }
            if(parent.getAdapter().equals(adapterType)){
                raf_type = parent.getItemAtPosition(position).toString();
            }
            if(parent.getAdapter().equals(adapterCon)) {
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
            jsonObject.addProperty("raf_note", edRafNote.getText().toString());

            String jsonOut = jsonObject.toString();
            addRafTask = new CommonTask(url, jsonOut);
            try {
                addRafTask.execute().get();

                Util.showToast(getActivity(), R.string.msg_raf_success);
                resetRaf();
                ((RafActivity)getActivity()).pager.getAdapter().notifyDataSetChanged();
                ((RafActivity)getActivity()).pager.setCurrentItem(1);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

    }

    public void resetRaf(){
        spRafLoc.setSelection(0, true);
        spRafType.setSelection(0, true);
        spRafCon.setSelection(0, true);
        edRafNote.setText(null);
    }

}
