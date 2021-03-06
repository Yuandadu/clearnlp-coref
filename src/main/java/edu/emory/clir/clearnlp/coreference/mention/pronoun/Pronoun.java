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
package edu.emory.clir.clearnlp.coreference.mention.pronoun;

import java.io.Serializable;

import edu.emory.clir.clearnlp.coreference.mention.EnglishMention;
import edu.emory.clir.clearnlp.coreference.type.EntityType;
import edu.emory.clir.clearnlp.coreference.type.GenderType;
import edu.emory.clir.clearnlp.coreference.type.NumberType;
import edu.emory.clir.clearnlp.coreference.type.PronounType;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	May 11, 2015
 */
public class Pronoun implements Serializable{
	private static final long serialVersionUID = 530671380338699884L;
	
	public String wordFrom;
	public EntityType e_type;
	public GenderType g_type;
	public NumberType  n_type;
	public PronounType p_type;
	
	public Pronoun(String s){
		wordFrom = s;
		e_type = EntityType.PRONOUN;
		g_type = GenderType.UNKNOWN;
		n_type = NumberType.UNKNOWN;
		p_type = PronounType.UNKNOWN;
	}
	
	public Pronoun(String s, EntityType e, GenderType g, NumberType n){
		wordFrom = s;
		e_type = e;
		g_type = g;
		n_type = n;
		p_type = PronounType.UNKNOWN;
	}
	
	public Pronoun(String s, String e, String g, String n){
		wordFrom = s;
		e_type = EntityType.valueOf(e);
		g_type = GenderType.valueOf(g);
		n_type = NumberType.valueOf(n);
		p_type = PronounType.UNKNOWN;
	}
	
	public Pronoun(String s, EntityType e, GenderType g, NumberType n, PronounType p){
		wordFrom = s;
		e_type = e;
		g_type = g;
		n_type = n;
		p_type = p;
	}
	
	public Pronoun(String s, String e, String g, String n, String p){
		wordFrom = s;
		e_type = EntityType.valueOf(e);
		g_type = GenderType.valueOf(g);
		n_type = NumberType.valueOf(n);
		p_type = PronounType.valueOf(p);
	}
	
	public EnglishMention toMention(int t_id, DEPTree tree, DEPNode node){
		EnglishMention mention = new EnglishMention(t_id, tree, node);
		mention.setEntityType(e_type);
		mention.setGenderType(g_type);
		mention.setNumberType(n_type);
		mention.setPronounType(p_type);
		return mention;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(wordFrom);
		sb.append('\t');	sb.append(e_type);
		sb.append('\t');	sb.append(g_type);
		sb.append('\t');	sb.append(n_type);
		sb.append('\t');	sb.append(p_type);
		return sb.toString();
	}
}
