package es.mule.areas.tp.utils;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
/**
 * This class is used to jsonize the strings coming from exceptions.
 * @author jf.jimenez
 *
 */
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
