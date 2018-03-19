package org.dice_research.sask.executer_ms.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class represents a jackson deserializer for the workflow class.
 * 
 * @author Kevin Haack
 *
 */
public class WorkflowDeserializer extends JsonDeserializer<Workflow> {

	@Override
	public Workflow deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode root = codec.readTree(jsonParser);

		return parse(root);
	}

	/**
	 * Parse the workflow.
	 * 
	 * @param root
	 * @return The workflow.
	 */
	protected Workflow parse(JsonNode root) {
		Workflow workflow = new Workflow();

		workflow.setLinks(parseLinks(root));
		workflow.setOperators(parseOperators(root));

		return workflow;
	}

	/**
	 * Parse the link nodes.
	 * 
	 * @param root
	 * @return The links.
	 */
	protected List<Link> parseLinks(JsonNode root) {
		List<Link> links = new ArrayList<>();

		if (root.has(Workflow.KEY_LINKS)) {
			JsonNode node = root.get(Workflow.KEY_LINKS);

			if (node.isContainerNode()) {
				for (final JsonNode sub : node) {
					Link link = parseLink(sub);
					if (null != link) {
						links.add(link);
					}
				}
			}
		}

		return links;
	}

	/**
	 * Parse the passed link.
	 * 
	 * @param root
	 * @return The link
	 */
	protected Link parseLink(JsonNode node) {
		if (!node.has(Link.KEY_FROM_CONNECTOR) || !node.has(Link.KEY_FROM_OPERATOR) || !node.has(Link.KEY_TO_CONNECTOR) || !node.has(Link.KEY_TO_OPERATOR)) {
			return null;
		}

		JsonNode fromConnector = node.get(Link.KEY_FROM_CONNECTOR);
		JsonNode fromOperator = node.get(Link.KEY_FROM_OPERATOR);
		JsonNode toConnector = node.get(Link.KEY_TO_CONNECTOR);
		JsonNode toOperator = node.get(Link.KEY_TO_OPERATOR);

		if (!fromConnector.isTextual() || !fromOperator.isTextual() || !toConnector.isTextual() || !toOperator.isTextual()) {
			return null;
		}

		Link link = new Link();
		link.setFromConnector(fromConnector.textValue());
		link.setFromOperator(fromOperator.textValue());
		link.setToConnector(toConnector.textValue());
		link.setToOperator(toOperator.textValue());

		return link;
	}

	/**
	 * Parse the operator nodes.
	 * 
	 * @param root
	 * @return The operators nodes.
	 */
	protected Map<String, Operator> parseOperators(JsonNode root) {
		Map<String, Operator> operators = new HashMap<>();

		if (root.has(Workflow.KEY_OPERATORS)) {
			JsonNode node = root.get(Workflow.KEY_OPERATORS);

			if (node.isObject()) {
				for (final JsonNode sub : node) {
					Operator operator = parseOperator(sub);
					if (null != operator) {
						operators.put(operator.getId(), operator);
					}
				}
			}
		}

		return operators;
	}

	/**
	 * Parse the passed operator.
	 * 
	 * @param node
	 * @return The Operator.
	 */
	protected Operator parseOperator(JsonNode node) {
		if (!node.has(Operator.KEY_PROPERTIES)) {
			return null;
		}

		JsonNode properties = node.get(Operator.KEY_PROPERTIES);

		if (!properties.isObject()) {
			return null;
		}

		if (!properties.has(Operator.KEY_CONTENT) || !properties.has(Operator.KEY_ID) || !properties.has(Operator.KEY_TYPE)) {
			return null;
		}

		JsonNode content = properties.get(Operator.KEY_CONTENT);
		JsonNode id = properties.get(Operator.KEY_ID);
		JsonNode type = properties.get(Operator.KEY_TYPE);

		if (!content.isTextual() || !id.isTextual() || !type.isTextual()) {
			return null;
		}

		Operator operator = new Operator();
		operator.setContent(content.textValue());
		operator.setId(id.textValue());
		operator.setType(type.textValue());

		// outputs
		if (properties.has(Operator.KEY_OUTPUTS)) {
			operator.setOutputs(parseIOs(properties.get(Operator.KEY_OUTPUTS)));
		}

		// inputs
		if (properties.has(Operator.KEY_INPUTS)) {
			operator.setInputs(parseIOs(properties.get(Operator.KEY_INPUTS)));
		}

		return operator;
	}

	/**
	 * Parse the out and inputs.
	 * 
	 * @param object
	 * @return The out or inputs.
	 */
	protected Map<String, String> parseIOs(JsonNode object) {
		Map<String, String> ios = new HashMap<String, String>();

		Iterator<String> keys = object.fieldNames();
		while (keys.hasNext()) {
			String key = keys.next();
			JsonNode out = object.get(key);

			if (out.isObject() && out.has(Operator.KEY_LABEL)) {
				JsonNode label = out.get(Operator.KEY_LABEL);

				if (label.isTextual()) {
					ios.put(key, label.textValue());
				}
			}
		}

		return ios;
	}
}
