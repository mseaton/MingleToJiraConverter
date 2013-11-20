package org.pih;

public class Murmur {

	private String id;
	private String type;
	private String author_id;
	private String created_at;
	private String murmur;
	private String origin_id;
	private String origin_type;

	public Murmur() {}

	public String toString() {
		return id + " (origin = " + origin_type + ": " + origin_id + "): " + murmur;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getMurmur() {
		return murmur;
	}

	public void setMurmur(String murmur) {
		this.murmur = murmur;
	}

	public String getOrigin_id() {
		return origin_id;
	}

	public void setOrigin_id(String origin_id) {
		this.origin_id = origin_id;
	}

	public String getOrigin_type() {
		return origin_type;
	}

	public void setOrigin_type(String origin_type) {
		this.origin_type = origin_type;
	}
}
