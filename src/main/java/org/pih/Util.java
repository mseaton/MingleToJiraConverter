package org.pih;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;

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
}
