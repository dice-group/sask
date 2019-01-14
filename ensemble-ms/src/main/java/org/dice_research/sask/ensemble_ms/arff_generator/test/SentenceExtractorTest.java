package org.dice_research.sask.ensemble_ms.arff_generator.test;

//import static org.junit.Assert.*;


import org.dice_research.sask.ensemble_ms.arff_generator.SentenceExtractor;
import org.junit.Assert;
import org.junit.Test;

public class SentenceExtractorTest  {


	
	@Test
	public void testFoxResponseMatching() {
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_fox.add("Sundar_Pichai");
		test_obj.pred_fox.add("ceo");
		test_obj.obj_fox.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.foxResponseMatching();

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
			double actual_score2 =test_obj.sorokinResponseMatching();
			delta =0;
			Assert.assertEquals(actual_score2, preicted_score2, delta);

		
	}
	
	@Test
	public void testSorokinResponseMatching() {
		
		
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_sorokin.add("Sundar_Pichai");
		test_obj.pred_sorokin.add("ceo");
		test_obj.obj_sorokin.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.sorokinResponseMatching();

		System.out.println(score);
		double preicted_score = 66.67;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);
	}
	
	
	@Test
	public void testOpenIEResponseMatching() {
		
		
		SentenceExtractor test_obj = new  SentenceExtractor();
		test_obj.sub_openIE.add("Sundar_Pichai");
		test_obj.pred_openIE.add("ceo");
		test_obj.obj_openIE.add("Google");


		test_obj.sub.add("Sundar_Pichai");
		test_obj.pred.add("workfor");
		test_obj.obj.add("Google");

		double score = test_obj.openIEResponseMathing();

		System.out.println(score);
		double preicted_score = 66.67;
		double delta =0;
		Assert.assertEquals(score, preicted_score, delta);
 
	}
	
	@Test
	public void testFindMaxScore() {
		
		
		SentenceExtractor test_obj = new  SentenceExtractor();
        double sc_fox;
        double sc_openIE; 
        double sc_sorokin;
        sc_fox = 21;
       sc_openIE= 13.33;
       sc_sorokin = 33.33;
       
		int  max_index = test_obj.findMaxscore(sc_fox, sc_openIE, sc_sorokin);
      System.out.println(max_index);
       Assert.assertEquals(2, max_index);

//      Test2
       sc_fox =0;
       sc_openIE= 66.66;
       sc_sorokin = 33.33;

       int  max_index2 = test_obj.findMaxscore(sc_fox, sc_openIE, sc_sorokin);
       System.out.println(max_index2);
       Assert.assertEquals(1, max_index2);
       
//     Test3
      sc_fox =33.33;
      sc_openIE= 16.66;
      sc_sorokin = 33.33;

      int  max_index3 = test_obj.findMaxscore(sc_fox, sc_openIE, sc_sorokin);
      System.out.println(max_index);
      Assert.assertEquals(2, max_index3);
       
 //   Test4
      sc_fox =33.33;
      sc_openIE= 16.66;
      sc_sorokin = 66.66;

      int  max_index4 = test_obj.findMaxscore(sc_fox, sc_openIE, sc_sorokin);
      System.out.println(max_index);
      Assert.assertEquals(2, max_index4);

	}
	
}
