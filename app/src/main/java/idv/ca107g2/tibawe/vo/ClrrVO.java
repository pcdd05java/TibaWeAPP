package idv.ca107g2.tibawe.vo;

import java.io.Serializable;
import java.sql.Date;

public class ClrrVO implements Serializable {
    private String clrr_no;
    private String memberaccount;
    private Date clrr_date;
    private Integer clrr_sttime;
    private Integer clrr_endtime;
    private String cr_no;
    private String class_no;

    public ClrrVO() {
    }

    public String getClrr_no() {
        return clrr_no;
    }

    public void setClrr_no(String clrr_no) {
        this.clrr_no = clrr_no;
    }

    public String getMemberaccount() {
        return memberaccount;
    }

    public void setMemberaccount(String memberaccount) {
        this.memberaccount = memberaccount;
    }

    public Date getClrr_date() {
        return clrr_date;
    }

    public void setClrr_date(Date clrr_date) {
        this.clrr_date = clrr_date;
    }

    public Integer getClrr_sttime() {
        return clrr_sttime;
    }

    public void setClrr_sttime(Integer clrr_sttime) {
        this.clrr_sttime = clrr_sttime;
    }

    public Integer getClrr_endtime() {
        return clrr_endtime;
    }

    public void setClrr_endtime(Integer clrr_endtime) {
        this.clrr_endtime = clrr_endtime;
    }

    public String getCr_no() {
        return cr_no;
    }

    public void setCr_no(String cr_no) {
        this.cr_no = cr_no;
    }

    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }

}
