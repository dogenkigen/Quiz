package com.mlaskows.quiz.model.entities;

import java.util.Collection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@Root
@DatabaseTable
public class Exercise {

	public static final String LEVEL_ID_FIELD_NAME = "level_id";
	public static final String QUESTION_ID_FIELD_NAME = "question_id";
	
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private boolean solved;

	@Element
	@DatabaseField
	private String hint;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = LEVEL_ID_FIELD_NAME)
	private Level level;

	@Element
	@DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = QUESTION_ID_FIELD_NAME)
	private Question question;

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
		return "ID: " + id + " (" + question.toString() + ")";
	}

}
