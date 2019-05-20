package idv.ca107g2.tibawe.lifezone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.io.IOException;
import java.util.List;

import idv.ca107g2.tibawe.R;
import idv.ca107g2.tibawe.task.CommonTask;
import idv.ca107g2.tibawe.task.ImageTask;
import idv.ca107g2.tibawe.tools.CirclePagerIndicatorDecoration;
import idv.ca107g2.tibawe.tools.Util;
import idv.ca107g2.tibawe.vo.Renting_House_Information_VO;

public class RhiDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "RhiDetailActivity";

    private Renting_House_Information_VO rhiVO;
    private TextView tvRhiDetailNote, tvRhiDetailTitle, tvRhiDetailName,tvRhiDetailPrice, tvRhiDetailPhone, tvRhiDetailLoc;
    private RecyclerView rvRhiDetailPic;
    private ImageTask rhiDetailImageTask;
    private CommonTask getPiccountTask;
    private int piccount;
    private List<Renting_House_Information_VO> rhiList;

    private GoogleMap map;

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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.rhiMap);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
    }
    @SuppressLint("MissingPermission")
    private void setUpMap() {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        String locationName = tvRhiDetailLoc.getText().toString().trim();
        if (locationName.length() > 0) {
            locationNameToMarker(locationName);
        } else {
            Toast.makeText(this, getString(R.string.msg_nodata), Toast.LENGTH_SHORT).show();
        }
    }


    // 將地名或地址轉成位置後在地圖打上對應標記
    private void locationNameToMarker(String locationName) {
        // 增加新標記前，先清除舊有標記
        map.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            // 解譯地名/地址後可能產生多筆位置資訊，所以回傳List<Address>
            // 將maxResults設為1，限定只回傳1筆
            addressList = geocoder.getFromLocationName(locationName, maxResults);
//            geocoder.getFromLocation()

            // 如果無法連結到提供服務的伺服器，會拋出IOException
        } catch (IOException ie) {
            Log.e(TAG, ie.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_nodata), Toast.LENGTH_SHORT).show();
        } else {
            // 因為當初限定只回傳1筆，所以只要取得第1個Address物件即可
            Address address = addressList.get(0);

            // Address物件可以取出緯經度並轉成LatLng物件
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            // 將地址取出當作標記的描述文字
            String snippet = address.getAddressLine(0);

            String code = address.getPostalCode();


            // 將地名或地址轉成位置後在地圖打上對應標記
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(code + locationName)
                    .snippet(snippet));

            // 將鏡頭焦點設定在使用者輸入的地點上
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(17)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
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
