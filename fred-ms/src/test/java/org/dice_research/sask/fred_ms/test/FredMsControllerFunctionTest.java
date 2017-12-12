package org.dice_research.sask.fred_ms.test;

import static org.junit.Assert.assertEquals;
import org.dice_research.sask.fred_ms.FredDTO;
import org.dice_research.sask.fred_ms.FredMsController;
import org.junit.Test;

/**
 * 
 * @author Andre
 * This class just do a functionality test
 */
public class FredMsControllerFunctionTest {

	@Test
	public void testExtract() throws Exception {

		FredMsController mockController = new FredMsController();
		FredDTO mockFred = new FredDTO();
		mockFred.setInput("Miles Davis was an american jazz musician");
		String realOutput = mockController.extract(mockFred).trim();
		String expectedOutput = "<http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#hasQuality>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_28_32_jazz>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"jazz\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"28\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"32\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NN> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_28_41_jazz+musician>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"Jazz Musician\"^^<http://www.w3.org/2001/XMLSchema#string> , \"jazz musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"28\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\n" + 
				"\n" + 
				"<http://www.w3.org/2000/01/rdf-schema#subClassOf>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#hasDeterminer>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#n> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#n> ;\n" + 
				"      <http://www.w3.org/2002/07/owl#equivalentClass>\n" + 
				"              <http://dbpedia.org/resource/Jazz> .\n" + 
				"\n" + 
				"<http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#StringDocuverse> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#hasContent>\n" + 
				"              \"Miles_Davis was an american jazz musician\" .\n" + 
				"\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#hasContent>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_33_41_musician>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#musician_1> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"33\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NN> .\n" + 
				"\n" + 
				"<http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1>\n" + 
				"      a       <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#hasDeterminer>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#an> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_41_american+jazz+musician>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"American Jazz Musician\"^^<http://www.w3.org/2001/XMLSchema#string> , \"american jazz musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> , <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#Quality> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#a> .\n" + 
				"\n" + 
				"<http://dbpedia.org/resource/Miles_Davis>\n" + 
				"      a       <http://schema.org/Person> , <http://schema.org/MusicGroup> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_0_11_Miles_Davis>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"Miles_Davis\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"0\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"11\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NNP> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_27_american>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"american\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"27\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#JJ> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_32_american+jazz>\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\n" + 
				"              \"american jazz\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1> ;\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\n" + 
				"              \"32\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis>\n" + 
				"      a       <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/boxer/boxer.owl#possibleType>\n" + 
				"              <http://dbpedia.org/ontology/Place> ;\n" + 
				"      =       <http://dbpedia.org/resource/Miles_Davis> .\n" + 
				"\n" + 
				"<http://www.ontologydesignpatterns.org/ont/boxer/boxer.owl#possibleType>\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .";
		assertEquals(expectedOutput, realOutput);
		
		
	}
}