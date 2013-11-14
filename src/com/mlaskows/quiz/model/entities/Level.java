package com.mlaskows.quiz.model.entities;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@Root
@DatabaseTable
public class Level {

	@Attribute
	@DatabaseField(id = true)
	private int id;

	@Attribute
	@DatabaseField
	private String name;

	@ElementList
	@ForeignCollectionField
	private Collection<Exercise> exercises;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Collection<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Collection<Exercise> exercises) {
		this.exercises = exercises;
	}

	@Override
	public String toString() {
		return name;
	}

}
