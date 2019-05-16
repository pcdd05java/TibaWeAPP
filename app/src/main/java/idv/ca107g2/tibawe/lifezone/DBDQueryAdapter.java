package idv.ca107g2.tibawe.lifezone;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.DBDMemberVO;
import idv.ca107g2.tibawe.vo.DBDOderVO;
import idv.ca107g2.tibawe.vo.StoreInformationVO;
import idv.ca107g2.tibawe.vo.StoreMenuVO;

public class DBDQueryAdapter extends RecyclerView.Adapter<DBDQueryAdapter.ViewHolder>{

    private List<DBDMemberVO> dbdMemberQuertlist;
    private List<Map<String, String>> dbdQueryhostDatalist;
    private List<DBDOderVO> dbdOderQueryVOlist;
    private List<StoreMenuVO> dbdStoreMenuQueryVOlist;
    private List<StoreInformationVO> dbdQuerystoreInformationVOlist;
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



    public DBDQueryAdapter(List<DBDMemberVO> dbdMemberQuertlist, List<Map<String, String>> dbdQueryhostDatalist,
                           List<DBDOderVO> dbdOderQueryVOlist, List<StoreMenuVO> dbdStoreMenuQueryVOlist,
                           List<StoreInformationVO> dbdQuerystoreInformationVOlist){
        this.dbdMemberQuertlist = dbdMemberQuertlist;
        this.dbdQueryhostDatalist = dbdQueryhostDatalist;
        this.dbdOderQueryVOlist = dbdOderQueryVOlist;
        this.dbdStoreMenuQueryVOlist = dbdStoreMenuQueryVOlist;
        this.dbdQuerystoreInformationVOlist = dbdQuerystoreInformationVOlist;
    }

    @NonNull
    @Override
    public DBDQueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dbd_query, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        DBDMemberVO dbdMemberVO = dbdMemberQuertlist.get(position);
        DBDOderVO dbdOderVO = dbdOderQueryVOlist.get(position);
        StoreMenuVO storeMenuVO = dbdStoreMenuQueryVOlist.get(position);
        StoreInformationVO  storeInformationVO = dbdQuerystoreInformationVOlist.get(position);
        Map<String, String> hostDataMap = dbdQueryhostDatalist.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

        TextView cvtvQueryDBDDate = cardView.findViewById(R.id.cvtvQueryDBDDate);
        if(!dbdOderVO.getDbdo_no().isEmpty()) {
            cvtvQueryDBDDate.setText(sdf.format(dbdOderVO.getFnl_time())); }else{
            cvtvQueryDBDDate.setText("---");}

        TextView cvtvQueryDBDclass = cardView.findViewById(R.id.cvtvQueryDBDclass);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvQueryDBDclass.setText(hostDataMap.get("className")); }else{
            cvtvQueryDBDclass.setText("---");}

        TextView cvtvQueryDBDhost = cardView.findViewById(R.id.cvtvQueryDBDhost);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvQueryDBDhost.setText(hostDataMap.get("memberName")); }else{
            cvtvQueryDBDhost.setText("---");}

        TextView cvtvQueryDBDstore = cardView.findViewById(R.id.cvtvQueryDBDstore);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvQueryDBDstore.setText(storeInformationVO.getStore_name()); }else{
            cvtvQueryDBDstore.setText("---");}

        TextView cvtvQueryDBDItem = cardView.findViewById(R.id.cvtvQueryDBDItem);
        if(!dbdMemberVO.getStorem_no().isEmpty()) {
        cvtvQueryDBDItem.setText(storeMenuVO.getStorem_name()+ "　x" + dbdMemberVO.getDbdm_q() + "　$" + dbdMemberVO.getDbdm_change()); }else{
            cvtvQueryDBDItem.setText("---");}

        TextView cvtvQueryDBDStatus = cardView.findViewById(R.id.cvtvQueryDBDStatus);
        if(!dbdMemberVO.getDbdm_no().isEmpty()) {
            switch (dbdMemberVO.getDbdm_attr()){
                case 0:
                    cvtvQueryDBDStatus.setText(R.string.DBD_query_attr_notpaid_0);
                    cvtvQueryDBDStatus.setTextColor(Color.rgb(209, 8, 55));
                    break;
                case 1:
                    cvtvQueryDBDStatus.setText(R.string.DBD_query_attr_alreadypaid_1);
                    break;
                case 2:
                    cvtvQueryDBDStatus.setText(R.string.DBD_query_attr_denied_2);
                    cvtvQueryDBDStatus.setTextColor(Color.rgb(209, 8, 55));
                    break;
            }
         }


        Button btnChangeDBD = cardView.findViewById(R.id.btnChangeDBD);
        btnChangeDBD.setOnClickListener(new View.OnClickListener() {
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
        return dbdMemberQuertlist.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

}
