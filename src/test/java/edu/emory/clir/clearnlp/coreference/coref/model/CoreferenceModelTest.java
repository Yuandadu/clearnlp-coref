/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.clir.clearnlp.coreference.coref.model;

import java.util.List;

import org.junit.Test;

import edu.emory.clir.clearnlp.collection.pair.IntIntPair;
import edu.emory.clir.clearnlp.collection.triple.Triple;
import edu.emory.clir.clearnlp.coreference.components.CoreferenceDecoder;
import edu.emory.clir.clearnlp.coreference.components.CoreferenceTrainer;
import edu.emory.clir.clearnlp.coreference.mention.AbstractMention;
import edu.emory.clir.clearnlp.coreference.utils.reader.CoreferenceTSVReader;
import edu.emory.clir.clearnlp.coreference.utils.structures.CoreferantSet;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.IOUtils;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	Jun 11, 2015
 */
public class CoreferenceModelTest {
	public static final int iter = 10;
	public static final int labelCutoff = 0;
	public static final int featureCutoff = 0;
	public static final boolean average = true;
	public static final double alpha = 0.01;
	public static final double rho = 0.1;
	public static final double bias = 0;
	
	public static int TRN_DOCUMENT = 0, EVAL_DOCUMENT = 0, CORRECT = 0, TOTAL = 0;
	
	@Test
	public void test(){
		CoreferenceTSVReader reader = new CoreferenceTSVReader(0, 1, 2, 3, 9, 4, 5, 6, -1, -1, 10);
		List<String> trn_filePaths = FileUtils.getFileList("/Users/HenryChen/Desktop/conll-13-dummy/train", ".cnlp", true),
				 dev_filePaths = FileUtils.getFileList("/Users/HenryChen/Desktop/conll-13-dummy/development", ".cnlp", true);
		
		CoreferenceTrainer trainer = new CoreferenceTrainer(labelCutoff, featureCutoff, average, alpha, rho, bias);
		train(trainer, reader, trn_filePaths);
		
		CoreferenceDecoder decoder = new CoreferenceDecoder(trainer.getModel());
		evaluate(decoder, reader, dev_filePaths);
		
		System.out.println("\nPerformance Summary:");
		System.out.println("Traning document count: " + TRN_DOCUMENT);
		System.out.println("Evaluation document count: " + EVAL_DOCUMENT);
		System.out.printf("Result: %d/%d, %.3f%%", CORRECT, TOTAL, (double)CORRECT/TOTAL*100);
	}
	
	private void train(CoreferenceTrainer trainer, CoreferenceTSVReader reader, List<String> trn_filePaths){	
		for(String filePath : trn_filePaths){
			System.out.println("Adding document " + FileUtils.getBaseName(filePath));
			reader.open(IOUtils.createFileInputStream(filePath));			
			trainer.addDocument(reader.getGoldCoNLLDocument());
			reader.close();	TRN_DOCUMENT++;
		}
		
		System.out.println("\nTRAINING...");
		trainer.initTrainer();
		for(int i = 0; i < iter; i++){
			System.out.println("Iteration #" + i);
			trainer.trainModel();
		}
		System.out.println(".........\nDONE!");
	}
	
	private void evaluate(CoreferenceDecoder decoder, CoreferenceTSVReader reader, List<String> l_filePaths){
		System.out.println("\nDECODING/EVALUATING...");
		
		IntIntPair evalResult;	CoreferantSet prediction;
		Triple<List<DEPTree>, List<AbstractMention>, CoreferantSet> document;
		
		for(String filePath : l_filePaths){
			reader.open(IOUtils.createFileInputStream(filePath));
			document = reader.getGoldCoNLLDocument();
			reader.close();	EVAL_DOCUMENT++;
			
			System.out.print("Decoding " + FileUtils.getBaseName(filePath) + "... ");
			prediction = decoder.decode(document.o1, document.o2, false);
			
			System.out.print("Evaluating... ");
			evalResult = evaluate(document.o3, prediction);
			System.out.println("DONE.");
			
			CORRECT += evalResult.i1;
			TOTAL += evalResult.i2;
		}
	}
	
	private IntIntPair evaluate(CoreferantSet key, CoreferantSet prediction){
		int i, size = key.size(), correctCount = 0;
		
		for(i = 0; i < size; i++)
			if(key.findHead(i) == prediction.findHead(i))
				correctCount++;
		
		return new IntIntPair(correctCount, size);
	}
}