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

package com.mlaskows.quiz.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.mlaskows.quiz.model.dao.LevelDao;

/**
 * Quiz level. It is XML element and DB entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable(daoClass = LevelDao.class)
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

	/** Is level unlocked. */
	@DatabaseField(canBeNull = false)
	private boolean unlocked;

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

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	/**
	 * Returns next unsolved exercise in cycle for specified
	 * id.
	 * 
	 * @param id
	 *            previous exercise id
	 * @param forward
	 *            search forward if true, backward if false
	 * @return next unsolved exercise
	 */
	public Exercise getUnsolvedInCycle(int id, boolean forward) {
		if (getExercises() == null || getExercises().isEmpty()) {
			return null;
		}
		List<Exercise> exercises = null;
		if (forward) {
			exercises = new ArrayList<Exercise>(getExercises());
		} else {
			exercises = Lists.reverse(new ArrayList<Exercise>(getExercises()));
		}
		Iterator<Exercise> ie = Iterators.cycle(exercises);
		int count = 0;
		boolean isAfterSpecified = false;
		// Iterate over cycle.
		while (count <= exercises.size() + id) {
			Exercise e = ie.next();
			/* Search for next unsolved exercise only if
			 * specified by method parameter was reached
			 * or if parameter equals 0.
			 */
			if ((isAfterSpecified || id == 0) && !e.isSolved()) {
				return e;
			}
			/* Exercise specified by method parameter is 
			 * reached. Now next exercise can be searched. 
			 */
			if (id == e.getId()) {
				isAfterSpecified = true;
			}
			count++;
		}
		return null;
	}

	/**
	 * Calculates progress.
	 * 
	 * @return level progress
	 */
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
		return "Level [id=" + id + ", name=" + name + ", scoring=" + scoring + ", exercises=" + exercises
				+ ", unlocked=" + unlocked + "]";
	}

}
