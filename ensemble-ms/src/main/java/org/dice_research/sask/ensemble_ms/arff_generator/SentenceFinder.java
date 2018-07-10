package org.dice_research.sask.ensemble_ms.arff_generator;

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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * This class obtain sentences from training data to be extracted later on
 * 
 * @author Sepide tari,Suganya Kannan
 *
 */

public class SentenceFinder {

	public void obtainSentencefromFiles(String filesPath) throws IOException {

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
		try {
			getResult(sentences);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	// method to get result from all the extractors

	public void getResult(List<String> sentences) throws IOException {

		Map<String, Integer> port_vs_extractorMap = new HashMap<String, Integer>();
		port_vs_extractorMap.put("fox", 2222);
		port_vs_extractorMap.put("fred", 2223);
		port_vs_extractorMap.put("spotlight", 2224);
		port_vs_extractorMap.put("cedric", 2225);
		port_vs_extractorMap.put("openIE", 2226);
		port_vs_extractorMap.put("sorookin", 2227);

		int portNumb[] = { 2222, 2223, 2224, 2225, 2226, 2227 };

		Map<String, String> foxRespMap = new HashMap<String, String>();
		Map<String, String> fredRespMap = new HashMap<String, String>();
		Map<String, String> spotlightRespMap = new HashMap<String, String>();
		Map<String, String> cedricRespMap = new HashMap<String, String>();
		Map<String, String> openIERespMap = new HashMap<String, String>();
		Map<String, String> sorookinRespMap = new HashMap<String, String>();

		for (int i = 0; i < sentences.size(); i++) {

			// calling every extractors and getting outputs

			for (int j = 0; j < portNumb.length; j++) {
				String _extractorAPICall_URL = "http://localhost:" + portNumb[j] + "/extractSimple?input="
						+ sentences.get(i);

				URL url;
				try {
					url = new URL(_extractorAPICall_URL);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(response.toString());
					if (j == 0) {
						foxRespMap.put(sentences.get(i), response.toString());
					} else if (j == 1) {
						fredRespMap.put(sentences.get(i), response.toString());
					} else if (j == 2) {
						spotlightRespMap.put(sentences.get(i), response.toString());
					} else if (j == 3) {
						cedricRespMap.put(sentences.get(i), response.toString());
					} else if (j == 4) {
						openIERespMap.put(sentences.get(i), response.toString());
					} else if (j == 5) {
						sorookinRespMap.put(sentences.get(i), response.toString());
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
