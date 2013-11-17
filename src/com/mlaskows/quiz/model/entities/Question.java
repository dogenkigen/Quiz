package com.mlaskows.quiz.model.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class represents question. It is XML element and DB
 * entity in one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable
public class Question {

	/** Question id. */
	@DatabaseField(generatedId = true)
	private int id;

	/**
	 * Question type (it can be {@link InputOutputType.TEXT}
	 * or {@link InputOutputType.IMAGE}.
	 */
	@Attribute(required = true)
	@DatabaseField(canBeNull = false)
	private InputOutputType type;

	/** Question value. */
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
		return "Question [id=" + id + ", type=" + type + ", value=" + value + "]";
	}

}
