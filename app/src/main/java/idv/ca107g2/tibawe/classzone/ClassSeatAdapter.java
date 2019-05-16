package idv.ca107g2.tibawe.classzone;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import idv.ca107g2.tibawe.R;

class ClassSeatAdapter extends RecyclerView.Adapter<ClassSeatAdapter.ViewHolder> {

    private String[] seatArray;

//    private Listener listener;

//    interface Listener {
//        void onClick(int position);
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public ClassSeatAdapter(String[] realSeat){
        this.seatArray = realSeat;
    }

    @NonNull
    @Override
    public ClassSeatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv =
                (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_class_seat, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;

        TextView cvtvSeatName = cardView.findViewById(R.id.cvtvSeatName);
        cvtvSeatName.setText(seatArray[position]);

//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (listener != null){
//                    listener.onClick(position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return seatArray.length;
    }

//    public void setListener(Listener listener){
//        this.listener = listener;
//    }




}
