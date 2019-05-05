package idv.ca107g2.tibawe.classzone;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.ClassInformationVO;

public class ClassInformationAdapter extends RecyclerView.Adapter<ClassInformationAdapter.ViewHolder>{

    private List<ClassInformationVO> classInformationList;
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

    public ClassInformationAdapter(List<ClassInformationVO> classInformationList){
        this.classInformationList = classInformationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_class_information, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        final ClassInformationVO classInformationVO = classInformationList.get(position);

        TextView cvtvClassInfoDate = cardView.findViewById(R.id.cvtvClassInfoDate);
        if(!classInformationVO.getIdate().toString().isEmpty()) {
            cvtvClassInfoDate.setText(classInformationVO.getIdate().toString()); }else{
            cvtvClassInfoDate.setText("---");}

        TextView cvtvClassInfo = cardView.findViewById(R.id.cvtvClassInfo);
        if(!classInformationVO.getInformationcontent().isEmpty()) {
            cvtvClassInfo.setText(classInformationVO.getInformationcontent()); }else{
            cvtvClassInfo.setText("---");}

        TextView cvtvClassName = cardView.findViewById(R.id.cvtvClassName);
        if(!classInformationVO.getClass_no().isEmpty()) {
            cvtvClassName.setText(classInformationVO.getClass_no()); }else{
            cvtvClassName.setText("---");}


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
        return classInformationList.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }




}
