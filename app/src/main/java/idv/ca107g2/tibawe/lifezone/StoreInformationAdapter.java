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
import idv.ca107g2.tibawe.vo.StoreInformationVO;

class StoreInformationAdapter extends RecyclerView.Adapter<StoreInformationAdapter.ViewHolder> {

    private List<StoreInformationVO> storeInformationList;
    private Listener listener;
    private ImageTask storeImageTask;
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

    public StoreInformationAdapter(List<StoreInformationVO> storeInformationList , int imageSize){
        this.storeInformationList = storeInformationList;
        this.imageSize = imageSize;

    }

    @NonNull
    @Override
    public StoreInformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_store_info, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView cvivInfoPic = cardView.findViewById(R.id.cvivInfoPic);
        final StoreInformationVO storeInformationVO = storeInformationList.get(position);
        String url = Util.URL + "StoreInformationServlet";
        String pk_no = storeInformationVO.getStore_no();
        storeImageTask = new ImageTask(url, pk_no, imageSize, cvivInfoPic);
        storeImageTask.execute();


        TextView cvtvInfoTitle = cardView.findViewById(R.id.cvtvInfoTitle);
        if(!storeInformationVO.getStore_name().isEmpty()) {
            cvtvInfoTitle.setText(storeInformationVO.getStore_name()); }else{
            cvtvInfoTitle.setText("---");}

        TextView cvtvInfoTel = cardView.findViewById(R.id.cvtvInfoTel);
        if(!storeInformationVO.getStore_phone().isEmpty()) {
            cvtvInfoTel.setText(storeInformationVO.getStore_phone()); }else{
            cvtvInfoTel.setText("---");}

//
//        ImageView cvivInfoPic = cardView.findViewById(R.id.cvivInfoPic);
//        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imageIds[position]);
//        cvivInfoPic.setImageDrawable(drawable);
//        cvivInfoPic.setContentDescription(storeInformationVO.getStore_name());

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
        return storeInformationList.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }




}
