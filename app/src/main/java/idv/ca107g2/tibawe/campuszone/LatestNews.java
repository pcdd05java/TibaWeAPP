package idv.ca107g2.tibawe.campuszone;

import idv.ca107g2.tibawe.R;

public class LatestNews {
    private String newsTitle;
    private String newsContent;
    private int newsPicId;

    public static final LatestNews[] latestNews = {
            new LatestNews("#1 4月6日公告訊息", R.drawable.news1, "場管須的風較至牛已現？比許香明景然，司為一兒無業體太，像基選如，做開市樣……長子權之發外是背前聽此本優根行治得術我？"),
            new LatestNews("#2 4月6日公告訊息", R.drawable.news2, "告服資之遠出看銷教人道部使滿要王灣了衣細充就面初：工和了經是、雖亞其張乎冷不月友滿臺卻學。"),
            new LatestNews("#3 4月6日公告訊息", R.drawable.news3, "師所試研常；著鄉下是；山考自造致父技般？樣數聽期未待，全區市和，元功所；價統亞發樣公眼行子選光不、地所生五細否於？球書想的活展進異地已。力有年變東？"),
            new LatestNews("#4 4月6日公告訊息", R.drawable.news4, "持臉畫明時人極是有氣水。人突客比院？成大了是。爸成等立時從給立是頭我原而消自此動以爾無光的善們第城相輕打、確重廣！"),
            new LatestNews("#5 4月6日公告訊息", R.drawable.news5, "反頭教學利久要來為和問形話得的制交做方意雖每然作慢人節喜，在選備林公物很人叫大，高看生？爾場手件？動出治資子必從見眼藝情頭線次。得童起地後。"),
            new LatestNews("#6 4月6日公告訊息", R.drawable.news6, "清大們樹考坡有生文聲是我下都到手四何紅兩水公場白山，專所他灣。同一你發子市樣！現物民，足更同海血評各北場人組麼負球分，濟果底提父不不在三麼了行們以士。"),
    };

    private LatestNews (String newsTitle, int newsPicId, String newsContent){
        this.newsTitle = newsTitle;
        this.newsPicId = newsPicId;
        this.newsContent = newsContent;
    }

    public String getNewsTitle(){
        return newsTitle;
    }

    public int getNewsPicId(){
        return newsPicId;
    }

    public String getNewsContent(){
        return newsContent;
    }
}

