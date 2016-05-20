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
		
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第六阶段/XES/新老用户";
		
		try {
			List<Map<String, Object>> reqList = new ArrayList<Map<String, Object>>();
			
			// 初始化调用参数
			// 预约成功
//			reqList.add(DB2XesStart2EndMain6th.initYuyue_Succ());
			// 预约业务
//			reqList.add(DB2XesStart2EndMain6th.initYuyue_All());
			// 门诊缴费
//			reqList.add(DB2XesStart2EndMain6th.initMenZhenJiaoFei_Start_End());		
			// 新/老用户
			reqList.add(DB2XesStart2EndMain6th.initYuYueAll_New());
			reqList.add(DB2XesStart2EndMain6th.initYuYueAll_Old());
			reqList.add(DB2XesStart2EndMain6th.initYuYueSucc_New());
			reqList.add(DB2XesStart2EndMain6th.initYuYueSucc_Old());
			reqList.add(DB2XesStart2EndMain6th.initYuYueFail_New());
			reqList.add(DB2XesStart2EndMain6th.initYuYueFail_Old());
			
			for (Map<String, Object> params : reqList) {
				if (params != null && !params.isEmpty()) {
					DB2XesStart2EndMain6th.genXes(filepath, (String)params.get("query_tmp"), (String)params.get("query"), (String)params.get("name"), (List<String>)params.get("startEvents"), (List<String>)params.get("endEvents"), (List<String>)params.get("inKeyEvents"), (List<String>)params.get("exKeyEvents"));
				}
			}
			
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");

	}
	
	public static Map<String, Object> initYuyue_Succ() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp","INSERT INTO db2xes.xesevents_tmp SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM (SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM qyw_4th_event WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event WHERE VISIT_MEAN LIKE \'%成功%\')) AS t3 INNER JOIN (SELECT DISTINCT t1.MEAN FROM qyw.qyw_4th_business_dict AS t1 INNER JOIN qyw.sys_business_dict_20151207_07 AS t2 ON t1.MEAN = t2.MEAN WHERE t2.TRIGGER_TYPE = \'用户点击\') AS t4 ON t3.VISIT_MEAN = t4.MEAN ORDER BY USER_ID, VISIT_TIME;");
		params.put("query","INSERT INTO db2xes.xesevents SELECT * FROM db2xes.xesevents_tmp WHERE CASE_ID IN (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\')) AS t2 ON t1.CASE_ID = t2.CASE_ID);");
		params.put("name","趣医网第六次日志（预约成功）_C端主页的预约挂号入口_获取预约挂号结果页面");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
//				add("获取预约挂号详情");
				add("获取预约挂号结果页面");
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuyue_All() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query","INSERT INTO db2xes.xesevents SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM (SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM qyw_4th_event WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event WHERE VISIT_MEAN LIKE \'%C端主页的预约挂号入口%\')) AS t3 INNER JOIN (SELECT DISTINCT t1.MEAN FROM qyw.qyw_4th_business_dict AS t1 INNER JOIN qyw.sys_business_dict_20151207_07 AS t2 ON t1.MEAN = t2.MEAN WHERE t2.TRIGGER_TYPE = \'用户点击\') AS t4 ON t3.VISIT_MEAN = t4.MEAN ORDER BY USER_ID, VISIT_TIME;");
		params.put("name","趣医网第六次日志_C端主页的预约挂号入口_ALL");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initMenZhenJiaoFei_Start_End() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", "INSERT INTO db2xes.xesevents_tmp SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM (SELECT DISTINCT CASE_ID, VISIT_TIME, USER_ID, VISIT_MEAN FROM qyw_4th_event WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event WHERE VISIT_MEAN = \'获取门诊已缴费记录\')) AS t3 INNER JOIN (SELECT DISTINCT t1.MEAN FROM qyw.qyw_4th_business_dict AS t1 INNER JOIN qyw.sys_business_dict_20151207_07 AS t2 ON t1.MEAN = t2.MEAN WHERE t2.TRIGGER_TYPE = \'用户点击\') AS t4 ON t3.VISIT_MEAN = t4.MEAN ORDER BY USER_ID, VISIT_TIME; ");
		params.put("query", "INSERT INTO db2xes.xesevents SELECT * FROM db2xes.xesevents_tmp WHERE CASE_ID IN (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'待缴费记录查询\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM db2xes.xesevents_tmp WHERE VISIT_MEAN IN (\'获取门诊已缴费记录\')) AS t2 ON t1.CASE_ID = t2.CASE_ID);");
		params.put("name", "趣医网第六次日志_门诊缴费");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("待缴费记录查询");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				add("获取门诊已缴费记录");
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueSucc_New() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_new_user WHERE CASE_ID IN (SELECT DISTINCT t5.CASE_ID FROM (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\', \'获取预约挂号详情\')) AS t2 ON t1.CASE_ID = t2.CASE_ID) AS t5 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN LIKE \'%成功%\') AS t6 ON t5.CASE_ID = t6.CASE_ID) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_新用户_预约业务（成功）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				add("获取预约挂号结果页面");
				add("获取预约挂号详情");
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				add("成功提交预约（不缴费）");
				add("成功提交预约（缴费）");
				add("成功提交挂号");
				add("预约成功后支付");
				add("挂号成功后支付");
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueSucc_Old() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_old_user WHERE CASE_ID IN (SELECT DISTINCT t5.CASE_ID FROM (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\', \'获取预约挂号详情\')) AS t2 ON t1.CASE_ID = t2.CASE_ID) AS t5 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN LIKE \'%成功%\') AS t6 ON t5.CASE_ID = t6.CASE_ID) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_老用户_预约业务（成功）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				add("获取预约挂号结果页面");
				add("获取预约挂号详情");
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				add("成功提交预约（不缴费）");
				add("成功提交预约（缴费）");
				add("成功提交挂号");
				add("预约成功后支付");
				add("挂号成功后支付");
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueAll_New() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_new_user WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_新用户_预约业务（所有）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueAll_Old() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_old_user WHERE CASE_ID IN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_老用户_预约业务（所有）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueFail_New() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_new_user WHERE CASE_ID IN (SELECT DISTINCT t7.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t7 LEFT JOIN (SELECT DISTINCT t5.CASE_ID FROM (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\', \'获取预约挂号详情\')) AS t2 ON t1.CASE_ID = t2.CASE_ID) AS t5 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_new_user WHERE VISIT_MEAN LIKE \'%成功%\') AS t6 ON t5.CASE_ID = t6.CASE_ID) AS t8 ON t7.CASE_ID = t8.CASE_ID WHERE t8.CASE_ID IS NULL) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_新用户_预约业务（失败）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				add("成功提交预约（不缴费）");
				add("成功提交预约（缴费）");
				add("成功提交挂号");
				add("预约成功后支付");
				add("挂号成功后支付");
			}
		});
		return params;
	}
	
	public static Map<String, Object> initYuYueFail_Old() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query_tmp", null);
		params.put("query", "INSERT INTO db2xes.xesevents SELECT DISTINCT t3.CASE_ID, t3.VISIT_TIME, t3.USER_ID, t3.VISIT_MEAN FROM (SELECT * FROM qyw_4th_event_old_user WHERE CASE_ID IN (SELECT DISTINCT t7.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t7 LEFT JOIN (SELECT DISTINCT t5.CASE_ID FROM (SELECT DISTINCT t1.CASE_ID FROM (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'C端主页的预约挂号入口\', \'获取医生列表\')) AS t1 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN IN (\'获取预约挂号结果页面\', \'获取预约挂号详情\')) AS t2 ON t1.CASE_ID = t2.CASE_ID) AS t5 INNER JOIN (SELECT DISTINCT CASE_ID FROM qyw_4th_event_old_user WHERE VISIT_MEAN LIKE \'%成功%\') AS t6 ON t5.CASE_ID = t6.CASE_ID) AS t8 ON t7.CASE_ID = t8.CASE_ID WHERE t8.CASE_ID IS NULL) ORDER BY VISIT_MEAN) AS t3 INNER JOIN (SELECT * FROM qyw.sys_business_dict_20151207_07 ORDER BY MEAN) AS t4 ON t3.VISIT_MEAN = t4.MEAN WHERE t4.TRIGGER_TYPE = \'用户点击\' ORDER BY t3.USER_ID, t3.VISIT_TIME;");
		params.put("name", "趣医网第六次日志_老用户_预约业务（失败）");
		params.put("startEvents", new ArrayList<String>() {
			{
				add("C端主页的预约挂号入口");
				add("获取医生列表");
			}
		});
		params.put("endEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("inKeyEvents", new ArrayList<String>() {
			{
				
			}
		});
		params.put("exKeyEvents", new ArrayList<String>() {
			{
				add("成功提交预约（不缴费）");
				add("成功提交预约（缴费）");
				add("成功提交挂号");
				add("预约成功后支付");
				add("挂号成功后支付");
			}
		});
		return params;
	}
	
	
	public static void genXes(String filepath, String sql_tmp, String sql, String name, List<String> startEvents, List<String> endEvents, List<String> inKeyEvents, List<String> exKeyEvents)  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = name;
		String xesname = name;
		String eventprefix = "qyw";
		String query = sql;
		
		Connection conn = DB2XesStart2End.getCon("qyw");
		Statement stmt = conn.createStatement();
		long beginTime = System.currentTimeMillis(); // 获取开始时间
		if (sql_tmp != null) {
			stmt.executeUpdate("TRUNCATE TABLE db2xes.xesevents_tmp");
			stmt.executeUpdate(sql_tmp);
		}
		if (sql != null) {
			stmt.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
			stmt.executeUpdate(sql);
		}
		long finishTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println(query + " 运行时间： " + (finishTime - beginTime) / 1000
				/ 60 + "min, " + (finishTime - beginTime) % (1000 * 60) / 1000
				+ "s, " + (finishTime - beginTime) % (1000 * 60) % 1000 + "ms");
		DB2XesStart2EndHelp.generateXES(filepath, descname, xesname, eventprefix, startEvents, endEvents, inKeyEvents, exKeyEvents);
		stmt.close();
		conn.close();
	}

}
