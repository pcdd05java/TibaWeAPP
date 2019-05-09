package idv.ca107g2.tibawe.vo;

import java.io.Serializable;

public class CrVO implements Serializable {
    private String cr_no;
    private String cr_name;
    private Integer cr_status;

    public CrVO() {
    }

    public String getCr_no() {
        return cr_no;
    }

    public void setCr_no(String cr_no) {
        this.cr_no = cr_no;
    }

    public String getCr_name() {
        return cr_name;
    }

    public void setCr_name(String cr_name) {
        this.cr_name = cr_name;
    }

    public Integer getCr_status() {
        return cr_status;
    }

    public void setCr_status(Integer cr_status) {
        this.cr_status = cr_status;
    }

}
