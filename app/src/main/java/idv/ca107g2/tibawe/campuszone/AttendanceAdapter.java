package idv.ca107g2.tibawe.campuszone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import idv.ca107g2.tibawe.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{

    private String[] attDate;
    private String[] attInterval;
    private String[] attCourse;
    private String[] attTeacher;
    private String[] attStatus;
    private AttendanceAdapter.Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public AttendanceAdapter(String[] attDate, String[] attInterval, String[] attCourse, String[] attTeacher, String[] attStatus){
        this.attDate = attDate;
        this.attInterval = attInterval;
        this.attCourse = attCourse;
        this.attTeacher = attTeacher;
        this.attStatus = attStatus;
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attendance, parent, false);

        return new AttendanceAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;

        TextView tvRecheckDate = cardView.findViewById(R.id.tvRecheckDate);
        tvRecheckDate.setText(attDate[position]);

        TextView tvRecheckInterval = cardView.findViewById(R.id.tvRecheckInterval);
        tvRecheckInterval.setText(attInterval[position]);

        TextView tvRecheckCourse = cardView.findViewById(R.id.tvRecheckCourse);
        tvRecheckCourse.setText(attCourse[position]);

        TextView tvRecheckTeacher = cardView.findViewById(R.id.tvRecheckTeacher);
        tvRecheckTeacher.setText(attTeacher[position]);

        TextView tvRecheckStatus = cardView.findViewById(R.id.tvRecheckStatus);
        tvRecheckStatus.setText(attStatus[position]);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attDate.length;
    }

    public void setListener(AttendanceAdapter.Listener listener){
        this.listener = listener;
    }




}
