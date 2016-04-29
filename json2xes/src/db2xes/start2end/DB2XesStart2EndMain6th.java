package db2xes.start2end;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB2XesStart2EndMain6th {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间
		
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第六阶段/XES";
		
		try {
			Map<String, String> params = new HashMap<String, String>();
			// yuyue_succ
			params.put("query_tmp","INSERT INTO db2xes.xesevents_tmp SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM (SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM qyw_4th_event WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event WHERE VISIT_MEAN LIKE \'%成功%\')) AS t3 INNER JOIN (SELECT DISTINCT t1.MEAN FROM qyw.qyw_4th_business_dict AS t1 INNER JOIN qyw.sys_business_dict_20151207_07 AS t2 ON t1.MEAN = t2.MEAN WHERE t2.TRIGGER_TYPE = \'用户点击\') AS t4 ON t3.VISIT_MEAN = t4.MEAN ORDER BY USER_ID, VISIT_TIME;");
			params.put("query","INSERT INTO db2xes.xesevents SELECT * FROM db2xes.xesevents_tmp WHERE CASE_ID IN (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\')) AS t2 ON t1.CASE_ID = t2.CASE_ID);");
			params.put("name","趣医网第六次日志（预约成功）C端主页的预约挂号入口_获取预约挂号结果页面");
			List<String> startEvents = new ArrayList<String>() {
				{
					add("C端主页的预约挂号入口");
				}
			};
			List<String> endEvents = new ArrayList<String>() {
				{
//					add("获取预约挂号详情");
					add("获取预约挂号结果页面");
				}
			};
			DB2XesStart2EndMain6th.genXes(filepath, params.get("query_tmp"), params.get("query"), params.get("name"), startEvents, endEvents);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");

	}
	
	public static void genXes(String filepath, String sql_tmp, String sql, String name, List<String> startEvents, List<String> endEvents)  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = name;
		String xesname = name;
		String eventprefix = "qyw";
		String query = sql;
		
		Connection conn = DB2XesStart2End.getCon("qyw");
		Statement stmt0 = conn.createStatement();
		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
		Statement stmt3 = conn.createStatement();
		long beginTime = System.currentTimeMillis(); // 获取开始时间
		stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
		stmt1.executeUpdate("TRUNCATE TABLE db2xes.xesevents_tmp");
		stmt2.executeUpdate(sql_tmp);
		stmt3.executeUpdate(sql);
		long finishTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println(query + " 运行时间： " + (finishTime - beginTime) / 1000
				/ 60 + "min, " + (finishTime - beginTime) % (1000 * 60) / 1000
				+ "s, " + (finishTime - beginTime) % (1000 * 60) % 1000 + "ms");
		DB2XesStart2EndHelp.generateXES(filepath, descname, xesname, eventprefix, startEvents, endEvents);
	}

}
