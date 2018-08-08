package es.mule.areas.tp.utils;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
public class JsonUtils {

	@SuppressWarnings("deprecation")
	public static String quoteJSONString(String s) {
		if (s == null) {
     		return "";
     	} else {
     		return String.valueOf(JsonStringEncoder.getInstance().quoteAsString(s));
    	}
	}
}
