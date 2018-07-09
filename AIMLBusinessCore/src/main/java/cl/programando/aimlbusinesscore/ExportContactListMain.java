package cl.programando.aimlbusinesscore;

import java.io.IOException;
import java.util.Date;

import org.json.simple.parser.ParseException;

/* Properties
#Parametros Eloqua
ELOQUA_USERNAME=UniversidadCentraldeChile\\Ceryx.Fernandez
ELOQUA_PASSWORD=Seidor.Infobip1
ELOQUA_URL=https://secure.p04.eloqua.com
ELOQUA_SENDSMS_DELETECONTACT=false
ELOQUA_SEARCH_TYPE=contactlist
ELOQUA_SEARCH_NAME=zag_test_shared_list_03

#Parametros ELoquea
ELOQUA_PARAM_SMS_TEXT=SMS_MSIDN_TEXT
ELOQUA_PARAM_TIMESTAMP=SMS_TIMESTAMP_DATE
ELOQUA_PARAM_SMS_MESSAGEID=SMS_MSIDN_ID

#Parametros asociados a SMS - InfoBIP
INFOBIP_URL=https://api.infobip.com/sms/1/text/single
INFOBIP_USERNAME=Seid0rUma!
INFOBIP_PASSWORD=UCentral2018
INFOBIP_SMS_FORMAT=Hola {name} este es un SMS personalizado por Eloqua, prueba lista nueva....Excelente2!

#Parametros del aplicativo
LOG_PATH=/tmp
LOG_CSV_NAME_FILE=ELOQUA_SEND_SMS_

*/
public class ExportContactListMain {
	
	public static void main(String[] args) throws ParseException, IOException {
		
		OEBCUtil.logInfo("OracleEloquaBusinessCore -> Start - "+(new Date()));
		String typeProcess=null;
		String path=null;
		
		if (args==null||args.length==0) {
			OEBCUtil.logInfo("Without parameters.....");
			
			typeProcess = "process";
			//typeProcess = "reviewProcess";
			
			path = "/tmp/OracleEloquaBusinessCore_test.properties";
		}else {
			typeProcess = args[0];
			path = args[1];
		}
		
		OEBCUtil.logInfo("Path configuration "+path);

		OracleEloquaController controller = new OracleEloquaController(path);
		
		if (typeProcess.equals("process")) {
			OEBCUtil.logInfo("Starting process....");
			controller.process();
		}else if (typeProcess.equals("reviewProcess")) {
			OEBCUtil.logInfo("Starting review process....");
			controller.reviewProcess();
		}
		
		OEBCUtil.logInfo("OracleEloquaBusinessCore -> End - "+(new Date()));

	}
	
}
