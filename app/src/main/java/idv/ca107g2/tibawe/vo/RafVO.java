package idv.ca107g2.tibawe.vo;

import java.io.Serializable;
import java.sql.Date;

public class RafVO implements Serializable {
    private String raf_no;
    private String memberAccount;
    private String raf_class;
    private Date raf_date;
    private Integer raf_status;
    private String raf_loc;
    private String raf_type;
    private String raf_con;
    private String raf_note;
    private String raf_record;
    private String raf_staff;

    public RafVO() {
    }

    public String getRaf_no() {
        return raf_no;
    }

    public void setRaf_no(String raf_no) {
        this.raf_no = raf_no;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getRaf_class() {
        return raf_class;
    }

    public void setRaf_class(String raf_class) {
        this.raf_class = raf_class;
    }

    public Date getRaf_date() {
        return raf_date;
    }

    public void setRaf_date(Date raf_date) {
        this.raf_date = raf_date;
    }

    public Integer getRaf_status() {
        return raf_status;
    }

    public void setRaf_status(Integer raf_status) {
        this.raf_status = raf_status;
    }

    public String getRaf_loc() {
        return raf_loc;
    }

    public void setRaf_loc(String raf_loc) {
        this.raf_loc = raf_loc;
    }

    public String getRaf_type() {
        return raf_type;
    }

    public void setRaf_type(String raf_type) {
        this.raf_type = raf_type;
    }

    public String getRaf_con() {
        return raf_con;
    }

    public void setRaf_con(String raf_con) {
        this.raf_con = raf_con;
    }

    public String getRaf_note() {
        return raf_note;
    }

    public void setRaf_note(String raf_note) {
        this.raf_note = raf_note;
    }

    public String getRaf_record() {
        return raf_record;
    }

    public void setRaf_record(String raf_record) {
        this.raf_record = raf_record;
    }

    public String getRaf_staff() {
        return raf_staff;
    }

    public void setRaf_staff(String raf_staff) {
        this.raf_staff = raf_staff;
    }

}
