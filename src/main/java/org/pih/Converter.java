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

	public static List<User> loadUsers() {
		return Util.loadList(MINGLE_DIRECTORY, "users", User.class);
	}

	public static List<Card> loadCards() {
		return Util.loadList(MINGLE_DIRECTORY, "pih_mirebalais_cards", Card.class);
	}

	public static List<CardVersion> loadCardVersions() {
		return Util.loadList(MINGLE_DIRECTORY, "pih_mirebalais_card_versions", CardVersion.class);
	}

	public static List<Attachment> loadAttachments() {
		return Util.loadList(MINGLE_DIRECTORY, "attachments", Attachment.class);
	}

	public static List<Attaching> loadAttachings() {
		return Util.loadList(MINGLE_DIRECTORY, "attachings", Attaching.class);
	}

	public static List<Tag> loadTags() {
		return Util.loadList(MINGLE_DIRECTORY, "tags", Tag.class);
	}

	public static List<Tagging> loadTaggings() {
		return Util.loadList(MINGLE_DIRECTORY, "taggings", Tagging.class);
	}

	public static List<Murmur> loadMurmurs() {
		return Util.loadList(MINGLE_DIRECTORY, "murmurs", Murmur.class);
	}

	public static ExportData loadExportData() {

		ExportData data = new ExportData();

		for (User user : loadUsers()) {
			data.getUserMap().put(user.getId(), user);
		}
		for (Attachment attachment : loadAttachments()) {
			data.getAttachmentMap().put(attachment.getId(), attachment);
		}
		for (Tag tag : loadTags()) {
			data.getTagMap().put(tag.getId(), tag);
		}

		List<Attaching> attachings = loadAttachings();
		List<Tagging> taggings = loadTaggings();
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

			ExportItem exportItem = new ExportItem();
			exportItem.setCard(card);

			String cardId = card.getId();

			Map<String, UserEntry> attachments = new HashMap<String, UserEntry>();
			List<UserEntry> comments = new ArrayList<UserEntry>();

			for (CardVersion version : versions) {
				if (version.getCard_id().equals(cardId)) {
					String versionId = version.getId();

					StringBuilder comment = new StringBuilder();
					if (Util.notNull(version.getComment())) {
						comment.append(version.getComment()).append(Util.newLine()).append(Util.newLine());
					}
					if (exportItem.getLatestVersion() != null) {
						String changes = Util.getChanges(exportItem.getLatestVersion(), version, data.getUserMap());
						if (Util.notNull(changes)) {
							comment.append(changes);
						}
					}
					exportItem.setLatestVersion(version);

					if (Util.notNull(comment.toString())) {
						UserEntry commentEntry = new UserEntry();
						commentEntry.setComment(comment.toString());
						commentEntry.setUser(Util.getUsername(version.getModified_by_user_id(), data.getUserMap()));
						commentEntry.setDate(version.getUpdated_at());
						comments.add(commentEntry);
					}

					Set<String> idsFound = new HashSet<String>();
					for (Attaching attaching : attachings) {
						if (attaching.getAttachable_id().equals(versionId)) {
							Attachment attachment = data.getAttachmentMap().get(attaching.getAttachment_id());
							String attachmentId = attachment.getId();
							UserEntry attachmentEntry = attachments.get(attachmentId);
							if (attachmentEntry == null) {
								attachmentEntry = new UserEntry();
								attachmentEntry.setUser(Util.getUsername(version.getModified_by_user_id(), data.getUserMap()));
								attachmentEntry.setDate(version.getUpdated_at());
								attachments.put(attachmentId, attachmentEntry);
							}
							attachmentEntry.setDirectoryPath(attachment.getPath());
							attachmentEntry.setFileName(attachment.getFile());
							idsFound.add(attachmentId);
						}
					}
					attachments.keySet().retainAll(idsFound);

					List<String> tags = new ArrayList<String>();
					for (Tagging tagging : taggings) {
						if (tagging.getTaggable_id().equals(versionId)) {
							tags.add(data.getTagMap().get(tagging.getTag_id()).getName());
						}
					}
					exportItem.setTags(tags);
				}
			}

			for (Murmur murmur : murmurs) {
				if (murmur.getOrigin_id().equals(cardId)) {
					UserEntry userEntry = new UserEntry();
					userEntry.setUser(Util.getUsername(murmur.getAuthor_id(), data.getUserMap()));
					userEntry.setDate(murmur.getCreated_at());
					userEntry.setComment(murmur.getMurmur());
					comments.add(userEntry);
				}
			}

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

		// TODO:  ADD other data needed for card

		/** TODO:
		 * Independently get release and iteration info, including:
		 *
		 * For Releases and Iterations:
		 * cp_release_date: Date a release is planned to go live (only one value in db for this for release 1)
		 * cp_iteration_start_date:  Date an iteration is planned to start (only used for iterations)
		 * cp_iteration_end_date:  Date an iteration is planned to end (only used for iterations)
		 */

		ExportData data = loadExportData();

		File f = new File(directory, "mingleExport.csv");
		CSVWriter writer = new CSVWriter(new FileWriter(f), ',');

		List<String> headers = new ArrayList<String>();
		headers.add("Number");
		headers.add("Summary");
		headers.add("Labels");
		for (int i=1; i<=data.getMaxComments(); i++) {
			headers.add("Comment");
		}
		for (int i=1; i<=data.getMaxAttachments(); i++) {
			headers.add("Attachment");
		}
		writer.writeNext(headers.toArray(new String[] {}));

		for (ExportItem item : data.getExportItems()) {
			List<String> row = new ArrayList<String>();

			Card card = item.getCard();
			CardVersion version = item.getLatestVersion();

			String cardNumber = card.getNumber();
			String cardType = card.getCard_type_name();
			String createdBy = Util.getUsername(card.getCreated_by_user_id(), data.getUserMap());
			String createdDate = card.getCreated_at();


			row.add(item.getCard().getNumber());
			row.add(item.getLatestVersion().getName());

			List<String> tags = item.getTags();
			// TODO: Add release and iteration info here

			StringBuilder tagCell = new StringBuilder();
			for (String tag : tags) {
				tag = tag.replace(" ", "-");
				tagCell.append(tagCell.length() == 0 ? "" : " ").append(tag);
			}
			row.add(tagCell.toString());

			for (int i=0; i<data.getMaxComments(); i++) {
				String comment = (item.getComments().size() > i ? item.getComments().get(i).getExportFormat() : "");
				row.add(comment);
			}
			for (int i=0; i<data.getMaxAttachments(); i++) {
				String attachment = (item.getAttachments().size() > i ? item.getAttachments().get(i).getExportFormat() : "");
				row.add(attachment);
			}
			writer.writeNext(row.toArray(new String[] {}));
		}
		writer.close();
	}
}
