package es.mule.areas.tp.sftp;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.mule.areas.tp.sftp.MySftp;
import es.mule.areas.tp.sftp.ThirdPartyInfo;

/**
 * This class explores all the folders in the "paths" property and returns a collection of 
 * FileDescriptor objects that contains all the relevant information for the process about
 * the files into the folders.
 * @author jf.jimenez
 *
 */
public class SftpDirectoryExplorer {
	
	private static final Log LOGGER = LogFactory.getLog(SftpDirectoryExplorer.class);

	// Config SFTP
	protected String host;
	protected int port;
	protected String user;
	protected String password;
	
	public SftpDirectoryExplorer(String host, int port, String user, String password) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	 
	public static Object call(String host, int port, String user, String password, List<ThirdPartyInfo> thirdPartiesInfo) throws Exception {
		return new SftpDirectoryExplorer(host, port, user, password).getFileDescriptors(thirdPartiesInfo);
	}
	public Object getFileDescriptors(List<ThirdPartyInfo> thirdPartiesInfo) throws Exception {
		List<FileDescriptor> files = new ArrayList<FileDescriptor>();
		try {

			MySftp sftp = new MySftp(host, port, user, password);
			LOGGER.debug("Connect to SFTP!...");
			
			if (sftp.connect()) {
				for (ThirdPartyInfo info : thirdPartiesInfo) {
					
					LOGGER.debug("cd '" + info.getPath() +"': " + sftp.cd(info.getPath()));
					
					List<String> fileNames = getFilesNames(sftp, info.getPath(), info.getRegExp());
					for (String name : fileNames ) {
						files.add(getFileDescriptor(info, name));
					}
					
				} 
				sftp.disconnect();
			}
			
		} catch (Exception ex) {
			LOGGER.error("Exception in SftpDirectoryExplorer.onCall: " + ex.getMessage(), ex);
			files.clear();
		}

		return files;
	}
	/**
	 * Returns a list of file names that are located into the folder defined 
	 * by the parameter "path" and that comply a regular expression defined 
	 * by the parameter "regex".
	 * @param sftp
	 * @param path
	 * @param regex
	 * @return
	 */
	private  List<String> getFilesNames(MySftp sftp, String path, String regex){
		
		Pattern p = Pattern.compile(regex);
		List<String> result = new ArrayList<String>();
		 
		List<String> fileNames = sftp.getFileNames(null);
		for (String name : fileNames) {
			Matcher m = p.matcher(name);
			if (m != null && m.find()) {
				result.add(name);
			}
		}
		return result;
	}
	
	private FileDescriptor getFileDescriptor(ThirdPartyInfo info, String fileName) {
		return new FileDescriptor(info, fileName);
	}
	
	
}