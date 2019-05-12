package idv.ca107g2.tibawe.classzone;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.Util;
import idv.ca107g2.tibawe.task.CommonTask;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseQueryFragment extends Fragment {
    private static final String TAG = "CourseQueryFragment";
    private CommonTask getCourseTask;
    private String class_no, sysdate, tomorrowDate, aftertomorrowDate;
    private RecyclerView courseRecycler;
    private List<Map> courseList;


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        class_no = preferences.getString("class_no", "");
        tomorrowDate = new java.sql.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000).toString();
        aftertomorrowDate = new java.sql.Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000).toString();
        sysdate = new java.sql.Date(System.currentTimeMillis()).toString();


        courseRecycler = (RecyclerView) inflater.inflate(R.layout.recyclerview_fragment, container, false);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        courseRecycler.setLayoutManager(layoutManager);

        findCourse(class_no, tomorrowDate, aftertomorrowDate);

        return courseRecycler;


    }
    

    public void findCourse(String class_no, String tomorrowDate, String aftertomorrowDate) {

        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "courseQueryin3days");
        jsonObject.addProperty("class_no", class_no);
        jsonObject.addProperty("tomorrowDate", tomorrowDate);
        jsonObject.addProperty("aftertomorrowDate", aftertomorrowDate);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getCourseTask = new CommonTask(url, jsonOut);
            try {
                String result = getCourseTask.execute().get();
                Type collectionType = new TypeToken<List<Map>>() {
                }.getType();
                courseList = gson.fromJson(result, collectionType);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (courseList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(getContext(), R.string.msg_CourseNotFound);
            } else {

                courseRecycler.setAdapter(new CourseQueryAdapter(courseList));
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }


}
