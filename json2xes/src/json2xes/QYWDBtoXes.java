package json2xes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
import org.dom4j.io.OutputFormat;  
import org.dom4j.io.XMLWriter; 

public class QYWDBtoXes {
	
	public boolean isBPMN = false;
	
	public Map<String, Integer> hospitalMaps = new HashMap<String, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("武汉市中心医院", 270001);
			put("十堰市太和医院", 7190002);
			put("十堰市人民医院", 7190001);
			put("洛阳市妇女儿童医疗保健中心", 3790008);
			put("济宁医学院附属医院", 1002);
			put("吉林省人民医院", 4310001);
			put("徐州市中心医院", 5160004);
			put("安徽省中医院", 5510002);
			put("洛阳市第一中医院", 3790010);
			put("昆明呈贡医院", 8710004);
		}
	};
	
	public boolean isDaoyi = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间  
		Document document = DocumentHelper.createDocument();
		QYWDBtoXes db2xes=new QYWDBtoXes();
		db2xes.isDaoyi = true;
		for (String hname : db2xes.hospitalMaps.keySet()) {
			String hid = ""+db2xes.hospitalMaps.get(hname);
			String name = hname+"_导医用户";
			System.out.println(name);
			String filename = "/Users/dujiawei/Desktop/趣医网-第二阶段/XES/qyw_"+hid+"_daoyi.xes";
			document = prepare(name);
			db2xes.isBPMN = false;
			try {
//				document = db2xes.generate(document, " LIMIT 1000, 1000", hid, hname);
				document = db2xes.generate(document, "", hid, hname);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			output(document, filename);
		}
		db2xes.isDaoyi = false;
		for (String hname : db2xes.hospitalMaps.keySet()) {
			String hid = ""+db2xes.hospitalMaps.get(hname);
			String name = hname+"_自发用户";
			System.out.println(name);
			String filename = "/Users/dujiawei/Desktop/趣医网-第二阶段/XES/qyw_"+hid+"_regular.xes";
			document = prepare(name);
			db2xes.isBPMN = false;
			try {
//				document = db2xes.generate(document, " LIMIT 1000, 1000", hid, hname);
				document = db2xes.generate(document, "", hid, hname);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			output(document, filename);
		}
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");
	}
	
	public Document generate(Document document, String limit, String hid, String hname) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		System.out.println("Generate Big");
		Connection con = this.getCon();
		Statement stmt = con.createStatement();
//		String query = "SELECT T1.GUAHAO_ID, T1.USER_ID, T1.ACTIVITY_TIME, T1.ACTIVITY FROM (SELECT * FROM renji.eventlog ORDER BY GUAHAO_ID) AS T1 INNER JOIN (SELECT DISTINCT GUAHAO_ID FROM renji.guahao WHERE GH_TIMESTAMP >= \'2014-04-01 00:00:00\' AND GH_TIMESTAMP <= \'2014-04-30 23:59:59\' ORDER BY GUAHAO_ID"+limit+") AS T2 ON T1.GUAHAO_ID = T2.GUAHAO_ID ORDER BY T1.GUAHAO_ID, T1.ACTIVITY_TIME ASC";
		String query = "";
		if (this.isDaoyi) {
			query = "SELECT CASE_ID, USER_ID, VISIT_TIME, VISIT_MEAN FROM qyw.daoyi_events_20151111_06 WHERE HOSPITAL_ID = "+hid+" AND TRIGGER_TYPE = \'用户点击\' ORDER BY CASE_ID, VISIT_TIME ASC";
		} else {
			query = "SELECT CASE_ID, USER_ID, VISIT_TIME, VISIT_MEAN FROM qyw.regular_events_20151111_06 WHERE HOSPITAL_ID = "+hid+" AND TRIGGER_TYPE = \'用户点击\' AND USER_ID > 0 ORDER BY CASE_ID, VISIT_TIME ASC";
		}
		long beginTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs = stmt.executeQuery(query);
		long finishTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println(query+" 运行时间： "+(finishTime-beginTime)/1000/60+"min, "+(finishTime-beginTime)%(1000*60)/1000+"s, "+(finishTime-beginTime)%(1000*60)%1000+"ms");
		Map<String, List<HospitalEvent>> guahao_events = new HashMap<String, List<HospitalEvent>>();
		int count = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = null;
		String endTime = null;
		String last_guahao_id = null;
		List<HospitalEvent> last_events = null;
		int idx = 0;
		while (rs.next()) {
//			String activity_time = sdf.format(rs.getTimestamp("ACTIVITY_TIME"));
//			String activity = rs.getNString("ACTIVITY");
//			String user_id = rs.getString("USER_ID");
//			String guahao_id = rs.getString("GUAHAO_ID"); 
			String activity_time = sdf.format(rs.getTimestamp("VISIT_TIME"));
			String activity = rs.getNString("VISIT_MEAN");
			String user_id = rs.getString("USER_ID");
			String guahao_id = rs.getString("CASE_ID"); 
			List<HospitalEvent> events = null;
			if (!guahao_events.containsKey(guahao_id)) {
				guahao_events.put(guahao_id, new ArrayList<HospitalEvent>());
				if (last_guahao_id != null && (last_events = guahao_events.get(last_guahao_id)) != null) {
					if (this.isBPMN) {
						last_events.add(0, new HospitalEvent("Start", startTime, "__NULL__", last_guahao_id));
						last_events.add(new HospitalEvent("End", endTime, "__NULL__", last_guahao_id));
					}
					guahao_events.put(last_guahao_id, last_events);
					System.out.println(++idx);
					System.out.println("Case " + last_guahao_id + ": " + count + "个event");
				}
				count = 0;
				if (this.isBPMN) {
					startTime = null;
					endTime = null;
				}
				last_guahao_id = guahao_id;
			}
			events = guahao_events.get(guahao_id);
			count++;
			if (this.isBPMN) {
				startTime = (startTime == null || startTime.compareTo(activity_time) > 0) ? activity_time : startTime;
				endTime = (endTime == null || endTime.compareTo(activity_time) < 0) ? activity_time : endTime;
			}
			events.add(new HospitalEvent(activity, activity_time, user_id, guahao_id));
			guahao_events.put(guahao_id, events);
		}
		if (last_guahao_id != null && (last_events = guahao_events.get(last_guahao_id)) != null) {
			if (this.isBPMN) {
				last_events.add(0, new HospitalEvent("Start", startTime, "__NULL__", last_guahao_id));
				last_events.add(new HospitalEvent("End", endTime, "__NULL__", last_guahao_id));
			}
			guahao_events.put(last_guahao_id, last_events);
			System.out.println(++idx);
			System.out.println("Case " + last_guahao_id + ": " + count + "个event");
		}
		con.close();
		for (String ghid : guahao_events.keySet()) {
			List<HospitalEvent> eves = guahao_events.get(ghid);
			if (eves != null) {
				document = addRegEvents(document, eves, ghid, hname);
			}
		}
		return document;
	}
	
	
	public Connection getCon() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//		System.out.println("ok1");
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		String url="jdbc:mysql://localhost:3306/cop?user=root&password=110211";
		String url="jdbc:mysql://localhost:3306/qyw";
		Connection con = DriverManager.getConnection(url, "root", "");
		return con;
	}
	
	static Document addRegEvents(Document document,List<HospitalEvent> list, String regid,String name){
//		System.out.println("ok addRegEvents");
		Document document2 = DocumentHelper.createDocument();
		
		document2=document;
		
		Element logElement = document2.getRootElement();
		
		Element trace=logElement.addElement("trace");
		Element key1=trace.addElement("string");
		key1.addAttribute("key", "concept:name");
		key1.addAttribute("value", name+"_"+regid);
		Element key2=trace.addElement("string");
		key2.addAttribute("key", "description");
		key2.addAttribute("value", "Process Instance");
		for (HospitalEvent eve : list){
			
			
			Element eventReg=trace.addElement("event");
				Element eleReg1=eventReg.addElement("string");
				eleReg1.addAttribute("key", "concept:name");
				eleReg1.addAttribute("value", eve.getName());
				Element eleReg2=eventReg.addElement("date");
				eleReg2.addAttribute("key", "time:timestamp");
				eleReg2.addAttribute("value", eve.getDate());
				Element eleReg3=eventReg.addElement("string");
				eleReg3.addAttribute("key", "org:resource");
				eleReg3.addAttribute("value", eve.getResource());
				Element eleReg4=eventReg.addElement("string");
				eleReg4.addAttribute("key", "service");
				eleReg4.addAttribute("value", "");
				Element eleReg5=eventReg.addElement("string");
				eleReg5.addAttribute("key", "lifecycle:transition");
				eleReg5.addAttribute("value", "complete");
				
	
			
		}
		return document2;
		
	}
	
	static Document prepare(String name){
		 Element root = DocumentHelper.createElement("log");
	        Document document = DocumentHelper.createDocument(root); 

	        // 添加属性
	        root.addAttribute("xes.version", "1.0");
	        root.addAttribute("xes.features", "nested-attributes");
	        root.addAttribute("openxes.version", "1.0RC7");
	        root.addAttribute("xmlns", "http://code.deckfour.org/xes");

	        // 添加子节点:add之后就返回这个元素
	        Element extConcept = root.addElement("extension");
	        extConcept.addAttribute("name", "Concept");
	        extConcept.addAttribute("prefix", "concept");
	        extConcept.addAttribute("uri","http://code.deckfour.org/xes/concept.xesext");
	        
	        Element extSemantic = root.addElement("extension");
	        extSemantic.addAttribute("name", "Semantic");
	        extSemantic.addAttribute("prefix", "semantic");
	        extSemantic.addAttribute("uri","http://code.deckfour.org/xes/semantic.xesext");
	        
	        Element extTime = root.addElement("extension");
	        extTime.addAttribute("name", "Time");
	        extTime.addAttribute("prefix", "time");
	        extTime.addAttribute("uri","http://code.deckfour.org/xes/time.xesext");
	        
	        Element extOrganizational = root.addElement("extension");
	        extOrganizational.addAttribute("name", "Organizational");
	        extOrganizational.addAttribute("prefix", "org");
	        extOrganizational.addAttribute("uri","http://code.deckfour.org/xes/org.xesext");
	        
	        Element extLifecycle = root.addElement("extension");
	        extLifecycle.addAttribute("name", "Lifecycle");
	        extLifecycle.addAttribute("prefix", "lifecycle");
	        extLifecycle.addAttribute("uri","http://code.deckfour.org/xes/lifecycle.xesext");
	        
	        Element globalTrace = root.addElement("global");
	        globalTrace.addAttribute("scope", "trace");
	        Element strCon1=globalTrace.addElement("string");
	        strCon1.addAttribute("key", "concept:name");
	        strCon1.addAttribute("value", "__INVALID__");
	        
	        Element globalEvent = root.addElement("global");
	        globalEvent.addAttribute("scope", "event");
	        Element strCon2=globalEvent.addElement("string");
	        strCon2.addAttribute("key", "concept:name");
	        strCon2.addAttribute("value", "__INVALID__");
	        Element strTrans=globalEvent.addElement("string");
	        strTrans.addAttribute("key", "lifecycle:transition");
	        strTrans.addAttribute("value", "complete");
	        
	        Element classifier1 = root.addElement("classifier");
	        classifier1.addAttribute("name", "MXML Legacy Classifier");
	        classifier1.addAttribute("keys", "concept:name lifecycle:transition");
	        Element classifier2 = root.addElement("classifier");
	        classifier2.addAttribute("name", "Event Name");
	        classifier2.addAttribute("keys", "concept:name");
	        Element classifier3 = root.addElement("classifier");
	        classifier3.addAttribute("name", "Resource");
	        classifier3.addAttribute("keys", "org:resource");
	        
	        Element strSource=root.addElement("string");
	        strSource.addAttribute("key", "source");
	        strSource.addAttribute("value", "CPN Tools simulation");
	        Element strConceptName=root.addElement("string");
	        strConceptName.addAttribute("key", "concept:name");
	        strConceptName.addAttribute("value", name);
	        Element strDescription=root.addElement("string");
	        strDescription.addAttribute("key", "description");
	        strDescription.addAttribute("value", "Simulated process");
	        Element strLifecycleModel=root.addElement("string");
	        strLifecycleModel.addAttribute("key", "lifecycle:model");
	        strLifecycleModel.addAttribute("value", "standard");
	        
	    	return document;
	}
	
	static void output(Document document, String filename){
        OutputFormat format = new OutputFormat("    ", true);// 设置缩进为4个空格，并且另起一行为true
        XMLWriter xmlWriter2;
		try {
			xmlWriter2 = new XMLWriter(
			        new FileOutputStream(filename), format);
			xmlWriter2.write(document);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("ok2");
	}

}
