package org.pih;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Converter {

	public static final String MINGLE_DIRECTORY = "/home/mseaton/Desktop/mingleToJira/pih_mirebalais";

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

		List<Card> cards = loadCards();
		List<CardVersion> versions = loadCardVersions();
		List<Attachment> attachments = loadAttachments();
		List<Attaching> attachings = loadAttachings();
		List<Tag> tags = loadTags();
		List<Tagging> taggings = loadTaggings();
		List<Murmur> murmurs = loadMurmurs();
		List<User> users = loadUsers();

		// Order versions by id
		Collections.sort(versions, new Comparator<CardVersion>() {
			public int compare(CardVersion cardVersion1, CardVersion cardVersion2) {
				return Integer.valueOf(cardVersion1.getId()).compareTo(Integer.valueOf(cardVersion2.getId()));
			}
		});

		for (CardVersion version : versions) {
			data.getLatestCardVersionMap().put(version.getCard_id(), version);
		}
		for (Attachment attachment : attachments) {
			data.getAttachmentMap().put(attachment.getId(), attachment);
		}
		for (Tag tag : tags) {
			data.getTagMap().put(tag.getId(), tag);
		}
		for (User user : users) {
			data.getUserMap().put(user.getId(), user);
		}

		for (Card card : cards) {

			ExportItem exportItem = new ExportItem();
			exportItem.setCard(card);

			String cardId = card.getId();

			Map<String, UserEntry> itemAttachments = new HashMap<String, UserEntry>();
			List<UserEntry> itemComments = new ArrayList<UserEntry>();

			for (CardVersion version : versions) {
				if (version.getCard_id().equals(cardId)) {
					String versionId = version.getId();

					StringBuilder comment = new StringBuilder();
					if (Util.notNull(version.getComment())) {
						comment.append(version.getComment()).append(Util.newLine()).append(Util.newLine());
					}
					if (exportItem.getLatestVersion() != null) {
						String changes = Util.getChanges(exportItem.getLatestVersion(), version, data);
						if (Util.notNull(changes)) {
							comment.append(changes);
						}
					}
					exportItem.setLatestVersion(version);

					if (Util.notNull(comment.toString())) {
						UserEntry commentEntry = new UserEntry();
						commentEntry.setComment(comment.toString());
						commentEntry.setUser(Util.getUsername(version.getModified_by_user_id(), data));
						commentEntry.setDate(version.getUpdated_at());
						itemComments.add(commentEntry);
					}

					Set<String> idsFound = new HashSet<String>();
					for (Attaching attaching : attachings) {
						if (attaching.getAttachable_id().equals(versionId)) {
							Attachment attachment = data.getAttachmentMap().get(attaching.getAttachment_id());
							String attachmentId = attachment.getId();
							UserEntry attachmentEntry = itemAttachments.get(attachmentId);
							if (attachmentEntry == null) {
								attachmentEntry = new UserEntry();
								attachmentEntry.setUser(Util.getUsername(version.getModified_by_user_id(), data));
								attachmentEntry.setDate(version.getUpdated_at());
								itemAttachments.put(attachmentId, attachmentEntry);
							}
							attachmentEntry.setDirectoryPath(attachment.getPath() + "/" + attachment.getId());
							attachmentEntry.setFileName(attachment.getFile());
							idsFound.add(attachmentId);
						}
					}
					itemAttachments.keySet().retainAll(idsFound);

					exportItem.getTags().clear();
					for (Tagging tagging : taggings) {
						if (tagging.getTaggable_id().equals(versionId)) {
							exportItem.getTags().add(data.getTagMap().get(tagging.getTag_id()).getName());
						}
					}
				}
			}

			for (Murmur murmur : murmurs) {
				if (murmur.getOrigin_id().equals(cardId)) {
					UserEntry userEntry = new UserEntry();
					userEntry.setUser(Util.getUsername(murmur.getAuthor_id(), data));
					userEntry.setDate(murmur.getCreated_at());
					userEntry.setComment(murmur.getMurmur());
					itemComments.add(userEntry);
				}
			}

			exportItem.getAttachments().addAll(itemAttachments.values());
			exportItem.getComments().addAll(itemComments);
			data.getExportItems().add(exportItem);

			if (itemAttachments.size() > data.getMaxAttachments()) {
				data.setMaxAttachments(itemAttachments.size());
			}

			if (itemAttachments.size() > data.getMaxComments()) {
				data.setMaxComments(itemAttachments.size());
			}
		}
		return data;
	}

	public static void exportData(String directory) throws Exception {

		ExportData data = loadExportData();

		// First thing to do is to export data for stories

		File cardExportFile = new File(directory, "mingleCardExport.csv");
		CSVWriter cardWriter = new CSVWriter(new FileWriter(cardExportFile), ',');

		List<String> headers = new ArrayList<String>();
		headers.add("Number");
		headers.add("Type");
		headers.add("Subtype");
		headers.add("Summary");
		headers.add("Description");
		headers.add("CreatedBy");
		headers.add("CreatedDate");
		headers.add("WorkStartedDate");
		headers.add("WorkAcceptedDate");
		headers.add("LastModifiedBy");
		headers.add("LastModifiedDate");
		headers.add("Status1");
		headers.add("Status2");
		headers.add("Priority");
		headers.add("Estimate");
		headers.add("AssignedTo");
		headers.add("PlannedRelease");
		headers.add("PlannedIteration");
		headers.add("ParentNumber");
		headers.add("Labels");

		for (int i=1; i<=data.getMaxComments(); i++) {
			headers.add("Comment");
		}
		for (int i=1; i<=data.getMaxAttachments(); i++) {
			headers.add("Attachment");
		}

		cardWriter.writeNext(headers.toArray(new String[] {}));

		for (ExportItem item : data.getExportItems()) {

			if (!item.isIterationOrRelease()) {
				List<String> row = new ArrayList<String>();

				Card card = item.getCard();
				CardVersion version = item.getLatestVersion();
				CardVersion parentStory = data.getLatestCardVersionMap().get(version.getCp_story_task_card_id());

				row.add(card.getNumber());
				row.add(parentStory != null ? "SubTask" : version.getCard_type_name());
				row.add(version.getCp_story_type());
				row.add(version.getName());
				row.add(Util.convertFromHtmlToWikiMarkup(version.getDescription()));

				row.add(Util.getUsername(card.getCreated_by_user_id(), data));
				row.add(card.getCreated_at());
				row.add(Util.formatDateStr(version.getCp_start_date()));
				row.add(Util.formatDateStr(version.getCp_qa_accepted_date()));
				row.add(Util.getUsername(version.getModified_by_user_id(), data));
				row.add(version.getUpdated_at());

				String status = version.getCp_status();
				if (Util.notNull(version.getCp_progress())) {
					status = version.getCp_progress();
				}
				row.add(status);
				row.add(status);

				String priority = version.getCp_defect_severity();
				if ("Yes".equals(version.getCp_blocked_())) {
					priority = "Blocker";
				}
				row.add(priority);

				row.add(version.getCp_estimate());

				row.add(Util.getUsername(version.getCp_assigned_to_user_id(), data));

				List<String> tags = item.getTags();
				CardVersion plannedReleaseCardVersion = data.getLatestCardVersionMap().get(version.getCp_release___release_card_id());
				CardVersion plannedIterationCardVersion = data.getLatestCardVersionMap().get(version.getCp_iteration_planned_card_id());

				String plannedReleaseName = Util.notNull(plannedReleaseCardVersion) ? plannedReleaseCardVersion.getName() : "";
				String plannedIterationName = Util.notNull(plannedIterationCardVersion) ? plannedIterationCardVersion.getName() : "";
				String parentStoryNumber = Util.notNull(parentStory) ? parentStory.getNumber() : "";

				row.add(plannedReleaseName);
				row.add(plannedIterationName);
				row.add(parentStoryNumber);

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
				cardWriter.writeNext(row.toArray(new String[] {}));
			}
		}
		cardWriter.close();

		// Now export data for iterations and releases so we can set these up manually

		File iterationExportFile = new File(directory, "mingleIterationExport.csv");
		CSVWriter iterationWriter = new CSVWriter(new FileWriter(iterationExportFile), ',');
		String[] iterationHeaders = {"Name", "IterationStartDate", "IterationEndDate", "ReleaseDate"};
		iterationWriter.writeNext(iterationHeaders);
		for (ExportItem item : data.getExportItems()) {
			if (item.isIterationOrRelease()) {
				Card card = item.getCard();
				CardVersion version = item.getLatestVersion();
				String name = version.getName();
				String iterationStartDate = version.getCp_iteration_start_date();
				String iterationEndDate = version.getCp_iteration_end_date();
				String releaseDate = version.getCp_release_date();
				String[] row = {name, iterationStartDate, iterationEndDate, releaseDate};
				iterationWriter.writeNext(row);
			}
		}
		iterationWriter.close();
	}
}
