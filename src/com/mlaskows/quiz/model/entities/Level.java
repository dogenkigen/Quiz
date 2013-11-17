package com.mlaskows.quiz.model.entities;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
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

	/** Level id. */
	@Attribute
	@DatabaseField(id = true)
	private int id;

	/** Level name. */
	@Attribute
	@DatabaseField
	private String name;

	/** Exercises list. */
	@ElementList
	@ForeignCollectionField
	private Collection<Exercise> exercises;

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

	public Collection<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Collection<Exercise> exercises) {
		this.exercises = exercises;
	}

	public int getProgress() {
		int ec = exercises.size();
		int pec = 0;
		for (Exercise exercise : exercises) {
			if (exercise.isSolved()) {
				pec++;
			}
		}
		return ((int) (pec / ec)) * 100;
	}

	@Override
	public String toString() {
		return "Level [id=" + id + ", name=" + name + "]";
	}

}
