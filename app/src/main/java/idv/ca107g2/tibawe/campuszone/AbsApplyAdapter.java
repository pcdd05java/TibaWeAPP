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
import idv.ca107g2.tibawe.tools.Util;

class AbsApplyAdapter extends RecyclerView.Adapter<AbsApplyAdapter.ViewHolder> {
    private List<Map<String, String>> absList;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public AbsApplyAdapter(List<Map<String, String>> absList) {
        this.absList = absList;
    }


    @NonNull
    @Override
    public AbsApplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_abs_query, parent, false);

        return new ViewHolder(cv);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AbsApplyAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final Map absMap = absList.get(position);

        TextView tvAbsqNO = cardView.findViewById(R.id.tvAbsqNO);
        tvAbsqNO.setText((String)absMap.get("absno"));
//
//        TextView tvAbsqDate = cardView.findViewById(R.id.tvAbsqDate);
//        tvAbsqDate.setText((absMap.get("absDate")).toString());

        TextView tvAbsqDate = cardView.findViewById(R.id.tvAbsqDate);
        if(absMap.get("absDate") != null) {
            tvAbsqDate.setText((absMap.get("absDate")).toString());
        }else{
            tvAbsqDate.setText("---");}

        //Morning
        TextView tvAbsqInterval = cardView.findViewById(R.id.tvAbsqInterval);
        if(absMap.get("absInterval") != null) {
            switch((String)absMap.get("absInterval")){
                case"1":
                    tvAbsqInterval.setText(R.string.tvMorning);
                    break;
                case"2":
                    tvAbsqInterval.setText(R.string.tvAfternoon);
                    break;
                case"3":
                    tvAbsqInterval.setText(R.string.tvEvening);
                    break;
            }

        }else{tvAbsqInterval.setText("---");}


        TextView tvAbsqCourse = cardView.findViewById(R.id.tvAbsqCourse);
        if(absMap.get("absCourse") != null) {
            tvAbsqCourse.setText((String) absMap.get("absCourse"));
        }else if(Integer.valueOf(String.valueOf(absMap.get("absInterval")))== 4){
                tvAbsqCourse.setText("全日課程");
        } else{
                tvAbsqCourse.setText("---");
        }

        TextView tvAbsqTeacher1 = cardView.findViewById(R.id.tvAbsqTeacher1);
        if(absMap.get("absTeacher1") != null) {
            tvAbsqTeacher1.setText((String) absMap.get("absTeacher1"));
            tvAbsqTeacher1.setVisibility(View.VISIBLE);
        }else{
            tvAbsqTeacher1.setVisibility(View.GONE);
            tvAbsqCourse.setGravity(Gravity.CENTER);}

        TextView tvAbsqTeacher2 = cardView.findViewById(R.id.tvAbsqTeacher2);
        if(absMap.get("absTeacher2") != null) {
            tvAbsqTeacher2.setText((String) absMap.get("absTeacher2"));
            tvAbsqTeacher2.setVisibility(View.VISIBLE);
            tvAbsqTeacher1.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            tvAbsqCourse.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        }else{
            tvAbsqTeacher2.setVisibility(View.GONE);}


        TextView tvAbsqTeacher3 = cardView.findViewById(R.id.tvAbsqTeacher3);
        if(absMap.get("absTeacher3") != null) {
            tvAbsqTeacher3.setText((String) absMap.get("absTeacher3"));
            tvAbsqTeacher3.setVisibility(View.VISIBLE);
            tvAbsqTeacher1.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            tvAbsqCourse.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        }else{
            tvAbsqTeacher3.setVisibility(View.GONE);}

        TextView tvAbsqReason = cardView.findViewById(R.id.tvAbsqReason);
        if(absMap.get("absReason") != null) {
            switch((String)absMap.get("absReason")){
                case"1":
                    tvAbsqReason.setText("事假");
                    break;
                case"2":
                    tvAbsqReason.setText("公假");
                    break;
                case"3":
                    tvAbsqReason.setText("病假");
                    break;
                case"4":
                    tvAbsqReason.setText("喪假");
                    break;
                case"5":
                    tvAbsqReason.setText("其他");
                    break;
            }

        }else{tvAbsqReason.setText("---");}


        TextView tvAbsqOutcome = cardView.findViewById(R.id.tvAbsqOutcome);
        Button btnAbsqUpdate = cardView.findViewById(R.id.btnAbsqUpdate);
        if(absMap.get("absOutcome") != null) {
            switch((String)absMap.get("absOutcome")){
                case"0":
                    tvAbsqOutcome.setText("審核不通過");
                    tvAbsqOutcome.setTextColor(Color.rgb(209, 8, 55));
                    break;
                case"1":
                    tvAbsqOutcome.setText("已通過");
                    tvAbsqOutcome.setTextColor(Color.rgb(22, 82, 193));
                    break;
                case"2":
                    tvAbsqOutcome.setText("審核中");
                    btnAbsqUpdate.setVisibility(View.VISIBLE);
                    break;
            }
        } else{
            tvAbsqOutcome.setText("---");
            btnAbsqUpdate.setVisibility(View.GONE);
        }

        TextView tvAbsqNote = cardView.findViewById(R.id.tvAbsqNote);
        if(absMap.get("absNote") != null) {
            tvAbsqNote.setText((absMap.get("absNote")).toString());
        }else{
            tvAbsqNote.setText("---");}

        btnAbsqUpdate = cardView.findViewById(R.id.btnAbsqUpdate);
        btnAbsqUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showToast(v.getContext(), "功能開發中");
            }
        });

    }

    @Override
    public int getItemCount() {
        return absList.size();
    }

}
