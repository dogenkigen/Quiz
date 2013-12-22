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
import com.mlaskows.quiz.model.dao.ScoringDao;

/**
 * Class represents scoring for {@link Level}. It is XML
 * element and DB entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable(daoClass = ScoringDao.class)
public class Scoring {

	public static final String LEVEL_ID_FIELD_NAME = "level_id";

	/** Scoring id. */
	@DatabaseField(generatedId = true)
	private int id;

	/** Points granted for correct answer. */
	@Text(required = true)
	@DatabaseField(canBeNull = false)
	private int value;

	/** Points subtracted after using a tip. */
	@Attribute
	@DatabaseField
	private int usingTip;

	/** Points subtracted after unsuccessful attempt */
	@Attribute
	@DatabaseField
	private int unsuccessfulAttempt;

	/** The level to which exercise belongs. */
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = LEVEL_ID_FIELD_NAME)
	private Level level;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getUsingTip() {
		return usingTip;
	}

	public void setUsingTip(int usingTip) {
		this.usingTip = usingTip;
	}

	public int getUnsuccessfulAttempt() {
		return unsuccessfulAttempt;
	}

	public void setUnsuccessfulAttempt(int unsuccessfulAttempt) {
		this.unsuccessfulAttempt = unsuccessfulAttempt;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "Scoring [id=" + id + ", value=" + value + ", usingTip=" + usingTip + ", unsuccessfulAttempt="
				+ unsuccessfulAttempt + "]";
	}

}
