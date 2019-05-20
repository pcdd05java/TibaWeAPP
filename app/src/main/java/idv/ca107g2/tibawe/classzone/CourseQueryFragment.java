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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.ClassaVO;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseQueryFragment extends Fragment {
    private static final String TAG = "CourseQueryFragment";
    private CommonTask getCourseTask, getClassNameTask;
    private String class_no, sysdate, tomorrowDate, aftertomorrowDate;
    private RecyclerView courseRecycler, rvCourseQuery;
    private List<Map> courseList;
    String memberType, membername;
    private List<ClassaVO> classaVOList;
    private ArrayAdapter<String> adapterClassName;
    private int teacherorclass;
    private TextView teacher_deafult;
    private Spinner spcqSelecClass;



    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                Util.PREF_FILE, MODE_PRIVATE);
        class_no = preferences.getString("class_no", "");
        memberType = preferences.getString("memberType", "");
        membername = preferences.getString("membername", "");

        tomorrowDate = new java.sql.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000).toString();
        aftertomorrowDate = new java.sql.Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000).toString();
        sysdate = new java.sql.Date(System.currentTimeMillis()).toString();

        View view = inflater.inflate(R.layout.fragment_course_query, container, false);

        courseRecycler = view.findViewById(R.id.rvCourseQuery);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        courseRecycler.setLayoutManager(layoutManager);
        teacher_deafult = view.findViewById(R.id.teacher_deafult);
        spcqSelecClass = view.findViewById(R.id.spcqSelecClass);
        rvCourseQuery = view.findViewById(R.id.rvCourseQuery);

        if(memberType.equals("4")){
            rvCourseQuery.setVisibility(View.GONE);
            teacher_deafult.setVisibility(View.VISIBLE);
            spcqSelecClass.setVisibility(View.VISIBLE);
            findClassName();
        }else if(memberType.equals("3")){
            rvCourseQuery.setVisibility(View.VISIBLE);
            teacher_deafult.setVisibility(View.GONE);
            spcqSelecClass.setVisibility(View.GONE);
            findCourseByTeacher(membername, tomorrowDate,aftertomorrowDate);
        }else if (memberType.equals("1")){
            rvCourseQuery.setVisibility(View.VISIBLE);
            teacher_deafult.setVisibility(View.GONE);
            spcqSelecClass.setVisibility(View.GONE);
            findCourseByClass(class_no, tomorrowDate, aftertomorrowDate);
        }

        return view;
    }
    public void findClassName(){
        String url = Util.URL + "ClassInformationServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getall");

        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getClassNameTask = new CommonTask(url, jsonOut);
            try {
                String result = getClassNameTask.execute().get();
                Type collectionType = new TypeToken<List<ClassaVO>>() {
                }.getType();
                classaVOList = gson.fromJson(result, collectionType);
                List<String> ClassName = new ArrayList<>();
                for (int i = 0; i < classaVOList.size()+1; i++) {
                    if(i==0){
                        ClassName.add("請選擇班級");
                    }else{
                    ClassName.add(classaVOList.get(i-1).getClassName());
                    }
                }
                adapterClassName= new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ClassName);
                adapterClassName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spcqSelecClass.setAdapter(adapterClassName);
                spcqSelecClass.setSelection(0, true);
                spcqSelecClass.setOnItemSelectedListener(listener);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (classaVOList.isEmpty()) {
//                view = inflater.inflate(R.layout.fragment_course_query, container, false);
                Util.showToast(getActivity(), R.string.msg_nodata);
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getActivity(), R.string.msg_NoNetwork);
        }

    }

    private Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter().equals(adapterClassName)){
                class_no = classaVOList.get(parent.getSelectedItemPosition()-1).getClass_no();
                findCourseByClass(class_no, tomorrowDate, aftertomorrowDate);
                rvCourseQuery.setVisibility(View.VISIBLE);
                teacher_deafult.setVisibility(View.GONE);
                spcqSelecClass.setVisibility(View.GONE);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void findCourseByTeacher(String membername, String tomorrowDate, String aftertomorrowDate) {
        rvCourseQuery.setVisibility(View.VISIBLE);
        teacher_deafult.setVisibility(View.GONE);

        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "courseQueryin3daysByTeacher");
        jsonObject.addProperty("membername", membername);
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
                teacherorclass = 0;
                courseRecycler.setAdapter(new CourseQueryAdapter(courseList, teacherorclass));
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }
    

    public void findCourseByClass(String class_no, String tomorrowDate, String aftertomorrowDate) {
        rvCourseQuery.setVisibility(View.VISIBLE);
        teacher_deafult.setVisibility(View.GONE);

        String url = Util.URL + "ScheduleServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "courseQueryin3daysByClassNo");
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
                teacherorclass = 1;
                courseRecycler.setAdapter(new CourseQueryAdapter(courseList, teacherorclass));
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }


}
