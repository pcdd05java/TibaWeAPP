package idv.ca107g2.tibawe.campuszone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;

public class AbsenceApplyActivity extends AppCompatActivity {
    private static TextView tvApplyDate;
    private static int year, month, day, hour, minute;
    private Spinner spAbsenceType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_apply);
        tvApplyDate = findViewById(R.id.tvApplyDate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinner();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

    }

    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        spAbsenceType = findViewById(R.id.spAbsenceType);
        String plsSelect = getString(R.string.msg_selectAbsenceType);
        String[] absenceType = {plsSelect, "事假", "病假", "公假", "娩假", "喪假"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, absenceType);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAbsenceType.setAdapter(adapter);
        spAbsenceType.setSelection(0, true);
        spAbsenceType.setOnItemSelectedListener(listener);
    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
            TextView tvSelected = (TextView)adapter.getDropDownView(position, view, parent);
            if(position == 0){
                Util.showToast(AbsenceApplyActivity.this, parent.getItemAtPosition(position).toString());
                tvSelected.setTextColor(getResources().getColor(R.color.colorDarkRed));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Util.showToast(AbsenceApplyActivity.this, R.string.msg_selectAbsenceType);
        }
    };

    // 按下「日期」按鈕
    public void onClickSelectDate(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        FragmentManager fm = getSupportFragmentManager();
        datePickerFragment.show(fm, "datePicker");
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

    // 將指定的日期顯示在TextView上
    private static void updateInfo() {
        tvApplyDate.setText(new StringBuilder().append(year).append("\n")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(month + 1)).append("/").append(parseNum(day)).append(" "));
//                .append(parseNum(hour)).append(":").append(parseNum(minute)));
    }

    // 若數字有十位數，直接顯示；若只有個位數則補0後再顯示。例如7會改成07後再顯示
    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
