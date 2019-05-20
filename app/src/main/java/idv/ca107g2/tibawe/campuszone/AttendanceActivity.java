package idv.ca107g2.tibawe.campuszone;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.CommonTask;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";
    private CommonTask atdTask, applyRCKTask;
    private Button btnRecheckSubmit, btnRecheckCancel, btnRecheckApply, btnAbsenceApply, btnApply;
    private EditText edRecheckNote;
    private Dialog recheckApplyDialog, optionDialog;
    private TextView atd_result, tvATDDate,tvATDCourseNo, tvATDInterval, tvATDCourse, tvATDTeacher1,
                    tvATDTeacher2, tvATDTeacher3, tvATDStatus, tvATDResult,tvAbsDate;
    private String memberaccount, class_no;
    private RecyclerView atdRecycler;
    private List<Map<String, String>> atdList;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = this.getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");
        class_no = preferences.getString("class_no", "");

        atdRecycler = findViewById(R.id.rvAttendance);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        atdRecycler.setLayoutManager(layoutManager);

        atd_result = findViewById(R.id.atd_result);

        findATD(memberaccount);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);
    }

    public void findATD(String memberaccount) {

        String url = Util.URL + "AttendanceServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "queryATD");
        jsonObject.addProperty("memberaccount", memberaccount);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            atdTask = new CommonTask(url, jsonOut);
            try {
                String result = atdTask.execute().get();
                Type collectionType = new TypeToken<List<Map<String, String>>>() {
                }.getType();
                atdList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (atdList.isEmpty()) {
                atd_result.setText(R.string.msg_attendance_norecord);
            } else {
                atdRecycler.setAdapter(new AttendanceAdapter(atdList));
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    public void applyOnClick(View view) {
        CardView cv = (CardView) view.getParent().getParent().getParent();
//                    loopViews(cv);
        ViewGroup vg = (ViewGroup) cv.getChildAt(0);
        ViewGroup vg0 = (ViewGroup) vg.getChildAt(0);
        tvATDDate = (TextView) vg0.getChildAt(1);
        tvATDInterval = (TextView) vg0.getChildAt(2);
        tvATDCourseNo = (TextView) vg0.getChildAt(3);
        tvAbsDate = (TextView) vg0.getChildAt(4);

        ViewGroup vg1 = (ViewGroup) vg.getChildAt(1);
        tvATDCourse = (TextView) vg1.getChildAt(1);
        if(vg1.getChildAt(2)!=null){
            tvATDTeacher1 = (TextView) vg1.getChildAt(2);}
        if(vg1.getChildAt(3)!=null){
            tvATDTeacher2 = (TextView) vg1.getChildAt(3);}
        if(vg1.getChildAt(4)!=null){
            tvATDTeacher3 = (TextView) vg1.getChildAt(4);}

        ViewGroup vg3 = (ViewGroup) vg.getChildAt(3);
        tvATDStatus = (TextView) vg3.getChildAt(1);
        tvATDResult = (TextView) vg3.getChildAt(2);

        btnApply = (Button)view;

        optionDialog = new Dialog(AttendanceActivity.this);
        optionDialog.setCancelable(true);
        optionDialog.setContentView(R.layout.dialog_option);
        Toolbar opttoolbar = optionDialog.findViewById(R.id.toolbar_dialog_option);
        setSupportActionBar(opttoolbar);
        getSupportActionBar().setTitle(R.string.dialog_title_option);

        Window optdialogWindow = optionDialog.getWindow();
        optdialogWindow.setGravity(Gravity.CENTER);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams p = optdialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        p.height = (int) (d.getHeight() * 0.3); // 高度設置為螢幕的0.6 (60%)
        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
        optdialogWindow.setAttributes(p);

        btnRecheckApply = optionDialog.findViewById(R.id.btnRecheckApply);
        btnRecheckApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recheckApplyDialog = new Dialog(AttendanceActivity.this);
                recheckApplyDialog.setCancelable(false);
                recheckApplyDialog.setContentView(R.layout.dialog_recheck_apply);
                optionDialog.cancel();

                Toolbar retoolbar = recheckApplyDialog.findViewById(R.id.dialog_recheck_toolbar);
                setSupportActionBar(retoolbar);
                getSupportActionBar().setTitle(R.string.dialog_title_recheck);

                Window redialogWindow = recheckApplyDialog.getWindow();
                redialogWindow.setGravity(Gravity.CENTER);

                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
                WindowManager.LayoutParams p = redialogWindow.getAttributes();
                p.height = (int) (d.getHeight() * 0.6); // 高度設置為螢幕的0.6 (60%)
                p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
                redialogWindow.setAttributes(p);

                TextView tvRecheckDate = recheckApplyDialog.findViewById(R.id.tvRecheckDate);
                tvRecheckDate.setText(tvATDDate.getText());

                TextView tvRecheckInterval = recheckApplyDialog.findViewById(R.id.tvRecheckInterval);
                tvRecheckInterval.setText(tvATDInterval.getText());

                TextView tvRecheckCourse = recheckApplyDialog.findViewById(R.id.tvRecheckCourse);
                tvRecheckCourse.setText(tvATDCourse.getText());

                if(tvATDTeacher1.getText()!=null){
                    TextView tvRecheckTeacher1 = recheckApplyDialog.findViewById(R.id.tvRecheckTeacher1);
                    tvRecheckTeacher1.setText(tvATDTeacher1.getText());
                }
                if(tvATDTeacher2.getText()!=null){
                    TextView tvRecheckTeacher2 = recheckApplyDialog.findViewById(R.id.tvRecheckTeacher2);
                    tvRecheckTeacher2.setText(tvATDTeacher2.getText());
                    tvRecheckTeacher2.setVisibility(View.VISIBLE);
                }
                if(tvATDTeacher3.getText()!=null){
                    TextView tvRecheckTeacher3 = recheckApplyDialog.findViewById(R.id.tvRecheckTeacher3);
                    tvRecheckTeacher3.setText(tvATDTeacher3.getText());
                    tvRecheckTeacher3.setVisibility(View.VISIBLE);
                }

                edRecheckNote = recheckApplyDialog.findViewById(R.id.edRecheckNote);

                btnRecheckSubmit = recheckApplyDialog.findViewById(R.id.btnRecheckSubmit);
                btnRecheckSubmit.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View v) {

                        if (Util.networkConnected(AttendanceActivity.this)) {
                            String url = Util.URL + "ReCheckSheetServlet";
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "applyRCK");
                            jsonObject.addProperty("memberaccount", memberaccount);
                            jsonObject.addProperty("coursetime_no", tvATDCourseNo.getText().toString());
                            jsonObject.addProperty("edRecheckNote", edRecheckNote.getText().toString());


                            String jsonOut = jsonObject.toString();
                            applyRCKTask = new CommonTask(url, jsonOut);
                            try {
                                applyRCKTask.execute();

                                Util.showToast(AttendanceActivity.this, R.string.msg_applyRCK_success);

                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        } else {
                            Util.showToast(AttendanceActivity.this, R.string.msg_NoNetwork);
                        }
                        // 關閉對話視窗

                        btnApply.setClickable(false);
                        btnApply.setTextColor(R.color.colorDarkBlue);
                        btnApply.setText(R.string.btn_already_apply);

                        tvATDStatus.setText(R.string.rck_status_review);
                        tvATDStatus.setVisibility(View.VISIBLE);
                        tvATDResult.setText(R.string.atd_status_no);
                        tvATDResult.setGravity(Gravity.CENTER|Gravity.TOP);

                        recheckApplyDialog.cancel();
                    }
                });

                btnRecheckCancel = recheckApplyDialog.findViewById(R.id.btnRecheckCancel);
                btnRecheckCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AttendanceActivity.this, R.string.cancel, Toast.LENGTH_SHORT).show();
                        // 關閉對話視窗
                        recheckApplyDialog.cancel();
                    }
                });
                recheckApplyDialog.show();
            }
        });

        btnAbsenceApply = optionDialog.findViewById(R.id.btnAbsenceApply);
        btnAbsenceApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, AbsApplyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("absCourse",tvATDCourse.getText().toString());
                if(tvATDTeacher1!=null) {
                    bundle.putString("absTeacher1", tvATDTeacher1.getText().toString());
                }
                if(tvATDTeacher2!=null) {
                    bundle.putString("absTeacher2", tvATDTeacher2.getText().toString());
                }
                if(tvATDTeacher3!=null) {
                    bundle.putString("absTeacher3", tvATDTeacher3.getText().toString());
                }
                switch (tvATDInterval.getText().toString()){
                    case"上午":
                        bundle.putInt("interval", 1);
                        break;
                    case"下午":
                        bundle.putInt("interval", 2);
                        break;
                    case"夜間":
                        bundle.putInt("interval", 3);
                        break;
                }
                bundle.putString("absDate", tvAbsDate.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                optionDialog.cancel();
            }
        });
        optionDialog.show();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }

    private void loopViews(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View v = view.getChildAt(i);

            if (v instanceof EditText) {
                // will be executed when its edittext
                Log.d("Check", "This is EditText" + String.valueOf(i));

            } else if (v instanceof TextView) {
                // will be executed when its textview,, and get the text..
                TextView x = (TextView) v;
                String aa = x.getText().toString() + i;
                Log.d("Check", "This is TextView with text : " + aa);

            } else if (v instanceof ViewGroup) {

                // will be executed when its viewgroup,, and loop it for get the child view..
                Log.d("Check", "This is ViewGroup"+String.valueOf(i));
                this.loopViews((ViewGroup) v);
            }
        }
    }
}
