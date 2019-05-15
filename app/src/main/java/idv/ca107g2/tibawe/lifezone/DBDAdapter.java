package idv.ca107g2.tibawe.lifezone;


import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.vo.DBDMemberVO;
import idv.ca107g2.tibawe.vo.DBDOderVO;
import idv.ca107g2.tibawe.vo.StoreInformationVO;

public class DBDAdapter extends RecyclerView.Adapter<DBDAdapter.ViewHolder>{

    private List<DBDOderVO> dbdOderVOlist;
    private List<StoreInformationVO> storeInformationVOlist;
    private List<List<DBDMemberVO>> dbdMemberTop3list;
    private List<Map<String, String>> hostDatalist;
    private List<String> dbdMemberCountlist;
    private List<Long> fnl_timelist;
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



    public DBDAdapter(List<DBDOderVO> dbdOderVOlist, List<StoreInformationVO> storeInformationVOlist,
                      List<List<DBDMemberVO>> dbdMemberTop3list, List<Map<String, String>> hostDatalist,
                      List<String> dbdMemberCountlist, List<Long> fnl_timelist){
        this.dbdOderVOlist = dbdOderVOlist;
        this.storeInformationVOlist = storeInformationVOlist;
        this.dbdMemberTop3list = dbdMemberTop3list;
        this.hostDatalist = hostDatalist;
        this.dbdMemberCountlist = dbdMemberCountlist;
        this.fnl_timelist = fnl_timelist;
    }

    @NonNull
    @Override
    public DBDAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dbd_order, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        DBDOderVO dbdOderVO = dbdOderVOlist.get(position);
        StoreInformationVO  storeInformationVO = storeInformationVOlist.get(position);
        List<DBDMemberVO>  dbdMemberVOlist = dbdMemberTop3list.get(position);
        Map<String, String> hostDataMap = hostDatalist.get(position);
        String dbdmembercount = dbdMemberCountlist.get(position);
        Long fnl_time = fnl_timelist.get(position);

        TextView cvtvDBDstore = cardView.findViewById(R.id.cvtvDBDstore);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvDBDstore.setText("#" + String.valueOf(position+1)+" "+storeInformationVO.getStore_name()); }else{
            cvtvDBDstore.setText("---");}

        TextView cvtvDBDclass = cardView.findViewById(R.id.cvtvDBDclass);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvDBDclass.setText(hostDataMap.get("className")); }else{
            cvtvDBDclass.setText("---");}

        TextView cvtvDBDhost = cardView.findViewById(R.id.cvtvDBDhost);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvDBDhost.setText(hostDataMap.get("memberName")); }else{
            cvtvDBDhost.setText("---");}

        TextView cvtvItemName1 = cardView.findViewById(R.id.cvtvItemName1);
        if(dbdMemberVOlist.size()>0) {
            cvtvItemName1.setText(dbdMemberVOlist.get(0).getStorem_no()); }else{
            cvtvItemName1.setText("---");}

        TextView cvtvItemName2 = cardView.findViewById(R.id.cvtvItemName2);
        if(dbdMemberVOlist.size()>1) {
            cvtvItemName2.setText(dbdMemberVOlist.get(1).getStorem_no()); }else{
            cvtvItemName2.setText("---");}

        TextView cvtvItemName3 = cardView.findViewById(R.id.cvtvItemName3);
        if(dbdMemberVOlist.size()>2) {
            cvtvItemName3.setText(dbdMemberVOlist.get(2).getStorem_no()); }else{
            cvtvItemName3.setText("---");}

        TextView cvtvDBDnowMemCount = cardView.findViewById(R.id.cvtvDBDnowMemCount);
        if(!dbdOderVO.getStore_no().isEmpty()) {
            cvtvDBDnowMemCount.setText(dbdmembercount.concat(" 人")); }else{
            cvtvDBDnowMemCount.setText("0".concat(" 人"));}



        if(!dbdOderVO.getStore_no().isEmpty()) {
            //開團倒數計時器
            java.sql.Timestamp nowTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
            Long countdown = fnl_time - (nowTimestamp.getTime());

            new CountDownTimer(countdown,1000){
                TextView cvtvDBDcountdown = cardView.findViewById(R.id.cvtvDBDcountdown);

                @Override
                public void onFinish() {
                    cvtvDBDcountdown.setText("00:00:00");
                }

                @Override
                public void onTick(long millisUntilFinished) {

                      cvtvDBDcountdown.setText(parseLong(millisUntilFinished));

                }

            }.start();

             }else{
            TextView cvtvDBDcountdown = cardView.findViewById(R.id.cvtvDBDcountdown);
            cvtvDBDcountdown.setText("---");}

        ImageButton btnJoinDBD = cardView.findViewById(R.id.btnJoinDBD);


        btnJoinDBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    public String parseLong(Long millisUntilFinished){

//        int ttlsecs = (int)(millisUntilFinished/1000) , hrs = 24, mins = 60, secs = 60;
//        int daysec = hrs*mins*secs, hrssec = mins*secs; //一天幾秒鐘, 一小時幾秒鐘
//        int hrsleft = ttlsecs%daysec, minleft = hrsleft%hrssec, secleft = minleft%secs;//剩餘多少小時秒, 剩餘多少分鐘秒, 剩餘多少秒

//        System.out.println("256559秒為"+ (ttlsecs/daysec) +"天"+ (hrsleft/hrssec) +"小時"+ (minleft/secs) +"分"+secleft+"秒");

                    long day = millisUntilFinished / (1000 * 24 * 60 * 60); //单位天
                    long hour = (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60); //单位时
                    long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60); //单位分
                    long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;//单位秒


        return parseNum(hour) +":"+parseNum(minute) +":"+ parseNum(second);  }


    // 若數字有十位數，直接顯示；若只有個位數則補0後再顯示。例如7會改成07後再顯示
    private static String parseNum(long day) {
        if (day >= 10)
            return String.valueOf(day);
        else
            return "0" + String.valueOf(day);
    }

    @Override
    public int getItemCount() {
        return dbdOderVOlist.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

}
