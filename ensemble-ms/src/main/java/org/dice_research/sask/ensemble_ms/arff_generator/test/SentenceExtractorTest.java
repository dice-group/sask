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
		double preicted_score = 66.67;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);
		
        System.out.println("........................................");
       
//		test 2
		 test_obj.sub_sorokin.add("Google");
		 test_obj.pred_sorokin.add("CEO");
		 test_obj.obj_sorokin.add("Sundar_Pichai");
		 test_obj.sub_sorokin.add("Google");
		 test_obj.pred_sorokin.add("located");
		 test_obj.obj_sorokin.add("California");
		 test_obj.sub.add("Google");
		 test_obj.pred.add("located");
		 test_obj.obj.add("Berlin");
			double preicted_score2 = 33.33;
			double actual_score2 =test_obj.sorokin_response_Matching();
			delta =0;
			Assert.assertEquals(actual_score2, preicted_score2, delta);

		
	}
	
	@Test
	public void testSorokin_response_Matching() {
		
		
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_sorokin.add("Sundar_Pichai");
		test_obj.pred_sorokin.add("ceo");
		test_obj.obj_sorokin.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.sorokin_response_Matching();

		System.out.println(score);
		double preicted_score = 66.67;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);
	}
	
	
	@Test
	public void testOpenIE_response_Matching() {
		
		
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_openIE.add("Sundar_Pichai");
		test_obj.pred_openIE.add("ceo");
		test_obj.obj_openIE.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.openIE_response_Mathing();

		System.out.println(score);
		double preicted_score = 66.67;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);
	}
	
	
}
