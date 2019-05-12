package idv.ca107g2.tibawe.campuszone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.RafVO;

class RafAdapter extends RecyclerView.Adapter<RafAdapter.ViewHolder> {

    private List<RafVO> rafVOList;
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

    public RafAdapter(List<RafVO> rafVOList, String membername){
        this.rafVOList = rafVOList;
        this.membername = membername;

    }

    @NonNull
    @Override
    public RafAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_raf_query, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final RafVO rafVO = rafVOList.get(position);

        TextView tvRafStatus = cardView.findViewById(R.id.tvRafStatus);
        if(!rafVO.getRaf_no().isEmpty()) {
            switch(rafVO.getRaf_status()){
                case 0:
                    LinearLayout lnLytraf = cardView.findViewById(R.id.lnLytraf);
                    lnLytraf.setVisibility(View.GONE);
                    tvRafStatus.setText(R.string.repair_status_0_notyet);
                    break;
                case 1:
                    tvRafStatus.setText(R.string.repair_status_1_ongoing);
                    break;
                case 2:
                    TextView tvRafRecord = cardView.findViewById(R.id.tvRafRecord);
                    tvRafRecord.setText(rafVO.getRaf_record());
                    tvRafStatus.setText(R.string.repair_status_2_done);
                    break;
            }
        }else {
            tvRafStatus.setText("---");
        }

        TextView tvRafNo = cardView.findViewById(R.id.tvRafNo);
        if(!rafVO.getRaf_no().isEmpty()) {
            tvRafNo.setText(rafVO.getRaf_no()); }else{
            tvRafNo.setText("---");}

        TextView tvRafLoc = cardView.findViewById(R.id.tvRafLoc);
        if(!rafVO.getRaf_no().isEmpty()) {
            tvRafLoc.setText(rafVO.getRaf_loc()); }else{
            tvRafLoc.setText("---");}

        TextView tvRafType = cardView.findViewById(R.id.tvRafType);
        if(!rafVO.getRaf_no().isEmpty()) {
            tvRafType.setText(rafVO.getRaf_type()); }else{
            tvRafType.setText("---");}

        TextView tvRafCon = cardView.findViewById(R.id.tvRafCon);
        if(!rafVO.getRaf_no().isEmpty()) {
            tvRafCon.setText(rafVO.getRaf_con()); }else{
            tvRafCon.setText("---");}

        TextView tvRafNote = cardView.findViewById(R.id.tvRafNote);
        if(!rafVO.getRaf_no().isEmpty()) {
            tvRafNote.setText(rafVO.getRaf_note()); }else{
            tvRafNote.setText("---");}

    }

    @Override
    public int getItemCount() {
        return rafVOList.size();
    }


}
