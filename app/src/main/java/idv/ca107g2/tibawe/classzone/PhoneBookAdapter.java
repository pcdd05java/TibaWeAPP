package idv.ca107g2.tibawe.classzone;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.MemberVO;

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.ViewHolder>{

    private List<MemberVO> memberVOlist;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public PhoneBookAdapter(List<MemberVO> memberVOlist){
        this.memberVOlist = memberVOlist;
    }

    @NonNull
    @Override
    public PhoneBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_phonebook, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final MemberVO memberVO = memberVOlist.get(position);

        TextView tvpbNo = cardView.findViewById(R.id.tvpbNo);
        if(!memberVO.getMemberAccount().isEmpty()) {
            tvpbNo.setText(String.valueOf(position+1)); }else{
            tvpbNo.setText("-");}

        TextView tvpbName = cardView.findViewById(R.id.tvpbName);
        if(!memberVO.getMemberName().isEmpty()) {
            tvpbName.setText(memberVO.getMemberName()); }else{
            tvpbName.setText("---");}

        TextView tvpbTel = cardView.findViewById(R.id.tvpbTel);
        if(!memberVO.getMemberPhone().toString().isEmpty()) {
            tvpbTel.setText(memberVO.getMemberPhone().toString()); }else{
            tvpbTel.setText("---");}

        TextView tvpbEmail = cardView.findViewById(R.id.tvpbEmail);
        if(!memberVO.getMemberEmail().isEmpty()) {
            tvpbEmail.setText(memberVO.getMemberEmail()); }else{
            tvpbEmail.setText("---");}

        TextView tvpbAdd = cardView.findViewById(R.id.tvpbAdd);
        if(!memberVO.getMemberAddress().isEmpty()) {
            tvpbAdd.setText(memberVO.getMemberAddress()); }else{
            tvpbAdd.setText("---");}

    }

    @Override
    public int getItemCount() {
        return memberVOlist.size();
    }





}
