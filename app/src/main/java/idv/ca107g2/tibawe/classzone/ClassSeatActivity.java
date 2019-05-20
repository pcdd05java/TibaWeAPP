package idv.ca107g2.tibawe.classzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

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
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.ClassaVO;

public class ClassSeatActivity extends AppCompatActivity {

    private static final String TAG = "ClassSeatActivity";
    private CommonTask classSeatTask, getClassNameTask;

    private TextView tvcsClassName, cvtvSeatName;
    private String memberaccount, class_no, className;
    private String[] seatArray;
    private Map classSeatData;
    private TextView teacher_deafult, blackboard;


    private List<ClassaVO> classaVOList;
    String memberType;
    SharedPreferences preferences;

    private RecyclerView rvClassSeat;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_seat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        preferences = this.getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);

        memberType = preferences.getString("memberType", "");

        memberaccount = preferences.getString("memberaccount", "");
        class_no = preferences.getString("class_no", "");

        rvClassSeat = findViewById(R.id.rvClassSeat);

        tvcsClassName = findViewById(R.id.tvcsClassName);
        teacher_deafult = findViewById(R.id.teacher_deafult);
        blackboard = findViewById(R.id.blackboard);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.VERTICAL);
        rvClassSeat.setLayoutManager(layoutManager);


        final HorizontalScrollView horizontalview = findViewById(R.id.horizontalview);
        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default)
                .setOnInterceptMoveEventListener(
                        new SwipeBack.OnInterceptMoveEventListener() {
                            @Override
                            public boolean isViewDraggable(View v, int dx,
                                                           int x, int y) {
                                if (v == horizontalview) {
                                    return (horizontalview.fullScroll(-1));
                                }

                                return false;
                            }
                        });



        if(memberType.equals("1")) {

            class_no = preferences.getString("class_no", "");

            findSeat(class_no);

        }else{
            teacher_deafult.setVisibility(View.VISIBLE);
            blackboard.setVisibility(View.GONE);
            findClassName();
        }
    }

    public void findClassName(){
        String url = Util.URL + "ClassInformationServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getall");

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getClassNameTask = new CommonTask(url, jsonOut);
            try {
                String result = getClassNameTask.execute().get();
                Type collectionType = new TypeToken<List<ClassaVO>>() {
                }.getType();
                classaVOList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (classaVOList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(this, R.string.msg_NoNetwork);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!memberType.equals("1")) {
            // 為了讓Toolbar的 Menu有作用，這邊的程式不可以拿掉
            getMenuInflater().inflate(R.menu.menu_top_teacher, menu);
            for (int i = 0; i < classaVOList.size(); i++) {
                menu.add(0, i, i, classaVOList.get(i).getClassName());
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!memberType.equals("1")) {
            if(item.getItemId()==android.R.id.home){
                onBackPressed();
                return true;
            }
            if(item.getItemId()<classaVOList.size()){
                int id = item.getItemId();
                class_no = classaVOList.get(id).getClass_no();
            }
            findSeat(class_no);
        }
        return true;
    }


    public void findSeat(String class_no) {
        teacher_deafult.setVisibility(View.GONE);
        blackboard.setVisibility(View.VISIBLE);

        String url = Util.URL + "ClassaServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "querySeat");
        jsonObject.addProperty("memberAccount", memberaccount);
        jsonObject.addProperty("class_no", class_no);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            classSeatTask = new CommonTask(url, jsonOut);
            try {
                String result = classSeatTask.execute().get();
                Type collectionTypeMap = new TypeToken<Map>() {
                }.getType();
                classSeatData = gson.fromJson(result, collectionTypeMap);

                className = gson.fromJson(classSeatData.get("className").toString(), String.class);

                seatArray = gson.fromJson(classSeatData.get("seatArray").toString(), String[].class);

Log.d("className", className);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (seatArray.length==0) {
                Util.showToast(this, R.string.msg_nodata);

            } else {
                tvcsClassName.setText(className);
                ClassSeatAdapter classSeatadapter = new ClassSeatAdapter(seatArray);
                rvClassSeat.setAdapter(classSeatadapter);
            }

        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

}
