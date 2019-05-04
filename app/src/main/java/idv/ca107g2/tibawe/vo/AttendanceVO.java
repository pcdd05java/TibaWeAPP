package idv.ca107g2.tibawe.vo;

public class AttendanceVO implements java.io.Serializable {
    private String attno;
    private String memberaccount;
    private String coursetime_no;
    private Integer status;
    private java.sql.Timestamp qrecord;


    public String getAttno() {
        return attno;
    }

    public void setAttno(String attno) {
        this.attno = attno;
    }

    public String getMemberaccount() {
        return memberaccount;
    }

    public void setMemberaccount(String memberaccount) {
        this.memberaccount = memberaccount;
    }

    public String getCoursetime_no() {
        return coursetime_no;
    }

    public void setCoursetime_no(String coursetime_no) {
        this.coursetime_no = coursetime_no;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setQrecord(java.sql.Timestamp qrecord) {
        this.qrecord = qrecord;
    }

    public java.sql.Timestamp getQrecord() {
        return qrecord;

    }

}
