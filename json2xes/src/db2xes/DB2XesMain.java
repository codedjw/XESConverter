package db2xes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DB2XesMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间
		
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第三阶段/XES";
		
		try {
//			DB2XesMain.genXES_SpecialCase(filepath+"/cases", "866102022218694");
//			DB2XesMain.genXES_SpecialCase(filepath+"/cases", "867620026805215");
//			DB2XesMain.genXES_AllHospital(filepath+"/hospitals", true, false);
//			DB2XesMain.genXES_AllHospital(filepath+"/hospitals", false, false);
//			DB2XesMain.genXES_AllModule(filepath+"/modules");
//			DB2XesMain.genXES_AllBusinessModule(filepath+"/bus_modules");
//			DB2XesMain.genXES_AllHospital(filepath+"/hos_modules", true, true);
//			DB2XesMain.genXES_AllHospital(filepath+"/hos_modules", false, true);
			DB2XesMain.genXES_AllNotLoginUsers(filepath+"/not_login");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");
	}
	
	// 生成典型案例XES
	public static void genXES_SpecialCase(String filepath, String IMEI_ID) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = "IMEI_ID_" + IMEI_ID;
		String xesname = "典型案例_" + IMEI_ID;
		String eventprefix = "典型案例";

		// prepare (produce data to db2xes.xesevents)
		Connection con = DB2Xes.getCon("qyw");
		Statement stmt = con.createStatement();
		Statement stmt0 = con.createStatement();
		long beginTime = System.currentTimeMillis(); // 获取开始时间
		stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
		String query = "INSERT INTO db2xes.xesevents SELECT CASE_ID, VISIT_TIME, USER_ID, CONCAT_WS(\'+\',VISIT_MEAN, IF(VISIT_OP REGEXP \'/$\', SUBSTRING_INDEX(VISIT_OP,\'/\',\'-3\'), SUBSTRING_INDEX(VISIT_OP,\'/\',\'-1\'))) AS VISIT_MEAN FROM qyw.c_cus_events_20151207_07 WHERE IMEI_ID = \'"
				+ IMEI_ID + "\' AND USER_ID > 0 ORDER BY VISIT_TIME";
		stmt.executeUpdate(query);
		long finishTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println(query + " 运行时间： " + (finishTime - beginTime) / 1000
				/ 60 + "min, " + (finishTime - beginTime) % (1000 * 60) / 1000
				+ "s, " + (finishTime - beginTime) % (1000 * 60) % 1000 + "ms");

		// generate
		DB2XesMain.generateXES(filepath, descname, xesname, eventprefix);

	}
	
	// 生成各医院的XES （isDaoyi = true表示导医用户，否则表示一般用户）
	public static void genXES_AllHospital(String filepath, boolean isDaoyi, boolean considerModule) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Map<String, Integer> hospitalMaps = new HashMap<String, Integer>() {
			/**
			 * 初始化十佳医院列表
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
		Map<String, List<String>> businessModule = new HashMap<String, List<String>>() {
			/**
			 * 业务模块<->VISIT_OP_CAT对应表
			 */
			private static final long serialVersionUID = 1L;

			{
				put("预约挂号", new ArrayList<String>() {
					/**
					 * 预约挂号
					 */
					private static final long serialVersionUID = 1L;

				{
				    add("/APP/appoint");
				    add("/APP/area");
				    add("/APP/center");
				    add("/APP/hospitalInform");
				    add("/APP/medicalGuideWindow");
				    add("/APP/multibusiness");
				    add("/APP/patientwords");
				    add("/APP/register");
				    add("/APP/satisfaction");
				}});
//				put("地理信息", new ArrayList<String>() {
//					/**
//					 * 预约挂号
//					 */
//					private static final long serialVersionUID = 1L;
//
//				{
//				    add("/APP/area");
//				}});
			}
		};
		Iterator<Entry<String, List<String>>> iter = businessModule.entrySet().iterator();
		do {
			String module = "";
			String module_str = "";
			if (considerModule) {
				Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iter.next();
				module = "_"+entry.getKey();
				List<String> visit_op_cats = entry.getValue();
				String visit_op_cats_str = "";
				for (String visit_op_cat : visit_op_cats) {
					visit_op_cats_str += ("\'"+visit_op_cat+"\',");
				}
				visit_op_cats_str = visit_op_cats_str.substring(0, visit_op_cats_str.length()-1);
				module_str = " AND SUBSTRING_INDEX(VISIT_OP,\'/\', 3) IN ("+visit_op_cats_str+")";
			}
			for (String hname : hospitalMaps.keySet()) {
				Integer hid = hospitalMaps.get(hname);
				String desc_suffix = ""; String xes_suffix = ""; String daoyi_query = "";
				if (isDaoyi) {
					desc_suffix = "daoyi";
					xes_suffix = "导医用户";
					daoyi_query = "NOT ";
				} else {
					desc_suffix = "regular";
					xes_suffix = "自发用户";
				}
				String descname = "qyw_" + hid + "_" + desc_suffix + module;
				String xesname = hname + "_" + xes_suffix + module;
				String eventprefix = hname;

				// prepare (produce data to db2xes.xesevents)
				Connection con = DB2Xes.getCon("qyw");
				Statement stmt = con.createStatement();
				Statement stmt0 = con.createStatement();
				long beginTime = System.currentTimeMillis(); // 获取开始时间
				stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
				String query = "INSERT INTO db2xes.xesevents SELECT CASE_ID, VISIT_TIME, t1.USER_ID, VISIT_MEAN FROM (SELECT CASE_ID, VISIT_TIME, USER_ID, CONCAT_WS(\'+\',VISIT_MEAN,IF(VISIT_OP REGEXP \'/$\', SUBSTRING_INDEX(VISIT_OP,\'/\',\'-3\'), SUBSTRING_INDEX(VISIT_OP,\'/\',\'-1\'))) AS VISIT_MEAN FROM qyw.c_cus_events_20151207_07 WHERE HOSPITAL_ID = \'"
						+ hid
						+ "\' AND USER_ID > 0"
						+ module_str
						+" ORDER BY USER_ID) AS t1 LEFT JOIN (SELECT user_id AS DAOYI_ID FROM qyw.daoyi_users_20151207_07 ORDER BY DAOYI_ID) AS t2 ON t1.USER_ID = t2.DAOYI_ID WHERE t2.DAOYI_ID IS "
						+ daoyi_query + "NULL ORDER BY VISIT_TIME";
				stmt.executeUpdate(query);
				long finishTime = System.currentTimeMillis(); // 获取结束时间
				System.out.println(query + " 运行时间： " + (finishTime - beginTime)
						/ 1000 / 60 + "min, " + (finishTime - beginTime)
						% (1000 * 60) / 1000 + "s, " + (finishTime - beginTime)
						% (1000 * 60) % 1000 + "ms");

				// generate
				DB2XesMain.generateXES(filepath, descname, xesname, eventprefix);
			}
		} while (considerModule && iter.hasNext());
	}
	
	// 生成各VISIT_OP_CAT对应的XES（按模块分析）-> VISIT_OP_CAT = SUBSTRING_INDEX(VISIT_OP,'/', 3)
	public static void genXES_AllModule(String filepath) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// get VISIT_OP_CAT DB
		Connection con = DB2Xes.getCon("qyw");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT DISTINCT VISIT_OP_CAT FROM qyw.visit_op_cat_20151207_07");
		while (rs.next()) {
			String visit_op_cat = rs.getString("VISIT_OP_CAT");

			String descname = "ALL" + visit_op_cat.replaceAll("/", "_");
			String xesname = visit_op_cat;
			String eventprefix = "功能模块";

			// prepare (produce data to db2xes.xesevents)
			Statement stmt0 = con.createStatement();
			Statement stmt1 = con.createStatement();
			long beginTime = System.currentTimeMillis(); // 获取开始时间
			stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
			String query = "INSERT INTO db2xes.xesevents SELECT CASE_ID, VISIT_TIME, USER_ID, CONCAT_WS(\'+\',VISIT_MEAN,IF(VISIT_OP REGEXP \'/$\', SUBSTRING_INDEX(VISIT_OP,\'/\',\'-3\'), SUBSTRING_INDEX(VISIT_OP,\'/\',\'-1\'))) AS VISIT_MEAN FROM qyw.c_cus_events_20151207_07 WHERE USER_ID > 0 AND SUBSTRING_INDEX(VISIT_OP,\'/\', 3) = \'"
					+ visit_op_cat + "\' ORDER BY VISIT_TIME";
			stmt1.executeUpdate(query);
			long finishTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println(query + " 运行时间： " + (finishTime - beginTime)
					/ 1000 / 60 + "min, " + (finishTime - beginTime)
					% (1000 * 60) / 1000 + "s, " + (finishTime - beginTime)
					% (1000 * 60) % 1000 + "ms");

			// generate
			DB2XesMain.generateXES(filepath, descname, xesname, eventprefix);
		}
	}
	
	// not recommend (too large, not general, so many noise)
	// 生成业务模块对应的XES（按模块分析）-> filter in VISIT_CAT，然后查找VISIT_OP_CAT = SUBSTRING_INDEX(VISIT_OP,'/', 3)
	public static void genXES_AllBusinessModule(String filepath) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Map<String, List<String>> businessModule = new HashMap<String, List<String>>() {
			/**
			 * 业务模块<->VISIT_OP_CAT对应表
			 */
			private static final long serialVersionUID = 1L;

			{
				put("预约挂号", new ArrayList<String>() {
					/**
					 * 预约挂号
					 */
					private static final long serialVersionUID = 1L;

				{
				    add("/APP/appoint");
				    add("/APP/area");
				    add("/APP/center");
				    add("/APP/hospitalInform");
				    add("/APP/medicalGuideWindow");
				    add("/APP/multibusiness");
				    add("/APP/patientwords");
				    add("/APP/register");
				    add("/APP/satisfaction");
				}});
			}
		};
		
		for (String module : businessModule.keySet()) {
			List<String> visit_op_cats = businessModule.get(module);
			String visit_op_cats_str = "";
			for (String visit_op_cat : visit_op_cats) {
				visit_op_cats_str += ("\'"+visit_op_cat+"\',");
			}
			visit_op_cats_str = visit_op_cats_str.substring(0, visit_op_cats_str.length()-1);
			String descname = "qyw_" + module;
			String xesname = module;
			String eventprefix = module;

			// prepare (produce data to db2xes.xesevents)
			Connection con = DB2Xes.getCon("qyw");
			Statement stmt = con.createStatement();
			Statement stmt0 = con.createStatement();
			long beginTime = System.currentTimeMillis(); // 获取开始时间
			stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
			String query = "INSERT INTO db2xes.xesevents SELECT CASE_ID, VISIT_TIME, USER_ID, CONCAT_WS(\'+\',VISIT_MEAN,IF(VISIT_OP REGEXP \'/$\', SUBSTRING_INDEX(VISIT_OP,\'/\',\'-3\'), SUBSTRING_INDEX(VISIT_OP,\'/\',\'-1\'))) AS VISIT_MEAN FROM qyw.c_cus_events_20151207_07 WHERE USER_ID > 0 AND SUBSTRING_INDEX(VISIT_OP,\'/\', 3) IN ("
					+ visit_op_cats_str + ") ORDER BY VISIT_TIME";
			stmt.executeUpdate(query);
			long finishTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println(query + " 运行时间： " + (finishTime - beginTime)
					/ 1000 / 60 + "min, " + (finishTime - beginTime)
					% (1000 * 60) / 1000 + "s, " + (finishTime - beginTime)
					% (1000 * 60) % 1000 + "ms");

			// generate
			DB2XesMain.generateXES(filepath, descname, xesname, eventprefix);
		}
	}
	
	public static void genXES_AllNotLoginUsers(String filepath) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = "qyw_" + "未登录用户";
		String xesname = "未登录用户";
		String eventprefix = "未登录用户";
		
		// prepare (produce data to db2xes.xesevents)
		Connection con = DB2Xes.getCon("qyw");
		Statement stmt = con.createStatement();
		Statement stmt0 = con.createStatement();
		long beginTime = System.currentTimeMillis(); // 获取开始时间
		stmt0.executeUpdate("TRUNCATE TABLE db2xes.xesevents");
		String query = "INSERT INTO db2xes.xesevents SELECT CONCAT_WS(\'@\',IMEI_ID,DATE_FORMAT(VISIT_TIME,\'%Y-%m-%d\')) AS CASE_ID, VISIT_TIME, IMEI_ID AS USER_ID, CONCAT_WS(\'+\',VISIT_MEAN,IF(VISIT_OP REGEXP \'/$\', SUBSTRING_INDEX(VISIT_OP,\'/\',\'-3\'), SUBSTRING_INDEX(VISIT_OP,\'/\',\'-1\'))) AS VISIT_MEAN FROM qyw.c_cus_events_20151207_07 WHERE USER_ID = -1 AND (IMEI_ID REGEXP \'^[0-9]{15}$\' OR IMEI_ID REGEXP \'^[0-9A-Z]{8}-[0-9A-Z]{4}-[0-9A-Z]{4}-[0-9A-Z]{4}-[0-9A-Z]{12}$\' OR IMEI_ID REGEXP \'^[0-9A-Fa-f]{14,15}$\') AND IMEI_ID NOT REGEXP \'^[0]{1,}$\' ORDER BY IMEI_ID, VISIT_TIME";
		stmt.executeUpdate(query);
		long finishTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println(query + " 运行时间： " + (finishTime - beginTime) / 1000
				/ 60 + "min, " + (finishTime - beginTime) % (1000 * 60) / 1000
				+ "s, " + (finishTime - beginTime) % (1000 * 60) % 1000 + "ms");

		// generate
		DB2XesMain.generateXES(filepath, descname, xesname, eventprefix);
	}
	
	public static void generateXES(String filepath, String descname, String xesname, String eventprefix) {
		String filename = filepath + "/" + descname + ".xes"; // 医院名_daoyi.xes
																// or
																// 医院名_regular.xes
		String logname = filepath + "/" + descname + ".log"; // 医院名_daoyi.log or
																// 医院名_regular.log
		PrintStream sysout = System.out; // always console output

		// --- BEGIN --- (redirect log output to file)
		PrintStream printStream = null;
		File logfile = new File(logname);
		try {
			logfile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(logfile);
			printStream = new PrintStream(fileOutputStream);
			System.setOut(printStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// --- END ---

		DB2Xes db2xes = new DB2Xes(false, true, "db2xes", "db2xes.xesevents",
				"CASE_ID", "USER_ID", "VISIT_TIME", "VISIT_MEAN", filename,
				xesname, eventprefix);
		db2xes.db2xes();

		System.setOut(sysout);
	}

}
