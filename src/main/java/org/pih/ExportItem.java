package org.pih;

import java.util.ArrayList;
import java.util.List;

public class ExportItem {

	private Card card;
	private List<UserEntry> attachments = new ArrayList<UserEntry>();
	private List<UserEntry> comments = new ArrayList<UserEntry>();

	public ExportItem() {}

	public String toString() {
		return card + " (" + attachments.size() + " attachments,  " + comments.size() + " comments)";
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public List<UserEntry> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UserEntry> attachments) {
		this.attachments = attachments;
	}

	public List<UserEntry> getComments() {
		return comments;
	}

	public void setComments(List<UserEntry> comments) {
		this.comments = comments;
	}
}
