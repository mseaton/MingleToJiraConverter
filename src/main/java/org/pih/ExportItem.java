package org.pih;

import java.util.ArrayList;
import java.util.List;

public class ExportItem {

	private Card card;
	private CardVersion latestVersion;
	private List<String> tags = new ArrayList<String>();
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

	public CardVersion getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(CardVersion latestVersion) {
		this.latestVersion = latestVersion;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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
