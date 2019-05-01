package idv.ca107g2.tibawe.lifezone;

import idv.ca107g2.tibawe.R;

public class StoreMenu {
    private String storeTitle;
    private int storePicId;

    public static final StoreMenu[] storeMenus = {
            new StoreMenu("八方雲集", R.drawable.store1),
            new StoreMenu("金拱門餐廳", R.drawable.store2),
            new StoreMenu("肯基基", R.drawable.store3),
            new StoreMenu("雖敗猶榮", R.drawable.store4),
            new StoreMenu("我家牛排", R.drawable.store5),
            new StoreMenu("It's Sparta!!", R.drawable.store6)
    };

    private StoreMenu(String storeTitle, int storePicId){
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
