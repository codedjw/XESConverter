package json2xes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetFromDB {
	Connection con;
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// TODO Auto-generated method stub
		GetFromDB gfd= new GetFromDB();
		gfd.getJianyan();
		gfd.getJianchaxindian();
		gfd.getFayao();
		gfd.getGuahao();
	}
	public Connection getCon() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		System.out.println("ok1");
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url="jdbc:mysql://localhost:3306/hospital?user=root&password=1234";
		Connection con = DriverManager.getConnection(url);
		return con;
	}
	
	
	public  void getGuahao() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Connection con =getCon();
		Statement stmt = con.createStatement();
		String query = "select * from 挂号就诊";
		long startTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs=stmt.executeQuery(query);
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("运行时间： "+(endTime-startTime)+"ms");  
		System.out.println("okdb");
		//结果集ResultSet
		
		int count=0;
		Statement stmt1 = con.createStatement();
		while(rs.next())
		{
			//a=rs.getString("检验.挂号序号");
			
			String upd="insert into event (name,date,resource,regid) values('挂号','"+rs.getString("挂号就诊.挂号日期")+"','"+rs.getString("挂号就诊.挂号科室")+"','"+rs.getString("挂号就诊.挂号序号")+"')";
			String upd2="insert into event (name,date,resource,regid) values('首次就诊','"+rs.getString("挂号就诊.首次接诊日期")+"','"+rs.getString("挂号就诊.首次接诊医生工号")+rs.getString("挂号就诊.首次接诊医生姓名")+"','"+rs.getString("挂号就诊.挂号序号")+"')";
			String upd3="insert into event (name,date,resource,regid) values('最后一次就诊','"+rs.getString("挂号就诊.最后一次接诊日期")+"','"+rs.getString("挂号就诊.最后一次接诊医生工号")+rs.getString("挂号就诊.最后一次接诊医生姓名")+"','"+rs.getString("挂号就诊.挂号序号")+"')";
			//String upd4="insert into event (name,date,resource,regid) values('检验报告','"+rs.getString("检验.报告日期")+"','"+rs.getString("检验.报告人员工号")+rs.getString("检验.报告人员")+"','"+rs.getString("检验.挂号序号")+"')";
			//String upd5="insert into event (name,date,resource,regid) values('检验报告审核','"+rs.getString("检验.报告审核日期")+"','"+rs.getString("检验.审核人员工号")+rs.getString("检验.审核人员")+"','"+rs.getString("检验.挂号序号")+"')";
			//String upd6="insert into event (name,date,resource,regid) values('检验报告发布','"+rs.getString("检验.报告发布日期")+"','"+rs.getString("检验.发布人员工号")+rs.getString("检验.发布人员")+"','"+rs.getString("检验.挂号序号")+"')";
			
			if(rs.getString("挂号就诊.首次接诊日期").equals("首次接诊日期")){
				System.out.println(upd);
				continue;
			}
			//b=rs.getString("检验.开单日期");
			//System.out.println(upd);
			try{
			int cond=stmt1.executeUpdate(upd);
			}
			catch(Exception e){
				//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd2);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd3);
				}
				catch(Exception e){
					//e.printStackTrace();
			}


			count++;
			//break;
		}
		System.out.println(count+"!!! ");

	}

	
public  void getFayao() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Connection con =getCon();
		Statement stmt = con.createStatement();
		String query = "select * from 发药";
		long startTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs=stmt.executeQuery(query);
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("运行时间： "+(endTime-startTime)+"ms");  
		System.out.println("okdb");
		//结果集ResultSet
		
		int count=0;
		Statement stmt1 = con.createStatement();
		while(rs.next())
		{
			//a=rs.getString("检验.挂号序号");
			
			String upd="insert into event (name,date,resource,regid) values('发药开方','"+rs.getString("发药.开方日期")+"','"+rs.getString("发药.开方医生工号")+rs.getString("发药.开方医生姓名")+"','"+rs.getString("发药.挂号序号")+"')";
			String upd2="insert into event (name,date,resource,regid) values('发药收费','"+rs.getString("发药.收费日期")+"','"+rs.getString("发药.收费人员工号")+rs.getString("发药.收费人员")+"','"+rs.getString("发药.挂号序号")+"')";
			String upd3="insert into event (name,date,resource,regid) values('配药','"+rs.getString("发药.配药日期")+"','"+rs.getString("发药.发配药人员工号")+rs.getString("发药.发配药人员姓名")+"','"+rs.getString("发药.挂号序号")+"')";
			String upd4="insert into event (name,date,resource,regid) values('发药','"+rs.getString("发药.发药日期")+"','"+rs.getString("发药.发配药人员工号")+rs.getString("发药.发配药人员姓名")+"','"+rs.getString("发药.挂号序号")+"')";
			//String upd5="insert into event (name,date,resource,regid) values('检查心电等报告审核','"+rs.getString("检查心电.报告审核日期")+"','"+rs.getString("检查心电.审核人员")+rs.getString("检查心电.发布人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			//String upd6="insert into event (name,date,resource,regid) values('检查心电等报告发布','"+rs.getString("检查心电.报告发布日期")+"','"+rs.getString("检查心电.发布人员工号")+rs.getString("检查心电.发布人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			
			if(rs.getString("发药.开方日期").equals("开方日期")){
				System.out.println(upd);
				continue;
			}
			//b=rs.getString("检验.开单日期");
			//System.out.println(upd);
			try{
			int cond=stmt1.executeUpdate(upd);
			}
			catch(Exception e){
				//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd2);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd3);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd4);
				}
				catch(Exception e){
					//e.printStackTrace();
			}


			count++;
			//break;
		}
		System.out.println(count+"!!! ");

	}
	
	
	
	public  void getJianchaxindian() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		Connection con =getCon();
		Statement stmt = con.createStatement();
		String query = "select * from 检查心电";
		long startTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs=stmt.executeQuery(query);
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("运行时间： "+(endTime-startTime)+"ms");  
		System.out.println("okdb");
		//结果集ResultSet
		
		int count=0;
		Statement stmt1 = con.createStatement();
		while(rs.next())
		{
			//a=rs.getString("检验.挂号序号");
			
			String upd="insert into event (name,date,resource,regid) values('检查心电等开单','"+rs.getString("检查心电.开单日期")+"','"+rs.getString("检查心电.开单医生工号")+rs.getString("检查心电.开单医生姓名")+"','"+rs.getString("检查心电.挂号序号")+"')";
			String upd2="insert into event (name,date,resource,regid) values('检查心电等收费','"+rs.getString("检查心电.收费日期")+"','"+"检查心电收费人员信息暂缺"+"','"+rs.getString("检查心电.挂号序号")+"')";
			String upd3="insert into event (name,date,resource,regid) values('检查心电等','"+rs.getString("检查心电.检查日期")+"','"+rs.getString("检查心电.检查人员工号")+rs.getString("检查心电.检查人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			String upd4="insert into event (name,date,resource,regid) values('检查心电等报告','"+rs.getString("检查心电.报告日期")+"','"+rs.getString("检查心电.报告人员工号")+rs.getString("检查心电.报告人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			String upd5="insert into event (name,date,resource,regid) values('检查心电等报告审核','"+rs.getString("检查心电.报告审核日期")+"','"+rs.getString("检查心电.审核人员")+rs.getString("检查心电.发布人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			String upd6="insert into event (name,date,resource,regid) values('检查心电等报告发布','"+rs.getString("检查心电.报告发布日期")+"','"+rs.getString("检查心电.发布人员工号")+rs.getString("检查心电.发布人员")+"','"+rs.getString("检查心电.挂号序号")+"')";
			
			if(rs.getString("检查心电.开单日期").equals("开单日期")){
				System.out.println(upd);
				continue;
			}
			//b=rs.getString("检验.开单日期");
			//System.out.println(upd);
			try{
			int cond=stmt1.executeUpdate(upd);
			}
			catch(Exception e){
				//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd2);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd3);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd4);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd5);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd6);
				}
				catch(Exception e){
					//e.printStackTrace();
			}

			count++;
			//break;
		}
		System.out.println(count+"!!! ");

	}
	
	
	public  void getJianyan() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		System.out.println("ok2");
		Connection con =getCon();
		Statement stmt = con.createStatement();
		String query = "select * from 检验";
		long startTime=System.currentTimeMillis();   //获取开始时间  
		ResultSet rs=stmt.executeQuery(query);
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("运行时间： "+(endTime-startTime)+"ms");  
		System.out.println("okdb");
		//结果集ResultSet
		
		int count=0;
		Statement stmt1 = con.createStatement();
		while(rs.next())
		{
			//a=rs.getString("检验.挂号序号");
			
			String upd="insert into event (name,date,resource,regid) values('检验开单','"+rs.getString("检验.开单日期")+"','"+rs.getString("检验.开单医生工号")+rs.getString("检验.开单医生姓名")+"','"+rs.getString("检验.挂号序号")+"')";
			String upd2="insert into event (name,date,resource,regid) values('检验收费','"+rs.getString("检验.收费日期")+"','"+"检验收费人员信息暂缺"+"','"+rs.getString("检验.挂号序号")+"')";
			String upd3="insert into event (name,date,resource,regid) values('检验','"+rs.getString("检验.检验日期")+"','"+rs.getString("检验.检验人员工号")+rs.getString("检验.检验人员")+"','"+rs.getString("检验.挂号序号")+"')";
			String upd4="insert into event (name,date,resource,regid) values('检验报告','"+rs.getString("检验.报告日期")+"','"+rs.getString("检验.报告人员工号")+rs.getString("检验.报告人员")+"','"+rs.getString("检验.挂号序号")+"')";
			String upd5="insert into event (name,date,resource,regid) values('检验报告审核','"+rs.getString("检验.报告审核日期")+"','"+rs.getString("检验.审核人员工号")+rs.getString("检验.审核人员")+"','"+rs.getString("检验.挂号序号")+"')";
			String upd6="insert into event (name,date,resource,regid) values('检验报告发布','"+rs.getString("检验.报告发布日期")+"','"+rs.getString("检验.发布人员工号")+rs.getString("检验.发布人员")+"','"+rs.getString("检验.挂号序号")+"')";
			
			if(rs.getString("检验.开单日期").equals("开单日期")){
				System.out.println(upd);
				continue;
			}
			//b=rs.getString("检验.开单日期");
			//System.out.println(upd);
			try{
			int cond=stmt1.executeUpdate(upd);
			}
			catch(Exception e){
				//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd2);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd3);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd4);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd5);
				}
				catch(Exception e){
					//e.printStackTrace();
			}
			try{
				int cond=stmt1.executeUpdate(upd6);
				}
				catch(Exception e){
					//e.printStackTrace();
			}

			count++;
			//break;
		}
		System.out.println(count+"!!! ");

	}


}
