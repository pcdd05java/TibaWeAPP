package idv.ca107g2.tibawe.campuszone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.ClrrVO;

class AbsApplyAdapter extends RecyclerView.Adapter<AbsApplyAdapter.ViewHolder> {

    private List<ClrrVO> clrrVOList;
    private String membername;

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

    public AbsApplyAdapter(List<ClrrVO> clrrVOList, String membername){
        this.clrrVOList = clrrVOList;
        this.membername = membername;

    }

    @NonNull
    @Override
    public AbsApplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clrr_query, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final ClrrVO clrrVO = clrrVOList.get(position);

        TextView tvClrrNo = cardView.findViewById(R.id.tvClrrNo);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClrrNo.setText(clrrVO.getClrr_no()); }else{
            tvClrrNo.setText("---");}

        TextView tvClrrApplicant = cardView.findViewById(R.id.tvClrrApplicant);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClrrApplicant.setText(membername); }else{
            tvClrrApplicant.setText("---");}

        TextView tvClrrDate = cardView.findViewById(R.id.tvClrrDate);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClrrDate.setText(clrrVO.getClrr_date().toString()); }else{
            tvClrrDate.setText("---");}

        TextView tvClrrRoom = cardView.findViewById(R.id.tvClrrRoom);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClrrRoom.setText(clrrVO.getCr_no()); }else{
            tvClrrRoom.setText("---");}

        TextView tvClassroomReserveRecordsStart = cardView.findViewById(R.id.tvClassroomReserveRecordsStart);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClassroomReserveRecordsStart.setText(parseNum(clrrVO.getClrr_sttime())+":00"); }else{
            tvClassroomReserveRecordsStart.setText("---");}

        TextView tvClassroomReserveRecordsEnd = cardView.findViewById(R.id.tvClassroomReserveRecordsEnd);
        if(!clrrVO.getClrr_no().isEmpty()) {
            tvClassroomReserveRecordsEnd.setText(parseNum(clrrVO.getClrr_endtime())+":00"); }else{
            tvClassroomReserveRecordsEnd.setText("---");}

    }

    @Override
    public int getItemCount() {
        return clrrVOList.size();
    }


    private static String parseNum(int time) {
        if (time >= 10)
            return String.valueOf(time);
        else
            return "0" + String.valueOf(time);
    }



}
