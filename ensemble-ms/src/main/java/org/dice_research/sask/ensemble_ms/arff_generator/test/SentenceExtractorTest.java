package org.dice_research.sask.ensemble_ms.arff_generator.test;

//import static org.junit.Assert.*;


import org.dice_research.sask.ensemble_ms.arff_generator.SentenceExtractor;
import org.junit.Assert;
import org.junit.Test;

public class SentenceExtractorTest  {


	
	@Test
	public void testFox_response_Matching() {
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_fox.add("Sundar_Pichai");
		test_obj.pred_fox.add("ceo");
		test_obj.obj_fox.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.fox_response_Matching();

		System.out.println(score);
		double preicted_score = 66.0;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);

		
		
		
		
	}

}
