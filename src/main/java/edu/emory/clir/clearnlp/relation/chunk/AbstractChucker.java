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
package edu.emory.clir.clearnlp.relation.chunk;

import java.util.ArrayList;
import java.util.List;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	Jul 7, 2015
 */
public abstract class AbstractChucker {
	
	protected TLanguage l_language;
	
	public AbstractChucker(TLanguage language){
		l_language = language;
	}
	
	abstract public List<List<DEPNode>> getChunk(DEPTree tree);
	
	public List<List<DEPNode>> getChucks(List<DEPTree> trees){
		List<List<DEPNode>> list = new ArrayList<>();
		for(DEPTree tree : trees) list.addAll(getChunk(tree));
		return list;
	}
}
