package org.pih;

import com.esotericsoftware.yamlbeans.YamlReader;
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

	public static String getChanges(CardVersion v1, CardVersion v2, Map<String, User> userMap) {
		StringBuilder sb = new StringBuilder();
		for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(CardVersion.class)) {
			String propertyName = pd.getName();
			boolean ignore = matchesAny(propertyName, "id", "modified_by_user_id", "updated_at", "version", "comment");
			if (!ignore) {
				boolean showChange = !propertyName.equals("name") && !propertyName.equals("description");
				String change = getChange(v1, v2, propertyName, showChange, userMap);
				if (change != null && !change.equals("")) {
					sb.append(change).append(Util.newLine());
				}
			}
		}
		return sb.toString();
	}

	public static String getChange(CardVersion v1, CardVersion v2, String property, boolean showChange, Map<String, User> userMap) {
		try {
			Object o1 = PropertyUtils.getProperty(v1, property);
			Object o2 = PropertyUtils.getProperty(v2, property);

			if (matchesAny(property, "cp_assigned_to_user_id")) {
				o1 = (notNull(o1) ? userMap.get(o1.toString()).getLogin() : o1);
				o2 = (notNull(o2) ? userMap.get(o2.toString()).getLogin() : o2);
			}

			if (o1 == null && o2 != null) {
				return property + " added" + (showChange ? (": " + o2) : "");
			}
			if (o1 != null && o2 == null) {
				return property + " removed";
			}
			if (o1 != null && o2 != null && !o1.equals(o2)) {
				return property + " changed" + (showChange ? (": " + o1 + " -> " + o2) : "");
			}
			return "";
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to get property " + property, e);
		}
	}

	public static String getUsername(String key, Map<String, User> userMap) {
		return userMap.get(key).getLogin();
	}

	public static String newLine() {
		return System.getProperty("line.separator");
	}

	public static boolean notNull(Object s) {
		return s != null && !s.equals("");
	}

}
