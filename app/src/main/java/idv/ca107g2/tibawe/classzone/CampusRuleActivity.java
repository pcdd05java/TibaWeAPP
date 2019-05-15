package idv.ca107g2.tibawe.classzone;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import idv.ca107g2.tibawe.R;

public class CampusRuleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_campus_rule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



       findView();

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setSwipeBackView(R.layout.swipeback_default);

    }

    public void findView(){

        TextView tvRuleTitle = findViewById(R.id.tvRuleTitle);
        TextView tvRuleVersion = findViewById(R.id.tvRuleVersion);
        TextView tvP1title = findViewById(R.id.tvP1title);
        TextView tvP1content = findViewById(R.id.tvP1content);
        TextView tvP2title = findViewById(R.id.tvP2title);
        TextView tvP2content = findViewById(R.id.tvP2content);
        TextView tvP3title = findViewById(R.id.tvP3title);
        TextView tvP3content = findViewById(R.id.tvP3content);
        TextView tvP4title = findViewById(R.id.tvP4title);
        TextView tvP4content = findViewById(R.id.tvP4content);

        tvRuleTitle.setText("學員受訓須知");
        tvRuleVersion.setText("修訂時間：一○七年三月五日");

        tvP1title.setText("壹、中心介紹");
        tvP1content.setText("\n"+"一、本中心隸屬資訊工業策進會數位教育研究所，其成立宗旨以辦理資訊人才推廣教育與資訊軟體研究發展為主。\n" +
                "\n" +
                "二、本中心資訊人才推廣教育係與國立中央大學合作，受中央大學指導與支援。\n" +
                "\n" +
                "三、本中心現有教學大樓位於中央大學工程二館的一至三樓。\n" +
                "\n" +
                "四、配合中央大學規定，本中心教學大樓全面禁煙。");

        tvP2title.setText("貳、教務行政");
        tvP2content.setText("\n"+"一、本中心教學大樓開放時間為八時三十分至二十二時三十分，若遇重大節日將不開放，屆時將另行通告。\n" +
                "\n" +
                "二、本中心上課時間如下：\n" +
                "　　　　　上午09:00~12:00\n"+"" +
                "　　　　　下午13:30~16:30\n"+"" +
                "　　　　　夜間18:30~21:30\n" +
                "\n" +
                "三、每節上課所使用的教室及機房，可參考網路課表上所刊載者，若有臨時異動會通知大家。\n" +
                "\n" +
                "四、訓練資源有限，請善加珍惜，務必請準時上課，並儘量保持全勤。\n" +
                "\n" +
                "五、每位學員務必於上、下午及夜間上課時使用紙本簽到，簽名時請簽全名，並可看出是簽哪些字，勿使用印章或代簽。可簽到時間的規定請依中心規範。\n" +
                "\n" +
                "六、受訓學員無故不到者視同曠課，曠課時數將以兩倍計算。若不能上課請事先請假，請假包含事假、病假、公假、產假、喪假五種。事假須事前請，病假三日以上（含）須醫院證明，公假須附證明文件，喪假須有訃文。\n" +
                "\n" +
                "七、每日上午9:15、下午1:45、夜間6:45之後無法簽到，若是當天遲到者請填寫黃色補簽到單，並請上課老師簽名後，拿至一樓櫃台才能補簽名。補簽到單及請假單請至一樓櫃檯拿取。\n" +
                "\n" +
                "八、短期班（四週或 100小時以下）請假（含公假）達到上課總時數五分之一者，長期班（四週或 100小時以上（含））請假（含公假）達到上課總時數十分之一者，即公告退訓，並不發結業證書。曠課乙次以請假兩次時數計算。\n" +
                "\n" +
                "九、總平均成績未達標準(60分或等級F)者，不核發結業證書。\n" +
                "\n" +
                "十、上課時間由教務行政人員不定期抽點到課情況，抽點不到視同曠課乙次。\n" +
                "\n" +
                "十一、請假記錄總表於結訓後函知委訓單位，退訓者亦函知委訓單位。\n" +
                "\n" +
                "十二、學員如要查詢課表，可至中心網站『班級課表系統』查詢，網址為：http://140.115.236.11。\n" +
                "\n" +
                "十三、使用實習機房時請勿攜帶茶水、食物及雨具入內，亦請不要在實習機房內長期放置私人物品或設備，應保持機房或教室內的整齊清潔，本中心將定期清理。\n" +
                "\n" +
                "十四、不得在機房或教室內煮食食物，包含使用咖啡機煮熱飲。也請勿於機房或教室內吸菸。\n" +
                "\n" +
                "十五、實習時，遇機器設備有問題，應立即向實習老師反映，並自行填妥機房內之「故障紀錄單」，絕對禁止拆開機器，或任意將機器設備搬離定位、換接插頭，情節嚴重者將可能退訓。\n" +
                "\n" +
                "十六、實習機房週一至週五夜間開放上機至22:30，週六、日部份機房開放至21:30，遇重大節日將不開放，將另行通告。夜間機房與假日機房需依規定提出線上使用申請，若中心有夜間或假日的課程活動時，或有其他原因，得調度某間機房或教室做臨時使用，則此時該班學員在使用夜間或假日機房時，請依循中心相關的彈性調動，與其他班學員共同使用同一間機房或教室。\n" +
                "\n" +
                "十七、若遇中心有機房或教室座位容量之調度考量，得於受訓期間內變動或更換各班級原使用的機房或教室，請學員配合搬遷，以利中心所有學員都能享有最大的學習效益。\n" +
                "\n" +
                "十八、本中心設有學員休息室，開放時間為08:30－22:30。\n" +
                "\n" +
                "十九、學員休息室報章雜誌，僅供當場閱讀，不外借。　\n" +
                "\n" +
                "二十、配合環保與資源再利用政策，本中心各樓層設有資源回收設備，請依分類投入各回收資源。\n" +
                "\n" +
                "二十一、偷竊中心硬體設備、違法盜拷軟體或使用、下載、安裝非法軟體者，將移送法辦。若因學員前述違法行為，致資策會與委訓單位被請求或被訴，學員應賠償資策會與委訓單位之一切損失（包括但不限於律師費、訴訟費用等）。\n" +
                "\n" +
                "二十二、為使課程順利進行，上課中請學員關閉行動電話。且為免侵害講師或資策會之權利，嚴禁學員錄音、攝影或錄影等，否則一切法律責任 請學員自負。");

        tvP3title.setText("參、日常生活");
        tvP3content.setText("\n"+"一、中央大學內有學生餐廳，後門、側門有快餐、客飯，按餐計費，費用自理。\n" +
                "\n" +
                "二、若有受傷，本中心學員可至中央大學保健室上藥。\n" +
                "\n" +
                "三、個人若有突發性疾病，請於報到後立刻告知室友、同學或導師急救之方式及所需之藥品。\n" +
                "\n" +
                "　　急救電話：壢新醫院　　　(03)4941234" +
                "\n" +
                "　　　　　　　陽明醫院　　　(03)4929929" +
                "\n" +
                "　　　　　　　署立桃園醫院　(03)3699721" +
                "\n" +
                "　　　　　　　林口長庚醫院　(03)3281200" +
                "\n" +
                "四、依據國立中央大學車輛管理實施要點：車輛入校需計時收費， 每小時20元。並請依校方之規定將汽車停放於劃有停車位置之停車格內。　　\n" +
                "\n" +
                "五、學員之郵寄信件請註明地址為「中央大學郵局第01號信箱 XXXXX班」。若是要郵寄包裹或快遞包裹，請改寄到「桃園市中壢區中大路300-1號 中央大學工程二館 資策會 XXXXX班」。班級名稱請務必註明。\n" +
                "\n" +
                "六、本中心辦公室電話、影印機及傳真機專供中心教職員使用，不對學員開放，惟親友若有緊急情事，於上班時間內可利用本中心電話留言，請本中心代為轉達。辦公室電話：(03)4257387。\n" +
                "\n" +
                "七、一樓營運服務組備有多種球具可供登記借用，請最慢隔天歸還。\n" +
                "\n" +
                "八、本中心係設於中央大學校區內，請共同遵守中央大學有關規定（如：不踐踏校區草地，不著背心、短褲、拖鞋進入餐廳、教室等公共場所等）。");

        tvP4title.setText("肆、校外宿舍管理規章");
        tvP4content.setText("\n"+"一、宿舍內之私人財物請妥善保管，中心不負保管之責。\n" +
                "\n" +
                "二、如有任何緊急狀況，上班時間請聯絡導師或營運服務組\n" +
                "聯絡電話：03-4257387　　\n" +
                "\n" +
                "三、非上班時段如有任何緊急狀況，請撥打下列電話：\n" +
                "\n" +
                "中心電話03-4257387 撥通後按“0”接總機\n" +
                "\n" +
                "營運服務組 湯士慶先生0975200081。\n" +
                "中央大學警衛室電話：03-4267144、03-4267158\n" +
                "\n" +
                "急救電話：壢新醫院　  　(03)4941234" +
                "\n" +
                "　　　　　陽明醫院    　(03)4929929" +
                "\n" +
                "　　　　　署立桃園醫院　(03)3699721" +
                "\n" +
                "　　　　　林口長庚醫院　(03)3281200");


    }

}
