package org.dice_research.sask.sorookin_ms;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dice_research.sask.sorookin_ms.sorookin.RDFTriples;
import org.dice_research.sask.sorookin_ms.sorookin.SorookinDTO;
import org.dice_research.sask.sorookin_ms.sorookin.SorookinResult;
import org.dice_research.sask.sorookin_ms.sorookin.Triple;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Suganya, Kevin Haack
 *
 */
@RestController
public class SorookinMsController {

	private static final String URI = "http://semanticparsing.ukp.informatik.tu-darmstadt.de:5000/relation-extraction/parse/";
	String NS = "http://example.org/";

	private Logger logger = Logger.getLogger(SorookinMsController.class.getName());

	/**
	 * Represent extraction.
	 * 
	 * @param input
	 *            The input to extract.
	 * @return The extraction result.
	 */
	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		this.logger.info("Sorookin-microservice extract() invoked");
		SorookinDTO dto = new SorookinDTO();
		dto.setInputtext(input);
		return extract(dto);
	}

	private String extract(SorookinDTO dto) {
		String input = dto.getInputtext();

		if (null == input || input == null || (input.trim().isEmpty())) {
			throw new IllegalArgumentException("No input");
		}

		try {
			logger.info("extract via " + URI);
			logger.info("extract " + input);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept", "application/json, text/javascript, */*; q=0.01");

			HttpEntity<SorookinDTO> entity = new HttpEntity<SorookinDTO>(dto, headers);

			RestTemplate restTemplate = new RestTemplate();

			String result = restTemplate.postForObject(URI, entity, String.class);

			JSONObject jsonObject = new JSONObject(result);

			SorookinResult sorookin = SorookinResultParser.parse(jsonObject);

			System.out.println(sorookin.getRelation_graph().getTokens());
			System.out.println(sorookin);

			List<Triple> triples = TripleFactory.create(sorookin.getRelation_graph());

			return RDFTriples.generateRDFTriples(triples).toString();

		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return "fail null pointer exception";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "fail";
		}

	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("sorookin-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("sorookin-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
