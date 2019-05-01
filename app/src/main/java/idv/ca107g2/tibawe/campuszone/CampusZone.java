package idv.ca107g2.tibawe.campuszone;

import idv.ca107g2.tibawe.R;

public class CampusZone {
    private int infoTitle;
    private int infoPicId;

    public static final CampusZone[] CAMPUS_ZONES = {
            new CampusZone(R.string.campus_news, R.drawable.d),
            new CampusZone(R.string.attendance, R.drawable.d),
            new CampusZone(R.string.teachinglog, R.drawable.d),
            new CampusZone(R.string.absence_apply, R.drawable.d),
            new CampusZone(R.string.classroom_reservation, R.drawable.d),
            new CampusZone(R.string.repair_apply, R.drawable.d)
    };

    private CampusZone(int infoTitle, int infoPicId){
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
