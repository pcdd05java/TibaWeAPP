package idv.ca107g2.tibawe.lifezone;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.DBDMemberVO;
import idv.ca107g2.tibawe.vo.DBDOderVO;
import idv.ca107g2.tibawe.vo.StoreInformationVO;
import idv.ca107g2.tibawe.vo.StoreMenuVO;


/**
 * A simple {@link Fragment} subclass.
 */
public class DBDFragment extends Fragment {

    private static final String TAG = "DBDFragment";
    private CommonTask getDBDTask;
    private List<DBDOderVO> dbdOderVOlist;
    private List<StoreInformationVO> storeInformationVOlist;
    private List<List<DBDMemberVO>> dbdMemberTop3list;
    private List<String> dbdMemberCountlist;
    private List<Map<String, String>> hostDatalist;
    private List<Long> fnl_timelist;
    private RecyclerView rvDBDlist, rvDBD;
    private TextView lastdbd_result, orderDBD, ttlnumDBD, numDBD;
    private Map<String, List> dbdMap;
    private Dialog dbdDialog;
    private CommonTask getStoreMenuTask, addDBDTask;
    private List<StoreMenuVO> smList;
    public boolean clicked = false;
    private Map addoderData = new HashMap();
    ImageButton plus1DBD, minus1DBD, cancel1DBD;
    Button btnDBDsubmit, btnDBDreset;
    LinearLayout lySelectedDBD;
    private String memberaccount;



    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dbd, container, false);

        rvDBDlist = view.findViewById(R.id.rvDBDlist);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvDBDlist.setLayoutManager(layoutManager);
        lastdbd_result = view.findViewById(R.id.lastdbd_result);

        SharedPreferences preferences = getActivity().getSharedPreferences(Util.PREF_FILE,
                getActivity().MODE_PRIVATE);
        memberaccount = preferences.getString("memberaccount", "");

        findDBD();

        return view;

    }

    public void findDBD() {

        String url = Util.URL + "DBDOderServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getNowDBD");
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getDBDTask = new CommonTask(url, jsonOut);
            try {
                String result = getDBDTask.execute().get();
                Type collectionTypeMap = new TypeToken<Map<String, List>>() {
                }.getType();
                dbdMap = gson.fromJson(result, collectionTypeMap);

                Type collectionTypeListDBDOderVO = new TypeToken<List<DBDOderVO>>() {
                }.getType();
                dbdOderVOlist = gson.fromJson(dbdMap.get("dbdOderVOlist").toString(), collectionTypeListDBDOderVO);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (dbdOderVOlist.size()==0) {
                lastdbd_result.setVisibility(View.VISIBLE);
                rvDBDlist.setVisibility(View.GONE);
            } else {
                Type collectionTypeListDBDOderVO = new TypeToken<List<DBDOderVO>>() {
                }.getType();
                dbdOderVOlist = gson.fromJson(dbdMap.get("dbdOderVOlist").toString(), collectionTypeListDBDOderVO);

                Type collectionTypeListStoreInformationVO = new TypeToken<List<StoreInformationVO>>() {
                }.getType();
                storeInformationVOlist = gson.fromJson(dbdMap.get("storeInformationVOlist").toString(), collectionTypeListStoreInformationVO);

                Type collectionTypeListListDBDMemberVO = new TypeToken<List<List<DBDMemberVO>>>() {
                }.getType();
                dbdMemberTop3list = gson.fromJson(dbdMap.get("dbdMemberTop3list").toString(), collectionTypeListListDBDMemberVO);

                Type collectionTypeListMap = new TypeToken<List<Map>>() {
                }.getType();
                hostDatalist = gson.fromJson(dbdMap.get("hostDatalist").toString(), collectionTypeListMap);


                Type collectionTypeListString = new TypeToken<List<String>>() {
                }.getType();
                dbdMemberCountlist = gson.fromJson(dbdMap.get("dbdMemberCountlist").toString(), collectionTypeListString);

                Type collectionTypeListLong = new TypeToken<List<Long>>() {
                }.getType();
                fnl_timelist = gson.fromJson(dbdMap.get("fnl_timelist").toString(), collectionTypeListLong);

                DBDAdapter adapter = new DBDAdapter(dbdOderVOlist, storeInformationVOlist,
                dbdMemberTop3list, hostDatalist, dbdMemberCountlist, fnl_timelist);
                rvDBDlist.setAdapter(adapter);
                adapter.setListener(new DBDAdapter.Listener() {

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(int position) {

                            openDBDdialog(position);
////
                    }
                });
            }

        } else {
//            view = inflater.inflate(R.layout.fragment_course_query, container, false);
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }

    private void openDBDdialog(final int position) {

        DBDOderVO dbdOderVO = dbdOderVOlist.get(position);
        String dbdo_no = dbdOderVO.getDbdo_no();
        String store_no = dbdOderVO.getStore_no();
        addoderData.put("dbdo_no", dbdo_no);

        dbdDialog = new Dialog(getContext());
        dbdDialog.setCancelable(true);
        dbdDialog.setContentView(R.layout.dialog_dbd_order);
        Toolbar dbdtoolbar = dbdDialog.findViewById(R.id.dialog_dbd_toolbar);
        dbdtoolbar.setLogo(R.drawable.icons8_buy_24);
        dbdtoolbar.setTitle("　參加揪團");


        Window dbddialogWindow = dbdDialog.getWindow();
        dbddialogWindow.setGravity(Gravity.CENTER);

        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams p = dbddialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        p.height = (int) (d.getHeight() * 0.75); // 高度設置為螢幕的0.6 (60%)
        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
        dbddialogWindow.setAttributes(p);

        rvDBD = dbdDialog.findViewById(R.id.rvDBD);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        rvDBD.setLayoutManager(layoutManager);

        findMenu(store_no);



        plus1DBD = dbdDialog.findViewById(R.id.plus1DBD);
        minus1DBD = dbdDialog.findViewById(R.id.minus1DBD);
        cancel1DBD = dbdDialog.findViewById(R.id.cancel1DBD);

        btnDBDsubmit = dbdDialog.findViewById(R.id.btnDBDsubmit);
        btnDBDreset = dbdDialog.findViewById(R.id.btnDBDreset);

        lySelectedDBD = dbdDialog.findViewById(R.id.lySelectedDBD);
        orderDBD = dbdDialog.findViewById(R.id.orderDBD);
        ttlnumDBD = dbdDialog.findViewById(R.id.ttlnumDBD);
        numDBD = dbdDialog.findViewById(R.id.numDBD);

        plus1DBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numcount = Integer.parseInt(numDBD.getText().toString());
                if (numcount != 0) {
                    int ttlnumcount = Integer.parseInt(ttlnumDBD.getText().toString());
                    int perprice = ttlnumcount / numcount;

                    numcount++;
                    numDBD.setText(String.valueOf(numcount));

                    ttlnumDBD.setText(String.valueOf(ttlnumcount + perprice));
                }
            }
                });

        minus1DBD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int numcount = Integer.parseInt(numDBD.getText().toString());
                        if(numcount!=0) {
                            int ttlnumcount = Integer.parseInt(ttlnumDBD.getText().toString());
                            int perprice = ttlnumcount / numcount;

                            if (numcount > 1) {
                                numcount--;
                                numDBD.setText(String.valueOf(numcount));

                                ttlnumDBD.setText(String.valueOf(ttlnumcount - perprice));
                            }
                        }
                    }
                });

        cancel1DBD.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                for (int j = 0; j < rvDBD.getChildCount(); j++) {
                    StoreMenuAdapter.MyViewHolder holder = (StoreMenuAdapter.MyViewHolder) rvDBD.findViewHolderForAdapterPosition(j);
                    holder.cvtvcartDBDitem.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartItemNo.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartDBDitemprice.setTextColor(R.color.colorPrimaryDark);
                    lySelectedDBD.setVisibility(View.INVISIBLE);
                    ttlnumDBD.setVisibility(View.INVISIBLE);

                    holder.cartaddDBD.setVisibility(View.VISIBLE);

                    clicked = false;

                    addoderData.put("storem_no", "");
                    addoderData.put("numDBD", "0");
                    addoderData.put("ttlnumDBD" ,"0");

                }


            }
        });

        btnDBDreset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                for (int j = 0; j < rvDBD.getChildCount(); j++) {
                    StoreMenuAdapter.MyViewHolder holder = (StoreMenuAdapter.MyViewHolder) rvDBD.findViewHolderForAdapterPosition(j);
                    holder.cvtvcartDBDitem.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartItemNo.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartDBDitemprice.setTextColor(R.color.colorPrimaryDark);
                    lySelectedDBD.setVisibility(View.INVISIBLE);
                    ttlnumDBD.setVisibility(View.INVISIBLE);

                    holder.cartaddDBD.setVisibility(View.VISIBLE);

                    clicked = false;

                    addoderData.put("storem_no", "");
                    addoderData.put("numDBD", "0");
                    addoderData.put("ttlnumDBD" ,"0");

                }


            }
        });


        btnDBDsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!clicked){
                    lySelectedDBD.setVisibility(View.VISIBLE);
                    orderDBD.setText("您尚未點餐");
                    numDBD.setText("0");
                }else if (clicked){
                addoderData.put("numDBD", numDBD.getText());
                addoderData.put("ttlnumDBD" , ttlnumDBD.getText());

                String url = Util.URL + "DBDMemberServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "addodermember");
                jsonObject.addProperty("memberaccount", memberaccount);
                jsonObject.addProperty("dbdo_no", addoderData.get("dbdo_no").toString());
                jsonObject.addProperty("storem_no", addoderData.get("storem_no").toString());
                jsonObject.addProperty("dbdm_q", addoderData.get("numDBD").toString());
                jsonObject.addProperty("dbdm_change", addoderData.get("ttlnumDBD").toString());


                String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
                if (Util.networkConnected(getActivity())) {
                    addDBDTask = new CommonTask(url, jsonOut);
                    try {
                        String result = addDBDTask.execute().get();
                        String checkDBD = result;

                        if(checkDBD.equals("\"done\"")){
                            DBDFragment.AlertFragment alertFragment = new DBDFragment.AlertFragment();
                            FragmentManager fm = getFragmentManager();
                            alertFragment.show(fm, "alertDBD");

                        }else{
                            Util.showToast(getActivity(), "something wrong..");
                        }


                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }else {
                    Util.showToast(getContext(), R.string.msg_NoNetwork);
                }

                dbdDialog.cancel();
            }
            }
        });
//
//

//

        dbdDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface dialog) {
                for (int j = 0; j < rvDBD.getChildCount(); j++) {
                    StoreMenuAdapter.MyViewHolder holder = (StoreMenuAdapter.MyViewHolder) rvDBD.findViewHolderForAdapterPosition(j);
                    holder.cvtvcartDBDitem.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartItemNo.setTextColor(R.color.colorPrimaryDark);
                    holder.cvtvcartDBDitemprice.setTextColor(R.color.colorPrimaryDark);
                    lySelectedDBD.setVisibility(View.INVISIBLE);
                    ttlnumDBD.setVisibility(View.INVISIBLE);

                    holder.cartaddDBD.setVisibility(View.VISIBLE);

                    clicked = false;
                }
            }
        });

        dbdDialog.show();
    }

    public void findMenu(String store_no){
        String url = Util.URL + "StoreMenuServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getStoreMenu");
        jsonObject.addProperty("store_no", store_no);
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(getActivity())) {
            getStoreMenuTask = new CommonTask(url, jsonOut);
            try {
                String result = getStoreMenuTask.execute().get();
                Type collectionType = new TypeToken<List<StoreMenuVO>>() {
                }.getType();
                smList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            rvDBD.setAdapter(new StoreMenuAdapter(getContext(), smList));
        }else {
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }

    private class StoreMenuAdapter extends RecyclerView.Adapter<StoreMenuAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        List<StoreMenuVO> smList;

        StoreMenuAdapter(Context context, List<StoreMenuVO> smList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.smList = smList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView cvtvcartItemNo, cvtvcartDBDitem, cvtvcartDBDitemprice;
            private ImageButton cartaddDBD;

            MyViewHolder(View itemView) {
                super(itemView);
                cvtvcartItemNo = itemView.findViewById(R.id.cvtvcartItemNo);
                cvtvcartDBDitem = itemView.findViewById(R.id.cvtvcartDBDitem);
                cvtvcartDBDitemprice = itemView.findViewById(R.id.cvtvcartDBDitemprice);
                cartaddDBD = itemView.findViewById(R.id.cartaddDBD);
            }
        }

        @Override
        public int getItemCount() {
            return smList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.cardview_dbd_cart, parent, false);
            return new MyViewHolder(itemView);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            final StoreMenuVO storeMenuVO = smList.get(position);
            holder.cvtvcartItemNo.setText(String.valueOf(position+1));
            holder.cvtvcartDBDitem.setText(storeMenuVO.getStorem_name());
            holder.cvtvcartDBDitemprice.setText("$ ".concat(String.valueOf(storeMenuVO.getStorem_price())));

            if (clicked) {
                holder.cvtvcartDBDitem.setTextColor(Color.argb(60,0,0,0));
                holder.cvtvcartItemNo.setTextColor(Color.argb(60,0,0,0));
                holder.cvtvcartDBDitemprice.setTextColor(Color.argb(60,0,0,0));
                lySelectedDBD.setVisibility(View.VISIBLE);
                ttlnumDBD.setVisibility(View.VISIBLE);

                holder.cartaddDBD.setVisibility(View.INVISIBLE);
            } else {
                holder.cvtvcartDBDitem.setTextColor(R.color.colorPrimaryDark);
                holder.cvtvcartItemNo.setTextColor(R.color.colorPrimaryDark);
                holder.cvtvcartDBDitemprice.setTextColor(R.color.colorPrimaryDark);
                lySelectedDBD.setVisibility(View.INVISIBLE);
                ttlnumDBD.setVisibility(View.INVISIBLE);

                holder.cartaddDBD.setVisibility(View.VISIBLE);
            }
            holder.cartaddDBD.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //ScaleAnimation(float fromX, float toX, float fromY, float toY)
                    //(fromX,toX)X軸從fromX的倍率放大/縮小至toX的倍率
                    //(fromY,toY)X軸從fromY的倍率放大/縮小至toX的倍率
//                Animation am = new AlphaAnimation(1f, 1.5f);
                    Animation am = new ScaleAnimation(1.025f, 0.975f, 1.025f, 0.975f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    //setDuration (long durationMillis) 設定動畫開始到結束的執行時間
                    am.setDuration(500);
                    //setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
                    am.setRepeatCount(0);
                    //將動畫參數設定到圖片並開始執行動畫
                    LinearLayout lv = (LinearLayout) v.getParent();
                    lv.startAnimation(am);
                    return false;
                }
            });
            holder.cartaddDBD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDBD.setText(storeMenuVO.getStorem_name());
                    numDBD.setText("1");
                    ttlnumDBD.setText(String.valueOf(storeMenuVO.getStorem_price()));
                    addoderData.put("storem_no", storeMenuVO.getStorem_no());

                    clicked = true;
                    notifyDataSetChanged();
                }
            });

        }
    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.icons8_checkout_24)
                    .setTitle(R.string.DBD_today)
                    //設定訊息內容
                    .setMessage(R.string.msg_DBD_success)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.abs_alert_return, this)
                    .create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    new CountDownTimer(2000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(getContext(), DBDQueryActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                            alertDialog.dismiss();
                        }
                    }.start();
                    Button posbtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    posbtn.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                }
            });
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(getActivity(), DBDQueryActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }

    }

}


