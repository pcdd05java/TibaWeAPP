package idv.ca107g2.tibawe.vo;

import java.sql.Date;

public class ClassaVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String class_no;
    private String classType_no;
    private String className;
    private Date initialDate;
    private Date endDate;
    private String seatingPlan;


    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }

    public String getClassType_no() {
        return classType_no;
    }

    public void setClassType_no(String classType_no) {
        this.classType_no = classType_no;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSeatingPlan() {
        return seatingPlan;
    }

    public void setSeatingPlan(String seatingPlan) {
        this.seatingPlan = seatingPlan;
    }
}