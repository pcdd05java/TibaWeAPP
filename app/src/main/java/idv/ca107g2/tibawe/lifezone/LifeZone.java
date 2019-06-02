package idv.ca107g2.tibawe.lifezone;

import idv.ca107g2.tibawe.R;

public class LifeZone {
    private int infoTitle;
    private int infoPicId;

    public static final LifeZone[] LIFE_ZONES = {
//            new LifeZone(R.string.hot_article, R.drawable.d),
//            new LifeZone(R.string.discussion_board, R.drawable.d),
            new LifeZone(R.string.DBD_order, R.drawable.d),
            new LifeZone(R.string.store_menu, R.drawable.d),
            new LifeZone(R.string.rhi, R.drawable.d),
    };

    private LifeZone(int infoTitle, int infoPicId){
        this.infoTitle = infoTitle;
        this.infoPicId = infoPicId;
    }

    public int getInfoTitle(){
        return infoTitle;
    }

    public int getInfoPicId(){
        return infoPicId;
    }
}
