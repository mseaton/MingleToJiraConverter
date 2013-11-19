package org.pih;

/**
 * Represents an entry in attachments_0
 */
public class Attachment {

	private String id;
	private String project_id;
	private String path;
	private String file;

	public Attachment() {}

	public String toString() {
		return id + " (" + path + "/" + file + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
