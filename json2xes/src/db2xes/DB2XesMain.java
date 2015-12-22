package db2xes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class DB2XesMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis();   //获取开始时间
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
		String filepath = "/Users/dujiawei/Desktop/流程挖掘案例/趣医网/趣医网-第三阶段/XES";
		String filename = filepath + "/all.xes"; // 医院名_daoyi.xes or 医院名_regular.xes
		String logname =filepath + "/all.log"; // 医院名_daoyi.log or 医院名_regular.log
		String xesname = "典型案例";  // 医院名_导医用户 or 医院名_自发用户
		String eventprefix = "典型案例";  // 医院名
		PrintStream sysout = System.out;
		
		// --- BEGIN --- (redirect log output to file)
		PrintStream printStream = null;
		File logfile=new File(logname);          
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
		
		DB2Xes db2xes = new DB2Xes(false, true, "qyw.daoyi_events_20151111_06", "CASE_ID", "USER_ID", "VISIT_TIME", "VISIT_MEAN", filename, xesname, eventprefix);
		db2xes.db2xes();
		
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.setOut(sysout);
		System.out.println("Whole 运行时间： "+(endTime-startTime)/1000/60+"min, "+(endTime-startTime)%(1000*60)/1000+"s, "+(endTime-startTime)%(1000*60)%1000+"ms");
	}

}
