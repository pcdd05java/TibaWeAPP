//package idv.ca107g2.tibawe.campuszone;
//
//import android.app.Dialog;
//import android.content.SharedPreferences;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.LineBackgroundSpan;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//import com.hannesdorfmann.swipeback.Position;
//import com.hannesdorfmann.swipeback.SwipeBack;
//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.DayViewDecorator;
//import com.prolificinteractive.materialcalendarview.DayViewFacade;
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
//import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import idv.ca107g2.tibawe.R;
//import idv.ca107g2.tibawe.task.CommonTask;
//import idv.ca107g2.tibawe.task.SpinnerTask;
//import idv.ca107g2.tibawe.tools.Util;
//import idv.ca107g2.tibawe.vo.CrVO;
//
//public class TeachingLogActivity extends AppCompatActivity {
//    private static final String TAG = "TeachingLogActivity";
//
//    private static TextView tvTLDate;
//    private TextView tvTLCourseName;
//    private TextView tvLastStudent;
//    private TextView tvTLTeacher1;
//    private TextView tvTLTeacher2;
//    private TextView tvTLTeacher3;
//    private TextView tvTLClassName;
//    private TextView tvTLClassRoom;
//    private Spinner spTLSelectInterval;
//    private SpinnerTask spinnerTask;
//    private CommonTask getTodayCourseTask, getCourseTask;
//    private EditText edAttendanceNum, edTLRecord, edTLNote;
//    private Button btnTLSubmit, btnTLRest;
//    private SharedPreferences preferences;
//    private String memberaccount, class_no, cr_no;
//    private FloatingActionButton fbtnSelectDate;
//    private static int year, month, nextmonth, day;
//    private List<CrVO> crList;
//    private Dialog selectDateDialog;
//    private MaterialCalendarView calendarView;
//    private List<java.util.Date> listDate;
//    private Map course;
//    private static ArrayAdapter<String> adapterInterval;
//    private boolean isSpinner = false;
//    private int tlInterval;
//
//    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_teaching_log);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        preferences = this.getSharedPreferences(Util.PREF_FILE,
//                this.MODE_PRIVATE);
//        fbtnSelectDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSelectDateDialog(v);
//            }
//        });
//
//
//
//        findDeafult();
//
//        // Init the swipe back
//        SwipeBack.attach(this, Position.LEFT)
//                .setSwipeBackView(R.layout.swipeback_default);
//
//    }
//
//
//
//    public void findDeafult(){
//
//        tvTLDate = findViewById(R.id.tvTLDate);
//        tvTLCourseName = findViewById(R.id.tvTLCourseName);
//        tvLastStudent = findViewById(R.id.tvLastStudent);
//        tvTLTeacher1 = findViewById(R.id.tvTLTeacher1);
//        tvTLTeacher2 = findViewById(R.id.tvTLTeacher2);
//        tvTLTeacher3 = findViewById(R.id.tvTLTeacher3);
//        tvTLClassName = findViewById(R.id.tvTLClassName);
//        spTLSelectInterval = findViewById(R.id.spTLSelectInterval);
//        tvTLClassRoom = findViewById(R.id.tvTLClassRoom);
//        edAttendanceNum = findViewById(R.id.edAttendanceNum);
//        edTLRecord = findViewById(R.id.edTLRecord);
//        edTLNote = findViewById(R.id.edTLNote);
//        btnTLSubmit = findViewById(R.id.btnTLSubmit);
//        btnTLRest = findViewById(R.id.btnTLRest);
//        btnTLSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editTL();
//            }
//        });
//        btnTLSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetTL();
//            }
//        });
//
//
//
//
//        tvLastStudent.setText(preferences.getString("membername",""));
//        tvTLClassName.setText(preferences.getString("className",""));
//        memberaccount = preferences.getString("memberaccount","");
//        class_no = preferences.getString("class_no","");
//
//        showRightNow();
//        spinner();
//        findTodayCourse();
//
//    }
//
//
//
//    public void findTodayCourse(){
//
//        if (Util.networkConnected(this)) {
//            String url = Util.URL + "ScheduleServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "editTL");
//            jsonObject.addProperty("class_no", class_no);
//
//            String jsonOut = jsonObject.toString();
//            getTodayCourseTask = new CommonTask(url, jsonOut);
//            try {
//                String result = getTodayCourseTask.execute().get();
//                Type collectionType = new TypeToken<List<Map>>() {
//                }.getType();
//                courseList = gson.fromJson(result, collectionType);
//
//
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Util.showToast(this, R.string.msg_NoNetwork);
//        }
//
//    }
//
//
//
//
////    public void editTL(){
////
////
////
////        if (Util.networkConnected(this)) {
////            String url = Util.URL + "ScheduleServlet";
////            JsonObject jsonObject = new JsonObject();
////            jsonObject.addProperty("action", "editTL");
////            jsonObject.addProperty("memberaccount", memberaccount);
////            jsonObject.addProperty("class_no", class_no);
////            jsonObject.addProperty("clrr_date", tvClassroomReserveDate.getText().toString());
////            jsonObject.addProperty("cr_no", cr_no);
////            jsonObject.addProperty("clrr_sttime", clrr_sttime);
////            jsonObject.addProperty("clrr_endtime", clrr_endtime);
////
////            String jsonOut = jsonObject.toString();
////            applyCLRRTask = new CommonTask(url, jsonOut);
////            try {
////                applyCLRRTask.execute();
////
////                Util.showToast(getActivity(), R.string.msg_clrr_success);
////                resetCLRR();
////                ((ClrrActivity)getActivity()).pager.getAdapter().notifyDataSetChanged();
////                ((ClrrActivity)getActivity()).pager.setCurrentItem(1);
////
////
////            } catch (Exception e) {
////                Log.e(TAG, e.toString());
////            }
////        } else {
////            Util.showToast(getActivity(), R.string.msg_NoNetwork);
////        }
////
////    }
//
//
//    private static void showRightNow() {
//        Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        updateInfo();
//    }
//
//    // 將指定的日期顯示在TextView上
//    private static void updateInfo() {
//        tvTLDate.setText(new StringBuilder().append(year).append("-")
//                //「month + 1」是因為一月的值是0而非1
//                .append(parseNum(month + 1)).append("-").append(parseNum(day)));
//    }
//
//
//
//
//
//    private void openSelectDateDialog(View v) {
//
//
//        selectDateDialog = new Dialog(this);
//        selectDateDialog.setCancelable(true);
//        selectDateDialog.setContentView(R.layout.dialog_tl_selectdate);
//        Toolbar dbdtoolbar = selectDateDialog.findViewById(R.id.toolbar_dialog_date_check);
//        dbdtoolbar.setLogo(R.drawable.icons8_event_24);
//        dbdtoolbar.setTitle("　選擇日期");
//
//
//        Window dbddialogWindow = selectDateDialog.getWindow();
//        dbddialogWindow.setGravity(Gravity.CENTER);
//
//        WindowManager m = this.getWindowManager();
//        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
//        WindowManager.LayoutParams p = dbddialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
//        p.height = (int) (d.getHeight() * 0.75); // 高度設置為螢幕的0.6 (60%)
//        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
//        dbddialogWindow.setAttributes(p);
//
//        setCalendarView();
//
//
//        selectDateDialog.show();
//    }
//
//    public void setCalendarView(){
//        calendarView.addDecorator(new DayViewDecorator() {
//            @Override
//            public boolean shouldDecorate(CalendarDay day) {
//                return day.getDate().equals(CalendarDay.today().getDate());
//            }
//
//            @Override
//            public void decorate(DayViewFacade view) {
//                view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorDarkRed)));
//            }
//        });
//
//        year = CalendarDay.today().getYear();
//        month = CalendarDay.today().getMonth();
//        if(month == 11){
//            nextmonth = 1;
//        }else{ nextmonth = month + 2;}
//        day = CalendarDay.today().getDay();
//
//        StringBuilder begin_date = new StringBuilder().append(year).append("-")
//                //「month + 1」是因為一月的值是0而非1
//                .append(parseNum(month + 1)).append("-").append("01");
//        StringBuilder end_date = new StringBuilder().append(year).append("-")
//                //「month + 1」是因為一月的值是0而非1
//                .append(parseNum(nextmonth)).append("-").append("01");
//
//        SharedPreferences preferences = getSharedPreferences(
//                Util.PREF_FILE, MODE_PRIVATE);
//        String class_no = preferences.getString("class_no", "");
//
//        connectDB(class_no, begin_date.toString(), end_date.toString());
//
//
//        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
//            @Override
//            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//                year = date.getYear();
//                month = date.getMonth();
//                if(month == 11){
//                    nextmonth = 1;
//                }else{ nextmonth = month + 2;}
//                day = date.getDay();
//
//                StringBuilder begin_date = new StringBuilder().append(year).append("-")
//                        //「month + 1」是因為一月的值是0而非1
//                        .append(parseNum(month + 1)).append("-").append("01");
//                StringBuilder end_date = new StringBuilder().append(year).append("-")
//                        //「month + 1」是因為一月的值是0而非1
//                        .append(parseNum(nextmonth)).append("-").append("01");
//
//                SharedPreferences preferences = getSharedPreferences(
//                        Util.PREF_FILE, MODE_PRIVATE);
//                String class_no = preferences.getString("class_no", "");
//
//                connectDB(class_no, begin_date.toString(), end_date.toString());
//
//            }
//        });
//
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                year = date.getYear();
//                month = date.getMonth();
//                day = date.getDay();
//
//                StringBuilder selected_date = new StringBuilder().append(year).append("-")
//                        //「month + 1」是因為一月的值是0而非1
//                        .append(parseNum(month + 1)).append("-").append(parseNum(day));
//
//                SharedPreferences preferences = getSharedPreferences(
//                        Util.PREF_FILE, MODE_PRIVATE);
//                String class_no = preferences.getString("class_no", "");
//
//                getCoursefromDB(class_no, selected_date.toString());
//
//            }
//        });
//    }
//
//    public void connectDB(String class_no, String begin_date, String end_date){
//        String url = Util.URL + "ScheduleServlet";
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "courseQueryForLabel");
//        jsonObject.addProperty("class_no", class_no);
//        jsonObject.addProperty("begin_date", begin_date);
//        jsonObject.addProperty("end_date", end_date);
//        String jsonOut = jsonObject.toString();
//
//        if (Util.networkConnected(this)) {
//            getCourseTask = new CommonTask(url, jsonOut);
//            try {
//                String result = getCourseTask.execute().get();
//                Type collectionType = new TypeToken<List<Date>>() {
//                }.getType();
//                listDate = gson.fromJson(result, collectionType);
//
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (listDate.isEmpty()) {
//
//            } else {
//                updateCalendar();
//            }
//
//        } else {
//            Util.showToast(this, R.string.msg_NoNetwork);
//        }
//
//    }
//    public void updateCalendar(){
//        calendarView.addDecorator(new DayViewDecorator() {
//            @Override
//            public boolean shouldDecorate(CalendarDay day) {
//                return listDate.contains(day.getDate());
//            }
//
//            @Override
//            public void decorate(DayViewFacade view) {
//                view.addSpan(new TeachingLogActivity.CircleBackGroundSpan());
//            }
//        });
//    }
//
//    private static String parseNum(int day) {
//        if (day >= 10)
//            return String.valueOf(day);
//        else
//            return "0" + String.valueOf(day);
//    }
//
//public class CircleBackGroundSpan implements LineBackgroundSpan {
//    @Override
//    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
//        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.colorAccent));
//        paint.setAlpha(180);
//        c.drawRoundRect(left+((right-left)/7f), baseline/1.25f, right-((right-left)/7f), bottom, 20, 20, paint);
//    }
//}
//
//    public void spinner(){
//        // 直接由程式碼動態產生Spinner做法
//        String[] selectInterval = {"請選擇時段", "上午", "下午", "夜間"};
//        // ArrayAdapter用來管理整個選項的內容與樣式，android.R.layout.simple_spinner_item為內建預設樣式
//        adapterInterval = new ArrayAdapter<>
//                (this, android.R.layout.simple_spinner_dropdown_item, selectInterval);
//        // android.R.layout.simple_spinner_dropdown_item為內建下拉選單樣式
//        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spTLSelectInterval.setAdapter(adapterInterval);
//        spTLSelectInterval.setSelection(0, true);
//        spTLSelectInterval.setOnItemSelectedListener(listener);
//
//    }
//
//    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            isSpinner = true;
//            if(parent.getAdapter().equals(adapterInterval)){
//                tlInterval = (int)parent.getItemAtPosition(position);
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
//
//    public void getCoursefromDB(String class_no, String selected_date){
//        String url = Util.URL + "ScheduleServlet";
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "get1dayCourse");
//        jsonObject.addProperty("class_no", class_no);
//        jsonObject.addProperty("selected_date", selected_date);
//        String jsonOut = jsonObject.toString();
//
//        if (Util.networkConnected(this)) {
//            getCourseTask = new CommonTask(url, jsonOut);
//            try {
//                String result = getCourseTask.execute().get();
//                Type collectionType = new TypeToken<Map>() {
//                }.getType();
//                course = gson.fromJson(result, collectionType);
//
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (course.isEmpty()) {
//                Util.showToast(this, R.string.msg_CourseNotFound);
//
//            } else {
//
//                courseDetails();
//            }
//
//        } else {
//            Util.showToast(this, R.string.msg_NoNetwork);
//        }
//
//    }
//
//}
