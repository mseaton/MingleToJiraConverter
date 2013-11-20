package org.pih;

import java.util.ArrayList;
import java.util.List;

public class ExportData {

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
}
