package idv.ca107g2.tibawe.vo;


import java.io.Serializable;

public class StoreInformationVO implements Serializable {
    private String store_no;
    private String store_name;
    private String store_phone;
    private String store_adress;
    private byte[] store_pic;
    private byte[] store_pic2;
    private String store_note;
    private Integer store_attr;

    public String getStore_no() {
        return store_no;
    }
    public String getStore_adress() {
        return store_adress;
    }
    public void setStore_adress(String store_adress) {
        this.store_adress = store_adress;
    }
    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }
    public String getStore_name() {
        return store_name;
    }
    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
    public String getStore_phone() {
        return store_phone;
    }
    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }
    public byte[] getStore_pic() {
        return store_pic;
    }
    public void setStore_pic(byte[] store_pic) {
        this.store_pic = store_pic;
    }
    public byte[] getStore_pic2() {
        return store_pic2;
    }
    public void setStore_pic2(byte[] store_pic2) {
        this.store_pic2 = store_pic2;
    }
    public String getStore_note() {
        return store_note;
    }
    public void setStore_note(String store_note) {
        this.store_note = store_note;
    }
    public Integer getStore_attr() {
        return store_attr;
    }
    public void setStore_attr(Integer store_attr) {
        this.store_attr = store_attr;
    }

}
