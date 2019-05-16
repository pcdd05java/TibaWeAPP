package idv.ca107g2.tibawe.lifezone;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import idv.ca107g2.tibawe.R;

class LifeZoneAdapter extends RecyclerView.Adapter<LifeZoneAdapter.ViewHolder> {

    private int[] informations;
    private int[] imageIds;
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

    public LifeZoneAdapter(int[] informations, int[] imageIds){
        this.informations = informations;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public LifeZoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_validmain, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.cvivValidmainPic);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imageIds[position]);
        imageView.setImageDrawable(drawable);

        TextView textView = cardView.findViewById(R.id.cvtvValidmainTitle);
        textView.setText(informations[position]);
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //ScaleAnimation(float fromX, float toX, float fromY, float toY)
                //(fromX,toX)X軸從fromX的倍率放大/縮小至toX的倍率
                //(fromY,toY)X軸從fromY的倍率放大/縮小至toX的倍率
//                Animation am = new AlphaAnimation(0.7f, 1f);
                Animation am2 = new ScaleAnimation(1.025f, 0.975f, 1.025f, 0.975f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                Animation am = new ScaleAnimation(1f, 0.7f, 1f, 0.7f);
                //setDuration (long durationMillis) 設定動畫開始到結束的執行時間
                am2.setDuration(100);
                //setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
                am2.setRepeatCount(0);
                //將動畫參數設定到圖片並開始執行動畫
                v.startAnimation(am2);
                return false;
            }
        });
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
        return informations.length;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }




}
