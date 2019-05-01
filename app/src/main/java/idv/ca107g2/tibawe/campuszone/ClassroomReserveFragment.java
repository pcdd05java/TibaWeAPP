package idv.ca107g2.tibawe.campuszone;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassroomReserveFragment extends Fragment {
    private static TextView tvClassroomReserveApplicant, tvClassroomReserveDate;
    private Spinner spClassroomNo, spClassroomReserveStart, spClassroomReserveEnd;
    private static int year, month, day;
    private FloatingActionButton fbtnSelectDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classroom_reserve, container, false);

        spClassroomNo = view.findViewById(R.id.spClassroomNo);
        spClassroomReserveStart = view.findViewById(R.id.spClassroomReserveStart);
        spClassroomReserveEnd = view.findViewById(R.id.spClassroomReserveEnd);
        tvClassroomReserveApplicant = view.findViewById(R.id.tvClassroomReserveApplicant);
        tvClassroomReserveDate = view.findViewById(R.id.tvClassroomReserveDate);
        fbtnSelectDate = view.findViewById(R.id.fbtnSelectDate);

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        tvClassroomReserveApplicant.setText(preferences.getString("membername",""));

        spinner();
        showRightNow();


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


    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        String[] classroomNo = {"106會議室", "107會議室", "205教室", "206教室", "309教室"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterClassroom = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, classroomNo);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterClassroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassroomNo.setAdapter(adapterClassroom);
        spClassroomNo.setSelection(0, true);
        spClassroomNo.setOnItemSelectedListener(listener);

        String[] classroomReserveStart = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterReserveStart = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, classroomReserveStart);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterReserveStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassroomReserveStart.setAdapter(adapterReserveStart);
        spClassroomReserveStart.setSelection(0, true);
        spClassroomReserveStart.setOnItemSelectedListener(listener);

        String[] classroomReserveEnd = {"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapterReserveEnd = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, classroomReserveEnd);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterReserveEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassroomReserveEnd.setAdapter(adapterReserveEnd);
        spClassroomReserveEnd.setSelection(0, true);
        spClassroomReserveEnd.setOnItemSelectedListener(listener);


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
