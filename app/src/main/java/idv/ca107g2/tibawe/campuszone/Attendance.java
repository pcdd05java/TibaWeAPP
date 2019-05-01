package idv.ca107g2.tibawe.campuszone;

public class Attendance {
    private String attDate;
    private String attInterval;
    private String attCourse;
    private String attTeacher;
    private String attStatus;
    private static java.sql.Date theDate = new java.sql.Date(System.currentTimeMillis());
    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

    public static final Attendance[] attendances = {
            new Attendance(sdf.format(theDate) ,"上午","Java","吳冠宏", "未申請"),
            new Attendance("4/11","夜間","夜間課輔","傅俊皓", "曠課"),
            new Attendance("4/9" ,"上午","Servlet","吳永志", "審核中"),
            new Attendance("4/9" ,"下午","JavaScript","董淑惠", "已簽到"),
    };

    public Attendance(String attDate, String attInterval, String attCourse, String attTeacher, String attStatus) {
        this.attDate = attDate;
        this.attInterval = attInterval;
        this.attCourse = attCourse;
        this.attTeacher = attTeacher;
        this.attStatus = attStatus;
    }

    public String getAttDate() {
        return attDate;
    }

    public void setAttDate(String attDate) {
        this.attDate = attDate;
    }

    public String getAttInterval() {
        return attInterval;
    }

    public void setAttInterval(String attInterval) {
        this.attInterval = attInterval;
    }

    public String getAttCourse() {
        return attCourse;
    }

    public void setAttCourse(String attCourse) {
        this.attCourse = attCourse;
    }

    public String getAttTeacher() {
        return attTeacher;
    }

    public void setAttTeacher(String attTeacher) {
        this.attTeacher = attTeacher;
    }

    public String getAttStatus() {
        return attStatus;
    }

    public void setAttStatus(String attStatus) {
        this.attStatus = attStatus;
    }
}
