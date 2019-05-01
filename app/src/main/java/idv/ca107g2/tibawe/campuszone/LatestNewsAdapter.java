package idv.ca107g2.tibawe.campuszone;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import idv.ca107g2.tibawe.R;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.ViewHolder>{

    private String[] latestnews;
    private String[] newsContent;
    private int[] imageIds;
    private LatestNewsAdapter.Listener listener;

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

    public LatestNewsAdapter(String[] latestnews, int[] imageIds, String[] newsContent){
        this.latestnews = latestnews;
        this.imageIds = imageIds;
        this.newsContent = newsContent;
    }

    @NonNull
    @Override
    public LatestNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_latest_news, parent, false);

        return new LatestNewsAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestNewsAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView cvivNewsPic = cardView.findViewById(R.id.cvivNewsPic);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imageIds[position]);
        cvivNewsPic.setImageDrawable(drawable);
        cvivNewsPic.setContentDescription(latestnews[position]);

        TextView cvtvNewsTitle = cardView.findViewById(R.id.cvtvNewsTitle);
        cvtvNewsTitle.setText(latestnews[position]);
        TextView cvtvNewsContent = cardView.findViewById(R.id.cvtvNewsContent);
        cvtvNewsContent.setText(newsContent[position]);


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
        return latestnews.length;
    }

    public void setListener(LatestNewsAdapter.Listener listener){
        this.listener = listener;
    }




}
