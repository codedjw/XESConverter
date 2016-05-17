package db2xes.start2end;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Document;

import db2xes.DB2Xes;
import db2xes.util.Event;
import db2xes.util.IntWrapper;

public class DB2XesStart2End extends DB2Xes {
	
	private List<String> start_events = new ArrayList<String>();
	
	private List<String> end_events = new ArrayList<String>();

	public DB2XesStart2End() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DB2XesStart2End(boolean isBPMN, boolean isDistinct, String dB_NAME,
			String tB_NAME, String cASE_ID, String uSER_RESOURCE,
			String tIMESTAMP, String aCTIVITY, String filename, String xesname,
			String csvname, String eventprefix) {
		super(isBPMN, isDistinct, dB_NAME, tB_NAME, cASE_ID, uSER_RESOURCE, tIMESTAMP,
				aCTIVITY, filename, xesname, csvname, eventprefix);
		// TODO Auto-generated constructor stub
	}
	
	public void addStartEvent(String s_event) {
		this.start_events.add(s_event);
	}
	
	public void addEndEvent(String e_event) {
		this.end_events.add(e_event);
	}
	
	public void clearAllStartEndEvents() {
		this.start_events.clear();
		this.end_events.clear();
	}
	
	public void addStartEvents(List<String> s_events) {
		this.start_events.addAll(s_events);
	}
	
	public void addEndEvents(List<String> e_events) {
		this.end_events.addAll(e_events);
	}
	
	protected Document generate(Document document, String eventprefix) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
			String activity = rs.getString("ACTIVITY");
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
					if ((this.start_events.isEmpty() && this.end_events.isEmpty()) || (this.start_events.isEmpty() && !this.end_events.isEmpty())) {
						System.out.println(++idx);
						System.out.println("Case " + last_case_id + ": " + count + "个event");
					}
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
		// for last BPMN case
		if (last_case_id != null && (last_events = case_events.get(last_case_id)) != null) {
			if (this.isBPMN) {
				last_events.add(0, new Event("Start", startTime, "__NULL__", last_case_id));
				last_events.add(new Event("End", endTime, "__NULL__", last_case_id));
			}
			case_events.put(last_case_id, last_events);
			if ((this.start_events.isEmpty() && this.end_events.isEmpty()) || (this.start_events.isEmpty() && !this.end_events.isEmpty())) {
				System.out.println(++idx);
				System.out.println("Case " + last_case_id + ": " + count + "个event");
			}
		}
		stmt.close();
		con.close();
		// do start_to_end filter
		Map<String, List<Event>> filter_case_events = new HashMap<String, List<Event>>();
		if (this.start_events.isEmpty() && this.end_events.isEmpty()) {
			System.out.println("no A and no F");
			filter_case_events = case_events;
		} else if (!this.start_events.isEmpty() && this.end_events.isEmpty()) {
			System.out.println("A and no F");
			IntWrapper cnt = new IntWrapper(0);
			for (String cid : case_events.keySet()) {
				Map<String, List<Event>> split_case_events = this.doFilterCaseEvent(cid, case_events.get(cid), true, false, cnt);
				if (!split_case_events.isEmpty()) {
					filter_case_events.putAll(split_case_events);
				}
			}
		} else if (!this.start_events.isEmpty() && !this.end_events.isEmpty()) {
			System.out.println("A and F");
			IntWrapper cnt = new IntWrapper(0);
			for (String cid : case_events.keySet()) {
				Map<String, List<Event>> split_case_events = this.doFilterCaseEvent(cid, case_events.get(cid), true, true, cnt);
				if (!split_case_events.isEmpty()) {
					filter_case_events.putAll(split_case_events);
				}
			}
		} else if (this.start_events.isEmpty() && !this.end_events.isEmpty()) {
			System.out.println("no A and F");
			filter_case_events = case_events;
		}
		for (String cid : filter_case_events.keySet()) {
			List<Event> eves = filter_case_events.get(cid);
			if (eves != null) {
				document = this.addRegEvents(document, eves, cid, eventprefix);
			}
		}
		this.caseEvents = filter_case_events;
		return document;
	}
	
	private Map<String, List<Event>> doFilterCaseEvent(String case_id, List<Event> case_events, boolean needStart, boolean needEnd, IntWrapper cnt) {
		Map<String, List<Event>> split_cases = new HashMap<String, List<Event>>();
		int idx = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i=case_events.size()-1; i>=0; i--) {
			Event curEvent = case_events.get(i);
			// 逆序遍历
			if (needStart && needEnd) {
				// A and F
				String activity = curEvent.getActivity();
				if (stack.empty() && this.end_events.contains(activity)) {
					// find F
					stack.push(i);
				}
				if (this.start_events.contains(activity) && ((i-1>=0 && !this.start_events.contains(case_events.get(i-1).getActivity())) || (i-1 < 0))) {
					// find A
					if (!stack.empty()) {
						int end_idx = stack.pop();
						int start_idx = i;
						String split_case_id = case_id + "_" + idx;
						List<Event> split_events = new ArrayList<Event>();
						String startTime = null;
						String endTime = null;
						for (int j=start_idx; j<=end_idx; j++) {
							Event nowEvent = case_events.get(j);
							nowEvent.setCaseid(split_case_id);
							split_events.add(nowEvent);
							String nowActivityTime = nowEvent.getTimestamp();
							if (this.isBPMN) {
								startTime = (startTime == null || startTime.compareTo(nowActivityTime) > 0) ? nowActivityTime : startTime;
								endTime = (endTime == null || endTime.compareTo(nowActivityTime) < 0) ? nowActivityTime : endTime;
							}
						}
						if (this.isBPMN) {
							split_events.add(0, new Event("Start", startTime, "__NULL__", split_case_id));
							split_events.add(new Event("End", endTime, "__NULL__", split_case_id));
						}
						if (!split_events.isEmpty()) {
							split_cases.put(split_case_id, split_events);
							System.out.println(cnt.incr());
							System.out.println("Case " + split_case_id + ": " + split_events.size() + "个event");
							idx ++;
						}
					}
				}
			} else if (needStart && !needEnd) {
				// A only
				String activity = curEvent.getActivity();
				if (i == case_events.size()-1) {
					stack.push(i);
				}
				if (this.start_events.contains(activity) && ((i-1>=0 && !this.start_events.contains(case_events.get(i-1).getActivity())) || (i-1 < 0))) {
					// find A
					if (!stack.empty()) {
						int end_idx = stack.pop();
						int start_idx = i;
						String split_case_id = case_id + "_" + idx;
						List<Event> split_events = new ArrayList<Event>();
						String startTime = null;
						String endTime = null;
						for (int j=start_idx; j<=end_idx; j++) {
							Event nowEvent = case_events.get(j);
							nowEvent.setCaseid(split_case_id);
							split_events.add(nowEvent);
							String nowActivityTime = nowEvent.getTimestamp();
							if (this.isBPMN) {
								startTime = (startTime == null || startTime.compareTo(nowActivityTime) > 0) ? nowActivityTime : startTime;
								endTime = (endTime == null || endTime.compareTo(nowActivityTime) < 0) ? nowActivityTime : endTime;
							}
						}
						if (this.isBPMN) {
							split_events.add(0, new Event("Start", startTime, "__NULL__", split_case_id));
							split_events.add(new Event("End", endTime, "__NULL__", split_case_id));
						}
						if (!split_events.isEmpty()) {
							split_cases.put(split_case_id, split_events);
							System.out.println(cnt.incr());
							System.out.println("Case " + split_case_id + ": " + split_events.size() + "个event");
							idx ++;
						}
						stack.push(i-1);
					}
				}
			}
		}
		return split_cases;
	}
	
}
