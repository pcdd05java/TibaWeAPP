package idv.ca107g2.tibawe.vo;

import java.sql.Timestamp;

public class DBDOderVO {
    private String dbdo_no;
    private String memberAccount;
    private String store_no;
    private Timestamp ini_time;
    private Timestamp fnl_time;
    private Integer dbdo_nop;
    private Integer dbdo_attr;

    public String getDbdo_no() {
        return dbdo_no;
    }
    public Integer getDbdo_nop() {
        return dbdo_nop;
    }
    public void setDbdo_nop(Integer dbdo_nop) {
        this.dbdo_nop = dbdo_nop;
    }
    public void setDbdo_no(String dbdo_no) {
        this.dbdo_no = dbdo_no;
    }
    public String getMemberAccount() {
        return memberAccount;
    }
    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }
    public String getStore_no() {
        return store_no;
    }
    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }
    public Timestamp getIni_time() {
        return ini_time;
    }
    public void setIni_time(Timestamp ini_time) {
        this.ini_time = ini_time;
    }
    public Timestamp getFnl_time() {
        return fnl_time;
    }
    public void setFnl_time(Timestamp fnl_time) {
        this.fnl_time = fnl_time;
    }
    public Integer getDbdo_attr() {
        return dbdo_attr;
    }
    public void setDbdo_attr(Integer dbdo_attr) {
        this.dbdo_attr = dbdo_attr;
    }

}
