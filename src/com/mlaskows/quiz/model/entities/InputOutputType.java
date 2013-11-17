package com.mlaskows.quiz.model.entities;

/**
 * Type of {@link Question} or {@link Answer}.
 * 
 * @author Maciej Laskowski
 * 
 */
public enum InputOutputType {

	/**
	 * Displayed text.
	 */
	TEXT,

	/**
	 * Input text field. For {@link Answer} only.
	 */
	TEXT_FIELD,

	/**
	 * Image.
	 */
	IMAGE
}
