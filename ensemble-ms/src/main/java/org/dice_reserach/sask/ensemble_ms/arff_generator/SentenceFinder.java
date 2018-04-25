package org.dice_reserach.sask.ensemble_ms.arff_generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

/**
 * This class obtain sentences from training data to be extracted later on
 * 
 * @author Sepide tari,
 *
 */

public class SentenceFinder {
	
	public List<String> obtainSentencefromFiles(String filesPath) {

		File path = new File(filesPath);
		File[] files = path.listFiles();
		List<String> sentences = new ArrayList<String>();

		// sort the files in numerical order
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				String s1 = f1.getName().substring(5, f1.getName().indexOf("."));
				String s2 = f2.getName().substring(5, f2.getName().indexOf("."));
				return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
			}
		});

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String sentence = readLineByLine(files[i].toString());
				sentences.add(StringUtils.substringBetween(sentence, "nif:isString    \"", "\" ."));
			}
		}
		return sentences;
	}

	public String readLineByLine(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
}