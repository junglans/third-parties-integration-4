package es.mule.areas.tp.sftp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Custom SFTP 
 * @version 1.0
 */

public class MySftp {
	
	private static final Log LOGGER = LogFactory.getLog(MySftp.class);
	
	// Config
	String 	host;
	int 	port;
	String 	user;
	String 	password;

	// Vars
	private JSch jsch;
	private Session session;
	private ChannelSftp sftp;

	// ---------------------------------------------------------------------
	
	public MySftp(String host, int port, String user, String password) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public boolean connect() {
		return this.connect(host, port, user, password);
	}

	/**
	 * Connect with SFTP
	 * 
	 * @return boolean
	 * @exception IOException
	 */
	public boolean connect(String host, int port, String user, String password) {
		boolean conect = true;

		try {
			// Connect to an SFTP server
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			//Accept the key without confirmation
			Properties prop = new Properties();
			prop.put("StrictHostKeyChecking", "no");
			session.setConfig(prop);
			session.connect();
			// Open channel and connect
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
		} catch (JSchException e) {
			LOGGER.error("JSchException =" + e.getMessage(), e);
			conect = false;
		}
		return conect;
	}

	// -------------------------------------------------------------------
	/**
	 * Change directory
	 * 
	 * @param directory
	 * @return boolean
	 * @exception IOException
	 */
	public boolean cd(String directory) {
		try {
			sftp.cd(directory);
			return true;
		} catch (SftpException e) {
			LOGGER.error("SftpException command cd: " + e.getMessage(), e);
		}
		return false;
	}

	// -------------------------------------------------------------------
	/**
	 * Create File
	 * 
	 * @param pathFileSftp
	 * @param pathFile
	 * @return boolean
	 * @exception IOException
	 */
	public boolean createFile(String pathFile, String pathFileSftp) {
		try {
			sftp.put(pathFile, pathFileSftp);
			return true;
		} catch (SftpException e) {
			LOGGER.error("SftpException command createFile: " + e.getMessage(), e);
		}
		return false;
	}

	// -------------------------------------------------------------------
	/**
	 * Remove file
	 * 
	 * @param pathFile
	 * @return boolean
	 * @exception IOException
	 */
	public boolean removeFile(String pathFile) {
		try {
			sftp.rm(pathFile);
			return true;
		} catch (SftpException e) {
			LOGGER.error("SftpException command removeFile: " + e.getMessage(), e);
		}
		return false;
	}

	// -----------------------------------------------------------------
	/**
	 * Disconnect SFTP
	 * 
	 * @return boolean
	 * @exception IOException
	 */
	public boolean disconnect() {
		sftp.exit();
		sftp.disconnect();
		session.disconnect();
		return true;
	}

	// -----------------------------------------------------------------
	/**
	 * Current directory
	 * 
	 * @return String
	 * @exception IOException
	 */
	public String pwd() {
		String path = null;
		try {
			path = sftp.pwd();
		} catch (SftpException e) {
			LOGGER.error("SftpException command pwd: " + e.getMessage(), e);
		}
		return path;
	}

	// -----------------------------------------------------------------
	/**
	 * Get file
	 * @param pathFileSFTP
	 * @param localPath
	 * @return boolean
	 * @exception IOException
	 */
	public boolean getFile(String pathFileSFTP, String localPath) {
		boolean retorno = true;
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(localPath))) {
			sftp.get(pathFileSFTP, os);
		} catch (IOException e) {
			LOGGER.error("IOException command getFile: " + e.getMessage(), e);
			retorno = false;
		} catch (SftpException e) {
			LOGGER.error("SftpException command getFile: " + e.getMessage(), e);
			retorno = false;
		}
		return retorno;

	}
	// -----------------------------------------------------------------
	
	/**
	 * List of files in the current directory, if the filter is empty shows all
	 * @param filter
	 * @return
	 */
	public List<String> getFileNames(String filter){
		List<String> names = new ArrayList<>();
		try {
			List<LsEntry> list = sftp.ls(StringUtils.isEmpty(filter)?"*":filter);
			if(list!=null){
				for(LsEntry entry : list) {
					names.add(entry.getFilename());
				}
			}
		} catch (SftpException e) {
			LOGGER.error("SftpException command getFileNames: " + e.getMessage(), e);
		}

		return names;
	}
	
	public String getFileLastModifiedDate(String fileName) {
		if (!StringUtils.isEmpty(fileName)) {
			try {
				return sftp.lstat(fileName).getMtimeString();
			} catch (SftpException e) {
				LOGGER.error("SftpException command getFileLastModifiedDate: " + e.getMessage(), e);
				return null;
			}
		} else {
			return null;
		}
	}
	
}