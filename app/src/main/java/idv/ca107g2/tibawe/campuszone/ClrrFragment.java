package idv.ca107g2.tibawe.campuszone;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.SpinnerTask;
import idv.ca107g2.tibawe.vo.CrVO;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClrrFragment extends Fragment {
    private static final String TAG = "CrReserveFragment";
    private static TextView tvClassroomReserveApplicant, tvClassroomReserveClassNo, tvClassroomReserveDate;
    private Spinner spClassroomNo, spClassroomReserveStart, spClassroomReserveEnd;
    private static int year, month, day;
    private FloatingActionButton fbtnSelectDate;
    private SpinnerTask spinnerTask;
    private ArrayAdapter<String> adapterClassroom, adapterReserveStart, adapterReserveEnd;
    private String memberaccount, clrr_sttime, clrr_endtime, cr_no, class_no;
    private Button btnCLRRApply, btnCLRRCancel;
    private CommonTask applyCLRRTask;
    private List<CrVO> crList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clrr, container, false);

        spClassroomNo = view.findViewById(R.id.spClassroomNo);
        spClassroomReserveStart = view.findViewById(R.id.spClassroomReserveStart);
        spClassroomReserveEnd = view.findViewById(R.id.spClassroomReserveEnd);
        tvClassroomReserveApplicant = view.findViewById(R.id.tvClassroomReserveApplicant);
        tvClassroomReserveClassNo = view.findViewById(R.id.tvClassroomReserveClassNo);
        tvClassroomReserveDate = view.findViewById(R.id.tvClassroomReserveDate);
        fbtnSelectDate = view.findViewById(R.id.fbtnSelectDate);
        btnCLRRApply = view.findViewById(R.id.btnCLRRApply);
        btnCLRRCancel = view.findViewById(R.id.btnCLRRCancel);
        btnCLRRApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCLRR();
            }
        });
        btnCLRRCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCLRR();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        tvClassroomReserveApplicant.setText(preferences.getString("membername",""));
        tvClassroomReserveClassNo.setText(preferences.getString("className",""));
        memberaccount = preferences.getString("memberaccount","");
        class_no = preferences.getString("class_no","");

        fbtnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentManager fmDate = getFragmentManager();
                datePickerFragment.show(fmDate, "datePicker");
            }
        });

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
                adapterClassroom = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, CrVO);
                adapterClassroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spClassroomNo.setAdapter(adapterClassroom);
                spClassroomNo.setSelection(0, true);
                spClassroomNo.setOnItemSelectedListener(listener);
                cr_no = crList.get(0).getCr_no();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        spinner();
        showRightNow();
    }


    public void spinner(){
//        // 直接由程式碼動態產生Spinner做法

        String[] classroomReserveStart = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterReserveStart = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, classroomReserveStart);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterReserveStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassroomReserveStart.setAdapter(adapterReserveStart);
        spClassroomReserveStart.setSelection(0, true);
        spClassroomReserveStart.setOnItemSelectedListener(listener);
        clrr_sttime = adapterReserveStart.getItem(0);

        String[] classroomReserveEnd = {"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterReserveEnd = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, classroomReserveEnd);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterReserveEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassroomReserveEnd.setAdapter(adapterReserveEnd);
        spClassroomReserveEnd.setSelection(0, true);
        spClassroomReserveEnd.setOnItemSelectedListener(listener);
        clrr_endtime = adapterReserveEnd.getItem(0);

    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterClassroom)){
                cr_no = crList.get(parent.getSelectedItemPosition()).getCr_no();
            }
            if(parent.getAdapter().equals(adapterReserveStart)){
                clrr_sttime = parent.getItemAtPosition(position).toString();
            }
            if(parent.getAdapter().equals(adapterReserveEnd)){
                clrr_endtime = parent.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void applyCLRR(){

        if (Util.networkConnected(getActivity())) {
            String url = Util.URL + "ClrrServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "applyCLRR");
            jsonObject.addProperty("memberaccount", memberaccount);
            jsonObject.addProperty("class_no", class_no);
            jsonObject.addProperty("clrr_date", tvClassroomReserveDate.getText().toString());
            jsonObject.addProperty("cr_no", cr_no);
            jsonObject.addProperty("clrr_sttime", clrr_sttime);
            jsonObject.addProperty("clrr_endtime", clrr_endtime);

            String jsonOut = jsonObject.toString();
            applyCLRRTask = new CommonTask(url, jsonOut);
            try {
               applyCLRRTask.execute();

               Util.showToast(getActivity(), R.string.msg_clrr_success);
               resetCLRR();
                ((ClrrActivity)getActivity()).pager.getAdapter().notifyDataSetChanged();
                ((ClrrActivity)getActivity()).pager.setCurrentItem(1);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    public void resetCLRR(){
        showRightNow();
        spClassroomNo.setSelection(0, true);
        spClassroomReserveStart.setSelection(0, true);
        spClassroomReserveEnd.setSelection(0, true);
    }

    private static void showRightNow() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateInfo();
    }

    // 將指定的日期顯示在TextView上
    private static void updateInfo() {
        tvClassroomReserveDate.setText(new StringBuilder().append(year).append("-")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(month + 1)).append("-").append(parseNum(day)));
    }

    // 若數字有十位數，直接顯示；若只有個位數則補0後再顯示。例如7會改成07後再顯示
    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        // 改寫此方法以提供Dialog內容
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // 建立DatePickerDialog物件
            // this為OnDateSetListener物件
            // year、month、day會成為日期挑選器預選的年月日
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            return datePickerDialog;
        }

        @Override
        // 日期挑選完成會呼叫此方法，並傳入選取的年月日
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m;
            day = d;
            updateInfo();
        }
    }
}
