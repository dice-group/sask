package chatbot.core.handlers.automaticworkflow;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This class represents a jackson serializer for the workflow class.
 * 
 * @author Juzer Kanchwala
 *
 */

public class WorkflowSerializer extends JsonSerializer<Workflow>  {

	@Override
	public void serialize(Workflow value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		
		gen.writeStartObject();
		writeOperators(gen, value);
		writeLinks(gen, value);
		gen.writeEndObject();
	}

	private void writeLinks(JsonGenerator gen, Workflow value) throws IOException {
		
		gen.writeFieldName(Workflow.KEY_LINKS);
		gen.writeStartObject();
		
		List<Link> links = value.getLinks();
		Iterator<Link> itr = links.iterator();
		int index = 0;
		while (itr.hasNext()) {
			Link link = itr.next();
			gen.writeFieldName(String.valueOf(index++));
			gen.writeStartObject();
			gen.writeStringField(Link.KEY_FROM_CONNECTOR, link.getFromConnector());
			gen.writeStringField(Link.KEY_TO_CONNECTOR, link.getToConnector());
			gen.writeStringField(Link.KEY_FROM_OPERATOR, link.getFromOperator());
			gen.writeStringField(Link.KEY_TO_OPERATOR, link.getToOperator());
			gen.writeEndObject();
		}
		
		gen.writeEndObject();
		
	}

	private void writeOperators(JsonGenerator gen, Workflow value) throws IOException {

	    gen.writeFieldName(Workflow.KEY_OPERATORS);
	    gen.writeStartObject();
	    
		Map<String, Operator> operators = value.getOperators();
		Iterator<String> itr = operators.keySet().iterator();
		while (itr.hasNext()) {
			String operatorName = itr.next();
			Operator operator =  operators.get(operatorName);
			writeNodes(gen, operatorName, operator);
		}
		
		gen.writeEndObject();
		
	}

	private void writeNodes(JsonGenerator gen, String operatorName, Operator operator) throws IOException {		
		
		gen.writeFieldName(operatorName);
		gen.writeStartObject();
		
		gen.writeFieldName("properties");
		gen.writeStartObject();
		
		gen.writeStringField(Operator.KEY_ID, operator.getId());
		gen.writeStringField(Operator.KEY_CONTENT, operator.getContent());
		gen.writeStringField(Operator.KEY_TYPE, operator.getType());
		writeIO(gen, Operator.KEY_INPUTS, operator.getInputs());
		writeIO(gen, Operator.KEY_OUTPUTS, operator.getOutputs());
		
		gen.writeEndObject();
		
		gen.writeEndObject();
		
	}

	private void writeIO(JsonGenerator gen, String fieldName, Map<String, String> io) throws IOException {

		gen.writeFieldName(fieldName);
		gen.writeStartObject();
		Iterator<String> itr = io.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			gen.writeFieldName(key);
			gen.writeStartObject();
			gen.writeStringField(Operator.KEY_LABEL, io.get(key));
			gen.writeEndObject();
		}
		gen.writeEndObject();
		
	}

}
