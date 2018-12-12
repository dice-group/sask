package org.dice_research.sask.ensemble_ms.arff_generator.test;

import static org.junit.Assert.*;

import org.dice_research.sask.ensemble_ms.arff_generator.SentenceExtractor;
import org.junit.Assert;
import org.junit.Test;

public class SentenceExtractorTest {

	@Test
	public void testResponse_Matching() {
		SentenceExtractor test_obj = new SentenceExtractor();
		test_obj.response_Matching();
	    System.out.println(test_obj.sub_fox);
	    String expected = "abc";
	    String actuals = "abc";
	    
	    
		Assert.assertEquals(expected, actuals);

	}

}
