package org.dice_research.sask.ensemble_ms.arff_generator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RiotException;
import org.apache.jena.util.FileManager;
import org.apache.log4j.BasicConfigurator;

/**
 * This class take data from oke folder folder sort them and provide it to
 * different extractors and store response from extractors
 * 
 * Perform Sparql query on the ttl files from oke to construct rdf triple and write in ttl format
 * 
 * Filter the exractor response by reading as rdf model and write in N - Triple format
 * @author Harsh Shah
 */

public class SentenceExtractor {
	private static File path = new File("oke data");
	private static File[] files = path.listFiles();
	static List<String> sentences = new ArrayList<String>();
	private static File file2 = new File("TrainingData\\traindata2.arff");
	private static String sentence_data;
	static String fox_response_string = null;
	static String sorokin_response_string = null;
	static String openIE_response_string = null;
	static String cedric_response_string = null;
	public List<String> sub_fox = new ArrayList<String>();
	List<String> obj_fox = new ArrayList<String>();
	List<String> pred_fox = new ArrayList<String>();
	List<String> sub_openIE = new ArrayList<String>();
	List<String> obj_openIE = new ArrayList<String>();
	List<String> pred_openIE = new ArrayList<String>();
	List<String> sub_sorokin = new ArrayList<String>();
	List<String> obj_sorokin = new ArrayList<String>();
	List<String> pred_sorokin = new ArrayList<String>();

	List<String> sub = new ArrayList<String>();
	List<String> obj = new ArrayList<String>();
	List<String> pred = new ArrayList<String>();

	public static void main(String[] args) {

		SentenceExtractor se = new SentenceExtractor();
		// sort the files in numerical order

		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				String s1 = f1.getName().substring(5, f1.getName().indexOf("."));
				String s2 = f2.getName().substring(5, f2.getName().indexOf("."));
				return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
			}
		});
		// number of ttl files
		// System.out.println(files.length);
		for (int i = 0; i < 5 ; i++) {
			se.sentence_Extracion(i);
			System.out.println("Extracted sentence From the file " + files[i].getName());

			System.out.println(sentences.get(i));
			System.out.println("Fox extractor response for file" + files[i].getName());
			System.out.println(fox_response_string);
			System.out.println("Sorokin extractor response for file " + files[i].getName());
			System.out.println(sorokin_response_string);
			System.out.println("Cedric extractor response for file" + files[i].getName());
			System.out.println(cedric_response_string);
			System.out.println("openIE extractor response for file " + files[i].getName());
			System.out.println(openIE_response_string);
			System.out.println("Gethering Sparql Query Result................................");

			String squery_Result = se.responseReader(i);
			 System.out.println("......................................");
			 System.out.println(squery_Result);
			
			 System.out.println("Training Data for file " + files[i].getName());
			String training_data = " ' " + sentences.get(i) + " ' " + ", " + "'" + fox_response_string + " ' " + ","
					+ " ' " + openIE_response_string + " ' " + "," + " ' " + openIE_response_string + " '  " + ","
					+ " ' " + cedric_response_string + " ' " + "," + " ' " + squery_Result + " ' ";

			System.out.println("------");
			System.out.println(training_data);
			System.out.println("List of Subjects in OKE files.........  ");
			System.out.println(se.sub);
			System.out.println("List of predicates  in OKE files.........  ");
			System.out.println(se.pred);
			System.out.println("List of Objects  in OKE files.........  ");
			;
			System.out.println(se.obj);
			System.out.println(" ...........");

			System.out.println("-------------------------------------------------------------");

			se.trainingFileWriter(training_data);
			se.response_Matching();
			se.sub_fox.clear();
			se.obj_fox.clear();
			se.pred_fox.clear();
			se.sub.clear();
			se.obj.clear();
			se.pred.clear();
			se.sub_openIE.clear();
			se.obj_openIE.clear();
			se.pred_openIE.clear();
			se.sub_sorokin.clear();
			se.obj_sorokin.clear();
			se.pred_openIE.clear();
			

		}

	}

	public void sentence_Extracion(int i) {
		if (files[i].isFile()) {
			String sentence = readLineByLine(files[i].toString());
			int x = sentence.indexOf("nif:isString   ");
			int start = x + 17;
			int end = sentence.indexOf("\" .");
			end--;
			sentence_data = sentence.substring(start, end);
			// System.out.println(sentence_data);
			sentences.add(sentence_data);
			// Extractors responses
			//
			// Map<String, Integer> port_vs_extractorMap = new HashMap<String, Integer>();
			// port_vs_extractorMap.put("fox", 2222);
			// port_vs_extractorMap.put("fred", 2223);
			// port_vs_extractorMap.put("spotlight", 2224);
			// port_vs_extractorMap.put("cedric", 2225);
			// port_vs_extractorMap.put("openIE", 2226);
			// port_vs_extractorMap.put("sorookin", 2227);

			// int portNumb[] = { 2222, 2224, 2225, 2226, 2227 };
//			port of extractor array
			int portNumb[] = { 2222, 2227, 2226, 2225 };

			Map<String, String> fredRespMap = new HashMap<String, String>();
			Map<String, String> cedricRespMap = new HashMap<String, String>();
			Map<String, String> openIERespMap = new HashMap<String, String>();
			Map<String, String> sorookinRespMap = new HashMap<String, String>();

			// calling every extractors and getting outputs

			for (int j = 0; j < portNumb.length; j++) {
				// To handle url_encoding exception
				try {
					// replacing url with space character
					String URL2 = java.net.URLEncoder.encode(sentences.get(i), "UTF-8").replace("+", "%20");
					// add extractor for url
					String _extractorURL = "http://localhost:" + portNumb[j] + "/extractSimple?input=" + URL2;
					// java.awt.Desktop.getDesktop().browse(java.net.URI.create(_extractorURL));
					// _extractorURL = "https://www.google.com/";

					URL url;

					url = new URL(_extractorURL);
					//
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					// System.out.println(url);
					con.setRequestMethod("GET");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {

						response.append(inputLine);
					}

					in.close();

					// print result from response

					if (j == 0) {

						List<String> fox_response_string_list = new ArrayList<String>();
						fox_response_string = response.toString();

						String Filtered_fox_response = fox_response_processing(fox_response_string, i);
						fox_response_string_list.add(Filtered_fox_response);
						System.out.println(Filtered_fox_response);
					}

					else if (j == 1) {
						fredRespMap.put(sentences.get(i), response.toString());
						List<String> sorokin_response_string_list = new ArrayList<String>();
						sorokin_response_string = response.toString();

						String Filtered_sorokin_response = sorokin_response_processing(sorokin_response_string, i);
						sorokin_response_string_list.add(Filtered_sorokin_response);
						System.out.println(Filtered_sorokin_response);;
					}

					else if (j == 2) {
						List<String> openIE_response_string_list = new ArrayList<String>();
						openIE_response_string = response.toString();
						String Filtered_openIE_response = openIE_response_processing(openIE_response_string, i);
						openIE_response_string_list.add(Filtered_openIE_response);
						System.out.println(Filtered_openIE_response);

					} else if (j == 3) {
						cedricRespMap.put(sentences.get(i), response.toString());
						ArrayList<String> cedric_response_string_list = new ArrayList<String>();
						cedric_response_string = response.toString();
						cedric_response_string_list.add(cedric_response_string);

					} else if (j == 4) {
						openIERespMap.put(sentences.get(i), response.toString());
					} else if (j == 5) {
						sorookinRespMap.put(sentences.get(i), response.toString());
					}

				}
				// end of try block
				catch (Exception e) {
					e.getMessage();
					// TODO: handle exception
				}

			}

		}

		// TODO Auto-generated method stub

	}

	public String sorokin_response_processing(String sorokin_response_string2, int i) throws IOException  {
		// create new file for for extractors response
		String filename = "ExtractorResponse//sorokinResponse//" + "sorokin" + i + ".ttl";
		File file = new File(filename);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(sorokin_response_string);
		writer.close();
		// create jena model for every files
		Model model = ModelFactory.createDefaultModel();
		model.read(filename);
		System.out.println("For the file " + filename);
		// Write as Turtle via model.write
		StringWriter modelAsString = new StringWriter();
		model.write(modelAsString, "N-TRIPLE");
		String sorokin_filtered_response = modelAsString.toString();
		StmtIterator statementIter = model.listStatements();
		Statement s;
		Resource subject;
		Property predicate;
		RDFNode object;
		while (statementIter.hasNext()) {
			s = statementIter.nextStatement();
			subject = s.getSubject();
			predicate = s.getPredicate();
			object = s.getObject();

			// System.out.println("List of Subject ");
			// System.out.println(subject.toString());
			sub_sorokin.add(subject.getLocalName());
			// System.out.println("List of predicates ");
			// System.out.println(" " + predicate.toString() + " ");
			pred_sorokin.add(predicate.getLocalName());

			// System.out.println("List of objects ");
			if (object instanceof Resource && object.toString().contains("/")) {
				obj_sorokin.add(object.asResource().getLocalName());

			} else if (object instanceof Literal) {

				obj_sorokin.add(object.toString());
			}

		}

		System.out.println("List of Subjects for sorokin extractor.........  ");
		System.out.println(sub_sorokin);
		System.out.println("List of predicates for sorokin extractor.........  ");
		System.out.println(pred_sorokin);
		System.out.println("List of Objects for sorokin extractor.........  ");

		System.out.println(obj_sorokin);
		System.out.println(" ...........");

		// Create Files

		return sorokin_filtered_response;

		

	}

	public static String readLineByLine(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}

	public String responseReader(int i) {
		String sparql_query_result = null;
		BasicConfigurator.configure();
		List<String> sparqueryList = new ArrayList<String>();

		// create an empty model
		// for (int i = 0; i < 3; i++)

		final String inputFileName = files[i].toString();
		Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		// read() method call is the URI which will be used for resolving relative URI's
		// model.read(in, "TURTLE");
		try {
			model.read(inputFileName);
		} catch (RiotException ro) {
			ro.getMessage();
		}
		// catch(MalformedURLException mur)
		// {
		//
		// mur.getMessage();
		// }

		// to run in command line sparql.bat --data=vc-db-1.rdf --query=q1.rq
		// write it to standard outString queryString = " .... " ;
		String queryString =

				"Prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + " CONSTRUCT {?s ?p ?o}" + "WHERE {"
						+ "  ?ss rdf:subject ?s." + " ?ss rdf:predicate ?p." + " ?ss rdf:object ?o." + "      }";

		Query query = QueryFactory.create(queryString);
		StringWriter modelAsString = new StringWriter();

		try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {

			Model results = qexec.execConstruct();
			// to separate subject, predcate,object
			StmtIterator statementIter = results.listStatements();

			Statement s;
			Resource subject;
			Property predicate;
			RDFNode object;
			while (statementIter.hasNext()) {
				s = statementIter.nextStatement();
				subject = s.getSubject();
				predicate = s.getPredicate();
				object = s.getObject();

				// System.out.println("List of Subject ");
				// System.out.println(subject.toString());
				sub.add(subject.getLocalName());
				// System.out.println("List of predicates ");
				// System.out.println(" " + predicate.toString() + " ");
				pred.add(predicate.getLocalName());

				// System.out.println("List of objects ");
				if (object instanceof Resource && object.toString().contains("/")) {
					obj.add(object.asResource().getLocalName());

				} else if (object instanceof Literal) {

					obj.add(object.toString());
				}

			}
			// System.out.println("List of Subjects in OKE files......... ");
			// System.out.println(sub);
			// System.out.println("List of predicates in OKE files......... ");
			// System.out.println(pred);
			// System.out.println("List of Objects in OKE files......... "); ;
			// System.out.println(obj);
			// System.out.println(" ...........");
			// results.write(modelAsString,"TTL");
			results.write(modelAsString, "N-TRIPLE");
			sparql_query_result = modelAsString.toString();
		}

		System.out.println(files[i].getName() + "  Result after pasing sparql query.......");
		sparqueryList.add(sparql_query_result);
		System.out.println(modelAsString.toString());
		return sparql_query_result;
	}

	public void trainingFileWriter(String training_data) {

		// List<String> sentences = new ArrayList<String>();

		FileWriter fileWriter;
		try {
			int x = 0;
			fileWriter = new FileWriter(file2);
			BufferedWriter bw2 = new BufferedWriter(fileWriter);
			if (x == 0) {
				bw2.write("@relation DataExtraction2");
				bw2.newLine();
				bw2.write("@ATTRIBUTE Sentence	string");
				bw2.newLine();
				bw2.write("@ATTRIBUTE Foxoutput	string");
				bw2.newLine();
				bw2.newLine();
				bw2.write("@ATTRIBUTE OPENIEoutput	string");
				bw2.newLine();
				bw2.write("@data");
				bw2.newLine();
				x++;
			}

			bw2.append(training_data);

			bw2.close();

			fileWriter.close();
			System.out.println("File Created and closed");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// write in train data file for weka

	}

	public String fox_response_processing(String fox_response_string, int i) throws IOException {
		// create new file for for extractors response
		String filename = "ExtractorResponse//FoxResponse//" + "Fox_" + i + ".ttl";
		File file = new File(filename);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(fox_response_string);
		writer.close();
		// create jena model for every files
		Model model = ModelFactory.createDefaultModel();
		model.read(filename);
		System.out.println("For the file " + filename);
		// Write as Turtle via model.write
		StringWriter modelAsString = new StringWriter();
		model.write(modelAsString, "N-TRIPLE");
		String fox_filtered_response = modelAsString.toString();
		StmtIterator statementIter = model.listStatements();

		Statement s;
		Resource subject;
		Property predicate;
		RDFNode object;

		while (statementIter.hasNext()) {
			s = statementIter.nextStatement();
			subject = s.getSubject();
			predicate = s.getPredicate();
			object = s.getObject();

			// System.out.println("List of Subject ");
			// System.out.println(subject.toString());
			sub_fox.add(subject.getLocalName());
			// System.out.println("List of predicates ");
			// System.out.println(" " + predicate.toString() + " ");
			pred_fox.add(predicate.getLocalName());

			// System.out.println("List of objects ");
			if (object instanceof Resource && object.toString().contains("/")) {
				obj_fox.add(object.asResource().getLocalName());

			} else if (object instanceof Literal) {

				obj_fox.add(object.toString());
			}

		}

		// System.out.println("List of Subjects......... ");
		// System.out.println(sub_fox);
		// System.out.println("List of predicates......... ");
		// System.out.println(pred_fox);
		// System.out.println("List of Objects......... ");
		//
		// System.out.println(obj_fox);
		// System.out.println(" ...........");

		// Create Files

		return fox_filtered_response;

	}

	public String openIE_response_processing(String openIE_response_string, int i) throws IOException {
		// create new file for for extractors response
		String filename = "ExtractorResponse//openIEResponse//" + "openIE_" + i + ".ttl";
		File file = new File(filename);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(openIE_response_string);
		writer.close();
		// create jena model for every files
		Model model = ModelFactory.createDefaultModel();
		model.read(filename);
		System.out.println("For the file " + filename);
		// Write as Turtle via model.write
		StringWriter modelAsString = new StringWriter();
		model.write(modelAsString, "N-TRIPLE");
		String openIE_filtered_response = modelAsString.toString();
		StmtIterator statementIter = model.listStatements();
		Statement s;
		Resource subject;
		Property predicate;
		RDFNode object;
		while (statementIter.hasNext()) {
			s = statementIter.nextStatement();
			subject = s.getSubject();
			predicate = s.getPredicate();
			object = s.getObject();

			// System.out.println("List of Subject ");
			// System.out.println(subject.toString());
			sub_openIE.add(subject.getLocalName());
			// System.out.println("List of predicates ");
			// System.out.println(" " + predicate.toString() + " ");
			pred_openIE.add(predicate.getLocalName());

			// System.out.println("List of objects ");
			if (object instanceof Resource && object.toString().contains("/")) {
				obj_openIE.add(object.asResource().getLocalName());

			} else if (object instanceof Literal) {

				obj_openIE.add(object.toString());
			}

		}

		System.out.println("List of Subjects for OPENIE extractor.........  ");
		System.out.println(sub_openIE);
		System.out.println("List of predicates for OPENIE extractor.........  ");
		System.out.println(pred_openIE);
		System.out.println("List of Objects for OPENIE extractor.........  ");

		System.out.println(obj_openIE);
		System.out.println(" ...........");

		// Create Files

		return openIE_filtered_response;

	}

	public void response_Matching() {
		 sub_fox.add("Google");
		 pred_fox.add("CEO");
		 obj_fox.add("Sundar_üichai");
		 sub_fox.add("Google");
		 pred_fox.add("CEO");
		 obj_fox.add("Sundar_Pichai");
		 sub_fox.add("Google");
		 pred_fox.add("Ceo1");
		 obj_fox.add("Sundar_üichai");
		 
		//
		 sub.add("Sundar_Pichai");
		 pred.add("workfor");
		 obj.add("Google");
		System.out.println("List of Subjects in OKE files.........  ");
		System.out.println(sub);
		System.out.println("List of predicates  in OKE files.........  ");
		System.out.println(pred);
		System.out.println("List of Objects  in OKE files.........  ");
		
		System.out.println(obj);
		System.out.println(" ...........");

		System.out.println("List of Subjects.........  ");
		System.out.println(sub_fox);
		System.out.println("List of predicates.........  ");
		System.out.println(pred_fox);
		System.out.println("List of Objects.........  ");
		System.out.println(obj_fox);
		System.out.println(" ...........");

      
		int size_fox = obj_fox.size();
		int size_oke = pred.size();
		float truth = 0;
		float result = 0;

			
			for (int a = 0; a < size_oke; a++) {
			for (int b = 0; b < size_fox; b++) {
//				if (obj.get(a).equals(obj_fox.get(b)) && pred.get(a).equals(pred_fox.get(b))
//						&& sub.get(a).equals(sub_fox.get(b))) 
//				{
//					System.out.println(" same triple found");
//					truth++;
//				}
		            int triple_counter = 0;
					if (obj.get(a).equals(obj_fox.get(b)))
					{triple_counter++;}
					if (sub.get(a).equals(sub_fox.get(b)))
					{triple_counter++;}
					if (sub.get(a).equals(sub_fox.get(b)))
					{triple_counter++;}
					if(triple_counter == 0)
					{
						System.out.println("Noting matched");
						
					}
					else if(triple_counter == 1)
					{
						System.out.println("one match found");
						truth = (float) (truth + 0.33);
					}
					else if(triple_counter == 2)
					{
						System.out.println("two match found");
						truth = (float) (truth + 0.66);
					}
					else if(triple_counter == 3)
					{
						System.out.println("three  match found");
						truth = truth + 1;
						
					}
					System.out.println("Value of truth " + truth);		
			}
			result = truth/ size_fox;
			System.out.println(result);
		}
			float score = (result/size_oke) *100;
			System.out.println("Extractor score for this sentence " + score + "%");	
	

		// response similarity function will be return here

		sub_fox.clear();
		obj_fox.clear();
		pred_fox.clear();
		sub.clear();
		obj.clear();
		pred.clear();

//		System.out.println(truth + " out of total  " + total_triples + " triples found");
	}

}
