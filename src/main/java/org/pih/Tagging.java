package org.pih;

/**
 * Represents an entry in attachings_x
 */
public class Tagging {

	private String id;
	private String taggable_type;
	private String taggable_id;
	private String tag_id;

	public Tagging() {}

	public String toString() {
		return id + " (" + taggable_type + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaggable_type() {
		return taggable_type;
	}

	public void setTaggable_type(String taggable_type) {
		this.taggable_type = taggable_type;
	}

	public String getTaggable_id() {
		return taggable_id;
	}

	public void setTaggable_id(String taggable_id) {
		this.taggable_id = taggable_id;
	}

	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}
}
