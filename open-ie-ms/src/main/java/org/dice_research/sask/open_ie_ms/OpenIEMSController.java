package org.dice_research.sask.open_ie_ms;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.Properties;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.URIref;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

@RestController
public class OpenIEMSController {
	/**
	 * The logger.
	 */
	protected Logger logger = Logger.getLogger(OpenIEMSController.class.getName());

	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		this.logger.info("OpenIE-microservice extract() invoked");
		OpenIEDTO openIE = new OpenIEDTO();
		openIE.setText(input);
		return extract(openIE);
	}
	
	

	public String extract(OpenIEDTO openIE) {
		logger.info("OpenIE-microservice extract invoked");

		if (openIE == null || openIE.getText() == null || (openIE.getText()
                .trim()
                .isEmpty())) {
			throw new IllegalArgumentException("No input");
		}

		// Create the Stanford CoreNLP pipeline
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
	    StringWriter modelAsString = new StringWriter();

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    String NS = "http://example.org/";

	    Model model = ModelFactory.createDefaultModel();
	    model.setNsPrefix( "", NS );
	    model.setNsPrefix( "rdf", RDF.getURI() );

	    // Annotate an example document.
	    
	    Annotation doc = new Annotation(openIE.getText()); 
	    pipeline.annotate(doc);
	    Collection<RelationTriple> triples;
	    // Loop over sentences in the document
	    for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
	      // Get the OpenIE triples for the sentence
	      triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
	    
	      
	     for (RelationTriple triple : triples) {
		
	   	  Resource statement = model.createResource();
	         Resource subject = model.createResource().addProperty( RDFS.label, triple.subjectLemmaGloss() );
	          Property predicate = model.createProperty( NS+URIref.encode( triple.relationLemmaGloss() ));
	         Resource object = model.createResource().addProperty( RDFS.label, triple.objectLemmaGloss() );
              statement.addProperty( RDF.subject, subject );
	          statement.addProperty( RDF.predicate, predicate );
	          statement.addProperty( RDF.object, object );

	      }
	     
	    }

	    RDFDataMgr.write( modelAsString, model, Lang.NTRIPLES );
		return modelAsString.toString();
	}
	

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("OpenIE-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("OpenIE-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
