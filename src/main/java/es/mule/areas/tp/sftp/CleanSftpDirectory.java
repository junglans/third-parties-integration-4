package es.mule.areas.tp.sftp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to remove older files from every third party's processed folder.
 */
public class CleanSftpDirectory {
	
	private static final Log LOGGER = LogFactory.getLog(CleanSftpDirectory.class);

	// Config SFTP
	String host;
	int port;
	String user;
	String password;
	// Clean Config
	int maxDays;
	String dateFormat;
	String regexPattern;
	
	int filesDeleted = 0;
	int filesNotDeleted = 0;
	
	public CleanSftpDirectory(String host, int port, String user, String password, int maxDays, String dateFormat,
			String regexPattern) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.maxDays = maxDays;
		this.dateFormat = dateFormat;
		this.regexPattern = regexPattern;
	}

	public static Map<String, Integer> call(String host, int port, String user, String password, int maxDays, String dateFormat,
			String regexPattern, List<ThirdPartyInfo> thirdPartiesInfo) throws Exception {
		return new CleanSftpDirectory(host, port, user, password, maxDays, dateFormat, regexPattern).cleanFolder(thirdPartiesInfo);
	}
	 
	public Map<String, Integer> cleanFolder(List<ThirdPartyInfo> thirdPartiesInfo) throws Exception {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {

			MySftp sftp = new MySftp(host, port, user, password);
			LOGGER.debug("Connect to SFTP!...");
			if (sftp.connect()) {
				for (ThirdPartyInfo info : thirdPartiesInfo) {
					LOGGER.debug("cd '" + info.getProcessPath() +"': " + sftp.cd(info.getProcessPath()));
					List<String> fileNames = sftp.getFileNames(null);
					LOGGER.debug("files: " + (fileNames!=null?fileNames.size():"null"));
					if(fileNames!=null){
						List<String> nameOutRange = filesDateOutRange(sftp, fileNames, regexPattern, dateFormat, maxDays);
						if(nameOutRange!=null && nameOutRange.size()>0){
							for (String name : nameOutRange) {
								LOGGER.info("Deleting file... " + info.getProcessPath() + "/" + name);
								if(sftp.removeFile(name)){
									filesDeleted++;
									LOGGER.info("The file: " + info.getProcessPath() + "/" + name + " has been deleted.");
								} else {
									filesNotDeleted++;
									LOGGER.info("The file: " + info.getProcessPath() + "/" + name + " has NOT been deleted.");
								}
								
							}
						}
					}
				}
				
				sftp.disconnect();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception in CleanSftpDirectory.onCall: "+ex.getMessage(), ex);
			throw ex;
		}
		result.put("successfulRecords", filesDeleted);
		result.put("failedRecords", filesNotDeleted);
 		return result;
	}

	private List<String> filesDateOutRange(MySftp sftp, List<String> fileName, String regex, String dateFormat, int maxDays){
		List<String> fileNameOutRange = new ArrayList<String>();
		
		SimpleDateFormat sd = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, (-1 * maxDays));

		Pattern pattern = Pattern.compile(regex);
		
		try {
			for (String name : fileName) {
				Matcher m = pattern.matcher(name);
				if (m!=null && m.find()) {
					String lastModifiedDate = sftp.getFileLastModifiedDate(name);
					DateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
					Date date = dateFormatter.parse(lastModifiedDate);
					if(date.before(cal.getTime())){
						LOGGER.debug("Clean file: " + name + " is before Date: " + cal.getTime() +
								" (" + sd.format(cal.getTime()) + ")");
						fileNameOutRange.add(name);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in CleanSftpDirectory.filesDateOutRange: " + e.getMessage() +
					" param -> fileName: " + fileName + " regex: " + regex + 
					" dateFormat: " + dateFormat + " maxDays: " + maxDays, e);
		}
		return fileNameOutRange;
	}

}