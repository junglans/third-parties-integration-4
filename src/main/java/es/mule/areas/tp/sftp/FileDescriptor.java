package es.mule.areas.tp.sftp;

public class FileDescriptor {

	/**
	 * Third party identifier
	 */
	private ThirdPartyInfo info;
	/**
	 * The name of the file.
	 */
	private String fileName;
	
	 
	public FileDescriptor(ThirdPartyInfo info, String fileName) {
		super();
		this.info = info;
		this.fileName = fileName;
		 
	}
 
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ThirdPartyInfo getInfo() {
		return info;
	}

	public void setInfo(ThirdPartyInfo info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "FileDescriptor [info=" + info + ", fileName=" + fileName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileDescriptor other = (FileDescriptor) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}
	
}
