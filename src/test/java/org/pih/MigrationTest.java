package org.pih;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MigrationTest {

	@Test
	public void shouldExportAttachmentCsv() throws Exception {
		Converter.exportAttachmentCsv("/home/mseaton/Desktop/attachmentExport.csv");
	}

	@Test
	public void shouldLoadExportItems() throws Exception {
		List<ExportItem> exportItems = Converter.loadExportItems();
		Assert.assertTrue(exportItems.size() > 0);
		System.out.println("Found " + exportItems.size() + " items");
		for (ExportItem item : exportItems) {
			System.out.println(item);
		}
	}

	@Test
	public void shouldLoadAttachments() throws Exception {
		List<Attachment> attachments = Converter.loadAttachments();
		Assert.assertTrue(attachments.size() > 0);
		for (Attachment a : attachments) {
			System.out.println(a);
		}
	}

	@Test
	public void shouldLoadCardVersions() throws Exception {
		List<CardVersion> cardVersions = Converter.loadCardVersions();
		Assert.assertTrue(cardVersions.size() > 0);
		for (CardVersion v : cardVersions) {
			System.out.println(v);
		}
	}

	@Test
	public void shouldLoadAttachings() throws Exception {
		List<Attaching> attachings = Converter.loadAttachings();
		Assert.assertTrue(attachings.size() > 0);
		for (Attaching a : attachings) {
			System.out.println(a);
		}
	}

	@Test
	public void shouldLoadMurmurs() throws Exception {
		List<Murmur> murmurs = Converter.loadMurmurs();
		Assert.assertTrue(murmurs.size() > 0);
		for (Murmur m : murmurs) {
			System.out.println(m);
		}
	}

	@Test
	public void shouldLoadCards() throws Exception {
		List<Card> cards = Converter.loadCards();
		Assert.assertTrue(cards.size() > 0);
		for (Card c : cards) {
			System.out.println(c);
		}
	}
}
