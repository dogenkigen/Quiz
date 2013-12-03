package com.mlaskows.quiz.model.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class represents scoring for {@link Level}. It is XML
 * element and DB entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable
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
