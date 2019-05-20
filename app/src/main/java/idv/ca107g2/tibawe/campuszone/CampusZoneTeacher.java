package idv.ca107g2.tibawe.campuszone;

import idv.ca107g2.tibawe.R;

public class CampusZoneTeacher {
    private int infoTitle;
    private int infoPicId;

    public static final CampusZoneTeacher[] CAMPUS_ZONES = {
            new CampusZoneTeacher(R.string.campus_news, R.drawable.d),
            new CampusZoneTeacher(R.string.teachinglog, R.drawable.d),
            new CampusZoneTeacher(R.string.classroom_reservation, R.drawable.d),
            new CampusZoneTeacher(R.string.repair_apply, R.drawable.d)
    };

    private CampusZoneTeacher(int infoTitle, int infoPicId){
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
