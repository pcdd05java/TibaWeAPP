package idv.ca107g2.tibawe.lifezone;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.Latest_News_VO;

public class HotArticleAdapter extends RecyclerView.Adapter<HotArticleAdapter.ViewHolder>{

    private List<Latest_News_VO> latest_news_list;
    private Listener listener;

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

    public HotArticleAdapter(List<Latest_News_VO> latest_news_list){
        this.latest_news_list = latest_news_list;
    }

    @NonNull
    @Override
    public HotArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_latest_news, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final Latest_News_VO latest_news_VO = latest_news_list.get(position);

        TextView cvtvNewsTitle = cardView.findViewById(R.id.cvtvNewsTitle);
        if(!latest_news_VO.getLn_title().isEmpty()) {
            cvtvNewsTitle.setText(latest_news_VO.getLn_title()); }else{
            cvtvNewsTitle.setText("---");}

        TextView cvtvNewsDate = cardView.findViewById(R.id.cvtvNewsDate);
        if(!latest_news_VO.getLn_date().toString().isEmpty()) {
            cvtvNewsDate.setText(latest_news_VO.getLn_date().toString()); }else{
            cvtvNewsDate.setText("---");}

        TextView cvtvNewsContent = cardView.findViewById(R.id.cvtvNewsContent);
        if(!latest_news_VO.getLn_content().isEmpty()) {
            cvtvNewsContent.setText(latest_news_VO.getLn_content()); }else{
            cvtvNewsContent.setText("---");}



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
        return latest_news_list.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }




}
