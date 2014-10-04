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

package com.mlaskows.quiz.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.activity.util.ActivityStarter;
import com.mlaskows.quiz.activity.util.BundleBuilder;
import com.mlaskows.quiz.model.dao.ExerciseDao;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.dao.ScoringDao;
import com.mlaskows.quiz.model.entity.Answer;
import com.mlaskows.quiz.model.entity.Exercise;
import com.mlaskows.quiz.model.entity.Level;
import com.mlaskows.quiz.model.entity.Question;
import com.mlaskows.quiz.model.entity.Scoring;
import com.mlaskows.quiz.model.enums.AnswerType;
import com.mlaskows.quiz.model.enums.QuestionType;
import com.mlaskows.quiz.utility.ImageUtility;

/**
 * This Activity displays exercise with {@link Question} and its {@link Answer}
 * s.
 * 
 * @author Maciej Laskowski
 * 
 */
@ContentView(R.layout.activity_exercise)
public class ExerciseActivity extends FullScreenActivity {

	/** Exercise's level. */
	private Level level;

	/** DAO for Level. */
	@Inject
	private LevelDao levelDao;

	/** Displayed exercise. */
	private Exercise exercise;

	/** DAO for Exercise. */
	@Inject
	private ExerciseDao exerciseDao;

	/** Level scoring. */
	private Scoring scoring;

	/** DAO for Scoring. */
	@Inject
	private ScoringDao scoringDao;

	/** Map of answer views and answer values. */
	private Map<View, String> answerViews = new HashMap<View, String>();

	/** Next button. */
	@InjectView(R.id.buttonNext)
	private Button buttonNext;

	/** Back button. */
	@InjectView(R.id.buttonBack)
	private Button buttonBack;

	/** Answer input field. */
	@InjectView(R.id.inputAnswer)
	private EditText inputAnswer;

	/** Tip button */
	@InjectView(R.id.imgButtonTip)
	private ImageButton imgButtonTip;

	/** Device display. */
	private Display display;

	/** Application name. */
	@InjectResource(R.string.app_name)
	private String applicationName;

	/** Error string. */
	@InjectResource(R.string.error)
	private String errorString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		initButtons();

		// Get levelId from Bundle
		Bundle b = getIntent().getExtras();
		int levelId = b.getInt(getString(R.string.level_id));
		int previousExerciseId = b.getInt(getString(R.string.previous_exercise_id));
		boolean backPressed = b.getBoolean(getString(R.string.back_pressed));

		level = levelDao.queryForId(levelId);
		exercise = level.getUnsolvedInCycle(previousExerciseId, !backPressed);
		if (exercise == null) {
			// This level is solved. Show score.
			Bundle bundle = new BundleBuilder().withInteger(getString(R.string.score), level.getScore())
					.withInteger(getString(R.string.level_id), level.getId()).build();
			ActivityStarter.start(this, bundle, ScoreActivity.class);
			return;
		}
		scoring = level.getScoring();
		displayExercise(exercise);

	}

	/**
	 * Displays {@link Exercise}.
	 * 
	 * @param exercise
	 *            element do display
	 */
	private void displayExercise(Exercise exercise) {
		displayQuestion(exercise.getQuestion(), exercise.getQuestionType());
		try {
			displayAnswers(exercise.getAnswers(), exercise.getAnswerType());
		} catch (Exception e) {
			Log.e(applicationName, errorString, e);
		}
	}

	/**
	 * Displays {@link Question}.
	 * 
	 * @param question
	 *            element to display
	 * @param type
	 *            type of question
	 */
	private void displayQuestion(Question question, QuestionType type) {
		switch (type) {
		case IMAGE:
			displayQuestionImage(question.getImage());
			break;
		case TEXT:
			displayQuestionText(question.getText());
			break;
		case TEXT_AND_IMAGE:
			displayQuestionImage(question.getImage());
			displayQuestionText(question.getText());
			break;
		default:
			return;
		}
	}

	/**
	 * Displays image for question.
	 * 
	 * @param image
	 *            path to image
	 */
	@SuppressWarnings("deprecation")
	private void displayQuestionImage(String image) {
		ImageView iv = (ImageView) findViewById(R.id.imageQuestion);
		iv.setVisibility(View.VISIBLE);
		try {
			Field field = R.drawable.class.getField(image);
			Drawable drawable = getResources().getDrawable(field.getInt(null));
			TypedValue typedValue = new TypedValue();
			getResources().getValue(R.dimen.exercise_question_image_scale_factor, typedValue, true);
			iv.setImageDrawable(ImageUtility.resizeDrawable(drawable, display.getHeight() * display.getWidth()
					* typedValue.getFloat()));
		} catch (Exception e) {
			// Pass this exception
			throw new RuntimeException(e);
		}
	}

	/**
	 * Displays text for question.
	 * 
	 * @param text
	 *            question text content
	 */
	private void displayQuestionText(String text) {
		TextView tv = (TextView) findViewById(R.id.textQuestion);
		tv.setVisibility(View.VISIBLE);
		tv.setText(text);
	}

	/**
	 * Displays {@link Answer}s.
	 * 
	 * @param answers
	 *            collection of answers to display
	 * @param type
	 *            type of answers
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("deprecation")
	private void displayAnswers(Collection<Answer> answers, AnswerType type) throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		View view = null;
		List<Answer> answeList = new ArrayList<Answer>(answers);
		switch (type) {
		case IMAGE:
			view = findViewById(R.id.tableAnswerImageB);
			for (int i = 0; i < 4; i++) {
				Field field = R.id.class.getField("imgButtonAns" + i);
				ImageButton imageButton = (ImageButton) findViewById(field.getInt(null));
				field = R.drawable.class.getField(answeList.get(i).getValue());
				Drawable drawable = getResources().getDrawable(field.getInt(null));
				TypedValue typedValue = new TypedValue();
				getResources().getValue(R.dimen.exercise_answer_image_scale_factor, typedValue, true);
				imageButton.setImageDrawable(ImageUtility.resizeDrawable(drawable,
						display.getHeight() * display.getWidth() * typedValue.getFloat()));
				imageButton.setOnTouchListener(new AnswerListener());
				answerViews.put(imageButton, answeList.get(i).getValue());
			}
			break;
		case TEXT:
			view = findViewById(R.id.tableAnswerTextB);
			for (int i = 0; i < 4; i++) {
				Field field = R.id.class.getField("txtButtonAns" + i);
				Button button = (Button) findViewById(field.getInt(null));
				button.setText(answeList.get(i).getValue());
				button.setOnTouchListener(new AnswerListener());
				answerViews.put(button, answeList.get(i).getValue());
			}
			break;
		case TEXT_FIELD:
			view = findViewById(R.id.layoutInputAnswer);
			break;
		default:
			return;
		}
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * Checks if selected/typed answer is valid.
	 * 
	 * @return <i>true</i> if selected/typed answer is valid.
	 */
	private boolean validateAnswer() {
		String ansString = null;
		if (AnswerType.TEXT_FIELD.equals(exercise.getAnswerType())) {
			ansString = ((EditText) findViewById(R.id.inputAnswer)).getText().toString();
		} else {
			View pressedButton = getPressedButton();
			if (getPressedButton() == null) {
				return false;
			}
			ansString = answerViews.get(pressedButton);
		}
		for (Answer answer : exercise.getAnswers()) {
			if (answer.isValid()) {
				String dbAns = answer.getValue().toLowerCase(Locale.getDefault());
				String userAns = ansString.toLowerCase(Locale.getDefault());
				if (dbAns.equals(userAns)) {
					return true;
				} else {
					break;
				}
			}
		}
		return false;
	}

	/**
	 * Returns pressed button View
	 * 
	 * @return pressed button View
	 */
	private View getPressedButton() {
		for (View v : answerViews.keySet()) {
			if (v.isPressed()) {
				return v;
			}
		}
		return null;
	}

	/**
	 * Try to go to next exercise.
	 */
	private void tryGoNext() {
		if (validateAnswer()) {
			// Correct answer
			exercise.setSolved(true);
			exerciseDao.update(exercise);
			level.setScore(scoring.getValue());
			levelDao.update(level);

		} else if (!(((AnswerType.TEXT.equals(exercise.getAnswerType()) || AnswerType.IMAGE.equals(exercise
				.getAnswerType())) && getPressedButton() == null) || (AnswerType.TEXT_FIELD.equals(exercise
				.getAnswerType()) && "".equals(((EditText) findViewById(R.id.inputAnswer)).getText().toString())))) {
			/*
			 * Above if statement is evaluated, when answer validation returned
			 * false. This statement checks if there is "no answer" situation.
			 * If not, wrong answer was given.
			 */
			// FIXME don't update scoring! update exercise
			scoring.setValue(scoring.getValue() + scoring.getUnsuccessfulAttempt());
			scoringDao.update(scoring);
			Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.wrong_answer), Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		final Bundle bundle = new BundleBuilder()
				.withInteger(getString(R.string.previous_exercise_id), exercise.getId())
				.withInteger(getString(R.string.level_id), level.getId()).build();
		ActivityStarter.start(this, bundle, ExerciseActivity.class);
	}

	/**
	 * Initialize buttons.
	 */
	private void initButtons() {
		// Next exercise
		buttonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tryGoNext();
			}
		});

		// Back
		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new BundleBuilder().withBoolean(getString(R.string.back_pressed), true)
						.withInteger(getString(R.string.previous_exercise_id), exercise.getId())
						.withInteger(getString(R.string.level_id), level.getId()).build();
				ActivityStarter.start(ExerciseActivity.this, bundle, ExerciseActivity.class);
			}

		});

		/*
		 * Next button in soft keyboard while editing answer input text.
		 */
		inputAnswer.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					tryGoNext();
					return true;
				}
				return false;
			}
		});

		// Display tip
		imgButtonTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), exercise.getTip(), Toast.LENGTH_LONG);
				toast.show();
				if (!exercise.isTipUsed()) {
					scoring.setValue(scoring.getValue() + scoring.getUsingTip());
					scoringDao.update(scoring);
					exercise.setTipUsed(true);
					exerciseDao.update(exercise);
				}
			}

		});
	}

	/**
	 * OnTouchListener for answers.
	 */
	class AnswerListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			/*
			 * If pressed button is already pressed, release it and exit.
			 */
			if (event.getAction() == MotionEvent.ACTION_DOWN && view.equals(getPressedButton())) {
				view.setPressed(false);
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP && getPressedButton() == null) {
				return true;
			}
			/* If some answer is already pressed, release it. */
			if (getPressedButton() != null) {
				getPressedButton().setPressed(false);
			}
			view.setPressed(true);
			return true;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}