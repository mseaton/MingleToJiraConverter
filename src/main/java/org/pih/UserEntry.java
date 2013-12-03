package org.pih;

public class UserEntry {

	private String comment;
	private String user;
	private String date;
	private String fileName;
	private String directoryPath;

	public UserEntry() {}

	public String toString() {
		return getExportFormat();
	}

	public String getExportFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append(date).append(";");
		sb.append(Util.normalizeUsername(user)).append(";");
		if (fileName != null) {
			sb.append(fileName).append(";");
			sb.append("file://").append(directoryPath + "/" + fileName);
		}
		if (comment != null) {
			sb.append(comment);
		}
		return sb.toString();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
}
