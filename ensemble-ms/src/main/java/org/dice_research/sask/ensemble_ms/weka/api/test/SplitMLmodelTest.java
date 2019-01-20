package org.dice_research.sask.ensemble_ms.weka.api.test;


import org.dice_research.sask.ensemble_ms.weka.api.SplitMLmodel;

import org.junit.Test;

import weka.core.Instances;


/**
 * SplitingMlmodel class take training data for machine learning model and split data set into train and test instances by percentage
 * Train machine learning model on train set using random forest tree classifier
 * Test dataset and compare the result of the classification
 * This class test the machine learning model performance with different percentage test and write f score of class o for every test 
 * @author Harsh Shah(hjshah142)
 */


public class SplitMLmodelTest {
	SplitMLmodel splitMLmodel = new SplitMLmodel();
	Double f1;
	Double f2;
	Double f3;


	@Test
	public void test() {
		
		try {
			
			//Test 1 
			System.out.println("Test 1 set spliting percentage to 80% ");
			Instances traindata = splitMLmodel.loadMLmodel();
			Integer percentage =80;
			
			f1 = splitMLmodel.evaluateModel(traindata,percentage);
			System.out.print("Fvalue for 90% split   ");
		    System.out.println(String.format("F-score "+ "%.2f", f1));
            System.out.println("--------------------------------------");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void test2() {
	
		try {
			
			//Test 2
			System.out.println("Test 2 set spliting percentage to 60% ");
			Instances traindata = splitMLmodel.loadMLmodel();
			Integer percentage =60;
			
			f2 = splitMLmodel.evaluateModel(traindata,percentage);
			   System.out.print("Fvalue for 60% split   ");
			   System.out.println(String.format("F-score "+ "%.2f", f2));
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test3() {
		
		try {
			
			//Test 2
			System.out.println("Test 2 set spliting percentage to 90% ");
			Instances traindata = splitMLmodel.loadMLmodel();
			Integer percentage =90;
			
			f3 = splitMLmodel.evaluateModel(traindata,percentage);
	   System.out.print("Fvalue for 90% split   ");
	   System.out.println(String.format("F-score "+ "%.2f", f3));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
	

