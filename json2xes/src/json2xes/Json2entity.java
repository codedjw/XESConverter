package json2xes;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Json2entity {
	// static ObjectMapper objectMapper = new ObjectMapper();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="/Users/tiffany/develop/TestWorkspace/json2xes/jsonfiles/json挂号就诊.json";
		
	}

	public List<Registration> read2entitylist(String jsonpath){
		String JsonContext = new Util().ReadFile(jsonpath);
		//TimestampConverter tc = new TimestampConverter();
		//StringBuffer sb=new StringBuffer(JsonContext);
//		sb.deleteCharAt(0);
//		sb.
		JsonContext=JsonContext.replaceFirst(",", "[");
		JsonContext=JsonContext+"]";
	    //System.out.println(JsonContext);
//		String JsonContext = new Util().ReadFile("D:\\test\\apkinfo.json");
		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		int size = jsonArray.size();
		//System.out.println("Size: " + size);
		List<Registration> list=new LinkedList<Registration>();
		for(int  i = 0; i < size-1; i++){//最后是endline
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			JSONObject resObject=jsonObject.getJSONObject("result");
			Registration reg = new Registration() ;
			reg.setRegID(resObject.get("挂号序号").toString());
			reg.setRegDate(TimestampConverter.convert(resObject.get("挂号日期").toString()));
			reg.setRegDepartment(resObject.get("挂号科室").toString());
			reg.setFirstReceptionDate(TimestampConverter.convert(resObject.get("首次接诊日期").toString()));
			reg.setFirstReceptionDoctorID(resObject.get("首次接诊医生工号").toString());
			reg.setFirstReceptionDoctorName(resObject.get("首次接诊医生姓名").toString());
			reg.setFinalReceptionDate(TimestampConverter.convert(resObject.get("最后一次接诊日期").toString()));
			reg.setFinalReceptionDoctorID(resObject.get("最后一次接诊医生工号").toString());
			reg.setFinalReceptionDoctorName(resObject.get("最后一次接诊医生姓名").toString());
			list.add(reg);
			//System.out.println(reg);
			//System.out.println("[" + i + "]挂号日期=" + jsonObject.getJSONObject("result").get("挂号日期").toString());
			//System.out.println("[" + i + "]挂号科室=" + jsonObject.getJSONObject("result").get("挂号科室"));
			//System.out.println("[" + i + "]挂号序号=" + jsonObject.getJSONObject("result").get("挂号序号"));
		}
		return list;
	}
	
//	public static void readJson2Array() {
//		//String JsonContext = new Util().ReadFile("D:\\test\\apkinfo.json");
//		String json = new Util().ReadFile("/Users/tiffany/develop/TestWorkspace/json2xes/jsonfiles/json挂号就诊.json");
//	    //String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
//	   //         "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
//	    try {
//	        Registration[] arr = objectMapper.readValue(json, Registration[].class);
//	        System.out.println(arr.length);
//	        for (int i = 0; i < arr.length; i++) {
//	            System.out.println(arr[i]);
//	        }
//	        
//	    } catch (JsonParseException e) {
//	        e.printStackTrace();
//	    } catch (JsonMappingException e) {
//	        e.printStackTrace();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	}
}
