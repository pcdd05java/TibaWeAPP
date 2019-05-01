package idv.ca107g2.tibawe.classzone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;

public class CourseQueryAdapter extends RecyclerView.Adapter<CourseQueryAdapter.ViewHolder>{
    private List<Map> courseList;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public CourseQueryAdapter(List<Map> courseList) {
        this.courseList = courseList;
    }


    @NonNull
    @Override
    public CourseQueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_course_query, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseQueryAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final Map courseMap = courseList.get(position);


        TextView tvCourseDate = cardView.findViewById(R.id.tvCourseDate);
        if(courseMap.get("courseDate") != null) {
            tvCourseDate.setText((courseMap.get("courseDate")).toString());
        }else{
            tvCourseDate.setText("---");}

            //Morning
        TextView tvCourseMorning = cardView.findViewById(R.id.tvCourseMorning);
        if(courseMap.get("courseMorning") != null) {
            tvCourseMorning.setText((String) courseMap.get("courseMorning"));
        }else{tvCourseMorning.setText("---");}


        TextView tvTeacherMorning1 = cardView.findViewById(R.id.tvTeacherMorning1);
        if(courseMap.get("teacherMorning1") != null) {
            tvTeacherMorning1.setText((String) courseMap.get("teacherMorning1"));
            tvTeacherMorning1.setVisibility(View.VISIBLE);
        }else{
            tvTeacherMorning1.setText("---");}

        TextView tvTeacherMorning2 = cardView.findViewById(R.id.tvTeacherMorning2);
        if(courseMap.get("teacherMorning2") != null) {
            tvTeacherMorning2.setText((String) courseMap.get("teacherMorning2"));
            tvTeacherMorning2.setVisibility(View.VISIBLE);
        }else{
            tvTeacherMorning2.setVisibility(View.GONE);}

        TextView tvTeacherMorning3 = cardView.findViewById(R.id.tvTeacherMorning3);
        if(courseMap.get("teacherMorning3") != null) {
            tvTeacherMorning3.setText((String) courseMap.get("teacherMorning3"));
            tvTeacherMorning3.setVisibility(View.VISIBLE);
        }else{
            tvTeacherMorning3.setVisibility(View.GONE);}


        TextView tvTeacherMorning4 = cardView.findViewById(R.id.tvTeacherMorning4);
        if(courseMap.get("TeacherMorning4") != null) {
            tvTeacherMorning4.setText((String) courseMap.get("TeacherMorning4"));
            tvTeacherMorning4.setVisibility(View.VISIBLE);
        }else{
            tvTeacherMorning4.setVisibility(View.GONE);}


        TextView tvClassroomMorning = cardView.findViewById(R.id.tvClassroomMorning);
        if(courseMap.get("classroomMorning") != null) {
            tvClassroomMorning.setText((String) courseMap.get("classroomMorning"));
        }else{
            tvClassroomMorning.setText("---");}

            //Afternoon
        TextView tvCourseAfternoon = cardView.findViewById(R.id.tvCourseAfternoon);
        if(courseMap.get("courseAfternoon") != null) {
            tvCourseAfternoon.setText((String) courseMap.get("courseAfternoon"));
        }else{tvCourseAfternoon.setText("---");}


        TextView tvTeacherAfternoon1 = cardView.findViewById(R.id.tvTeacherAfternoon1);
        if(courseMap.get("teacherAfternoon1") != null) {
            tvTeacherAfternoon1.setText((String) courseMap.get("teacherAfternoon1"));
            tvTeacherAfternoon1.setVisibility(View.VISIBLE);
        }else{
            tvTeacherAfternoon1.setText("---");}

        TextView tvTeacherAfternoon2 = cardView.findViewById(R.id.tvTeacherAfternoon2);
        if(courseMap.get("teacherAfternoon2") != null) {
            tvTeacherAfternoon2.setText((String) courseMap.get("teacherAfternoon2"));
            tvTeacherAfternoon2.setVisibility(View.VISIBLE);
        }else{
            tvTeacherAfternoon2.setVisibility(View.GONE);}

        TextView tvTeacherAfternoon3 = cardView.findViewById(R.id.tvTeacherAfternoon3);
        if(courseMap.get("teacherAfternoon3") != null) {
            tvTeacherAfternoon3.setText((String) courseMap.get("teacherAfternoon3"));
            tvTeacherAfternoon3.setVisibility(View.VISIBLE);
        }else{
            tvTeacherMorning3.setVisibility(View.GONE);}


        TextView tvTeacherAfternoon4 = cardView.findViewById(R.id.tvTeacherAfternoon4);
        if(courseMap.get("teacherAfternoon4") != null) {
            tvTeacherAfternoon4.setText((String) courseMap.get("teacherAfternoon4"));
            tvTeacherAfternoon4.setVisibility(View.VISIBLE);
        }else{
            tvTeacherAfternoon4.setVisibility(View.GONE);}


        TextView tvClassroomAfternoon = cardView.findViewById(R.id.tvClassroomAfternoon);
        if(courseMap.get("classroomAfternoon") != null) {
            tvClassroomAfternoon.setText((String) courseMap.get("classroomAfternoon"));
        }else{
            tvClassroomAfternoon.setText("---");}

        //Evening
        TextView tvCourseEvening = cardView.findViewById(R.id.tvCourseEvening);
        if(courseMap.get("courseEvening") != null) {
            tvCourseEvening.setText((String) courseMap.get("courseEvening"));
        }else{tvCourseEvening.setText("---");}


        TextView tvTeacherEvening1 = cardView.findViewById(R.id.tvTeacherEvening1);
        if(courseMap.get("teacherEvening1") != null) {
            tvTeacherEvening1.setText((String) courseMap.get("teacherEvening1"));
            tvTeacherEvening1.setVisibility(View.VISIBLE);
        }else{
            tvTeacherEvening1.setText("---");}

        TextView tvTeacherEvening2 = cardView.findViewById(R.id.tvTeacherEvening2);
        if(courseMap.get("teacherEvening2") != null) {
            tvTeacherEvening2.setText((String) courseMap.get("teacherEvening2"));
            tvTeacherEvening2.setVisibility(View.VISIBLE);
        }else{
            tvTeacherEvening2.setVisibility(View.GONE);}

        TextView tvTeacherEvening3 = cardView.findViewById(R.id.tvTeacherEvening3);
        if(courseMap.get("teacherEvening3") != null) {
            tvTeacherEvening3.setText((String) courseMap.get("teacherEvening3"));
            tvTeacherEvening3.setVisibility(View.VISIBLE);
        }else{
            tvTeacherEvening3.setVisibility(View.GONE);}


        TextView tvTeacherEvening4 = cardView.findViewById(R.id.tvTeacherEvening4);
        if(courseMap.get("teacherEvening4") != null) {
            tvTeacherEvening4.setText((String) courseMap.get("teacherEvening4"));
            tvTeacherEvening4.setVisibility(View.VISIBLE);
        }else{
            tvTeacherEvening4.setVisibility(View.GONE);}


        TextView tvClassroomEvening = cardView.findViewById(R.id.tvClassroomEvening);
        if(courseMap.get("classroomEvening") != null) {
            tvClassroomEvening.setText((String) courseMap.get("classroomEvening"));
        }else{
            tvClassroomEvening.setText("---");}
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

}
