package db2xes;

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
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DB2Xes {
	
	public boolean isBPMN = false;
	
	public boolean isDistinct = false;
	
	public String DB_NAME = "";
	
	public String TB_NAME = "";
	
	public String CASE_ID = "CASE_ID";
	
	public String USER_RESOURCE = "USER_ID";
	
	public String TIMESTAMP = "VISIT_TIME";
	
	public String ACTIVITY = "VISIT_MEAN";
	
	public String filename = "";
	
	public String xesname = "";
	
	public String eventprefix = "";

	public DB2Xes() {
		
	}
	
	public DB2Xes(boolean isBPMN, boolean isDistinct, String dB_NAME,
			String tB_NAME, String cASE_ID, String uSER_RESOURCE,
			String tIMESTAMP, String aCTIVITY, String filename, String xesname,
			String eventprefix) {
		super();
		this.isBPMN = isBPMN;
		this.isDistinct = isDistinct;
		DB_NAME = dB_NAME;
		TB_NAME = tB_NAME;
		CASE_ID = cASE_ID;
		USER_RESOURCE = uSER_RESOURCE;
		TIMESTAMP = tIMESTAMP;
		ACTIVITY = aCTIVITY;
		this.filename = filename;
		this.xesname = xesname;
		this.eventprefix = eventprefix;
	}

	public void db2xes() {
		System.out.println(xesname);
		Document document = DocumentHelper.createDocument();
		document = this.prepare(xesname);
		try {
			document = this.generate(document, eventprefix);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.output(document);
	}
	
	public static Connection getCon(String dbname) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//		System.out.println("ok1");
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		String url="jdbc:mysql://localhost:3306/cop?user=root&password=110211";
		String url="jdbc:mysql://localhost:3306/"+dbname;
		Connection con = DriverManager.getConnection(url, "root", "");
		return con;
	}
	
	private Document prepare(String name){
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

	public Document generate(Document document, String eventprefix) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		System.out.println("Generate Big");
		Connection con = DB2Xes.getCon(this.DB_NAME);
		Statement stmt = con.createStatement();
		String query = "";
		String is_dis = "";
		if (this.isDistinct) {
			is_dis = "DISTINCT ";
		}
		query = "SELECT "+is_dis+CASE_ID+" AS CASE_ID, "+USER_RESOURCE+" AS USER_RESOURCE, "+TIMESTAMP+" AS TIMESTAMP, "+ACTIVITY+" AS ACTIVITY FROM "+TB_NAME+" ORDER BY CASE_ID, TIMESTAMP ASC";
		long beginTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs = stmt.executeQuery(query);
		long finishTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println(query+" 运行时间： "+(finishTime-beginTime)/1000/60+"min, "+(finishTime-beginTime)%(1000*60)/1000+"s, "+(finishTime-beginTime)%(1000*60)%1000+"ms");
		Map<String, List<Event>> case_events = new HashMap<String, List<Event>>();
		int count = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = null;
		String endTime = null;
		String last_case_id = null;
		List<Event> last_events = null;
		int idx = 0;
		while (rs.next()) {
			String activity_time = sdf.format(rs.getTimestamp("TIMESTAMP"));
			String activity = rs.getNString("ACTIVITY");
			String user_id = rs.getString("USER_RESOURCE");
			String case_id = rs.getString("CASE_ID"); 
			List<Event> events = null;
			if (!case_events.containsKey(case_id)) {
				case_events.put(case_id, new ArrayList<Event>());
				if (last_case_id != null && (last_events = case_events.get(last_case_id)) != null) {
					if (this.isBPMN) {
						last_events.add(0, new Event("Start", startTime, "__NULL__", last_case_id));
						last_events.add(new Event("End", endTime, "__NULL__", last_case_id));
					}
					case_events.put(last_case_id, last_events);
					System.out.println(++idx);
					System.out.println("Case " + last_case_id + ": " + count + "个event");
				}
				count = 0;
				if (this.isBPMN) {
					startTime = null;
					endTime = null;
				}
				last_case_id = case_id;
			}
			events = case_events.get(case_id);
			count++;
			if (this.isBPMN) {
				startTime = (startTime == null || startTime.compareTo(activity_time) > 0) ? activity_time : startTime;
				endTime = (endTime == null || endTime.compareTo(activity_time) < 0) ? activity_time : endTime;
			}
			events.add(new Event(activity, activity_time, user_id, case_id));
			case_events.put(case_id, events);
		}
		if (last_case_id != null && (last_events = case_events.get(last_case_id)) != null) {
			if (this.isBPMN) {
				last_events.add(0, new Event("Start", startTime, "__NULL__", last_case_id));
				last_events.add(new Event("End", endTime, "__NULL__", last_case_id));
			}
			case_events.put(last_case_id, last_events);
			System.out.println(++idx);
			System.out.println("Case " + last_case_id + ": " + count + "个event");
		}
		con.close();
		for (String cid : case_events.keySet()) {
			List<Event> eves = case_events.get(cid);
			if (eves != null) {
				document = this.addRegEvents(document, eves, cid, eventprefix);
			}
		}
		return document;
	}
	
	private Document addRegEvents(Document document,List<Event> list, String case_id,String name){
//		System.out.println("ok addRegEvents");
		Document document2 = DocumentHelper.createDocument();
		
		document2=document;
		
		Element logElement = document2.getRootElement();
		
		Element trace=logElement.addElement("trace");
		Element key1=trace.addElement("string");
		key1.addAttribute("key", "concept:name");
		key1.addAttribute("value", name+"_"+case_id);
		Element key2=trace.addElement("string");
		key2.addAttribute("key", "description");
		key2.addAttribute("value", "Process Instance");
		for (Event eve : list){
			
			
			Element eventReg=trace.addElement("event");
				Element eleReg1=eventReg.addElement("string");
				eleReg1.addAttribute("key", "concept:name");
				eleReg1.addAttribute("value", eve.getActivity());
				Element eleReg2=eventReg.addElement("date");
				eleReg2.addAttribute("key", "time:timestamp");
				eleReg2.addAttribute("value", eve.getTimestamp()+"+08:00");
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
	
	private void output(Document document){
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
