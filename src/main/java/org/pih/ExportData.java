package org.pih;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportData {

	private Map<String, User> userMap = new HashMap<String, User>();
	private Map<String, Attachment> attachmentMap = new HashMap<String, Attachment>();
	private Map<String, Tag> tagMap = new HashMap<String, Tag>();

	private int maxAttachments = 0;
	private int maxComments = 0;
	private List<ExportItem> exportItems = new ArrayList<ExportItem>();

	public ExportData() {}

	public String toString() {
		return exportItems.size() + " items to export";
	}

	public int getMaxAttachments() {
		return maxAttachments;
	}

	public void setMaxAttachments(int maxAttachments) {
		this.maxAttachments = maxAttachments;
	}

	public int getMaxComments() {
		return maxComments;
	}

	public void setMaxComments(int maxComments) {
		this.maxComments = maxComments;
	}

	public List<ExportItem> getExportItems() {
		return exportItems;
	}

	public void setExportItems(List<ExportItem> exportItems) {
		this.exportItems = exportItems;
	}

	public Map<String, User> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}

	public Map<String, Attachment> getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map<String, Attachment> attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public Map<String, Tag> getTagMap() {
		return tagMap;
	}

	public void setTagMap(Map<String, Tag> tagMap) {
		this.tagMap = tagMap;
	}
}
