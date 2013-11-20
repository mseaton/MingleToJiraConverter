package org.pih;

public class Card {

	private String id;
	private String number;
	private String card_type_name;
	private String created_at;
	private String created_by_user_id;

	public Card() {}

	public String toString() {
		return id + " (" + card_type_name + " #" + number + ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCard_type_name() {
		return card_type_name;
	}

	public void setCard_type_name(String card_type_name) {
		this.card_type_name = card_type_name;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_by_user_id() {
		return created_by_user_id;
	}

	public void setCreated_by_user_id(String created_by_user_id) {
		this.created_by_user_id = created_by_user_id;
	}
}
