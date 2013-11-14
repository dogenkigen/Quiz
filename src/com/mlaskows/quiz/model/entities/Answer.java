package com.mlaskows.quiz.model.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@Root
@DatabaseTable
public class Answer {

	public static final String EXERCISE_ID_FIELD_NAME = "exercise_id";

	@DatabaseField(generatedId = true)
	private int id;

	@Attribute(required = true)
	@DatabaseField(canBeNull = false)
	private InputOutputType type;

	@Text
	@DatabaseField(canBeNull = false)
	private String value;

	@Attribute(required = false)
	@DatabaseField
	private boolean valid;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = EXERCISE_ID_FIELD_NAME)
	private Exercise exercise;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InputOutputType getType() {
		return type;
	}

	public void setType(InputOutputType type) {
		this.type = type;
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
		return value;
	}

}
