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
 * @author Harsh Shah
 */

public class SentenceExtractor {
	

	private static File path = new File("oke data");
	private static File[] files = path.listFiles();
	static List<String> sentences = new ArrayList<String>();
	private static File file2 = new File("TrainingData\\traindata2.arff");	
	private static String sentence_data;
	static String fox_response_string = null;
	static String openie_response_string = null;
	static String sorokin_response_string = null;
	static String cedric_response_string = null;
    
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
//			 number of ttl files
//			System.out.println(files.length);
			for (int i = 0; i < 5; i++) 
			{
			se.sentence_Extracion(i);
			System.out.println("Extracted sentence From the file " + files[i].getName());	

			System.out.println(sentences.get(i));
			System.out.println("Fox extractor response for file" + files[i].getName());			
			System.out.println(fox_response_string);
			
			System.out.println("OpenIE extractor response for file " + files[i].getName());			
			System.out.println(openie_response_string);
			System.out.println("Cedric extractor response for file"  + files[i].getName());		
			System.out.println(cedric_response_string);
			System.out.println("Sorokin extractor response for file " + files[i].getName());				
			System.out.println(sorokin_response_string);
			System.out.println("Gethering Sparql Query Result................................");
			
			String squery_Result = se.responseReader(i);
			System.out.println("......................................");
			System.out.println(squery_Result);
			
			System.out.println("Training Data for file " + files[i].getName());		
			String training_data = " ' " + sentences.get(i) + " ' " + ", " 
						+ "'" + fox_response_string + " ' " 
						+ "," + " ' " + openie_response_string + " ' " 
						+ "," + " ' " + sorokin_response_string + " '  "
						+ ","+ " ' "  + cedric_response_string +   " ' " 
						+ "," + " ' "+  squery_Result + " ' ";
			
			System.out.println("------");
			System.out.println(training_data);

			System.out.println("-------------------------------------------------------------");
			se.trainingFileWriter(training_data);

			}
			
	}



 public void sentence_Extracion(int i) 
 	{
			if (files[i].isFile()) {
				String sentence = readLineByLine(files[i].toString());
				int x = sentence.indexOf("nif:isString   ");
				int start = x + 17;
				int end = sentence.indexOf("\" .");
				end--;
				sentence_data = sentence.substring(start, end);
				// System.out.println(sentence_data);
				sentences.add(sentence_data);					
//			Extractors responses
//
//				Map<String, Integer> port_vs_extractorMap = new HashMap<String, Integer>();
//				port_vs_extractorMap.put("fox", 2222);
//				port_vs_extractorMap.put("fred", 2223);
//				port_vs_extractorMap.put("spotlight", 2224);
//				port_vs_extractorMap.put("cedric", 2225);
//				port_vs_extractorMap.put("openIE", 2226);
//				port_vs_extractorMap.put("sorookin", 2227);

				// int portNumb[] = { 2222, 2224, 2225, 2226, 2227 };
				int portNumb[] = { 2222, 2226, 2227, 2225 };


				Map<String, String> fredRespMap = new HashMap<String, String>();
				Map<String, String> spotlightRespMap = new HashMap<String, String>();
				Map<String, String> cedricRespMap = new HashMap<String, String>();
				Map<String, String> openIERespMap = new HashMap<String, String>();
				Map<String, String> sorookinRespMap = new HashMap<String, String>();

				// calling every extractors and getting outputs

				for (int j = 0; j < portNumb.length; j++) {
					//To handle url_encoding exception
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

						if (j == 0) 
						{
							
							
							List<String> fox_response_string_list = new ArrayList<String>();
							fox_response_string = response.toString();
							
							String Filtered_fox_response = fox_response_processing(fox_response_string, i);
							fox_response_string_list.add(Filtered_fox_response);
							System.out.println(Filtered_fox_response);
						}

						else if (j == 1) 
						{
							fredRespMap.put(sentences.get(i), response.toString());
							ArrayList<String> openie_response_string_list = new ArrayList<String>();
							openie_response_string = response.toString();
							openie_response_string_list.add(openie_response_string);
						}

						else if (j == 2) 
						{
							spotlightRespMap.put(sentences.get(i), response.toString());
							ArrayList<String> sorokin_response_string_list = new ArrayList<String>();
							sorokin_response_string = response.toString();
							sorokin_response_string_list.add(sorokin_response_string);
						}
						else if (j == 3) 
						{
							cedricRespMap.put(sentences.get(i), response.toString());
							ArrayList<String> cedric_response_string_list = new ArrayList<String>();
							cedric_response_string = response.toString();
							cedric_response_string_list.add(cedric_response_string);								

						}
						else if (j == 4) {
							openIERespMap.put(sentences.get(i), response.toString());
						}
						else if (j == 5) 
						{
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





	public static String readLineByLine(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
	public String responseReader(int i)
	{
		String sparql_query_result = null;
		BasicConfigurator.configure();
      List<String> sparqueryList = new ArrayList<String>();
	
        // create an empty model
//		for (int i = 0; i < 3; i++) 
		
		 final String inputFileName  = files[i].toString();
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open( inputFileName );
        if (in == null)
        {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        
        // read the RDF/XML file
//        read() method call is the URI which will be used for resolving relative URI's
//       model.read(in, "TURTLE");
       try
        {
        model.read(inputFileName) ;
        }
       catch(RiotException ro)
        {      	
       	ro.getMessage();
        }
//        catch(MalformedURLException mur)
//        {
//   
//        	mur.getMessage();
//        }
        
//      to run in command line sparql.bat --data=vc-db-1.rdf --query=q1.rq               
        // write it to standard outString queryString = " .... " ;
		String queryString = 
				
			    "Prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			    + " CONSTRUCT {?s ?p ?o}" +
			    "WHERE {" +
			    "  ?ss rdf:subject ?s." +" ?ss rdf:predicate ?p." +" ?ss rdf:object ?o." +
			    "      }";
			 
        Query query = QueryFactory.create(queryString) ;
        StringWriter modelAsString = new StringWriter();
         
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) 
        {
        	
         Model results = qexec.execConstruct() ;
         results.write(modelAsString,"TTL");
         sparql_query_result  = modelAsString.toString();
         
   
          
          
//          System.out.println(results.toString());


//          while(iter.hasNext())
//          {
//        	  squery.append(iter.next().toString()); 
////              squery.append(System.getProperty("line.separator"));
//          }
        }	
        System.out.println(files[i].getName() + "  Result after pasing sparql query.......");
		sparqueryList.add(sparql_query_result);
	     System.out.println(modelAsString.toString());
		return sparql_query_result;
	}

	public void trainingFileWriter(String training_data)
	{

//		List<String> sentences = new ArrayList<String>();

		FileWriter fileWriter;
		try 
		{
			int x =0;
			fileWriter = new FileWriter(file2);
			BufferedWriter bw2 = new BufferedWriter(fileWriter);
			if(x == 0)
			{
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
	
			
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		// write in train data file for weka
		
	
		}

	public String fox_response_processing(String fox_response_string, int i) throws IOException 
	{
//		create new file for for extractors response
		String filename= "ExtractorResponse//FoxResponse//" +"Fox_" + i + ".ttl";
		File file = new File(filename); 
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    writer.write(fox_response_string);
	    writer.close();
//	    create jena model for every files
	    Model model = ModelFactory.createDefaultModel();
	    model.read(filename);
	    System.out.println("For the file " + filename);
	    // Write as Turtle via model.write
        StringWriter modelAsString = new StringWriter();
	    model.write(modelAsString, "N-TRIPLE") ;
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
	    	  
	    	System.out.println("List of Subject  ");
	    	  System.out.println(subject.toString());
	    		System.out.println("List of predicates   ");
	            System.out.println(" " + predicate.toString() + " ");

	            	System.out.println("List ofobjects    ");
	                System.out.println(object.toString());
	                


	                


	            System.out.println(" .");
	    		
////	    	  keyWords.add(predicate.getLocalName());
//
//	    	  // local name of (non blank nodes) Resources
//	    	  if (object instanceof Resource && object.toString().contains("/")) {
////	    		keyWords.add(object.asResource().getLocalName());
//	    		  
//	    	  } else if (object instanceof Literal) {
//	    		// object is a Literal
//
//	    	  }
	    	  
	    }
	  
	    
		
//		Create Files
		
		
		
		
		


		 return fox_filtered_response;
		
		
		 

		
	}

	
	

}
