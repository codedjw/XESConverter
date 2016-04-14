package db2xes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class DB2XesMain5th {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间
		
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第五阶段/XES";
		
		try {
			DB2XesMain5th.genXes(filepath);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");
	}
	
	
	
	public static void genXes(String filepath)  throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String descname = "趣医网第五次日志";
		String xesname = "趣医网第五次日志";
		String eventprefix = "qyw";
		String query = "INSERT INTO db2xes.xesevents SELECT DISTINCT CASE_ID, OPERATE_TIME AS VISIT_TIME, USER_ID, ELEMENT_NAME AS VISIT_MEAN FROM qyw.qyw_5th_event WHERE ELEMENT_NAME != \'\' AND ELEMENT_NAME IS NOT null;";
		
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
