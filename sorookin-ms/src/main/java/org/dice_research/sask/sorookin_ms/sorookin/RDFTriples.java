package org.dice_research.sask.sorookin_ms.sorookin;

import java.io.StringWriter;
import org.dice_research.sask.sorookin_ms.sorookin.Triple;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.URIref;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class RDFTriples {
	/**
	 * Creates NTRIPLES from the list of natural language triples.
	 * 
	 * @param A
	 *            list of triples..
	 * @return Jena model containing the RDF data in N-Triples format.
	 */
	public static StringWriter generateRDFTriples(List<Triple> triples) {
		String NS = "http://example.org/";
		StringWriter modelAsString = new StringWriter();
		Model model = ModelFactory.createDefaultModel();
		for (Triple t : triples) {
			Property subject = model.createProperty(NS + URIref.encode(t.getSubject()));
			Property predicate = model.createProperty(NS + URIref.encode(t.getPredicate()));
			Property object = model.createProperty(NS + URIref.encode(t.getObject()));
			model.add(subject, predicate, object);
		}

		RDFDataMgr.write(modelAsString, model, Lang.NTRIPLES);
		return modelAsString;
	}

}
