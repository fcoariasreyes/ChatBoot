package cl.programando.aimlbusinesscore;

public class ValidacionesType {

	public  static boolean isNumber(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
}
