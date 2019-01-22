package org.dice_research.sask.ensemble_ms.weka.api.test;


import org.dice_research.sask.ensemble_ms.weka.api.TestMLmodel;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;

import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
 * 
 * This class built machine learning model and predict the best extractor for each sentence
 * This junit-test test and build the machine learning model using different classifiers such as RandomForest, J48,
 * -Naivebayes classifier and Support vector machine
 * For each classifier it predict the best extractor for given training data 

 * @author Harsh Shah(hjshah142)
 */




public class MLmodelTesting {
	TestMLmodel obj = new TestMLmodel();

	@Test
	public void test() {
		
//		Test using Random Forest Classifier
		
		try 
		{
			Instances traindata = obj.loadMLmodel();
			Instances testingSet = obj.loadTestData();
//			Test using Random Forest Classifier
			System.out.println("Test using Random Forest Classifier");
			Classifier classifier = new RandomForest();
			
			
			obj.evaluateMLmodel(traindata,testingSet, classifier);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	@Test
	public void test2()
	{
		
		
		
		try 
		{
			Instances traindata = obj.loadMLmodel();
			Instances testingSet = obj.loadTestData();
			Classifier classifier = new J48();
//			Test using Random Forest Classifier
			System.out.println("Test using Weka J48 Classifier");
			
			obj.evaluateMLmodel(traindata,testingSet, classifier);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test3()
	{
		
		
		
		try 
		{
			Instances traindata = obj.loadMLmodel();
			Instances testingSet = obj.loadTestData();
//			Test using Random Forest Classifier
			System.out.println("Test using Naivebayes classifier");
			
			Classifier classifier = new NaiveBayesMultinomial();
			
			obj.evaluateMLmodel(traindata,testingSet, classifier);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	@Test
	public void test4()
	{
		
		
		
		try 
		{
			Instances traindata = obj.loadMLmodel();
			Instances testingSet = obj.loadTestData();
			
//			Test using Random Forest Classifier
			System.out.println("Test using Weka SMO Classifier");
			
			
			Classifier classifier = new SMO();
			
			obj.evaluateMLmodel(traindata,testingSet, classifier);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	

}
