package org.pih;

/**
 * Represents an entry in attachments_0
 */
public class Tag {

	private String id;
	private String name;

	public Tag() {}

	public String toString() {
		return id + " (" + name + ")";
	}

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
}
