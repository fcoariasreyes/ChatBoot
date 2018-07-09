package cl.programando.aimlbusinesscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class OracleEloquaController {

	private String ELOQUA_USERNAME;
    private String ELOQUA_PASSWORD;
    private String ELOQUA_URL;
    private Boolean ELOQUA_SENDSMS_DELETECONTACT=false;
    private String ELOQUA_SEARCH_TYPE;
    private String ELOQUA_SEARCH_NAME;
    
    private String ELOQUA_PARAM_SMS_TEXT;
    private String ELOQUA_PARAM_TIMESTAMP;
    private String ELOQUA_PARAM_SMS_MESSAGEID;
    private String ELOQUA_PARAM_SMS_STATUS;
    
    private Integer ELOQUA_PARAM_SMS_TEXT_ID;
    private Integer ELOQUA_PARAM_TIMESTAMP_ID;
    private Integer ELOQUA_PARAM_SMS_MESSAGEID_ID;
    private Integer ELOQUA_PARAM_SMS_STATUS_ID;
    

    private String INFOBIP_USERNAME;
    private String INFOBIP_PASSWORD;
    private String INFOBIP_URL;
    private String INFOBIP_URL_REVIEW;
    private String INFOBIP_SMS_FORMAT;
    
    private Boolean LOG_DEBUG;
    private String LOG_PATH;
    private String LOG_CSV_NAME_FILE;
	
    public OracleEloquaController(String path) throws ParseException {
    	load(path);
    }
    
    private boolean load(String path) throws ParseException {
    	Properties prop = new Properties();
    	InputStream input = null;

    	try {

    		OEBCUtil.logInfo("Load properties "+path);
    		input = new FileInputStream(path);

    		OEBCUtil.logInfo("Parse properties "+path);
    		prop.load(input);
    		
    		this.ELOQUA_USERNAME=prop.getProperty("ELOQUA_USERNAME");
    		this.ELOQUA_PASSWORD=prop.getProperty("ELOQUA_PASSWORD");
    		this.ELOQUA_URL=prop.getProperty("ELOQUA_URL");
    		this.ELOQUA_SENDSMS_DELETECONTACT=Boolean.parseBoolean(prop.getProperty("ELOQUA_SENDSMS_DELETECONTACT"));
    		this.ELOQUA_SEARCH_TYPE=prop.getProperty("ELOQUA_SEARCH_TYPE");
    		this.ELOQUA_SEARCH_NAME=prop.getProperty("ELOQUA_SEARCH_NAME");
    		
    		this.ELOQUA_PARAM_SMS_TEXT=prop.getProperty("ELOQUA_PARAM_SMS_TEXT");
    		this.ELOQUA_PARAM_TIMESTAMP=prop.getProperty("ELOQUA_PARAM_TIMESTAMP");
    		this.ELOQUA_PARAM_SMS_MESSAGEID=prop.getProperty("ELOQUA_PARAM_SMS_MESSAGEID");
    		this.ELOQUA_PARAM_SMS_STATUS=prop.getProperty("ELOQUA_PARAM_SMS_STATUS");

    		this.INFOBIP_USERNAME=prop.getProperty("INFOBIP_USERNAME");
    		this.INFOBIP_PASSWORD=prop.getProperty("INFOBIP_PASSWORD");
    		this.INFOBIP_URL=prop.getProperty("INFOBIP_URL");
    		this.INFOBIP_URL_REVIEW=prop.getProperty("INFOBIP_URL_REVIEW");
    		this.INFOBIP_SMS_FORMAT=prop.getProperty("INFOBIP_SMS_FORMAT");
    		
    		this.LOG_DEBUG=Boolean.parseBoolean(prop.getProperty("LOG_DEBUG"));
    		this.LOG_PATH=prop.getProperty("LOG_PATH");
    		this.LOG_CSV_NAME_FILE=prop.getProperty("LOG_CSV_NAME_FILE");
    		
    		OEBCUtil.logInfo("ELOQUA_USERNAME "+this.ELOQUA_USERNAME);
    		OEBCUtil.logInfo("ELOQUA_PASSWORD "+this.ELOQUA_PASSWORD);
    		OEBCUtil.logInfo("ELOQUA_URL "+this.ELOQUA_URL);
    		OEBCUtil.logInfo("ELOQUA_SENDSMS_DELETECONTACT "+this.ELOQUA_SENDSMS_DELETECONTACT);
    		
    		OEBCUtil.logInfo("INFOBIP_USERNAME "+this.INFOBIP_USERNAME);
    		OEBCUtil.logInfo("INFOBIP_PASSWORD "+this.INFOBIP_PASSWORD);
    		OEBCUtil.logInfo("INFOBIP_URL "+this.INFOBIP_URL);

    		OEBCUtil.logInfo("LOG_DEBUG "+this.LOG_DEBUG);
    		OEBCUtil.logInfo("LOG_PATH "+this.LOG_PATH);
    		OEBCUtil.logInfo("LOG_CSV_NAME_FILE "+this.LOG_CSV_NAME_FILE);
    		
    		//Buscamos lod ids de los fields	
    		String json = callEloquaGET("/api/REST/1.0/assets/contact/fields?search="+this.ELOQUA_PARAM_SMS_TEXT);
    		List<Object> jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
    		if (jsonList!=null && jsonList.size()!=0) {
    			ELOQUA_PARAM_SMS_TEXT_ID = Integer.parseInt((String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id"));

    		}
    		
    		json = callEloquaGET("/api/REST/1.0/assets/contact/fields?search="+this.ELOQUA_PARAM_TIMESTAMP);
    		jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
    		if (jsonList!=null && jsonList.size()!=0) {
    			ELOQUA_PARAM_TIMESTAMP_ID = Integer.parseInt((String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id"));

    		}
    		
    		json = callEloquaGET("/api/REST/1.0/assets/contact/fields?search="+this.ELOQUA_PARAM_SMS_MESSAGEID);
    		jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
    		if (jsonList!=null && jsonList.size()!=0) {
    			ELOQUA_PARAM_SMS_MESSAGEID_ID = Integer.parseInt((String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id"));

    		}
    		
    		json = callEloquaGET("/api/REST/1.0/assets/contact/fields?search="+this.ELOQUA_PARAM_SMS_STATUS);
    		jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
    		if (jsonList!=null && jsonList.size()!=0) {
    			ELOQUA_PARAM_SMS_STATUS_ID = Integer.parseInt((String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id"));

    		}
    		
    		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_TEXT = "+ELOQUA_PARAM_SMS_TEXT+ "\tELOQUA_PARAM_SMS_TEXT_ID = "+ ELOQUA_PARAM_SMS_TEXT_ID);
    		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_MESSAGEID = "+ELOQUA_PARAM_SMS_MESSAGEID+ "\tELOQUA_PARAM_SMS_MESSAGEID_ID = "+ ELOQUA_PARAM_SMS_MESSAGEID_ID);
    		OEBCUtil.logInfo("ELOQUA_PARAM_TIMESTAMP = "+ELOQUA_PARAM_TIMESTAMP+ "\tELOQUA_PARAM_TIMESTAMP_ID = "+ ELOQUA_PARAM_TIMESTAMP_ID);
    		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_STATUS = "+ELOQUA_PARAM_SMS_STATUS+ "\tELOQUA_PARAM_SMS_STATUS_ID = "+ ELOQUA_PARAM_SMS_STATUS_ID);

    		return true;
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	return false;
    }
	private  String callEloquaGET(String url) {
		
		try {
	    	OEBCUtil.logInfo(ELOQUA_URL+url);
			
	    	String json = OEBCUtil.execMethodGET(ELOQUA_URL+url, ELOQUA_USERNAME, ELOQUA_PASSWORD);
			
	    	OEBCUtil.logDebug("Return JSON: " + json);
		    return json;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	
	
	private  boolean deleteClientFromIdToContactList(Integer contactListId, String contactListName, Integer clientId) throws ParseException{
		
		String url="/api/REST/1.0/assets/contact/list/"+contactListId;
	    
	    try {
			String deleteClientJson = "{\n" + 
					"  \"id\": \""+contactListId+"\",\n" + 
					"  \"name\": \""+contactListName+"\",\n" + 
					"\"membershipDeletions\":[\""+clientId+"\"]\n" + 
					"}";
	    	
			OEBCUtil.execMethodPUT(ELOQUA_URL+url, ELOQUA_USERNAME, ELOQUA_PASSWORD, deleteClientJson);
			
		    return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return false;
	}
	
	private  boolean updateParametersContact(Integer contactId, JSONObject contactJSON) throws ParseException{
		
		String url="/api/REST/1.0/data/contact/"+contactId;
	    
	    try {
	    	
	    	OEBCUtil.execMethodPUT(ELOQUA_URL+url, ELOQUA_USERNAME, ELOQUA_PASSWORD, contactJSON.toJSONString());
			
		    return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return false;
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public  void process() throws ParseException, IOException {
		
		OEBCUtil.LOG_DEBUG=this.LOG_DEBUG;
		
		String dataTime=OEBCUtil.getDateTimeYYYYMMDDHHMMSS(new Date());
		
		List<JSONObject> peopleList = new ArrayList<JSONObject>();
		List<JSONObject> peopleDetailList = new ArrayList<JSONObject>();
		String  json=null;
		String searchTypeId = null;
		LOG_CSV_NAME_FILE+=this.ELOQUA_SEARCH_TYPE+"_"+this.ELOQUA_SEARCH_NAME+"_"+dataTime+".csv";
		
		if (this.ELOQUA_SEARCH_TYPE.equalsIgnoreCase("contactlist")) {
			json = callEloquaGET("/api/REST/1.0/assets/contact/lists?search="+this.ELOQUA_SEARCH_NAME);
			List<Object> jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
			if (jsonList!=null && jsonList.size()==1) {
			
				searchTypeId = (String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id");
				OEBCUtil.logInfo(this.ELOQUA_SEARCH_NAME + " id= "+searchTypeId);
				json = callEloquaGET("/api/REST/1.0/data/contacts/list/"+searchTypeId+"?depth=complete");
				peopleList = OEBCUtil.searchJSONArray4JSONFromString(json,"elements");
				OEBCUtil.logDebug("People List from contactlist "+this.ELOQUA_SEARCH_NAME+" : "+peopleList);
			}else if (jsonList!=null && jsonList.size()>1) {
				OEBCUtil.logError("ERROR: Existen mas de un contactlist con el nombre "+this.ELOQUA_SEARCH_NAME+".");
			}else {
				OEBCUtil.logError("ERROR: No se ha encontrado el contactlist "+ this.ELOQUA_SEARCH_NAME);
			}
		}else if (this.ELOQUA_SEARCH_TYPE.equalsIgnoreCase("segment")) {
			json = callEloquaGET("/api/REST/1.0/assets/contact/segments?search="+this.ELOQUA_SEARCH_NAME);
			List<Object> jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
			if (jsonList!=null && jsonList.size()==1) {
			
				searchTypeId = (String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id");
				OEBCUtil.logInfo(this.ELOQUA_SEARCH_NAME + " id= "+searchTypeId);
				
				json = callEloquaGET("/api/REST/1.0/data/contacts/segment/"+searchTypeId+"?depth=complete");
				peopleList = OEBCUtil.searchJSONArray4JSONFromString(json,"elements");
				OEBCUtil.logDebug("People List from segment "+searchTypeId+" : "+peopleList);
			}else if (jsonList!=null && jsonList.size()>1) {
				OEBCUtil.logError("ERROR: Existen mas de un segmento "+this.ELOQUA_SEARCH_NAME);
			}else {
				OEBCUtil.logError("ERROR: No se ha encontrado el segmento "+this.ELOQUA_SEARCH_NAME);
			}
		}else if (this.ELOQUA_SEARCH_TYPE.equalsIgnoreCase("campaign")) {
			json = callEloquaGET("/api/REST/2.0/assets/campaigns?search=?search="+this.ELOQUA_SEARCH_NAME+"?depth=complete");
			List<Object> jsonList =  OEBCUtil.searchArray4JSONFromString(json, "elements");
			if (jsonList!=null && jsonList.size()==1) {
			
				searchTypeId = (String) OEBCUtil.searchObject4JSONFromString(jsonList.get(0).toString(), "id");

				OEBCUtil.logInfo(this.ELOQUA_SEARCH_NAME + " id= "+searchTypeId);
				json = callEloquaGET("/api/REST/1.0/data/contacts/campaign/"+searchTypeId);
				peopleList = OEBCUtil.searchJSONArray4JSONFromString(json, "elements");
				OEBCUtil.logDebug("People List from campaign "+this.ELOQUA_SEARCH_NAME+" : "+peopleList);
				
			}else if (jsonList!=null && jsonList.size()>1) {
				OEBCUtil.logError("ERROR: Existen mas una campana "+this.ELOQUA_SEARCH_NAME);
			}else {
				OEBCUtil.logError("ERROR: No se ha encontrado la campana "+this.ELOQUA_SEARCH_NAME);
			}
		}
				
		//Buscamos el detalle de cada persona
		for (JSONObject peopleJson : peopleList) {
			json = callEloquaGET("/api/REST/1.0/data/contact/"+peopleJson.get("id")+"?depth=complete");
			peopleDetailList.add(OEBCUtil.searchJSONObject4JSONFromString(json));
		}
		
		OEBCUtil.logDebug("People Detail List : "+peopleDetailList);
		
		//Enviamos SMSs
		FileWriter fichero = null;
        PrintWriter pw = null;
        try {
        	
        	fichero = new FileWriter(LOG_PATH+"/"+LOG_CSV_NAME_FILE);
	        pw = new PrintWriter(fichero);
            pw.println("MSISDN;SMS_ID;SMS_STATUS;SMS_MESSAGE_ID;ELOQUA_CONTACT_ID;ELOQUA_CONTACT_NAME;SMS_MESSAGE");

			for (JSONObject peopleDetailJson : peopleDetailList) {
				String msisdn = OEBCUtil.formatMSISDN_CL(peopleDetailJson.get("mobilePhone").toString());
				String peopleName = peopleDetailJson.get("firstName").toString();
				String peopleID = peopleDetailJson.get("id").toString();
				if (msisdn!=null) {
					
					String messageSMS = this.INFOBIP_SMS_FORMAT.replace("{name}", peopleName);
					String smsJSON = "{\"to\":\""+msisdn+"\", \"text\":\""+messageSMS+"\"}";
					OEBCUtil.logDebug("SMS to MSISDN ORIGINAL "+peopleDetailJson.get("mobilePhone")+ " MSISDN "+ msisdn + " MESSAGE ["+messageSMS+"]");
		
					String smsResponse = OEBCUtil.execMethodPOST(this.INFOBIP_URL, this.INFOBIP_USERNAME, this.INFOBIP_PASSWORD, smsJSON);
					
					OEBCUtil.logDebug("SMS to MSISDN "+ msisdn + " MESSAGE ["+messageSMS+"] + RESPONSE "+ smsResponse);
					JSONObject smsResponseJSON = (JSONObject)((JSONArray)OEBCUtil.searchJSONObject4JSONFromString(smsResponse).get("messages")).get(0);
					JSONObject smsStatusJSON = (JSONObject)smsResponseJSON.get("status");
					
					OEBCUtil.logDebug("SMSRESPONSE" + smsResponseJSON.toJSONString());
					OEBCUtil.logDebug("SMSSTATUS" + smsStatusJSON.toJSONString());

	                pw.println(msisdn + ";"+ smsStatusJSON.get("id")+";"+smsStatusJSON.get("name")+";"+smsResponseJSON.get("messageId")+";"+peopleID+";"+peopleName+";"+messageSMS);

	                //Se actualiza la informacion del contacto
	                JSONArray fields = (JSONArray)peopleDetailJson.get("fieldValues");
	                OEBCUtil.logDebug("JSONARRAY Fields " + fields);

                	//Buscamos los IDs
	                for (int i = 0; i < fields.size(); i++) {
	                	JSONObject field = (JSONObject) fields.get(i);
	                	OEBCUtil.logInfo("JSONObject Field " + field);

	                	if (field.get("id").toString().equals(this.ELOQUA_PARAM_SMS_TEXT_ID.toString())) {
	                		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_TEXT_ID value " + messageSMS);
	                		field.put("value", messageSMS);
	                		fields.set(i,field);
	                	}else if (field.get("id").toString().equals(this.ELOQUA_PARAM_SMS_MESSAGEID_ID.toString())) {
	                		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_MESSAGEID_ID value " + smsResponseJSON.get("messageId"));
	                		field.put("value", smsResponseJSON.get("messageId").toString());
	                		fields.set(i,field);
	                	}else if (field.get("id").toString().equals(this.ELOQUA_PARAM_TIMESTAMP_ID.toString())) {
	                		Date date = new Date();
	                		OEBCUtil.logInfo("ELOQUA_PARAM_TIMESTAMP_ID value " + date);
	                		field.put("value", OEBCUtil.getDateTimeMMDDYYYY(date));
	                		fields.set(i,field);
	                	}else if (field.get("id").toString().equals(this.ELOQUA_PARAM_SMS_STATUS_ID.toString())) {
	                		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_STATUS_ID value " + smsStatusJSON.get("name"));
	                		field.put("value", smsStatusJSON.get("name"));
	                		fields.set(i,field);
	                	}
	                }
	                
	                peopleDetailJson.put("fieldValues", fields);
                	OEBCUtil.logInfo("JSONObject People " + peopleDetailJson);
	                updateParametersContact(Integer.parseInt(peopleID),peopleDetailJson);
                
	                //Si es una contactlist, entonces eliminamos de la lista
	                
                	if (this.ELOQUA_SENDSMS_DELETECONTACT) {
                		OEBCUtil.logInfo("Delete client for contactlist");
                		if (this.ELOQUA_SEARCH_TYPE.equalsIgnoreCase("contactlist")) {
		                	deleteClientFromIdToContactList(Integer.parseInt(searchTypeId),this.ELOQUA_SEARCH_NAME,Integer.parseInt(peopleID));
		                }
                	}else {
                		OEBCUtil.logInfo("Not delete client for contactlist");

                	}
				}else {
					OEBCUtil.logError("SMS to MSISDN ORIGINAL "+peopleDetailJson.get("mobilePhone")+ " MSISDN INVALIDO!!");
	                pw.println(peopleDetailJson.get("mobilePhone").toString() + ";"+";ERROR_MSISDN;"+";"+peopleID+";"+peopleName+";;");
				}
			}
		
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
        	   if (null != fichero)
        		   fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		
	}
	
	public  void reviewProcess() throws ParseException, IOException {
		
		OEBCUtil.LOG_DEBUG=this.LOG_DEBUG;

		//Buscamos los archivos
		File f = new File(LOG_PATH);
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith(LOG_CSV_NAME_FILE) && name.endsWith("csv");
		    }
		});
		
		if (matchingFiles.length==0) {
			OEBCUtil.logWarn("This folder don't has logs ELOQUA.");
		}
		
		for (File log : matchingFiles) {
			OEBCUtil.logInfo("Read log ELOQUA "+log.getAbsolutePath());
			String columns[] = {"MSISDN","SMS_ID","SMS_STATUS","SMS_MESSAGE_ID","ELOQUA_CONTACT_ID","ELOQUA_CONTACT_NAME","SMS_MESSAGE"};
			List<HashMap<String,Object>> contentLog = OEBCUtil.readFile(log.getAbsolutePath(), columns);
			OEBCUtil.logInfo("Read log ELOQUA "+log.getAbsolutePath());
			OEBCUtil.logInfo("Log ELOQUA "+contentLog);
		
			//Enviamos SMSs
			FileWriter fichero = null;
	        PrintWriter pw = null;
	        
	        try {
	        	
	        	fichero = new FileWriter(LOG_PATH+"/REVIEW_"+log.getName());
		        pw = new PrintWriter(fichero);
	            pw.println("MSISDN;SMS_ID;SMS_STATUS;SMS_MESSAGE_ID;ELOQUA_CONTACT_ID;ELOQUA_CONTACT_NAME;SMS_MESSAGE");
	
	            for (HashMap<String,Object> smsLog : contentLog) {
	            	if (smsLog.get("SMS_STATUS").toString().equals("PENDING_ENROUTE")) {
	            		String messageId = smsLog.get("SMS_MESSAGE_ID").toString();
	            		String smsId = smsLog.get("SMS_ID").toString();
	            		String smsResponse = OEBCUtil.execMethodGET(this.INFOBIP_URL_REVIEW+messageId, this.INFOBIP_USERNAME, this.INFOBIP_PASSWORD);
	            		OEBCUtil.logInfo("Log INFOBIT CONSULTA ESTADO MENSAJE "+smsResponse);
	            		
	            		JSONArray resultJSON = ((JSONArray)OEBCUtil.searchJSONObject4JSONFromString(smsResponse).get("results"));
	            		
	            		if (resultJSON.size()>0) {
		            		JSONObject statusSmsResponseJSON = (JSONObject) resultJSON.get(0);
		            		String status = ((JSONObject)statusSmsResponseJSON.get("status")).get("groupName").toString();
							OEBCUtil.logInfo("Log INFOBIT status  "+status);
							
			                pw.println(smsLog.get("MSISDN") + ";"+ smsLog.get("SMS_ID")+";"+status+";"+smsLog.get("SMS_MESSAGE_ID")+";"+smsLog.get("ELOQUA_CONTACT_ID")+";"+smsLog.get("ELOQUA_CONTACT_NAME")+";"+smsLog.get("SMS_MESSAGE"));
	            		
			                String json = callEloquaGET("/api/REST/1.0/data/contact/"+smsLog.get("ELOQUA_CONTACT_ID")+"?depth=complete");
			    			JSONObject peopleJSON = OEBCUtil.searchJSONObject4JSONFromString(json);
			    		
			                //Actualizacion usuario en eloqua
			    			JSONArray fields = (JSONArray) peopleJSON.get("fieldValues");
			                for (int i = 0; i < fields.size(); i++) {
			                	JSONObject field = (JSONObject) fields.get(i);
			                	OEBCUtil.logInfo("JSONObject Field " + field);

			                	if (field.get("id").toString().equals(this.ELOQUA_PARAM_SMS_STATUS_ID.toString())) {
			                		OEBCUtil.logInfo("ELOQUA_PARAM_SMS_STATUS_ID value " + status);
			                		field.put("value", status);
			                		fields.set(i,field);
			                	}
			                }
			                updateParametersContact(Integer.parseInt(smsLog.get("ELOQUA_CONTACT_ID").toString()),peopleJSON);
			                
	            		}
	            	}
	            }
	            
	            boolean rename = log.renameTo(new File(LOG_PATH+"/R_"+log.getName()));
		        if (!rename) {
		        	OEBCUtil.logError("Error for rename "+ log.getName() + " to "+"/R_"+log.getName());
		        }else {
		        	OEBCUtil.logInfo("Rename "+ log.getName() + " to "+"/R_"+log.getName());
		        }
			
	            
		        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	           try {
	        	   if (null != fichero)
	        		   fichero.close();
	           } catch (Exception e2) {
	              e2.printStackTrace();
	           }
	        }
		}
		
	}
	
	
	
}
