package idv.ca107g2.tibawe;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

//import idv.david.bookstoreandroid.book.BookActivity;
//import idv.david.bookstoreandroid.member.MemberShipActivity;
//import idv.david.bookstoreandroid.order.CartActivity;
//import idv.david.bookstoreandroid.order.OrderBook;

public class Util {
    // 模擬器連Tomcat
//    public static String URL = "http://10.0.2.2:8081/CA107G2_APP/";
    // 手機連雲端GCP
    public static String URL = "http://34.80.134.113:8081/CA107G2_APP/";

    // 偏好設定檔案名稱
    public final static String PREF_FILE = "preference";

    public static int qrmsg;


    public static int msgCode(int msg_code){
        switch(msg_code){
            case 0:
                qrmsg = R.string.msg_qr_0_failed;
                break;
            case 1:
                qrmsg = R.string.msg_qr_1_invalid_date;
                break;
            case 2:
                qrmsg = R.string.msg_qr_2_no_need;
                break;
            case 3:
                qrmsg = R.string.msg_qr_3_invalid_delayed;
                break;
            case 4:
                qrmsg = R.string.msg_qr_4_success;
                break;
            case 5:
                qrmsg = R.string.msg_qr_5_already;
                break;
            case 6:
                qrmsg = R.string.msg_qr_6_invalid_interval;
                break;
            case 7:
                qrmsg =  R.string.msg_qr_7_norecord;
                break;
        }
        return qrmsg;
    }

    // 功能分類
//    public final static Page[] PAGES = {
//            new Page(0, "Book", R.drawable.books, BookActivity.class),
//            new Page(1, "Order", R.drawable.cart_empty, CartActivity.class),
//            new Page(2, "Member", R.drawable.user, MemberShipActivity.class),
//            new Page(3, "Setting", R.drawable.setting, ChangeUrlActivity.class)
//    };

    // 要讓商品在購物車內順序能夠一定，且使用RecyclerView顯示時需要一定順序，List較佳
//    public static ArrayList<OrderBook> CART = new ArrayList<>();

    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }


    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
