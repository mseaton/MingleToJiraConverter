package org.pih;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportItem {

	private Card card;
	private Map<String, String> attachments;
	private List<Comment> comments;

	public ExportItem() {}

	public String toString() {
		return card + ": " + getAttachments().size() + " attachments, " + getComments().size() + " comments";
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Map<String, String> getAttachments() {
		if (attachments == null) {
			attachments = new HashMap<String, String>();
		}
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

	public List<Comment> getComments() {
		if (comments == null) {
			comments = new ArrayList<Comment>();
		}
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
