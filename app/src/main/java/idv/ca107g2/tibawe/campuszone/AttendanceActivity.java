package idv.ca107g2.tibawe.campuszone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import idv.ca107g2.tibawe.R;

public class AttendanceActivity extends AppCompatActivity {
    private Button btnRecheckSubmit, btnRecheckCancel, btnRecheckApply, btnAbsenceApply, btnApply;
    private EditText edRecheckNote;
    private Dialog recheckApplyDialog, optionDialog;
    private TextView tvRecheckStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        String[] attDates = new String[Attendance.attendances.length];
        for(int i = 0 ; i <attDates.length; i++){
            attDates[i] = Attendance.attendances[i].getAttDate();
        }
        String[] attIntervals = new String[Attendance.attendances.length];
        for(int i = 0 ; i <attIntervals.length; i++){
            attIntervals[i] = Attendance.attendances[i].getAttInterval();
        }
        String[] attCourses = new String[Attendance.attendances.length];
        for(int i = 0 ; i <attCourses.length; i++){
            attCourses[i] = Attendance.attendances[i].getAttCourse();
        }
        String[] attTeachers = new String[Attendance.attendances.length];
        for(int i = 0 ; i <attTeachers.length; i++){
            attTeachers[i] = Attendance.attendances[i].getAttTeacher();
        }
        String[] attStatus = new String[Attendance.attendances.length];
        for(int i = 0 ; i <attStatus.length; i++){
            attStatus[i] = Attendance.attendances[i].getAttStatus();
        }



        RecyclerView attRecycler = findViewById(R.id.rvAttendance);
        AttendanceAdapter adapter = new AttendanceAdapter(attDates, attIntervals, attCourses, attTeachers, attStatus);
        attRecycler.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        attRecycler.setLayoutManager(layoutManager);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);
    }

    public void applyOnClick(View view){
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

                    btnRecheckSubmit = recheckApplyDialog.findViewById(R.id.btnRecheckSubmit);
                    btnRecheckSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edRecheckNote = recheckApplyDialog.findViewById(R.id.edRecheckNote);
                            Toast.makeText(AttendanceActivity.this, R.string.apply, Toast.LENGTH_SHORT).show();
                            // 關閉對話視窗
                            tvRecheckStatus = findViewById(R.id.tvRecheckStatus);
                            tvRecheckStatus.setText("審核中");
                            btnApply = findViewById(R.id.btnApply);
                            btnApply.setClickable(false);
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
                    Intent intent = new Intent(AttendanceActivity.this, AbsenceApplyActivity.class);
                    startActivity(intent);
                    optionDialog.cancel();
                }
            });
            optionDialog.show();


           }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
