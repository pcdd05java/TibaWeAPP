package idv.ca107g2.tibawe.vo;

import java.sql.Date;

public class ClassInformationVO implements java.io.Serializable {
    private String information_no;
    private String class_no;
    private String informationcontent;
    private Date idate;

    public String getInformation_no() {
        return information_no;
    }
    public void setInformation_no(String information_no) {
        this.information_no = information_no;
    }
    public String getClass_no() {
        return class_no;
    }
    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }
    public String getInformationcontent() {
        return informationcontent;
    }
    public void setInformationcontent(String informationcontent) {
        this.informationcontent = informationcontent;
    }
    public Date getIdate() {
        return idate;
    }
    public void setIdate(Date idate) {
        this.idate = idate;
    }


}
