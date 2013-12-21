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

package com.mlaskows.quiz;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entities.Answer;
import com.mlaskows.quiz.model.entities.Exercise;
import com.mlaskows.quiz.model.entities.Level;
import com.mlaskows.quiz.model.entities.Question;
import com.mlaskows.quiz.model.entities.Scoring;
import com.mlaskows.quiz.model.enums.InputOutputType;

/**
 * This Activity displays exercise with {@link Question} and
 * its {@link Answer}s.
 * 
 * @author Maciej Laskowski
 * 
 */
public class ExerciseActivity extends Activity {

	/** Exercise's level. */
	private Level level;

	/** Displayed exercise. */
	private Exercise exercise;

	/** Map of answer views and answer values. */
	private Map<View, String> answerViews = new HashMap<View, String>();

	/** Database helper. */
	private DatabaseHelper dbHelper;

	/** DAO for Level. */
	private Dao<Level, Integer> lvlDao;

	/** DAO for Exercise. */
	private Dao<Exercise, Integer> exerciseDao;

	/** DAO for Scoring. */
	private Dao<Scoring, Integer> scoringDao;

	/** Level scoring. */
	private Scoring scoring;

	/** Previous exercise id flag. */
	private static final String PREVIOUS_EXERCISE_ID = "previous_exercise_id";

	/** Level id flag. */
	private static final String LEVEL_ID = "level_id";

	/** Back pressed flag. */
	private static final String BACK_PRESSED = "back_pressed";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initVariables();
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_exercise);
		initButtons();

		// Get levelId from Bundle
		Bundle b = getIntent().getExtras();
		int levelId = b.getInt(LEVEL_ID);
		int previousExerciseId = b.getInt(PREVIOUS_EXERCISE_ID);
		boolean backPressed = b.getBoolean(BACK_PRESSED);

		try {
			level = lvlDao.queryForId(levelId);
			exercise = level.getUnsolvedInCycle(previousExerciseId, !backPressed);
			if (exercise == null) {
				// This level is solved. Show score.
				Bundle bundle = new Bundle();
				bundle.putInt("score", level.getScore());
				Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				return;
			}
			scoring = level.getScoring();
			displayExercise(exercise);

		} catch (SQLException e) {
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
		}

	}

	/**
	 * Initialize variables.
	 */
	private void initVariables() {
		dbHelper = new DatabaseHelper(getApplicationContext());
		lvlDao = dbHelper.getLevelDao();
		exerciseDao = dbHelper.getExerciseDao();
		scoringDao = dbHelper.getScoringDao();
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
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
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
	private void displayQuestion(Question question, InputOutputType type) {
		if (InputOutputType.TEXT.equals(type)) {
			// Text type question
			TextView tv = (TextView) findViewById(R.id.textQuestion);
			tv.setVisibility(View.VISIBLE);
			tv.setText(question.getValue());
		} else if (InputOutputType.IMAGE.equals(type)) {
			// Image type question
			ImageView iv = (ImageView) findViewById(R.id.imageQuestion);
			iv.setVisibility(View.VISIBLE);
			try {
				Field field = R.drawable.class.getField(question.getValue());
				iv.setImageResource(field.getInt(null));
			} catch (Exception e) {
				// Pass this exception
				throw new RuntimeException(e);
			}
		}
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
	private void displayAnswers(Collection<Answer> answers, InputOutputType type) throws NoSuchFieldException,
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
				imageButton.setImageResource(field.getInt(null));
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
			view = findViewById(R.id.inputAnswer);
			break;
		default:
			return;
		}
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * Checks if selected/typed answer is valid.
	 * 
	 * @return <i>true</i> if selected/typed answer is
	 *         valid.
	 */
	private boolean validateAnswer() {
		String ansString = null;
		if (InputOutputType.TEXT_FIELD.equals(exercise.getAnswerType())) {
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
	 * Initialize buttons.
	 */
	private void initButtons() {
		// Next exercise
		((Button) findViewById(R.id.buttonNext)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (validateAnswer()) {
					// Correct answer
					try {
						exercise.setSolved(true);
						updateExercise(exercise);
						level.setScore(scoring.getValue());
						lvlDao.update(level);
					} catch (SQLException e) {
						Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
					}
				} else if (!(((InputOutputType.TEXT.equals(exercise.getAnswerType()) || InputOutputType.IMAGE
						.equals(exercise.getAnswerType())) && getPressedButton() == null) || (InputOutputType.TEXT_FIELD
						.equals(exercise.getAnswerType()) && "".equals(((EditText) findViewById(R.id.inputAnswer))
						.getText().toString())))) {
					/*
					 * Above if statement is evaluated, when answer
					 * validation returned false. This statement 
					 * checks if there is "no answer" situation. 
					 * If not, wrong answer was given.
					 */
					updateScoring(scoring.getUnsuccessfulAttempt());
					Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.wrong_answer),
							Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				final Bundle bundle = new Bundle();
				bundle.putInt(PREVIOUS_EXERCISE_ID, exercise.getId());
				openExerciseActivity(bundle);
			}
		});

		// Back
		((Button) findViewById(R.id.buttonBack)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putBoolean(BACK_PRESSED, true);
				bundle.putInt(PREVIOUS_EXERCISE_ID, exercise.getId());
				openExerciseActivity(bundle);
			}

		});

		// Display tip
		((ImageButton) findViewById(R.id.imgButtonTip)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(getApplicationContext(), exercise.getTip(), Toast.LENGTH_LONG);
				toast.show();
				if (!exercise.isTipUsed()) {
					updateScoring(scoring.getUsingTip());
					exercise.setTipUsed(true);
					updateExercise(exercise);
				}
			}

		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		openLevelsActivity();
	}

	/**
	 * Updates scoring for specified value.
	 * 
	 * @param value
	 *            points added to scoring
	 */
	private void updateScoring(int value) {
		try {
			scoring.setValue(scoring.getValue() + (value));
			scoringDao.update(scoring);
		} catch (SQLException e) {
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
		}
	}

	/**
	 * Updates specified exercise.
	 * 
	 * @param exercise
	 *            element to update
	 */
	private void updateExercise(Exercise exercise) {
		try {
			exerciseDao.update(exercise);
		} catch (SQLException e) {
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
		}
	}

	/**
	 * Opens {@link LevelsActivity} screen.
	 */
	private void openLevelsActivity() {
		Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	/**
	 * Opens {@link ExerciseActivity} screen.
	 * 
	 * @param bundle
	 *            parameters for new Activity
	 */
	private void openExerciseActivity(Bundle bundle) {
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putInt(LEVEL_ID, level.getId());
		Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * OnTouchListener for answers.
	 */
	class AnswerListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			/* If pressed button is already pressed, release it
			 * and exit.
			 */
			if (event.getAction() == MotionEvent.ACTION_DOWN && view.equals(getPressedButton())) {
				view.setPressed(false);
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP && getPressedButton() == null) {
				return true;
			}
			/* If some answer is already pressed, release it.*/
			if (getPressedButton() != null) {
				getPressedButton().setPressed(false);
			}
			view.setPressed(true);
			return true;
		}
	}
}