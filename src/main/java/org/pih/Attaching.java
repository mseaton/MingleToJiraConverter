package org.pih;

/**
 * Represents an entry in attachings_x
 */
public class Attaching {

	private String id;
	private String attachable_type;
	private String attachable_id;
	private String attachment_id;

	public Attaching() {}

	public String toString() {
		return id + " (" + attachable_type + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttachable_type() {
		return attachable_type;
	}

	public void setAttachable_type(String attachable_type) {
		this.attachable_type = attachable_type;
	}

	public String getAttachable_id() {
		return attachable_id;
	}

	public void setAttachable_id(String attachable_id) {
		this.attachable_id = attachable_id;
	}

	public String getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(String attachment_id) {
		this.attachment_id = attachment_id;
	}
}
