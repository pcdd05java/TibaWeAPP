package idv.ca107g2.tibawe.classzone;

import idv.ca107g2.tibawe.R;

public class ClassZoneTeacher {
    private int infoTitle;
    private int infoPicId;


    public static final ClassZoneTeacher[] CLASS_ZONES = {
            new ClassZoneTeacher(R.string.class_news, R.drawable.d),
            new ClassZoneTeacher(R.string.course_query, R.drawable.d),
            new ClassZoneTeacher(R.string.seattable, R.drawable.d),
            new ClassZoneTeacher(R.string.phonebook, R.drawable.d),
    };

    private ClassZoneTeacher(int infoTitle, int infoPicId){
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
