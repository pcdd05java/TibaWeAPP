package idv.ca107g2.tibawe.campuszone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{
    private List<Map<String, String>> atdList;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public AttendanceAdapter(List<Map<String, String>> atdList) {
        this.atdList = atdList;
    }


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attendance, parent, false);

        return new ViewHolder(cv);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final Map atdMap = atdList.get(position);

        TextView tvATDCourseNo = cardView.findViewById(R.id.tvATDCourseNo);
        tvATDCourseNo.setText((String)atdMap.get("coursetime_no"));
        TextView tvAbsDate = cardView.findViewById(R.id.tvAbsDate);
        tvAbsDate.setText((atdMap.get("atdDate")).toString());

        TextView tvATDDate = cardView.findViewById(R.id.tvATDDate);
        if(atdMap.get("atdDate") != null) {
            tvATDDate.setText((atdMap.get("atdDate")).toString().substring(5).replace("-","/"));
        }else{
            tvATDDate.setText("---");}

            //Morning
        TextView tvATDInterval = cardView.findViewById(R.id.tvATDInterval);
        if(atdMap.get("atdInterval") != null) {
            switch((String)atdMap.get("atdInterval")){
                case"1":
                    tvATDInterval.setText(R.string.tvMorning);
                    break;
                case"2":
                    tvATDInterval.setText(R.string.tvAfternoon);
                    break;
                case"3":
                    tvATDInterval.setText(R.string.tvEvening);
                    break;
            }

        }else{tvATDInterval.setText("---");}


        TextView tvATDCourse = cardView.findViewById(R.id.tvATDCourse);
        if(atdMap.get("atdCourse") != null) {
            tvATDCourse.setText((String) atdMap.get("atdCourse"));
        }else{
            tvATDCourse.setText("---");}

        TextView tvATDTeacher1 = cardView.findViewById(R.id.tvATDTeacher1);
        if(atdMap.get("atdTeacher1") != null) {
            tvATDTeacher1.setText((String) atdMap.get("atdTeacher1"));
            tvATDTeacher1.setVisibility(View.VISIBLE);
        }else{
            tvATDTeacher1.setVisibility(View.GONE);}

        TextView tvATDTeacher2 = cardView.findViewById(R.id.tvATDTeacher2);
        if(atdMap.get("atdTeacher2") != null) {
            tvATDTeacher2.setText((String) atdMap.get("atdTeacher2"));
            tvATDTeacher2.setVisibility(View.VISIBLE);
            tvATDTeacher1.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            tvATDCourse.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        }else{
            tvATDTeacher2.setVisibility(View.GONE);}


        TextView tvATDTeacher3 = cardView.findViewById(R.id.tvATDTeacher3);
        if(atdMap.get("atdTeacher3") != null) {
            tvATDTeacher3.setText((String) atdMap.get("atdTeacher3"));
            tvATDTeacher3.setVisibility(View.VISIBLE);
            tvATDTeacher1.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            tvATDCourse.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        }else{
            tvATDTeacher3.setVisibility(View.GONE);}


        TextView tvATDStatus = cardView.findViewById(R.id.tvATDStatus);
        TextView tvATDResult = cardView.findViewById(R.id.tvATDResult);
        Button btnApply = cardView.findViewById(R.id.btnApply);
        if(atdMap.get("atdStatus") != null) {
            btnApply.setClickable(false);
            btnApply.setTextColor(R.color.colorDarkBlue);
            btnApply.setText(R.string.btn_already_apply);
            switch((String)atdMap.get("atdStatus")){
                case"1":
                    tvATDStatus.setText(R.string.rck_status_review);
                    tvATDResult.setText(R.string.atd_status_no);
                    tvATDResult.setTextColor(Color.rgb(209, 8, 55));
                    break;
                case"2":
                    tvATDStatus.setText(R.string.rck_status_outcome);
                    switch ((String)atdMap.get("atdResult")){
                        case"1":
                            tvATDResult.setText(R.string.atd_status_yes);
                            break;
                        case"3":
                            tvATDResult.setText(R.string.atd_status_no);
                            tvATDResult.setTextColor(Color.rgb(209, 8, 55));
                            break;
                    }
                    break;
            }
        }else if(atdMap.get("atdResult") != null) {
            tvATDStatus.setVisibility(View.GONE);
            tvATDResult.setVisibility(View.VISIBLE);
            tvATDResult.setGravity(Gravity.CENTER);
            tvATDResult.setText(R.string.atd_status_no);
            tvATDResult.setTextColor(Color.rgb(209, 8, 55));
            btnApply.setClickable(true);
            btnApply.setText(R.string.btn_apply);
        }else{
            tvATDStatus.setText("---");}
    }

    @Override
    public int getItemCount() {
        return atdList.size();
    }

}
