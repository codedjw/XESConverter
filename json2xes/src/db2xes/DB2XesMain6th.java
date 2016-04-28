package db2xes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DB2XesMain6th {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间
		
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第六阶段/XES";
		
		try {
			Map<String, String> params = new HashMap<String, String>();
			// yuyue_succ
			params.put("query","INSERT INTO db2xes.xesevents SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM (SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM qyw_4th_event WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event WHERE VISIT_MEAN LIKE \'%成功%\') ) AS t3 INNER JOIN (SELECT DISTINCT t1.MEAN FROM qyw.qyw_4th_business_dict AS t1 INNER JOIN qyw.sys_business_dict_20151207_07 AS t2 ON t1.MEAN = t2.MEAN WHERE t2.TRIGGER_TYPE = \'用户点击\') AS t4 ON t3.VISIT_MEAN = t4.MEAN ORDER BY USER_ID, VISIT_TIME;");
			params.put("name","趣医网第六次日志（预约成功）");
			DB2XesMain6th.genXes(filepath, params.get("query"), params.get("name"));
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");

	}
	
	public static void genXes(String filepath, String sql, String name)  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = name;
		String xesname = name;
		String eventprefix = "qyw";
		String query = sql;
		
		Connection conn = DB2Xes.getCon("qyw");
		Statement stmt0 = conn.createStatement();
		Statement stmt = conn.createStatement();
		long beginTime = System.currentTimeMillis(); // 获取开始时间
		stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
		stmt.executeUpdate(query);
		long finishTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println(query + " 运行时间： " + (finishTime - beginTime) / 1000
				/ 60 + "min, " + (finishTime - beginTime) % (1000 * 60) / 1000
				+ "s, " + (finishTime - beginTime) % (1000 * 60) % 1000 + "ms");
		DB2XesHelp.generateXES(filepath, descname, xesname, eventprefix);
	}

}
