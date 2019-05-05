package idv.ca107g2.tibawe.lifezone;

import idv.ca107g2.tibawe.R;

public class StoreInformation {
    private String storeTitle;
    private int storePicId;

    public static final StoreInformation[] STORE_INFORMATIONS = {
            new StoreInformation("八方雲集", R.drawable.store1),
            new StoreInformation("金拱門餐廳", R.drawable.store2),
            new StoreInformation("肯基基", R.drawable.store3),
            new StoreInformation("雖敗猶榮", R.drawable.store4),
            new StoreInformation("我家牛排", R.drawable.store5),
            new StoreInformation("It's Sparta!!", R.drawable.store6)
    };

    private StoreInformation(String storeTitle, int storePicId){
        this.storeTitle = storeTitle;
        this.storePicId = storePicId;
    }

    public String getStoreTitle(){
        return storeTitle;
    }

    public int getStorePicId(){
        return storePicId;
    }
}
