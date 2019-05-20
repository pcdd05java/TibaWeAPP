package idv.ca107g2.tibawe.campuszone;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.SpinnerTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.CrVO;
import idv.ca107g2.tibawe.vo.ScheduleVO;

public class TeachingLogActivity extends AppCompatActivity {
    private static final String TAG = "TeachingLogActivity";

    private static TextView tvTLDate;
    private TextView tvTLCourseName;
    private TextView tvLastStudent;
    private TextView tvTLTeacher1;
    private TextView tvTLTeacher2;
    private TextView tvTLTeacher3;
    private TextView tvTLClassName;
    private TextView tvTLClassRoom;
    private Spinner spTLSelectInterval;
    private CommonTask editTLTask, getCourseTask, checkTLTask;
    private EditText edAttendanceNum, edTLRecord, edTLHomework;
    private Button btnTLSubmit, btnTLRest, btnTLCheck;
    private SharedPreferences preferences;
    private String memberaccount, class_no, membername;
    private FloatingActionButton fbtnSelectDate;
    private static int year, month, nextmonth, day;
    private List<CrVO> crList;
    private Dialog selectDateDialog;
    private MaterialCalendarView calendarView;
    private List<java.util.Date> listDate;
    private List<ScheduleVO> scheduleForTLlist;
    private static ArrayAdapter<String> adapterInterval;
    private int tlInterval;
    private String courseTime_no, memberType;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teaching_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        preferences = this.getSharedPreferences(Util.PREF_FILE,
                this.MODE_PRIVATE);
        class_no = preferences.getString("class_no", "");
        memberType = preferences.getString("memberType", "");
        membername = preferences.getString("membername", "");
        memberaccount = preferences.getString("memberaccount", "");

        fbtnSelectDate = findViewById(R.id.fbtnSelectDate);
        fbtnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectDateDialog(v);
            }
        });

        findDeafult();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

    }



    public void findDeafult(){

        tvTLDate = findViewById(R.id.tvTLDate);
        tvTLCourseName = findViewById(R.id.tvTLCourseName);
        tvLastStudent = findViewById(R.id.tvLastStudent);
        tvTLTeacher1 = findViewById(R.id.tvTLTeacher1);
        tvTLTeacher2 = findViewById(R.id.tvTLTeacher2);
        tvTLTeacher3 = findViewById(R.id.tvTLTeacher3);
        tvTLClassName = findViewById(R.id.tvTLClassName);
        spTLSelectInterval = findViewById(R.id.spTLSelectInterval);
        tvTLClassRoom = findViewById(R.id.tvTLClassRoom);
        edAttendanceNum = findViewById(R.id.edAttendanceNum);
        edTLRecord = findViewById(R.id.edTLRecord);
        edTLHomework = findViewById(R.id.edTLHomework);
        btnTLSubmit = findViewById(R.id.btnTLSubmit);
        btnTLRest = findViewById(R.id.btnTLRest);
        btnTLCheck = findViewById(R.id.btnTLCheck);
        btnTLSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTL();
            }
        });
        btnTLRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTL();
            }
        });
        btnTLCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTL();
            }
        });

        tvLastStudent.setText("填寫人員　" +preferences.getString("membername",""));
        if(memberType.equals("3") || memberType.equals("4")){
            tvLastStudent.setText("審核教師　" +preferences.getString("membername",""));
        }

        tvTLClassName.setText(preferences.getString("className",""));
        if(memberType.equals("3") || memberType.equals("4")){
            tvTLClassName.setVisibility(View.GONE);
        }

        memberaccount = preferences.getString("memberaccount","");
        class_no = preferences.getString("class_no","");


        spinner();


    }
    public void resetTL(){
        edAttendanceNum.setText(null);
        edTLRecord.setText(null);
        edTLHomework.setText(null);
    }

    public void editTL(){
        if(edAttendanceNum.getText().toString().equals("") || edTLRecord.getText().toString().equals("") || edTLRecord.getText().toString().equals("")){
            Util.showToast(TeachingLogActivity.this, "請確實填寫內容");
        }else{
            String attendance = edAttendanceNum.getText().toString();
            String record = edTLRecord.getText().toString();
            String homework = edTLHomework.getText().toString();
            String memberaccount = preferences.getString("memberaccount", "");

            if (Util.networkConnected(this)) {
                String url = Util.URL + "ScheduleServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "editTL");
                jsonObject.addProperty("courseTime_no", courseTime_no);
                jsonObject.addProperty("lastStudent", memberaccount);
                jsonObject.addProperty("attendance", attendance);
                jsonObject.addProperty("record", record);
                jsonObject.addProperty("homework", homework);

                String jsonOut = jsonObject.toString();
                editTLTask = new CommonTask(url, jsonOut);
                try {
                    String result = editTLTask.execute().get();

                    if(result.equals("done")) {
                        Util.showToast(this, "已送出");
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Util.showToast(this, R.string.msg_NoNetwork);
            }
        }
    }

    public void checkTL(){
            if (Util.networkConnected(this)) {
                String url = Util.URL + "ScheduleServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "checkTL");
                jsonObject.addProperty("courseTime_no", courseTime_no);

                String jsonOut = jsonObject.toString();

                checkTLTask = new CommonTask(url, jsonOut);
                try {
                    String result = checkTLTask.execute().get();

                    if(result.equals("done")) {
                        Util.showToast(this, "已審核");
                        btnTLCheck.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Util.showToast(this, R.string.msg_NoNetwork);
            }
        }


    private void openSelectDateDialog(View v) {

        selectDateDialog = new Dialog(this);
        selectDateDialog.setCancelable(true);
        selectDateDialog.setContentView(R.layout.dialog_tl_selectdate);
        Toolbar dbdtoolbar = selectDateDialog.findViewById(R.id.toolbar_dialog_date_check);
        dbdtoolbar.setLogo(R.drawable.icons8_event_24);
        dbdtoolbar.setTitle("　選擇日期");


        Window dbddialogWindow = selectDateDialog.getWindow();
        dbddialogWindow.setGravity(Gravity.CENTER);

        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams p = dbddialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        p.height = (int) (d.getHeight() * 0.75); // 高度設置為螢幕的0.6 (60%)
        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
        dbddialogWindow.setAttributes(p);

        calendarView = selectDateDialog.findViewById(R.id.tlCalendar);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        setCalendarView(class_no,memberType,membername);


        selectDateDialog.show();
    }

    public void setCalendarView(final String class_no,final String memberType,final String membername){
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.getDate().equals(CalendarDay.today().getDate());
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorDarkRed)));
            }
        });

        year = CalendarDay.today().getYear();
        month = CalendarDay.today().getMonth();
        if(month == 11){
            nextmonth = 1;
        }else{ nextmonth = month + 2;}
        day = CalendarDay.today().getDay();

        StringBuilder begin_date = new StringBuilder().append(year).append("-")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(month + 1)).append("-").append("01");
        StringBuilder end_date = new StringBuilder().append(year).append("-")
                //「month + 1」是因為一月的值是0而非1
                .append(parseNum(nextmonth)).append("-").append("01");

        if(memberType.equals("3")){
            connectDBByTeacher(membername, begin_date.toString(), end_date.toString());}
        else{
            connectDBByClass(class_no, begin_date.toString(), end_date.toString());
        }


        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                year = date.getYear();
                month = date.getMonth();
                if(month == 11){
                    nextmonth = 1;
                }else{ nextmonth = month + 2;}
                day = date.getDay();

                StringBuilder begin_date = new StringBuilder().append(year).append("-")
                        //「month + 1」是因為一月的值是0而非1
                        .append(parseNum(month + 1)).append("-").append("01");
                StringBuilder end_date = new StringBuilder().append(year).append("-")
                        //「month + 1」是因為一月的值是0而非1
                        .append(parseNum(nextmonth)).append("-").append("01");

                if(memberType.equals("3")){
                    connectDBByTeacher(membername, begin_date.toString(), end_date.toString());
                }else{
                    connectDBByClass(class_no, begin_date.toString(), end_date.toString());
                }

            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                year = date.getYear();
                month = date.getMonth();
                day = date.getDay();

                StringBuilder selected_date = new StringBuilder().append(year).append("-")
                        //「month + 1」是因為一月的值是0而非1
                        .append(parseNum(month + 1)).append("-").append(parseNum(day));

                if(memberType.equals("3")){
                    getCoursefromDBByTeacher(membername, selected_date.toString());
                }else{
                    getCoursefromDBByClass(class_no, selected_date.toString());
                }
                selectDateDialog.cancel();

            }
        });
    }

    public void connectDBByClass(String class_no, String begin_date, String end_date){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "courseQueryForLabelByClass");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("begin_date", begin_date);
        jsonObject.addProperty("end_date", end_date);
        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<Date>>() {
                }.getType();
                listDate = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (listDate.isEmpty()) {

            } else {
                updateCalendar();
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }
    public void connectDBByTeacher(String membername, String begin_date, String end_date){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "courseQueryForLabelByTeacher");
        jsonObject.addProperty("membername", membername);
        jsonObject.addProperty("begin_date", begin_date);
        jsonObject.addProperty("end_date", end_date);
        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<Date>>() {
                }.getType();
                listDate = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (listDate.isEmpty()) {
                updateCalendar();
            } else {
                updateCalendar();
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }
    public void updateCalendar(){
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return listDate.contains(day.getDate());
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new TeachingLogActivity.CircleBackGroundSpan());
            }
        });
    }

    private static String parseNum(int day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }

public class CircleBackGroundSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setAlpha(180);
        c.drawRoundRect(left+((right-left)/7f), baseline/1.25f, right-((right-left)/7f), bottom, 20, 20, paint);
    }
}

    public void spinner(){
        // 直接由程式碼動態產生Spinner做法
        String[] selectInterval = {"選擇時段", "上午", "下午", "夜間"};
        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
        adapterInterval = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, selectInterval);
        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTLSelectInterval.setAdapter(adapterInterval);
        spTLSelectInterval.setSelection(0, true);
        spTLSelectInterval.setOnItemSelectedListener(listener);

    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterInterval)){
                tlInterval = parent.getSelectedItemPosition();
                if(tvTLDate.getText().toString().equals("請選擇日期")){
                    Util.showToast(TeachingLogActivity.this, "請先選擇日期");
                }else if(memberType.equals("3") || memberType.equals("4")) {
                    updateTLByTeacher(tlInterval);
                }else if(memberType.equals("1")){
                    updateTL(tlInterval);

                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void updateTL(int tlInterval){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get1dayCourseForTLUpdate");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("selected_date", tvTLDate.getText().toString());
        jsonObject.addProperty("tlInterval", tlInterval);

        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<ScheduleVO>>() {
                }.getType();
                scheduleForTLlist = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (scheduleForTLlist.isEmpty()) {
                Util.showToast(this, "該時段沒有課");
                tvTLCourseName.setText("");
                tvTLTeacher1.setText("");
                tvTLTeacher2.setText("");
                tvTLTeacher3.setText("");
                edAttendanceNum.setText(null);
                tvTLClassRoom.setText("");
                edTLRecord.setText(null);
                edTLHomework.setText(null);
            } else {
                courseDetails(scheduleForTLlist);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }
    public void updateTLByTeacher(int tlInterval){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get1dayCourseForTLUpdateByTeacher");
        jsonObject.addProperty("membername", membername);
        jsonObject.addProperty("selected_date", tvTLDate.getText().toString());
        jsonObject.addProperty("tlInterval", tlInterval);

        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<ScheduleVO>>() {
                }.getType();
                scheduleForTLlist = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (scheduleForTLlist.isEmpty()) {
                Util.showToast(this, "該時段沒有課");
                tvTLCourseName.setText("");
                tvTLTeacher1.setText("");
                tvTLTeacher2.setText("");
                tvTLTeacher3.setText("");
                edAttendanceNum.setText(null);
                tvTLClassRoom.setText("");
                edTLRecord.setText(null);
                edTLHomework.setText(null);
            } else {
                courseDetails(scheduleForTLlist);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }
    public void getCoursefromDBByClass(String class_no, String selected_date){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get1dayCourseForTL");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("selected_date", selected_date);
        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<ScheduleVO>>() {
                }.getType();
                scheduleForTLlist = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (scheduleForTLlist.isEmpty()) {
                Util.showToast(this, R.string.msg_CourseNotFound);
            } else {
                courseDetails(scheduleForTLlist);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }
    public void getCoursefromDBByTeacher(String membername, String selected_date){
        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get1dayCourseForTLByTeacher");
        jsonObject.addProperty("membername", membername);
        jsonObject.addProperty("selected_date", selected_date);
        String jsonOut = jsonObject.toString();

        if (Util.networkConnected(this)) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<ScheduleVO>>() {
                }.getType();
                scheduleForTLlist = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (scheduleForTLlist.isEmpty()) {
                Util.showToast(this, R.string.msg_CourseNotFound);
            } else {
                courseDetails(scheduleForTLlist);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }

    public void courseDetails(List<ScheduleVO> scheduleForTLlist){
        ScheduleVO scheduleVO = scheduleForTLlist.get(0);

        if(scheduleVO.getCourseTime_no() != null) {
            courseTime_no = scheduleVO.getCourseTime_no();
            tvTLDate.setText(scheduleVO.getSdate().toString());
        }else{
            tvTLDate.setText("---");}
        switch (scheduleVO.getInterval()){
            case 1:
                spTLSelectInterval.setSelection(1, true);
                break;
            case 2:
                spTLSelectInterval.setSelection(2, true);
                break;
            case 3:
                spTLSelectInterval.setSelection(3, true);
                break;

        }
        if(scheduleVO.getSubjectName() != null) {
            tvTLCourseName.setText(scheduleVO.getSubjectName());
        }else{
            tvTLCourseName.setText("---");}
        if(scheduleVO.getTeacherName1() != null) {
            tvTLTeacher1.setText(scheduleVO.getTeacherName1());
            tvTLTeacher1.setVisibility(View.VISIBLE);
        }else{
            tvTLTeacher1.setText("---");}
        if(scheduleVO.getTeacherName2() != null) {
            tvTLTeacher2.setText(scheduleVO.getTeacherName2());
            tvTLTeacher2.setVisibility(View.VISIBLE);
        }else{
            tvTLTeacher2.setVisibility(View.GONE);}
        if(scheduleVO.getTeacherName3() != null) {
            tvTLTeacher3.setText(scheduleVO.getTeacherName3());
            tvTLTeacher3.setVisibility(View.VISIBLE);
        }else{
            tvTLTeacher3.setVisibility(View.GONE);}
        if(scheduleVO.getClassroom_no() != null) {
            tvTLClassRoom.setText(scheduleVO.getClassroom_no());
        }else{
            tvTLClassRoom.setText("---");}
        if(scheduleVO.getAttendance() != null) {
            edAttendanceNum.setText(String.valueOf(scheduleVO.getAttendance()));
        }else{
            edAttendanceNum.setText(null);}
        if(scheduleVO.getRecord() != null) {
            edTLRecord.setText(scheduleVO.getRecord());
        }else{
            edTLRecord.setText(null);}
        if(scheduleVO.getHomework() != null) {
            edTLHomework.setText(scheduleVO.getHomework());
        }else{
            edTLHomework.setText(null);}

            if(scheduleVO.getNote().equals("1")){
                edAttendanceNum.setEnabled(false);
                edTLRecord.setEnabled(false);
                edTLHomework.setEnabled(false);
                btnTLSubmit.setVisibility(View.INVISIBLE);
                btnTLRest.setVisibility(View.INVISIBLE);
                btnTLCheck.setVisibility(View.GONE);
            }else{
                edAttendanceNum.setEnabled(true);
                edTLRecord.setEnabled(true);
                edTLHomework.setEnabled(true);
                btnTLSubmit.setVisibility(View.VISIBLE);
                btnTLRest.setVisibility(View.VISIBLE);
            }

            if(memberType.equals("3") || memberType.equals("4")){
                tvTLClassName.setText(scheduleVO.getClass_no());
                tvTLClassName.setVisibility(View.VISIBLE);
                edAttendanceNum.setEnabled(false);
                edAttendanceNum.setTextColor(Color.rgb(22, 82, 193));
                edTLRecord.setEnabled(false);
                edTLRecord.setTextColor(Color.rgb(22, 82, 193));
                edTLHomework.setEnabled(false);
                edTLHomework.setTextColor(Color.rgb(22, 82, 193));
                btnTLSubmit.setVisibility(View.GONE);
                btnTLRest.setVisibility(View.INVISIBLE);
                btnTLCheck.setVisibility(View.VISIBLE);
                if(scheduleVO.getNote().equals("1")){
                    btnTLCheck.setVisibility(View.GONE);
                }
            }
    }

}
