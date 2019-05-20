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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.ClassaVO;
import idv.ca107g2.tibawe.vo.MemberVO;

public class PhoneBookActivity extends AppCompatActivity {

    private static final String TAG = "PhoneBookActivity";
    private CommonTask getPhoneBookTask, getClassNameTask;
    private List<MemberVO> memberVOlist;
    private List<ClassaVO> classaVOList;
    private RecyclerView rvPhonebook;
    private String class_no;
    private TextView teacher_deafult, phonebook;

    SharedPreferences preferences;
    String memberType;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

        preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);

        memberType = preferences.getString("memberType", "");

        teacher_deafult = findViewById(R.id.teacher_deafult);
        phonebook = findViewById(R.id.phonebook);

        if(memberType.equals("1")) {

            class_no = preferences.getString("class_no", "");

            findPhonebook();

        }else{

            teacher_deafult.setVisibility(View.VISIBLE);
            phonebook.setVisibility(View.GONE);
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
            findPhonebook();
        }
        return true;
    }


    public void findPhonebook() {
        teacher_deafult.setVisibility(View.GONE);
        phonebook.setVisibility(View.VISIBLE);

        String url = Util.URL + "MemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getPhoneBook");
        jsonObject.addProperty("class_no", class_no);

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getPhoneBookTask = new CommonTask(url, jsonOut);
            try {
                String result = getPhoneBookTask.execute().get();
                Type collectionType = new TypeToken<List<MemberVO>>() {
                }.getType();
                memberVOlist = gson.fromJson(result, collectionType);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (memberVOlist.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(this, R.string.msg_nodata);
            } else {
                MemberVO memberVO = memberVOlist.get(0);
                TextView tvpbClass = findViewById(R.id.tvpbClass);
                if(!memberVO.getClass_no().isEmpty()) {
                    tvpbClass.setText(memberVO.getClass_no()); }else{
                    tvpbClass.setText("---");}

                rvPhonebook = findViewById(R.id.rvPhonebook);

                PhoneBookAdapter adapter = new PhoneBookAdapter(memberVOlist);
                rvPhonebook.setAdapter(adapter);
//                adapter.setListener(new LatestNewsAdapter.Listener() {
//                    @Override
//                    public void onClick(int position) {
//                        Intent intent = new Intent(CampusNewsActivity.this, StoreDetailActivity.class);
////                        intent.putExtra(StoreDetailActivity.EXTRA_INFO_ID, position);
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//                    }
//                });
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rvPhonebook.setLayoutManager(layoutManager);
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
