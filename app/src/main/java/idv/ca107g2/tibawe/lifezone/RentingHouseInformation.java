package idv.ca107g2.tibawe.lifezone;

import idv.ca107g2.tibawe.R;

public class RentingHouseInformation {
    private String rhiTitle;
    private int rhiPicId;

    public static final RentingHouseInformation[] rentingHouseInformations = {
            new RentingHouseInformation("極品天下", R.drawable.rh1_1),
            new RentingHouseInformation("虹橋瓦舍", R.drawable.rh2),
            new RentingHouseInformation("伯爵雅居", R.drawable.rh3),
            new RentingHouseInformation("普羅旺斯", R.drawable.rh4),
            new RentingHouseInformation("鼎品", R.drawable.rh5),
            new RentingHouseInformation("學園套房", R.drawable.rh6)
    };

    private RentingHouseInformation (String rhiTitle, int rhiPicId){
        this.rhiTitle = rhiTitle;
        this.rhiPicId = rhiPicId;
    }

    public String getRhiTitle(){
        return rhiTitle;
    }

    public int getRhiPicId(){
        return rhiPicId;
    }
}
