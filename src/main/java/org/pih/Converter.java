package org.pih;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Converter {

	public static final String MINGLE_DIRECTORY = "/home/mseaton/Desktop/pih_mirebalais";

	public static List<Attachment> loadAttachments() {
		return Util.loadList(MINGLE_DIRECTORY, "attachments", Attachment.class);
	}

	public static List<User> loadUsers() {
		return Util.loadList(MINGLE_DIRECTORY, "users", User.class);
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

	public static ExportData loadExportData() {

		ExportData data = new ExportData();

		Map<String, Attachment> attachmentMap = new HashMap<String, Attachment>();
		for (Attachment attachment : loadAttachments()) {
			attachmentMap.put(attachment.getId(), attachment);
		}

		Map<String, User> userMap = new HashMap<String, User>();
		for (User user : loadUsers()) {
			userMap.put(user.getId(), user);
		}

		List<Attaching> attachings = loadAttachings();
		List<Murmur> murmurs = loadMurmurs();

		// Order versions by id
		List<CardVersion> versions = loadCardVersions();
		Collections.sort(versions, new Comparator<CardVersion>() {
			public int compare(CardVersion cardVersion1, CardVersion cardVersion2) {
				return Integer.valueOf(cardVersion1.getId()).compareTo(Integer.valueOf(cardVersion2.getId()));
			}
		});

		Map<String, Collection<UserEntry>> attachmentsForExport = new LinkedHashMap<String, Collection<UserEntry>>();
		Map<String, Collection<UserEntry>> commentsForExport = new LinkedHashMap<String, Collection<UserEntry>>();

		for (Card card : loadCards()) {

			String cardId = card.getId();
			String cardNumber = card.getNumber();
			String cardType = card.getCard_type_name();
			String createdBy = userMap.get(card.getCreated_by_user_id()).getLogin();
			String createdDate = card.getCreated_at();

			Map<String, UserEntry> attachments = new HashMap<String, UserEntry>();
			List<UserEntry> comments = new ArrayList<UserEntry>();

			for (CardVersion version : versions) {
				if (version.getCard_id().equals(cardId)) {
					String versionId = version.getId();

					if (version.getComment() != null && !version.getComment().equals("")) {
						UserEntry userEntry = new UserEntry();
						userEntry.setComment(version.getComment());
						userEntry.setUser(userMap.get(version.getModified_by_user_id()).getLogin());
						userEntry.setDate(version.getUpdated_at());
						comments.add(userEntry);
					}

					Set<String> idsFound = new HashSet<String>();
					for (Attaching attaching : attachings) {
						if (attaching.getAttachable_id().equals(versionId)) {
							Attachment attachment = attachmentMap.get(attaching.getAttachment_id());
							String attachmentId = attachment.getId();
							UserEntry userEntry = attachments.get(attachmentId);
							if (userEntry == null) {
								userEntry = new UserEntry();
								userEntry.setUser(userMap.get(version.getModified_by_user_id()).getLogin());
								userEntry.setDate(version.getUpdated_at());
								attachments.put(attachmentId, userEntry);
							}
							userEntry.setDirectoryPath(attachment.getPath());
							userEntry.setFileName(attachment.getFile());
							idsFound.add(attachmentId);
						}
					}
					attachments.keySet().retainAll(idsFound);
				}
			}

			for (Murmur murmur : murmurs) {
				if (murmur.getOrigin_id().equals(cardId)) {
					UserEntry userEntry = new UserEntry();
					userEntry.setUser(userMap.get(murmur.getAuthor_id()).getLogin());
					userEntry.setDate(murmur.getCreated_at());
					userEntry.setComment(murmur.getMurmur());
					comments.add(userEntry);
				}
			}

			ExportItem exportItem = new ExportItem();
			exportItem.setCard(card);
			exportItem.getAttachments().addAll(attachments.values());
			exportItem.getComments().addAll(comments);
			data.getExportItems().add(exportItem);

			if (attachments.size() > data.getMaxAttachments()) {
				data.setMaxAttachments(attachments.size());
			}

			if (comments.size() > data.getMaxComments()) {
				data.setMaxComments(comments.size());
			}
		}
		return data;
	}

	public static void exportData(String directory) throws Exception {

		ExportData data = loadExportData();

		{
			File f = new File(directory, "attachmentExport.csv");
			CSVWriter writer = new CSVWriter(new FileWriter(f), ',');
			String[] headerRow = new String[data.getMaxAttachments() + 1];
			headerRow[0] = "Number";
			for (int i=1; i<=data.getMaxAttachments(); i++) {
				headerRow[i] = "Attachment";
			}
			writer.writeNext(headerRow);
			for (ExportItem item : data.getExportItems()) {
				if (item.getAttachments().size() > 0) {
					String[] row = new String[data.getMaxAttachments() + 1];
					row[0] = item.getCard().getNumber();
					int num = 1;
					for (UserEntry entry : item.getAttachments()) {
						row[num++] = entry.getExportFormat();
					}
					writer.writeNext(row);
				}
			}
			writer.close();
		}

		{
			File f = new File(directory, "commentExport.csv");
			CSVWriter writer = new CSVWriter(new FileWriter(f), ',');
			String[] headerRow = new String[data.getMaxComments() + 1];
			headerRow[0] = "Number";
			for (int i=1; i<=data.getMaxComments(); i++) {
				headerRow[i] = "Comment";
			}
			writer.writeNext(headerRow);
			for (ExportItem item : data.getExportItems()) {
				if (item.getComments().size() > 0) {
					String[] row = new String[data.getMaxComments() + 1];
					row[0] = item.getCard().getNumber();
					int num = 1;
					for (UserEntry entry : item.getComments()) {
						row[num++] = entry.getExportFormat();
					}
					writer.writeNext(row);
				}
			}
			writer.close();
		}
	}
}
