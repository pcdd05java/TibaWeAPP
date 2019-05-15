package idv.ca107g2.tibawe.lifezone;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.ImageTask;
import idv.ca107g2.tibawe.tools.CirclePagerIndicatorDecoration;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.Renting_House_Information_VO;

public class RhiDetailActivity extends AppCompatActivity {
    private static final String TAG = "RhiDetailActivity";

    private Renting_House_Information_VO rhiVO;
    private TextView tvRhiDetailNote, tvRhiDetailTitle, tvRhiDetailName,tvRhiDetailPrice, tvRhiDetailPhone, tvRhiDetailLoc;
    private RecyclerView rvRhiDetailPic;
    private ImageTask rhiDetailImageTask;
    private CommonTask getPiccountTask;
    private int piccount;
    private List<Renting_House_Information_VO> rhiList;

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhi_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();

        rhiVO = (Renting_House_Information_VO) bundle.getSerializable("rhiVO");

        rvRhiDetailPic = findViewById(R.id.rvRhiDetailPic);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRhiDetailPic.setLayoutManager(layoutManager);
        rvRhiDetailPic.addItemDecoration(new CirclePagerIndicatorDecoration());
        rvRhiDetailPic.setAdapter(new RhiDetailPicAdapter(this, rhiVO));
        rvRhiDetailPic.setHorizontalScrollBarEnabled(false);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvRhiDetailPic);


        getView();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default)
                .setOnInterceptMoveEventListener(
                        new SwipeBack.OnInterceptMoveEventListener() {
                            @Override
                            public boolean isViewDraggable(View v, int dx,
                                                           int x, int y) {
                                if (v == rvRhiDetailPic) {
                                    return (rvRhiDetailPic.canScrollHorizontally(-1));
                                }

                                return false;
                            }
                        });

    }

    public void getView(){

        tvRhiDetailNote = findViewById(R.id.tvRhiDetailNote);
        tvRhiDetailNote.setText(rhiVO.getRhi_content().split(",")[3]);

        tvRhiDetailTitle = findViewById(R.id.tvRhiDetailTitle);
        tvRhiDetailTitle.setText(rhiVO.getRhi_content().split(",")[4]);

        tvRhiDetailName = findViewById(R.id.tvRhiDetailName);
        tvRhiDetailName.setText(rhiVO.getRhi_content().split(",")[0]);

        tvRhiDetailPrice = findViewById(R.id.tvRhiDetailPrice);
        tvRhiDetailPrice.setText("$ ".concat(rhiVO.getRhi_content().split(",")[2]));

        tvRhiDetailPhone = findViewById(R.id.tvRhiDetailPhone);
        tvRhiDetailPhone.setText(rhiVO.getRhi_content().split(",")[1]);

        tvRhiDetailLoc = findViewById(R.id.tvRhiDetailLoc);
        tvRhiDetailLoc.setText(rhiVO.getRhi_content().split(",")[5]);

    }


    ///////////StorePic

    private class RhiDetailPicAdapter extends RecyclerView.Adapter<RhiDetailPicAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private Renting_House_Information_VO rhiVO;
        private int imageSize;

        RhiDetailPicAdapter(Context context, Renting_House_Information_VO rhiVO) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.rhiVO = rhiVO;
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
            String url = Util.URL + "Renting_House_Information_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPiccount");
            String pk_no = rhiVO.getRhi_no();
            jsonObject.addProperty("pk_no", pk_no);
            String jsonOut = jsonObject.toString();
//        Util.showToast(getContext(), jsonOut);
            if (Util.networkConnected(RhiDetailActivity.this)) {
                getPiccountTask = new CommonTask(url, jsonOut);
                try {
                    piccount = Integer.valueOf(getPiccountTask.execute().get());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }else {
                Util.showToast(RhiDetailActivity.this, R.string.msg_NoNetwork);
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
            String url = Util.URL + "Renting_House_Information_Servlet";
            String pk_no = rhiVO.getRhi_no();
            int picnum = position+1;
            rhiDetailImageTask = new ImageTask(url, pk_no, imageSize, holder.cvivDetailPic, picnum);
            rhiDetailImageTask.execute();
        }
    }

        @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }
}
