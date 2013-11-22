package org.pih;

import com.esotericsoftware.yamlbeans.YamlReader;
import info.bliki.html.HTML2WikiConverter;
import info.bliki.html.wikipedia.ToWikipedia;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Util {

	public static List<File> getFiles(String directory, String prefix) {
		List<File> ret = new ArrayList<File>();
		File f = new File(directory, prefix + "_0.yml");
		while (f.exists()) {
			ret.add(f);
			f = new File(directory, prefix + "_" + ret.size() + ".yml");
		}
		return ret;
	}

	public static <T> List<T> loadList(String directory, String baseFileName, Class<T> type) {
		List<T> l = new ArrayList<T>();
		for (File f : Util.getFiles(directory, baseFileName)) {
			System.out.println("Loading file: " + f);
			l.addAll(Util.loadListFromYaml(f, type));
		}
		return l;
	}

	public static <T> List<T> loadListFromYaml(File f, Class<T> type) {
		try {
			String yml = FileUtils.readFileToString(f, "UTF-8");
			YamlReader reader = new YamlReader(yml);
			Object ret = reader.read();
			List l = (List) ret;
			List<T> newList = new ArrayList<T>();
			for (Object o : l) {
				Map m = (Map) o;
				T obj = type.newInstance();
				for (Object key : m.keySet()) {
					Object value = m.get(key);
					if (PropertyUtils.isWriteable(obj, key.toString())) {
						PropertyUtils.setProperty(obj, key.toString(), value);
					}
				}
				newList.add(obj);
			}
			return newList;
		}
		catch (Exception e) {
			throw new RuntimeException("Error loading " + f.getAbsolutePath() + " from yml");
		}
	}

	public static <T> T loadFromYaml(File directory, String fileName, Class<T> type) throws Exception {
		File f = new File(directory, fileName);
		String yml = FileUtils.readFileToString(f, "UTF-8");
		YamlReader reader = new YamlReader(yml);
		Object ret = reader.read();
		return (T) ret;
	}

	public static boolean matchesAny(String toTest, String...valuesToCheck) {
		for (String s : valuesToCheck) {
			if (toTest.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static String getChanges(CardVersion v1, CardVersion v2, ExportData data) {
		StringBuilder sb = new StringBuilder();
		for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(CardVersion.class)) {
			String propertyName = pd.getName();
			boolean ignore = matchesAny(propertyName, "id", "modified_by_user_id", "updated_at", "version", "comment");
			if (!ignore) {
				boolean showChange = !propertyName.equals("name") && !propertyName.equals("description");
				String change = getChange(v1, v2, propertyName, showChange, data);
				if (change != null && !change.equals("")) {
					sb.append(change).append(Util.newLine());
				}
			}
		}
		return sb.toString();
	}

	public static String getChange(CardVersion v1, CardVersion v2, String property, boolean showChange, ExportData data) {
		try {
			Object o1 = PropertyUtils.getProperty(v1, property);
			Object o2 = PropertyUtils.getProperty(v2, property);

			if (matchesAny(property, "cp_assigned_to_user_id", "modified_by_user_id", "cp_assigned_to_user_id")) {
				o1 = (notNull(o1) ? getUsername(o1.toString(), data) : o1);
				o2 = (notNull(o2) ? getUsername(o2.toString(), data) : o2);
			}

			if (matchesAny(property, "cp_iteration_planned_card_id", "cp_release___release_card_id", "cp_defect_iteration___iteration_card_id")) {
				o1 = (notNull(o1) ? getCardName(o1.toString(), data) : o1);
				o2 = (notNull(o2) ? getCardName(o2.toString(), data) : o2);
			}

			if (matchesAny(property, "cp_story_task_card_id")) {
				o1 = (notNull(o1) ? getCardNumber(o1.toString(), data) : o1);
				o2 = (notNull(o2) ? getCardNumber(o2.toString(), data) : o2);
			}

			if (o1 == null && o2 != null) {
				return getDisplay(property) + " added" + (showChange ? (": " + o2) : "");
			}
			if (o1 != null && o2 == null) {
				return getDisplay(property) + " removed";
			}
			if (o1 != null && o2 != null && !o1.equals(o2)) {
				return getDisplay(property) + " changed" + (showChange ? (": " + o1 + " -> " + o2) : "");
			}
			return "";
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to get property " + property, e);
		}
	}

	public static String getUsername(String key, ExportData data) {
		User u = data.getUserMap().get(key);
		if (u != null) {
			return u.getLogin();
		}
		return "";
	}

	public static String getCardName(String key, ExportData data) {
		CardVersion v = data.getLatestCardVersionMap().get(key);
		if (v != null) {
			return v.getName();
		}
		return "";
	}

	public static String getCardNumber(String key, ExportData data) {
		CardVersion v = data.getLatestCardVersionMap().get(key);
		if (v != null) {
			return v.getNumber();
		}
		return "";
	}

	public static String newLine() {
		return System.getProperty("line.separator");
	}

	public static boolean notNull(Object s) {
		return s != null && !s.equals("");
	}

	public static String formatDateStr(String date) {
		if (notNull(date)) {
			return date + " 00:00:00.0";
		}
		return "";
	}

	public static String convertFromHtmlToWikiMarkup(String html) {
		HTML2WikiConverter converter = new HTML2WikiConverter();
		converter.setInputHTML(html);
		String wiki = converter.toWiki(new ToWikipedia());
		return wiki;
	}

	public static String getDisplay(String property) {
		if ("id".equals(property)) { return "Version Database Primary Key"; }
		if ("card_id".equals(property)) { return "Card Database Primary Key"; }
		if ("number".equals(property)) { return "Card Number"; }
		if ("version".equals(property)) { return "Version Number"; }
		if ("name".equals(property)) { return "Summary"; }
		if ("description".equals(property)) { return "Description"; }
		if ("card_type_name".equals(property)) { return "Issue Type"; }
		if ("comment".equals(property)) { return "Comment"; }
		if ("updated_at".equals(property)) { return "Date Updated"; }
		if ("modified_by_user_id".equals(property)) { return "Modified By"; }
		if ("cp_date_raised".equals(property)) { return "Date Created"; }
		if ("cp_assigned_to_user_id".equals(property)) { return "Assigned To"; }
		if ("cp_start_date".equals(property)) { return "Work Started Date"; }
		if ("cp_blocked_".equals(property)) { return "Blocked"; }
		if ("cp_defect_severity".equals(property)) { return "Severity"; }
		if ("cp_status".equals(property)) { return "Status"; }
		if ("cp_progress".equals(property)) { return "Progress"; }
		if ("cp_story_type".equals(property)) { return "Sub-type"; }
		if ("cp_estimate".equals(property)) { return "Estimate"; }
		if ("cp_qa_accepted_date".equals(property)) { return "Date Accepted"; }
		if ("cp_iteration_planned_card_id".equals(property)) { return "Planned Iteration"; }
		if ("cp_release___release_card_id".equals(property)) { return "Planned Release"; }
		if ("cp_defect_iteration___iteration_card_id".equals(property)) { return "Defect Iteration"; }
		if ("cp_story_task_card_id".equals(property)) { return "Parent Issue"; }
		if ("cp_toggle_status".equals(property)) { return "Feature Toggle Status"; }
		if ("cp_toggle_due_date".equals(property)) { return "Feature Toggle Due Date"; }
		if ("system_generated_comment".equals(property)) { return "System-generated Comment"; }
		return property;
	}
}
