package org.pih;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Converter {

	public static final String MINGLE_DIRECTORY = "/home/mseaton/Desktop/pih_mirebalais";

	public static List<Attachment> loadAttachments() {
		return Util.loadList(MINGLE_DIRECTORY, "attachments", Attachment.class);
	}

	public static List<CardVersion> loadCardVersions() {
		return Util.loadList(MINGLE_DIRECTORY, "pih_mirebalais_card_versions", CardVersion.class);
	}

	public static List<Attaching> loadAttachings() {
		return Util.loadList(MINGLE_DIRECTORY, "attachings", Attaching.class);
	}

	public static List<Murmur> loadMurmurs() {
		return Util.loadList(MINGLE_DIRECTORY, "murmurs", Murmur.class);
	}

	public static List<Card> loadCards() {
		return Util.loadList(MINGLE_DIRECTORY, "pih_mirebalais_cards", Card.class);
	}

	public static List<ExportItem> loadExportItems() {

		Map<String, ExportItem> itemMap = new LinkedHashMap<String, ExportItem>();

		for (Card card : loadCards()) {
			ExportItem item = new ExportItem();
			item.setCard(card);
			itemMap.put(card.getId(), item);
		}

		Map<String, Attachment> attachmentMap = new HashMap<String, Attachment>();
		for (Attachment attachment : loadAttachments()) {
			attachmentMap.put(attachment.getId(), attachment);
		}

		List<Attaching> attachings = loadAttachings();

		// Order versions by id
		List<CardVersion> versions = loadCardVersions();
		Collections.sort(versions, new Comparator<CardVersion>() {
			public int compare(CardVersion cardVersion1, CardVersion cardVersion2) {
				return cardVersion1.getId().compareTo(cardVersion2.getId());
			}
		});

		for (CardVersion version : versions) {
			String versionId = version.getId();
			String cardId = version.getCard_id();
			ExportItem item = itemMap.get(cardId);
			if (item != null) {
				if (version.getComment() != null && !version.getComment().equals("")) {
					Comment comment = new Comment();
					comment.setComment(version.getComment());
					comment.setUser(version.getModified_by_user_id());
					comment.setDate(version.getUpdated_at());
					item.getComments().add(comment);
				}
				for (Attaching attaching : attachings) {
					if (attaching.getAttachable_id().equals(versionId)) {
						Attachment attachment = attachmentMap.get(attaching.getAttachment_id());
						item.getAttachments().put(attachment.getFile(), attachment.getPath() + "/" + attachment.getFile());
					}
				}
			}
			else {
				System.out.println("WARNING: NO CARD FOUND FOR VERSION: " + version);
			}
		}

		for (Murmur murmur : loadMurmurs()) {
			ExportItem item = itemMap.get(murmur.getOrigin_id());
			if (item != null) {
				Comment comment = new Comment();
				comment.setComment(murmur.getMurmur());
				item.getComments().add(comment);
			}
		}

		return new ArrayList<ExportItem>(itemMap.values());
	}

	public static void exportAttachmentCsv(String directory) throws Exception {
		CSVWriter writer = new CSVWriter(new FileWriter(directory), ',');
		for (ExportItem item : loadExportItems()) {
			for (String attachment: item.getAttachments().values()) {
				String[] row = new String[] {item.getCard().getNumber(), attachment};
				writer.writeNext(row);
			}
		}
		writer.close();
	}
}
