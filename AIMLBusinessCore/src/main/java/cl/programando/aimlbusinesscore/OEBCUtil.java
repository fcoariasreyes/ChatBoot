package cl.programando.aimlbusinesscore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class OEBCUtil {
	
	public static boolean LOG_DEBUG=true;
	private static BufferedReader br;
	
	public static String execMethodGET(String url, String username, String password) throws IOException {
		 
	    URL obj = new URL(url);
	    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	 
	    conn.setDoOutput(true);
	 
	    conn.setRequestMethod("GET");
	 
	    if (username!=null && password!=null) {
		    String userpass = username + ":" + password;
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		    conn.setRequestProperty ("Authorization", basicAuth);
	    }

	    String response =  OEBCUtil.imputStreamToString(new InputStreamReader(conn.getInputStream())); 	
	    
    	OEBCUtil.logInfo("GET "+url);
    	OEBCUtil.logInfo("StatusCode "+conn.getResponseCode()+ " Message "+conn.getResponseMessage());
    	OEBCUtil.logInfo("RESPONSE "+response);

    	return response;

	}
	
	public static String execMethodPOST(String urlStr, String username, String password, String data) throws IOException {
		 
		try {
			 		 
		    URL obj = new URL(urlStr);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		 
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		 
		    conn.setRequestMethod("POST");
		 
		    if (username!=null && password!=null) {
			    String userpass = username + ":" + password;
			    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			    conn.setRequestProperty ("Authorization", basicAuth);
		    }
		    
	    	OEBCUtil.logInfo("POST "+urlStr);

		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    out.write(data);
		    out.close();
		 		    
		    BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

	    	OEBCUtil.logInfo("StatusCode "+conn.getResponseCode()+ " Message "+conn.getResponseMessage());
	    	OEBCUtil.logInfo("RESPONSE "+sb.toString());

			return sb.toString();
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	 
		  
		return null;
	}
	
	public static String execMethodPUT(String urlStr, String username, String password, String data) throws IOException {
		 
		try {
			 		 
		    URL obj = new URL(urlStr);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		 
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		 
		    conn.setRequestMethod("PUT");
		 
		    if (username!=null && password!=null) {
			    String userpass = username + ":" + password;
			    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			    conn.setRequestProperty ("Authorization", basicAuth);
		    }
		    
	    	OEBCUtil.logInfo("PUT "+urlStr);

		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    out.write(data);
		    out.close();
	    	
	    	OEBCUtil.logInfo("PUT "+urlStr);
	    	OEBCUtil.logInfo("Message "+data);
	    	OEBCUtil.logInfo("StatusCode "+conn.getResponseCode()+ " Message "+conn.getResponseMessage());
	    	
		    
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	 
		  
		return null;
	}
	
	
	public static String execMethodDELETE(String urlStr, String username, String password, String data) throws IOException {
		 
		try {
			 		 
		    URL obj = new URL(urlStr);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		 
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		 
		    conn.setRequestMethod("DELETE");
		    if (username!=null && password!=null) {
			    String userpass = username + ":" + password;
			    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
			    conn.setRequestProperty ("Authorization", basicAuth);
		    }

		    OEBCUtil.logInfo("DELETE "+urlStr);
	    	OEBCUtil.logInfo("StatusCode "+conn.getResponseCode()+ " Message "+conn.getResponseMessage());

		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    out.write(data);
		    out.close();
		 		    
		    			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	 
		  
		return null;
	}
	public static String execMethodGET(String url) throws IOException {
		 
	    return execMethodGET(url, null, null);    
	}
	
	public static String execMethodPOST(String url,String data) throws IOException {
		 
	    return execMethodGET(url, null, null);    
	}
	
	public static String formatMSISDN_CL(String str) {
		String msisdn = str.trim();
		msisdn=msisdn.replace(" ", "");
		
		OEBCUtil.logDebug("MSISDN RECIBIDO"+str + " LIMPIO " + msisdn);
		
		if (msisdn.length()==8) {
			msisdn="+569"+msisdn;
		}else if (msisdn.length()==9){
			msisdn="+56"+msisdn;
		}
		
		OEBCUtil.logDebug("MSISDN RECIBIDO"+str + " FORMATEADO "+ msisdn + " LARGO "+msisdn.length());
		
		try {
			Integer val=Integer.parseInt(msisdn.substring(4, msisdn.length()));
			if (val > 10000000 && val<=99999999) {
				OEBCUtil.logDebug("MSISDN "+msisdn + " VALIDADO " + val);
				return msisdn;
			}else {
				OEBCUtil.logDebug("MSISDN "+msisdn + " INVALIDO " + val);
			}
		}catch(NumberFormatException e) {
			OEBCUtil.logDebug("MSISDN "+msisdn + " NO ES NUMERICO");
		}
		return null;
	}
	
	public static String imputStreamToString(InputStreamReader is) throws IOException {
		final int bufferSize = 1024;
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		Reader in = is;
		for (; ; ) {
		    int rsz = in.read(buffer, 0, buffer.length);
		    if (rsz < 0)
		        break;
		    out.append(buffer, 0, rsz);
		}
		return out.toString();
	}
	
	public static void logDebug(String str) {
		if (LOG_DEBUG)
			System.out.println("[DEBUG] ["+new Date()+"] :: " + str);
	}
	
	public static void logError(String str) {
		System.out.println("[ERROR] ["+new Date()+"] :: " + str);
	}
	
	public static void logInfo(String str) {
		System.out.println("[INFO] ["+new Date()+"] :: " + str);
	}

	public static void logWarn(String str) {
		System.out.println("[WARN] ["+new Date()+"] :: " + str);
	}
	
	public static String getDateTimeYYYYMMDDHHMMSS(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return df.format(date);
	}
	
	public static String getDateTimeDDMMYYYY(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(date);
	}
	
	public static String getDateTimeMMDDYYYY(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(date);
	}
	
	public static List<HashMap<String,Object>> readFile(String pathFile, String[] nombreColumnas) throws FileNotFoundException{
		String csvFile = pathFile;
		String line = "";
		String cvsSplitBy = ";";
		List<HashMap<String,Object>> listaArhivo = new ArrayList<HashMap<String,Object>> ();

		try {
				
		    br = new BufferedReader(new FileReader(csvFile)); 
			OEBCUtil.logDebug("Estoy leyendo archivo desde: " + pathFile);
			while ((line = br.readLine()) != null) {
				OEBCUtil.logDebug("Comienzo a iterar la lectura de achivo: " + pathFile);
				// use comma como separador
				String[] values = line.split(cvsSplitBy);

				//se debe obtener registro para dejarlo en lista de hashMap
				HashMap<String,Object> hm = new HashMap<String,Object>();

				for (int j=0; j< nombreColumnas.length && j<values.length; j++){

					@SuppressWarnings("unused")
					Object valor = values[j];
					
					if (ValidacionesType.isNumber(values[j])){
						values[j] = values[j].replaceAll("\"", "");
						valor = values[j];
						hm.put(nombreColumnas[j], Integer.valueOf(values[j]));
					}else{
						values[j] = values[j].replaceAll("\"", "");
						valor = values[j];
						hm.put(nombreColumnas[j], String.valueOf(values[j]));
					}
						
				}
				listaArhivo.add(hm);				
			}
		} catch (IOException e) {
			e.printStackTrace();
			OEBCUtil.logDebug("Tengo problemas para leer" +e.getMessage().toString() +" archivo "+ pathFile);
			return null;
		}
		OEBCUtil.logDebug("Salgo de lectura");
		return listaArhivo;
	}
	
	public static List<Object> searchArrayFromJSON(String json) throws ParseException{
		List<Object> list = new ArrayList<Object>();
		
		OEBCUtil.logDebug("Return JSON: " + json);
		JSONParser jsonParser = new JSONParser();
		JSONArray array = (JSONArray) jsonParser.parse(json);
		
		for(int i=0; i<array.size(); i++){
			OEBCUtil.logDebug("JSON "+i+" : " + array.get(i));
			list.add(array.get(i));
		}

		return list;
	}
	
	public static List<Object> searchArray4JSONFromString(String json, String tagJSON) throws ParseException{
		List<Object> list = new ArrayList<Object>();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
		JSONArray array= (JSONArray) jsonObject.get(tagJSON);
		
		for(int i=0; i<array.size(); i++){
			OEBCUtil.logDebug("JSON "+i+" : " + array.get(i));
			list.add(array.get(i));
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<JSONObject> searchJSONArray4JSONFromString(String json, String tagJSON) throws ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
		
		return (JSONArray) jsonObject.get(tagJSON);
	}
	
/*	public static JSONObject searchJSONObject4JSONFromString(String url) throws ParseException{
		
		String  json = OracleEloquaController.callEloquaGET(url);
		JSONParser jsonParser = new JSONParser();
		
		return (JSONObject) jsonParser.parse(json);
	}*/
	
	public static Object searchObject4JSONFromString(String str, String tagJSON) throws ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
		return jsonObject.get(tagJSON);
	}
	
	public static JSONObject searchJSONObject4JSONFromString(String str) throws ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
		return jsonObject;
	}

	public static JSONObject searchJSONObject4JSONFromString(String str, String tagJSON) throws ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(str);
		return (JSONObject)jsonObject.get(tagJSON);
	}
	
}
