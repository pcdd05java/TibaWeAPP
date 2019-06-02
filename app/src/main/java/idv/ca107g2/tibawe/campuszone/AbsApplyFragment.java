package idv.ca107g2.tibawe.campuszone;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsApplyFragment extends Fragment {
    private static final String TAG = "AbsApplyFragment";
    private static TextView tvApplyDate, tvAbsCourse, tvAbsTeacher1, tvAbsTeacher2, tvAbsTeacher3;

    private Spinner spAbsReason, spAbsInterval;
    private static int year, month, day, hour, minute;
    private FloatingActionButton fbtnSelectDate;
    private static EditText edAbsNote;
    private int absInterval, absReason, interval;
    private boolean isCourseExist = false;

    private ArrayAdapter<String> adapterReason;
    private static ArrayAdapter<String> adapterInterval;

    private String absCourse, absTeacher1, absTeacher2, absTeacher3, absDate;
    private static String memberaccount, cr_no, class_no;

    private Button btnAbsSubmit, btnAbsCancel, btnAbsMagic;
    private static CommonTask absApplyTask, absCourseTask;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_abs_apply, container, false);

        spAbsReason = view.findViewById(R.id.spAbsReason);
        spAbsInterval = view.findViewById(R.id.spAbsInterval);
        tvApplyDate = view.findViewById(R.id.tvApplyDate);
        tvAbsCourse = view.findViewById(R.id.tvAbsCourse);
        tvAbsTeacher1 = view.findViewById(R.id.tvAbsTeacher1);
        tvAbsTeacher2 = view.findViewById(R.id.tvAbsTeacher2);
        tvAbsTeacher3 = view.findViewById(R.id.tvAbsTeacher3);
        fbtnSelectDate = view.findViewById(R.id.fbtnSelectDate);

        edAbsNote = view.findViewById(R.id.edAbsNote);
        btnAbsMagic = view.findViewById(R.id.btnAbsMagic);
        btnAbsSubmit = view.findViewById(R.id.btnAbsSubmit);
        btnAbsCancel = view.findViewById(R.id.btnAbsCancel);
        btnAbsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCourseExist) {
                    applyAbs();
                }else{
                    Util.showToast(getActivity(), R.string.abs_msg_nocourse);
                }
            }
        });
        btnAbsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAbsApply();
            }
        });

        //神奇小按鈕
        btnAbsMagic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edAbsNote.setText("今天我生日！！");
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        memberaccount= preferences.getString("memberaccount","");
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
        spinner();
        showRightNow();

        AbsApplyActivity activity = (AbsApplyActivity)getActivity();
        Bundle results = activity.passBundle();
        if(results.getString("absCourse")!=null) {
            absCourse = results.getString("absCourse");
            if (results.getString("absTeacher1") != null) {
                absTeacher1 = results.getString("absTeacher1");
            }
            if (results.getString("absTeacher2") != null) {
                absTeacher2 = results.getString("absTeacher2");
            }
            if (results.getString("absTeacher3") != null) {
                absTeacher3 = results.getString("absTeacher3");
            }
            absDate = results.getString("absDate");
            interval = results.getInt("interval");

            tvApplyDate.setText(absDate);

            absInterval = interval;
            spAbsInterval.setSelection(absInterval, true);

            tvAbsCourse.setText(absCourse);
            if(absTeacher1!= null){
                tvAbsTeacher1.setText(absTeacher1);
            }else {
                tvAbsTeacher1.setText("---");
            }
            if(absTeacher2 != null){
                tvAbsTeacher2.setText(absTeacher2);
                tvAbsTeacher2.setVisibility(View.VISIBLE);
            }else {
                tvAbsTeacher2.setVisibility(View.INVISIBLE);
                tvAbsTeacher2.setText("---");
            }
            if(absTeacher3 != null){
                tvAbsTeacher3.setText(absTeacher3);
                tvAbsTeacher3.setVisibility(View.VISIBLE);
            }else {
                tvAbsTeacher3.setVisibility(View.INVISIBLE);
                tvAbsTeacher3.setText("---");
            }
        }
    }



    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        String[] absenceReason = {"事假", "公假", "病假", "喪假","其他"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterReason = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, absenceReason);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAbsReason.setAdapter(adapterReason);
        spAbsReason.setSelection(0, true);
        spAbsReason.setOnItemSelectedListener(listener);

        // 直接由程式碼動態產生Spinner做法
        String[] absenceInterval = {"請選擇時段", "上午", "下午", "夜間"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterInterval = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, absenceInterval);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAbsInterval.setAdapter(adapterInterval);
        spAbsInterval.setSelection(0, true);
        spAbsInterval.setOnItemSelectedListener(listener);
    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterReason)){
                absReason = parent.getSelectedItemPosition()+1;
//                if(absReason==6){
//                    edAbsNote.setFocusable(true);
//                    edAbsNote.requestFocus();
//                }
            }
            if(parent.getAdapter().equals(adapterInterval)){
                absInterval = parent.getSelectedItemPosition();
                if(absInterval!=0){
                    findAbsCourse();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void findAbsCourse(){
        btnAbsSubmit.setVisibility(View.VISIBLE);
        java.sql.Date nowDate= new java.sql.Date(System.currentTimeMillis());
        if((tvApplyDate.getText().toString()).equals("請選擇日期")){
            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getFragmentManager();
            alertFragment.show(fm, "alert");
        }
        if (Util.networkConnected(getActivity())) {
            String url = Util.URL + "ScheduleServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findAbsCourse");
            jsonObject.addProperty("sdate", (String)tvApplyDate.getText());
            jsonObject.addProperty("interval", absInterval);
            jsonObject.addProperty("class_no", class_no);
            jsonObject.addProperty("memberaccount", memberaccount);

            String jsonOut = jsonObject.toString();
            absCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = absCourseTask.execute().get();
                Type collectionType = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> absCourseData = gson.fromJson(result, collectionType);

                isCourseExist = Boolean.valueOf(absCourseData.get("isCourseExist"));
                if(!isCourseExist){
                    isCourseExist =false;
                    edAbsNote.setText(R.string.abs_msg_nocourse);
                    edAbsNote.setTextColor(Color.rgb(209, 8, 55));
//                    btnAbsSubmit.setClickable(false);
//                    btnAbsSubmit.setTextColor(R.color.colorDarkBlue);
                    edAbsNote.setText(null);
                    tvAbsCourse.setText("");
                    tvAbsTeacher1.setText("");
                    tvAbsTeacher2.setText("");
                    tvAbsTeacher3.setText("");
                }else if(Boolean.valueOf(absCourseData.get("alreadyAbs"))){
                    isCourseExist =false;
//                    Util.showToast(getActivity(), R.string.abs_msg_alreadyAbs);
                    btnAbsSubmit.setVisibility(View.GONE);
                    edAbsNote.setText(R.string.abs_msg_alreadyAbs);
                    edAbsNote.setTextColor(Color.rgb(209, 8, 55));
                }else {
                    edAbsNote.setText(null);
                    edAbsNote.setTextColor(Color.rgb(0, 0, 0));
//                    btnAbsSubmit.setClickable(true);
                        tvAbsCourse.setText(absCourseData.get("subjectName"));
                        if(absCourseData.get("teacherName1") != null){
                            tvAbsTeacher1.setText(absCourseData.get("teacherName1"));
                        }else {
                            tvAbsTeacher1.setText("---");
                        }
                        if(absCourseData.get("teacherName2") != null){
                            tvAbsTeacher2.setText(absCourseData.get("teacherName2"));
                            tvAbsTeacher2.setVisibility(View.VISIBLE);
                        }else {
                            tvAbsTeacher2.setVisibility(View.INVISIBLE);
                            tvAbsTeacher2.setText("---");
                        }
                        if(absCourseData.get("teacherName3") != null){
                            tvAbsTeacher3.setText(absCourseData.get("teacherName3"));
                            tvAbsTeacher3.setVisibility(View.VISIBLE);
                        }else {
                            tvAbsTeacher3.setVisibility(View.INVISIBLE);
                            tvAbsTeacher3.setText("---");
                        }
                    }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_event_24b)
                    .setTitle("日期提醒")
                    //設定訊息內容
                    .setMessage(R.string.abs_alert_msg)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.abs_alert_return, this)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button btnReturn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnReturn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }
    }

    public void applyAbs(){

        if (Util.networkConnected(getActivity())) {
            String url = Util.URL + "AbsenceServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "applyABS");
            jsonObject.addProperty("memberaccount", memberaccount);
            jsonObject.addProperty("sdate", (String)tvApplyDate.getText());
            jsonObject.addProperty("absinterval", absInterval);
            if(absReason==0){
                absReason=1;
            }
            jsonObject.addProperty("absReason", absReason);
            jsonObject.addProperty("note", edAbsNote.getText().toString());


            String jsonOut = jsonObject.toString();
            absApplyTask = new CommonTask(url, jsonOut);
            try {
                String result = absApplyTask.execute().get();

               Util.showToast(getActivity(), "假單已送出");
                resetAbsApply();

                ((AbsApplyActivity)getActivity()).pager.getAdapter().notifyDataSetChanged();
                ((AbsApplyActivity)getActivity()).pager.setCurrentItem(1);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    public void resetAbsApply(){
        btnAbsSubmit.setVisibility(View.VISIBLE);
        showRightNow();
        spAbsReason.setSelection(0, true);
        spAbsInterval.setSelection(0, true);
        edAbsNote.setText(null);
        edAbsNote.setTextColor(Color.rgb(0, 0, 0));
        tvAbsCourse.setText("");
        tvAbsTeacher1.setText("");
        tvAbsTeacher2.setText("");
        tvAbsTeacher3.setText("");

    }

    private static void showRightNow() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
//        updateInfo();
    }

    // 將指定的日期顯示在TextView上
    private static void updateInfo() {
        tvApplyDate.setText(new StringBuilder().append(year).append("-")
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
            datePickerDialog.getDatePicker();

            return datePickerDialog;
        }

        @Override
        // 日期挑選完成會呼叫此方法，並傳入選取的年月日
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m;
            day = d;
            updateInfo();
            Util.showToast(getActivity(), R.string.abs_msg_select_interval);
        }
    }

}
