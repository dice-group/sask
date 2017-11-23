/**
 * 
 */
package chatbot.core.handlers.qa;


import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import chatbot.core.handlers.*;
import javax.xml.ws.http.HTTPException;

/**
 * @author Prashanth
 *
 */
public class qa extends Handler {
	//Handle Hawk Service.
	 private static final String URL = "http://localhost:8181/simple-search?query="; //URL of Hawk Service.
	 private HttpClient client;
	 private int timeout = 7000;
	public qa() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).build();
        this.client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}
	private String createHTTPRequest(String question) {
        try {
        		System.out.println("here");
        		String[] strgs = question.split(" "); //Remove and create words instead of passing complete sentences. Follow format specified in gitthub rdocumentation
        		String query="";
        		for (int j=0; j<strgs.length; j++) {
        			query+= strgs[j] + "+";
        		}
        		query=query.substring(0 , query.length()-1);
        		System.out.println(query);
        		//query.remove(query.length()-1, query.length()) //Remove last +sign
        		//query+= "%3F";
        		String URLText= URL+query;
        		System.out.println(URLText);
            HttpPost httpPost = new HttpPost(URLText);
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query", question));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            // Error Scenario
            if(response.getStatusLine().getStatusCode() >= 400) {
                System.out.println("Error In HTTP Request");
                throw new HTTPException(response.getStatusLine().getStatusCode());
            }

            return EntityUtils.toString(response.getEntity());
        }
        catch(Exception e) {
        	//Check if we can create a logger.
        		System.out.println("Exception occured in qa.java");
            throw new InternalError();
        }
        
    }
	
	public String search(String question) throws JsonProcessingException, IOException {
		//String answer="";
		String response = createHTTPRequest(question);
		//String 
		response = output;
		//Print JSON for now.
		//System.out.println("Response Received:");
		//System.out.println(response);
		
		ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        String Text= rootNode.path("answer").path("value").toString();
        //JsonNode answers = 
        //System.out.println(rootNode.toString());
       // System.out.println("HERE" +Text );
		return Text;
		
	}
	private String output = "{\n" + 
			"  \"annotation\": [\n" + 
			"    {\n" + 
			"      \"annotations\": [\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capital\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/Capital\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalCountry\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalCoordinates\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalRegion\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalPosition\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalPlace\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalDistrict\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalMountain\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/capitalElevation\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/distanceToCapital\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/CapitalOfRegion\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/perCapitaIncomeRank\",\n" + 
			"        \"http:\\/\\/dbpedia.org\\/ontology\\/numberOfCapitalDeputies\"\n" + 
			"      ],\n" + 
			"      \"label\": \"capital\"\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"tree_full\": {\n" + 
			"    \"children\": [\n" + 
			"      \n" + 
			"    ],\n" + 
			"    \"label\": \"is\"\n" + 
			"  },\n" + 
			"  \"answer\": {\n" + 
			"    \"value\": [\n" + 
			"      {\n" + 
			"        \"thumbnail\": \"http:\\/\\/commons.wikimedia.org\\/wiki\\/Special:FilePath\\/Bust_Hadrian_Musei_Capitolini_MC817.jpg?width=300\",\n" + 
			"        \"comment\": \"Hadrian (Latin: Publius Aelius Hadrianus Augustus 24 January, 76 AD \\u2013 10 July, 138 AD) was Roman Emperor from 117 to 138. In Latin, the full imperial title of Hadrian was also rendered as Tito Ael[io] Hadriano, just as it appears in ancient epigraphic records. He re-built the Pantheon and constructed the Temple of Venus and Roma. He is also known for building Hadrian's Wall, which marked the northern limit of Roman Britain.\",\n" + 
			"        \"abstract\": \"Hadrian (Latin: Publius Aelius Hadrianus Augustus 24 January, 76 AD \\u2013 10 July, 138 AD) was Roman Emperor from 117 to 138. In Latin, the full imperial title of Hadrian was also rendered as Tito Ael[io] Hadriano, just as it appears in ancient epigraphic records. He re-built the Pantheon and constructed the Temple of Venus and Roma. He is also known for building Hadrian's Wall, which marked the northern limit of Roman Britain. Hadrian was regarded by some as a humanist and was philhellene in most of his tastes. He is regarded as one of the Five Good Emperors. Hadrian was born Publius Aelius Hadrianus to an ethnically Italian family, either in Italica near Santiponce (in modern-day Spain). His predecessor Trajan was a maternal cousin of Hadrian's father. Trajan never officially designated an heir, but according to his wife Pompeia Plotina, Trajan named Hadrian emperor immediately before his death. Trajan's wife and his friend Licinius Sura were well-disposed towards Hadrian, and he may well have owed his succession to them.During his reign, Hadrian traveled to nearly every province of the Empire. An ardent admirer of Greece, he sought to make Athens the cultural capital of the Empire and ordered the construction of many opulent temples in the city. He used his relationship with his Greek favorite Antinous to underline his philhellenism and led to the creation of one of the most popular cults of ancient times. He spent extensive amounts of his time with the military; he usually wore military attire and even dined and slept amongst the soldiers. He ordered military training and drilling to be more rigorous and even made use of false reports of attack to keep the army alert.Upon his accession to the throne, Hadrian withdrew from Trajan's conquests in Mesopotamia and Armenia, and even considered abandoning Dacia. Late in his reign he suppressed the Bar Kokhba revolt in Judaea, renaming the province Syria Palaestina. In 136 an ailing Hadrian adopted Lucius Aelius as his heir, but the latter died suddenly two years later. In 138, Hadrian resolved to adopt Antoninus Pius if he would in turn adopt Marcus Aurelius and Aelius' son Lucius Verus as his own eventual successors. Antoninus agreed, and soon afterward Hadrian died at Baiae.\",\n" + 
			"        \"URI\": \"http:\\/\\/dbpedia.org\\/resource\\/Hadrian\"\n" + 
			"      }\n" + 
			"    ]\n" + 
			"  },\n" + 
			"  \"POS_tags\": [\n" + 
			"    {\n" + 
			"      \"POS\": \"VBZ\",\n" + 
			"      \"key\": \"is\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"POS\": \"NN\",\n" + 
			"      \"key\": \"capital\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"POS\": \"ADD\",\n" + 
			"      \"key\": \"http:\\/\\/dbpedia.org\\/resource\\/Spain\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"POS\": \"WP\",\n" + 
			"      \"key\": \"What\"\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"final_sparql_base64\": \"UFJFRklYIHRleHQ6PGh0dHA6Ly9qZW5hLmFwYWNoZS5vcmcvdGV4dCM+IApTRUxFQ1QgRElTVElOQ1QgP3Byb2ogV0hFUkUgewogP3Byb2ogP3BicmlkZ2UgPGh0dHA6Ly9kYnBlZGlhLm9yZy9yZXNvdXJjZS9TcGFpbj4uIAp9CkxJTUlUIDE=\",\n" + 
			"  \"input_question\": \"What is the capital of Spain ?\",\n" + 
			"  \"tree_pruned\": {\n" + 
			"    \"children\": [\n" + 
			"      \n" + 
			"    ],\n" + 
			"    \"label\": \"is\"\n" + 
			"  },\n" + 
			"  \"pruning_messages\": [\n" + 
			"    {\n" + 
			"      \"label\": \"Number of Queries before pruning\",\n" + 
			"      \"value\": 840\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"Underdefined pruned\",\n" + 
			"      \"value\": 336\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries containing no project variable pruned\",\n" + 
			"      \"value\": 140\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries containing too many nodes as text lookup pruned\",\n" + 
			"      \"value\": 70\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries with more than one predicate between the same variables pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries with more than one type per variable pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries without connected BGP pruned\",\n" + 
			"      \"value\": 112\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries containing cycic triple pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries without bound variables pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries without text filter over existing variables pruned\",\n" + 
			"      \"value\": 28\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries with unbound triples pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries with mismatching types pruned\",\n" + 
			"      \"value\": 56\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"SPARQL queries with disjoint classes pruned\",\n" + 
			"      \"value\": 0\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"Number of Queries after pruning\",\n" + 
			"      \"value\": 98\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"Cardinality of question results\",\n" + 
			"      \"value\": 1\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"label\": \"Number of sofar executed queries\",\n" + 
			"      \"value\": 196\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"named_entities\": [\n" + 
			"    {\n" + 
			"      \"value\": \"http:\\/\\/dbpedia.org\\/resource\\/Spain\",\n" + 
			"      \"key\": \"Spain\"\n" + 
			"    }\n" + 
			"  ]\n" + 
			"}";

}
