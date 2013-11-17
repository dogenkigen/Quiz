package com.mlaskows.quiz.model.entities;

import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class represents exercise in quiz. It contains
 * question and answers. It is XML element and DB entity in
 * one.
 * 
 * @author Maciej Laskowski
 * 
 */
@Root
@DatabaseTable
public class Exercise {

	public static final String LEVEL_ID_FIELD_NAME = "level_id";
	public static final String QUESTION_ID_FIELD_NAME = "question_id";

	/** Exercise id. */
	@DatabaseField(generatedId = true)
	private int id;

	/** Determines if exercise is solved or not. */
	@DatabaseField
	private boolean solved;

	/** Hint text. */
	@Element
	@DatabaseField
	private String hint;

	/** The level to which exercise belongs. */
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = LEVEL_ID_FIELD_NAME)
	private Level level;

	/** Question element. */
	@Element
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = QUESTION_ID_FIELD_NAME)
	private Question question;

	/** Answers list */
	@ElementList
	@ForeignCollectionField
	private Collection<Answer> answers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Collection<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Collection<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", solved=" + solved + ", hint=" + hint + ", level=" + level + ", question="
				+ question + ", answers=" + answers + "]";
	}

}
