package json2xes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
import org.dom4j.io.OutputFormat;  
import org.dom4j.io.XMLWriter; 

public class Enity2xes {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="/Users/tiffany/develop/TestWorkspace/json2xes/jsonfiles/json挂号就诊5月上.json";
		List<Registration> list=new LinkedList<Registration>();
		Json2entity j2e=new Json2entity();
		list=j2e.read2entitylist(s);
		System.out.println("ok1");
		Document document = DocumentHelper.createDocument();
		String name="医院流程";
		document=prepare(name);
		document=addRegEvents(document,list,"医院挂号就诊流程");

		output(document);
		
		
       
		
		
	}
	
	static Document addRegEvents(Document document,List<Registration> list,String name){
		System.out.println("okreg");
		Document document2 = DocumentHelper.createDocument();
		
		document2=document;
		
		Element logElement = document2.getRootElement();
		for (Registration reg : list){
			Element trace=logElement.addElement("trace");
			Element key1=trace.addElement("string");
			key1.addAttribute("key", "concept:name");
			key1.addAttribute("value", name+"_"+reg.getRegID());
			Element key2=trace.addElement("string");
			key2.addAttribute("key", "description");
			key2.addAttribute("value", "Process Instance");
			
			Element eventReg=trace.addElement("event");
				Element eleReg1=eventReg.addElement("string");
				eleReg1.addAttribute("key", "concept:name");
				eleReg1.addAttribute("value", "挂号");
				Element eleReg2=eventReg.addElement("date");
				eleReg2.addAttribute("key", "time:timestamp");
				eleReg2.addAttribute("value", reg.getRegDate());
				Element eleReg3=eventReg.addElement("string");
				eleReg3.addAttribute("key", "org:resource");
				eleReg3.addAttribute("value", reg.getRegDepartment());
				Element eleReg4=eventReg.addElement("string");
				eleReg4.addAttribute("key", "service");
				eleReg4.addAttribute("value", "");
				Element eleReg5=eventReg.addElement("string");
				eleReg5.addAttribute("key", "lifecycle:transition");
				eleReg5.addAttribute("value", "complete");
				
			Element eventFirstReception=trace.addElement("event");
				Element eleFirstRecption1=eventFirstReception.addElement("string");
				eleFirstRecption1.addAttribute("key", "concept:name");
				eleFirstRecption1.addAttribute("value", "首次就诊");
				Element eleFirstRecption2=eventFirstReception.addElement("date");
				eleFirstRecption2.addAttribute("key", "time:timestamp");
				eleFirstRecption2.addAttribute("value", reg.getFirstReceptionDate());
				Element eleFirstRecption3=eventFirstReception.addElement("string");
				eleFirstRecption3.addAttribute("key", "org:resource");
				eleFirstRecption3.addAttribute("value", reg.getFirstReceptionDoctorID()+reg.getFirstReceptionDoctorName());
				Element eleFirstRecption4=eventFirstReception.addElement("string");
				eleFirstRecption4.addAttribute("key", "service");
				eleFirstRecption4.addAttribute("value", "");
				Element eleFirstRecption5=eventFirstReception.addElement("string");
				eleFirstRecption5.addAttribute("key", "lifecycle:transition");
				eleFirstRecption5.addAttribute("value", "complete");
			
			
			
			Element eventFinalReception=trace.addElement("event");
				Element eleFinalRecption1=eventFinalReception.addElement("string");
				eleFinalRecption1.addAttribute("key", "concept:name");
				eleFinalRecption1.addAttribute("value", "最后一次就诊");
				Element eleFinalRecption2=eventFinalReception.addElement("date");
				eleFinalRecption2.addAttribute("key", "time:timestamp");
				eleFinalRecption2.addAttribute("value", reg.getFinalReceptionDate());
				Element eleFinalRecption3=eventFinalReception.addElement("string");
				eleFinalRecption3.addAttribute("key", "org:resource");
				eleFinalRecption3.addAttribute("value", reg.getFinalReceptionDoctorID()+reg.getFinalReceptionDoctorName());
				Element eleFinalRecption4=eventFinalReception.addElement("string");
				eleFinalRecption4.addAttribute("key", "service");
				eleFinalRecption4.addAttribute("value", "");
				Element eleFinalRecption5=eventFinalReception.addElement("string");
				eleFinalRecption5.addAttribute("key", "lifecycle:transition");
				eleFinalRecption5.addAttribute("value", "complete");
			
		}
		return document2;
		
	}
	
	static Document prepare(String name){
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
	  

	        
//	       
	//
//	        int i = 0;  
//	        for (Registration o : list) {  
//	            Element nodeElement = nodesElement.addElement("node");  
//	            if (o instanceof Map) {  
//	                for (Object obj : ((Map) o).keySet()) {  
//	                    Element keyElement = nodeElement.addElement("key");  
//	                    keyElement.addAttribute("label", String.valueOf(obj));  
//	                    keyElement.setText(String.valueOf(((Map) o).get(obj)));  
//	                }  
//	            } else {  
//	                Element keyElement = nodeElement.addElement("key");  
//	                keyElement.addAttribute("label", String.valueOf(i));  
//	                keyElement.setText(String.valueOf(o));  
//	            }  
//	            i++;  
//	        }  
	       // return doc2String(document);  

	}
	
	static void output(Document document){
        OutputFormat format = new OutputFormat("    ", true);// 设置缩进为4个空格，并且另起一行为true
        XMLWriter xmlWriter2;
		try {
			xmlWriter2 = new XMLWriter(
			        new FileOutputStream("test0612b.xes"), format);
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
