package idv.ca107g2.tibawe.vo;

public class DBDMemberVO {
    private String dbdm_no;
    private String memberAccount;
    private String dbdo_no;
    private String storem_no;
    private Integer dbdm_q;
    private Integer dbdm_change;
    private Integer dbdm_attr;

    public Integer getDbdm_change() {
        return dbdm_change;
    }
    public void setDbdm_change(Integer dbdm_change) {
        this.dbdm_change = dbdm_change;
    }
    public String getDbdo_no() {
        return dbdo_no;
    }
    public void setDbdo_no(String dbdo_no) {
        this.dbdo_no = dbdo_no;
    }
    public String getStorem_no() {
        return storem_no;
    }
    public void setStorem_no(String storem_no) {
        this.storem_no = storem_no;
    }
    public String getDbdm_no() {
        return dbdm_no;
    }
    public void setDbdm_no(String dbdm_no) {
        this.dbdm_no = dbdm_no;
    }
    public String getMemberAccount() {
        return memberAccount;
    }
    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }
    public Integer getDbdm_q() {
        return dbdm_q;
    }
    public void setDbdm_q(Integer dbdm_q) {
        this.dbdm_q = dbdm_q;
    }
    public Integer getDbdm_attr() {
        return dbdm_attr;
    }
    public void setDbdm_attr(Integer dbdm_attr) {
        this.dbdm_attr = dbdm_attr;
    }

}
