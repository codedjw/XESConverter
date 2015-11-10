package json2xes;

/**
 * @author tiffany
 *
 */
public class Registration {
	//挂号序号；
	//挂号日期；挂号科室；
	//最后一次接诊日期；最后一次接诊医生工号；最后一次接诊医生姓名；
	//首次接诊日期；首次接诊医生工号；首次接诊医生姓名；
	String regID;
	String regDate;
	String regDepartment;
	String finalReceptionDate;
	String finalReceptionDoctorID;
	String finalReceptionDoctorName;
	String firstReceptionDate;
	String firstReceptionDoctorID;
	String firstReceptionDoctorName;
	
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getRegDepartment() {
		return regDepartment;
	}
	public void setRegDepartment(String regDepartment) {
		this.regDepartment = regDepartment;
	}
	public String getFinalReceptionDate() {
		return finalReceptionDate;
	}
	public void setFinalReceptionDate(String finalReceptionDate) {
		this.finalReceptionDate = finalReceptionDate;
	}
	public String getFinalReceptionDoctorID() {
		return finalReceptionDoctorID;
	}
	public void setFinalReceptionDoctorID(String finalReceptionDoctorID) {
		this.finalReceptionDoctorID = finalReceptionDoctorID;
	}
	public String getFinalReceptionDoctorName() {
		return finalReceptionDoctorName;
	}
	public void setFinalReceptionDoctorName(String finalReceptionDoctorName) {
		this.finalReceptionDoctorName = finalReceptionDoctorName;
	}
	public String getFirstReceptionDate() {
		return firstReceptionDate;
	}
	public void setFirstReceptionDate(String firstReceptionDate) {
		this.firstReceptionDate = firstReceptionDate;
	}
	public String getFirstReceptionDoctorID() {
		return firstReceptionDoctorID;
	}
	public void setFirstReceptionDoctorID(String firstReceptionDoctorID) {
		this.firstReceptionDoctorID = firstReceptionDoctorID;
	}
	public String getFirstReceptionDoctorName() {
		return firstReceptionDoctorName;
	}
	public void setFirstReceptionDoctorName(String firstReceptionDoctorName) {
		this.firstReceptionDoctorName = firstReceptionDoctorName;
	}
	
	
}
