package db2xes.start2end;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class DB2XesStart2EndHelp {
	
	public static void generateXES(String filepath, String descname, String xesname, String eventprefix, List<String> startEvents, List<String> endEvents) {
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

		DB2XesStart2End db2xes = new DB2XesStart2End(false, true, "db2xes", "db2xes.xesevents",
				"CASE_ID", "USER_ID", "VISIT_TIME", "VISIT_MEAN", filename,
				xesname, eventprefix);
		if (startEvents != null && !startEvents.isEmpty()) {
			db2xes.addStartEvents(startEvents);
		}
		if (endEvents != null && !endEvents.isEmpty()) {
			db2xes.addEndEvents(endEvents);
		}
		db2xes.db2xes();

		System.setOut(sysout);
	}

}
