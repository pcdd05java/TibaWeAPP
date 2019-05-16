package idv.ca107g2.tibawe.lifezone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.lang.reflect.Type;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.ImageTask;
import idv.ca107g2.tibawe.tools.CirclePagerIndicatorDecoration;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.StoreInformationVO;
import idv.ca107g2.tibawe.vo.StoreMenuVO;

public class StoreDetailActivity extends AppCompatActivity {
    private static final String TAG = "SDetailActivity";

    private Dialog menuDialog;
    private StoreInformationVO storeInformationVO;
    private StoreMenuVO storeMenuVO;
    private TextView tvSDetailTitle, tvSDetailPhone, tvSDetailAdress, tvSDetailNote;
    private RecyclerView rvSDetailPic, rvSM;
    private ImageTask sDetailImageTask;
    private CommonTask getPiccountTask, getStoreMenuTask;
    private int piccount;
    private ImageButton btnMenu;
    private Button btnSMReturn, btnSMSubmit;
    private List<StoreMenuVO> smList;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();

        storeInformationVO = (StoreInformationVO) bundle.getSerializable("storeInformationVO");

        rvSDetailPic = findViewById(R.id.rvSDetailPic);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSDetailPic.setLayoutManager(layoutManager);
        rvSDetailPic.addItemDecoration(new CirclePagerIndicatorDecoration());
        rvSDetailPic.setAdapter(new SDetailPicAdapter(this, storeInformationVO));
        rvSDetailPic.setHorizontalScrollBarEnabled(false);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvSDetailPic);


        getView();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default)
                .setOnInterceptMoveEventListener(
                        new SwipeBack.OnInterceptMoveEventListener() {
                            @Override
                            public boolean isViewDraggable(View v, int dx,
                                                           int x, int y) {
                                if (v == rvSDetailPic) {
                                    return (rvSDetailPic.canScrollHorizontally(-1));
                                }

                                return false;
                            }
                        });

    }

    public void getView(){

        btnMenu = findViewById(R.id.btnMenu);


        tvSDetailTitle = findViewById(R.id.tvSDetailTitle);
        tvSDetailTitle.setText(storeInformationVO.getStore_name());

        tvSDetailPhone = findViewById(R.id.tvSDetailPhone);
        tvSDetailPhone.setText(storeInformationVO.getStore_phone());

        tvSDetailAdress = findViewById(R.id.tvSDetailAdress);
        tvSDetailAdress.setText(storeInformationVO.getStore_adress());
//
//        tvSDetailNote = findViewById(R.id.tvSDetailNote);
//        tvSDetailNote.setText(storeInformationVO.getStore_note());

    }


    public Dialog clickMenu(View view) {

        menuDialog = new Dialog(StoreDetailActivity.this);
        menuDialog.setTitle(getString(R.string.store_detail_menu));
        // 使用者無法自行取消對話視窗，需要進行操作才行
        menuDialog.setCancelable(true);
        menuDialog.setContentView(R.layout.dialog_store_menu);
        Toolbar toolbar = menuDialog.findViewById(R.id.dialog_menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.store_detail_menu);

        rvSM = menuDialog.findViewById(R.id.rvSM);
        StaggeredGridLayoutManager smlayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvSM.setLayoutManager(smlayoutManager);

        findMenu();

        // 透過myDialog.getWindow()取得這個對話視窗的Window物件
        Window dialogWindow = menuDialog.getWindow();
                /*
                    設定對話視窗位置：
                    當参數值包含Gravity.LEFT時，對話視窗出現在左邊
                    當参數值包含Gravity.RIGHT時，對話視窗出現在右邊
                    當参數值包含Gravity.TOP時，對話視窗出現在上邊,
                    當参數值包含Gravity.BOTTOM時，對話視窗出現在下邊
                    當参數值包含Gravity.CENTER_HORIZONTAL時，對話視窗水平居中
                    當参數值包含Gravity.CENTER_VERTICAL時，對話視窗垂直居中
                    gravity的默認值為Gravity.CENTER，即Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
                 */
        dialogWindow.setGravity(Gravity.CENTER);

                 /*
                    設定對話視窗大小：
                    呼叫getAttributes()，取得LayoutParams物件即可進行屬性設定
                    相關屬性：
                    x：X座標
                    y：Y座標
                    width：寬度
                    height：高度
                    alpha：透明度 (0.0 ～ 1.0)
                 */
//                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                        lp.width = 1000;
//                        lp.alpha = 1.0f;
//                        dialogWindow.setAttributes(lp);

                /*
                    將對話視窗的大小依螢幕大小的百分比設置
                 */

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
        p.height = (int) (d.getHeight() * 0.65); // 高度設置為螢幕的0.6 (60%)
        p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
        dialogWindow.setAttributes(p);

        btnSMReturn = menuDialog.findViewById(R.id.btnSMReturn);
        btnSMReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuDialog.cancel();
            }
        });
        // 小心！一定要記得show()
        menuDialog.show();

        return menuDialog;


    }

    public void findMenu(){
        String url = Util.URL + "StoreMenuServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getStoreMenu");
        jsonObject.addProperty("store_no", storeInformationVO.getStore_no());
        String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
        if (Util.networkConnected(this)) {
            getStoreMenuTask = new CommonTask(url, jsonOut);
            try {
                String result = getStoreMenuTask.execute().get();
                Type collectionType = new TypeToken<List<StoreMenuVO>>() {
                }.getType();
                smList = gson.fromJson(result, collectionType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            rvSM.setAdapter(new StoreMenuAdapter(this, smList));
        }else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    ///////////StorePic

    private class SDetailPicAdapter extends RecyclerView.Adapter<SDetailPicAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private StoreInformationVO storeInformationVO;
        private int imageSize;

        SDetailPicAdapter(Context context, StoreInformationVO storeInformationVO) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.storeInformationVO = storeInformationVO;
            /* 螢幕寬度當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView cvivDetailPic;

            MyViewHolder(View itemView) {
                super(itemView);
                cvivDetailPic = itemView.findViewById(R.id.cvivDetailPic);
            }
        }

        @Override
        public int getItemCount() {
            String url = Util.URL + "StoreInformationServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPiccount");
            String pk_no = storeInformationVO.getStore_no();
            jsonObject.addProperty("pk_no", pk_no);
            String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
            if (Util.networkConnected(StoreDetailActivity.this)) {
                getPiccountTask = new CommonTask(url, jsonOut);
                try {
                    piccount = Integer.valueOf(getPiccountTask.execute().get());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }else {
                Util.showToast(StoreDetailActivity.this, R.string.msg_NoNetwork);
            }
            return piccount;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.cardview_detail_pic, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String url = Util.URL + "StoreInformationServlet";
            String pk_no = storeInformationVO.getStore_no();
            int picnum = position+1;
            sDetailImageTask = new ImageTask(url, pk_no, imageSize, holder.cvivDetailPic, picnum);
            sDetailImageTask.execute();
        }
    }
///////////StoreMenu
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
            TextView cvtvItemNo, cvtvItemName, cvtvItemPrice;

            MyViewHolder(View itemView) {
                super(itemView);
                cvtvItemNo = itemView.findViewById(R.id.cvtvItemNo);
                cvtvItemName = itemView.findViewById(R.id.cvtvItemName);
                cvtvItemPrice = itemView.findViewById(R.id.cvtvItemPrice);
            }
        }

        @Override
        public int getItemCount() {
            return smList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.cardview_store_menu, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            StoreMenuVO storeMenuVO = smList.get(position);
            holder.cvtvItemNo.setText(String.valueOf(position+1));
            holder.cvtvItemName.setText(storeMenuVO.getStorem_name());
            holder.cvtvItemPrice.setText("$ ".concat(String.valueOf(storeMenuVO.getStorem_price())));
        }
    }


        @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
