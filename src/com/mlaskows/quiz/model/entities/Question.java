package com.mlaskows.quiz.model.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@Root
@DatabaseTable
public class Question {

	@DatabaseField(generatedId = true)
	private int id;

	@Attribute(required = true)
	@DatabaseField(canBeNull = false)
	private InputOutputType type;

	@Text(required = true)
	@DatabaseField(canBeNull = false)
	private String value;

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

	@Override
	public String toString() {
		return value;
	}

}
