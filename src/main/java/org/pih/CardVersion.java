package org.pih;

import java.util.List;

public class CardVersion {

	private String id;
	private String card_id;
	private String number;
	private String version;
	private String comment;
	private String updated_at;
	private String modified_by_user_id;

	public CardVersion() {}

	public String toString() {
		return id + " (card " + card_id + ", #" + number + " v" + version + ")";
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getModified_by_user_id() {
		return modified_by_user_id;
	}

	public void setModified_by_user_id(String modified_by_user_id) {
		this.modified_by_user_id = modified_by_user_id;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
