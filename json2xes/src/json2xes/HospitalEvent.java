package json2xes;

public class HospitalEvent {
	String name;
	String date;
	String resource;
	String regID;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public HospitalEvent(String name1, String date1,String resource1, String regID1 ){
		
		//System.out.println("!!!name:"+name1+" date:"+date1+" resource:"+resource1+" regID:"+regID1);
		
		
		name=name1;
		date=TimestampConverter.convert(date1);
		resource=resource1;
		regID=regID1;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	
	public void print(){
		System.out.println("name:"+name+" date:"+date+" resource:"+resource+" regID:"+regID);
	}
	

}
