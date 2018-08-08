package es.mule.areas.tp.sftp;

import java.util.Map;
/**
 * This class encapsulates the common data of every Third Party report configuration.
 * @author jf.jimenez
 *
 */
public class ThirdPartyInfo {
	/**
	 * The report identifier. For example: TP1, TP2, etc.
	 */
	private String id;
	/**
	 * The name of the report. For example: 'Sales Adidas', 'Stock Adidas'.
	 */
	private String name;
	/**
	 * The origin path where the reports are initially located.
	 */
	private String path;
	/**
	 * The path where the reports are kept in case of error.
	 */
	private String errorPath;
	/**
	 * The path where reports are kept when there is no error.
	 */
	private String processPath;
	/**
	 * The type of flow the performs the file transfer.
	 */
	private String flowType;
	/**
	 * Regular expression file names must comply.
	 */
	private String regExp;
	/**
	 * Map containing all the final endpoint's configuration properties.
	 */
	private Map<String, String> destination;
	
	public ThirdPartyInfo() {}
 
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getErrorPath() {
		return errorPath;
	}
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}
	public String getProcessPath() {
		return processPath;
	}
	public void setProcessPath(String processPath) {
		this.processPath = processPath;
	}
	
	public Map<String, String> getDestination() {
		return destination;
	}
	public void setDestination(Map<String, String> destination) {
		this.destination = destination;
	}
	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((errorPath == null) ? 0 : errorPath.hashCode());
		result = prime * result + ((flowType == null) ? 0 : flowType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((processPath == null) ? 0 : processPath.hashCode());
		result = prime * result + ((regExp == null) ? 0 : regExp.hashCode());
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
		ThirdPartyInfo other = (ThirdPartyInfo) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (errorPath == null) {
			if (other.errorPath != null)
				return false;
		} else if (!errorPath.equals(other.errorPath))
			return false;
		if (flowType == null) {
			if (other.flowType != null)
				return false;
		} else if (!flowType.equals(other.flowType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (processPath == null) {
			if (other.processPath != null)
				return false;
		} else if (!processPath.equals(other.processPath))
			return false;
		if (regExp == null) {
			if (other.regExp != null)
				return false;
		} else if (!regExp.equals(other.regExp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThirdPartyInfo [id=" + id + ", name=" + name + ", path=" + path + ", errorPath=" + errorPath
				+ ", processPath=" + processPath + ", flowType=" + flowType + ", regExp=" + regExp + ", destination="
				+ destination + "]";
	}

	
}
