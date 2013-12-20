/*
 * Copyright (c) 2013, Maciej Laskowski. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact mlaskowsk@gmail.com if you need additional information
 * or have any questions.
 */

package com.mlaskows.quiz.model.entities;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Quiz level. It is XML element and DB entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable
public class Level {

	private static final String SCORING_ID_FIELD_NAME = "scoring_id";

	/** Level id. */
	@Attribute
	@DatabaseField(id = true)
	private int id;

	/** Level name. */
	@Attribute
	@DatabaseField(canBeNull = false)
	private String name;

	/** Scoring element. */
	@Element
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = SCORING_ID_FIELD_NAME)
	private Scoring scoring;

	/** Exercises list. */
	@ElementList
	@ForeignCollectionField
	private Collection<Exercise> exercises;

	/** Level score. */
	@DatabaseField
	private int score;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Scoring getScoring() {
		return scoring;
	}

	public void setScoring(Scoring scoring) {
		this.scoring = scoring;
	}

	public Collection<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Collection<Exercise> exercises) {
		this.exercises = exercises;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getProgress() {
		int ec = exercises.size();
		int pec = 0;
		for (Exercise exercise : exercises) {
			if (exercise.isSolved()) {
				pec++;
			}
		}
		return (int) (((double) pec / (double) ec) * 100);
	}

	@Override
	public String toString() {
		return "Level [id=" + id + ", name=" + name + ", scoring=" + scoring + ", exercises=" + exercises + "]";
	}

}
