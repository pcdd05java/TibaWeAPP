package idv.ca107g2.tibawe.vo;
import java.sql.Date;

public class ScheduleVO implements java.io.Serializable {
	private String courseTime_no;
	private String classroom_no;
	private String classroom_Note;
	private String class_no;
	private Date sdate;
	private Integer interval;
	private String subjectName;
	private String teacherName1;
	private String teacherName2;
	private String teacherName3;
	private String teacherName4;
	private String lastStudent;
	private String record;
	private Integer attendance;
	private String homework;
	private Integer signreq;
	private String note;

	public String getCourseTime_no() {
		return courseTime_no;
	}

	public void setCourseTime_no(String courseTime_no) {
		this.courseTime_no = courseTime_no;
	}
	public String getClassroom_no() {
		return classroom_no;
	}
	public void setClassroom_no(String classroom_no) {
		this.classroom_no = classroom_no;
	}
	public String getClassroom_Note() {
		return classroom_Note;
	}
	public void setClassroom_Note(String classroom_Note) {
		this.classroom_Note = classroom_Note;
	}
	public String getClass_no() {
		return class_no;
	}
	public void setClass_no(String class_no) {
		this.class_no = class_no;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTeacherName1() {
		return teacherName1;
	}
	public void setTeacherName1(String teacherName1) {
		this.teacherName1 = teacherName1;
	}
	public String getTeacherName2() {
		return teacherName2;
	}
	public void setTeacherName2(String teacherName2) {
		this.teacherName2 = teacherName2;
	}
	public String getTeacherName3() {
		return teacherName3;
	}
	public void setTeacherName3(String teacherName3) {
		this.teacherName3 = teacherName3;
	}
	public String getTeacherName4() {
		return teacherName4;
	}
	public void setTeacherName4(String teacherName4) {
		this.teacherName4 = teacherName4;
	}
	public String getLastStudent() {
		return lastStudent;
	}
	public void setLastStudent(String lastStudent) {
		this.lastStudent = lastStudent;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public Integer getAttendance() {
		return attendance;
	}
	public void setAttendance(Integer attendance) {
		this.attendance = attendance;
	}
	public String getHomework() {
		return homework;
	}
	public void setHomework(String homework) {
		this.homework = homework;
	}
	public Integer getSignreq() {
		return signreq;
	}
	public void setSignreq(Integer signreq) {
		this.signreq = signreq;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	
	

}
