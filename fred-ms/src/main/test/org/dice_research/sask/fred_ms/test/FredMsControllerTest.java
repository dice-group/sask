package org.dice_research.sask.fred_ms.test;

import static org.junit.Assert.assertEquals;
import org.dice_research.sask.fred_ms.FredDTO;
import org.dice_research.sask.fred_ms.FredMsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(FredMsController.class)
public class FredMsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testExtract() throws Exception {

		FredDTO mockFred = new FredDTO();
		mockFred.setInput("Miles Davis was an american jazz musician.");
		String inputInJson = this.mapToJson(mockFred);

		String URI = "/extract";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
					.get(URI)
					.accept(MediaType.APPLICATION_JSON)
					.content(inputInJson)
					.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		String output = response.getContentAsString();
		String expectedOutput = "<http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#hasQuality>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_28_32_jazz>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"jazz\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"28\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"32\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NN> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_28_41_jazz+musician>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"Jazz Musician\"^^<http://www.w3.org/2001/XMLSchema#string> , \"jazz musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"28\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\r\n" + 
				"\r\n" + 
				"<http://www.w3.org/2000/01/rdf-schema#subClassOf>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#hasDeterminer>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#n> ;\r\n" + 
				"      <http://www.w3.org/2002/07/owl#equivalentClass>\r\n" + 
				"              <http://dbpedia.org/resource/Musician> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#n> ;\r\n" + 
				"      <http://www.w3.org/2002/07/owl#equivalentClass>\r\n" + 
				"              <http://dbpedia.org/resource/Jazz> .\r\n" + 
				"\r\n" + 
				"<http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#StringDocuverse> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#hasContent>\r\n" + 
				"              \"Miles_Davis was an american jazz musician.\" .\r\n" + 
				"\r\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#hasContent>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_33_41_musician>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#musician_1> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"33\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NN> .\r\n" + 
				"\r\n" + 
				"<http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1>\r\n" + 
				"      a       <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#hasDeterminer>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/quantifiers.owl#an> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_41_american+jazz+musician>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"American Jazz Musician\"^^<http://www.w3.org/2001/XMLSchema#string> , \"american jazz musician\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> , <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"41\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#Quality> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#boxerpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#a> .\r\n" + 
				"\r\n" + 
				"<http://dbpedia.org/resource/Miles_Davis>\r\n" + 
				"      a       <http://schema.org/Person> , <http://schema.org/MusicGroup> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_0_11_Miles_Davis>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"Miles_Davis\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"0\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"11\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#NNP> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_27_american>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"american\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#American> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"27\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#pennpos>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/pos.owl#JJ> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Musician> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Jazz> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#Class> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#subClassOf>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#JazzMusician> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#associatedWith>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#offset_19_32_american+jazz>\r\n" + 
				"      a       <http://www.essepuntato.it/2008/12/earmark#PointerRange> ;\r\n" + 
				"      <http://www.w3.org/2000/01/rdf-schema#label>\r\n" + 
				"              \"american jazz\"^^<http://www.w3.org/2001/XMLSchema#string> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#denotes>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#jazz_1> ;\r\n" + 
				"      <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#hasInterpretant>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazz> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#begins>\r\n" + 
				"              \"19\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#ends>\r\n" + 
				"              \"32\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\r\n" + 
				"      <http://www.essepuntato.it/2008/12/earmark#refersTo>\r\n" + 
				"              <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#docuverse> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/fred/domain.owl#Miles_davis>\r\n" + 
				"      a       <http://www.ontologydesignpatterns.org/ont/fred/domain.owl#AmericanJazzMusician> ;\r\n" + 
				"      <http://www.ontologydesignpatterns.org/ont/boxer/boxer.owl#possibleType>\r\n" + 
				"              <http://dbpedia.org/ontology/Place> ;\r\n" + 
				"      <http://www.w3.org/2002/07/owl#sameAs>\r\n" + 
				"              <http://dbpedia.org/resource/Miles_Davis> .\r\n" + 
				"\r\n" + 
				"<http://www.ontologydesignpatterns.org/ont/boxer/boxer.owl#possibleType>\r\n" + 
				"      a       <http://www.w3.org/2002/07/owl#ObjectProperty> .\r\n" + 
				"";
		
		assertEquals(expectedOutput, output);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/**
	 * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}