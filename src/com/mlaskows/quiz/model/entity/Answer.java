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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.mlaskows.quiz.model.dao.AnswerDao;

/**
 * Exercise answer. It is XML element and DB entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable(daoClass = AnswerDao.class)
public class Answer {

	/** Name of exercise id field. */
	public static final String EXERCISE_ID_FIELD_NAME = "exercise_id";

	/** Answer id. */
	@DatabaseField(generatedId = true)
	private int id;

	/** Answer value. */
	@Text
	@DatabaseField(canBeNull = false)
	private String value;

	/** Determines if answer is valid. */
	@Attribute(required = false)
	@DatabaseField
	private boolean valid;

	/** The exercise to which answer belongs. */
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = EXERCISE_ID_FIELD_NAME)
	private Exercise exercise;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", value=" + value + ", valid=" + valid + ", exercise=" + exercise + "]";
	}

}
