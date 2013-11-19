package org.pih;

public class Card {

	private String id;
	private String number;
	private String name;
	private String card_type_name;

	public Card() {}

	public String toString() {
		return id + " (" + card_type_name + " #" + number + "): " + name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCard_type_name() {
		return card_type_name;
	}

	public void setCard_type_name(String card_type_name) {
		this.card_type_name = card_type_name;
	}
}
