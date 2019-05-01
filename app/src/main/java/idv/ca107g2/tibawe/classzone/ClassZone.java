package idv.ca107g2.tibawe.classzone;

import idv.ca107g2.tibawe.R;

public class ClassZone {
    private int infoTitle;
    private int infoPicId;


    public static final ClassZone[] CLASS_ZONES = {
            new ClassZone(R.string.class_news, R.drawable.d),
            new ClassZone(R.string.course_query, R.drawable.d),
            new ClassZone(R.string.phonebook, R.drawable.d),
            new ClassZone(R.string.seattable, R.drawable.d),
            new ClassZone(R.string.campusrule, R.drawable.d),
    };

    private ClassZone(int infoTitle, int infoPicId){
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
