package idv.ca107g2.tibawe.lifezone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.task.ImageTask;
import idv.ca107g2.tibawe.vo.Renting_House_Information_VO;

class RhiAdapter extends RecyclerView.Adapter<RhiAdapter.ViewHolder> {

    private List<Renting_House_Information_VO> rhiList;
    private Listener listener;
    private ImageTask rhiImageTask;
    private int imageSize;

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

    public RhiAdapter(List<Renting_House_Information_VO> rhiList , int imageSize){
        this.rhiList = rhiList;
        this.imageSize = imageSize;

    }

    @NonNull
    @Override
    public RhiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_rhi, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView cvivRhiPic = cardView.findViewById(R.id.cvivRhiPic);
        final Renting_House_Information_VO rhiVO = rhiList.get(position);
        String url = Util.URL + "Renting_House_Information_Servlet";
        String pk_no = rhiVO.getRhi_no();
        rhiImageTask = new ImageTask(url, pk_no, imageSize, cvivRhiPic);
        rhiImageTask.execute();


        TextView cvtvRhiTitle = cardView.findViewById(R.id.cvtvRhiTitle);
        if(!rhiVO.getRhi_content().isEmpty()) {
            cvtvRhiTitle.setText(rhiVO.getRhi_content().split(",")[4]); }else{
            cvtvRhiTitle.setText("---");}

        TextView cvtvRhiLoc = cardView.findViewById(R.id.cvtvRhiLoc);
        if(!rhiVO.getRhi_content().isEmpty()) {
            cvtvRhiLoc.setText(rhiVO.getRhi_content().split(",")[5].substring(0,6));}else{
            cvtvRhiLoc.setText("---");}

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
        return rhiList.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }




}
