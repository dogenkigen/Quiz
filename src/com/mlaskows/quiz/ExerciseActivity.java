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
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entities.Answer;
import com.mlaskows.quiz.model.entities.Exercise;
import com.mlaskows.quiz.model.entities.InputOutputType;
import com.mlaskows.quiz.model.entities.Level;
import com.mlaskows.quiz.model.entities.Question;

/**
 * This Activity displays exercise with {@link Question} and
 * its {@link Answer}s.
 * 
 * @author Maciej Laskowski
 * 
 */
public class ExerciseActivity extends Activity {

	/** Id of exercise's level. */
	private int levelId;

	/** Displayed exercise. */
	private Exercise exercise;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_exercise);
		initButtons();

		// Get levelId from Bundle
		Bundle b = getIntent().getExtras();
		levelId = b.getInt("level_id");

		Dao<Level, Integer> dbDao = new DatabaseHelper(getApplicationContext()).getLevelDao();
		try {
			Level level = dbDao.queryForId(levelId);
			if (level == null) {
				// TODO game is over handle situation
				// move this code (where?)
				return;
			}
			exercise = null;
			for (Exercise e : level.getExercises()) {
				if (!e.isSolved()) {
					exercise = e;
					break;
				}
			}
			if (exercise == null) {
				// TODO this level is solved. handle this
				// situation. Show score?
			}
			displayExercise(exercise);

		} catch (SQLException e) {
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
		}

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
				ImageButton ib = (ImageButton) findViewById(field.getInt(null));
				field = R.drawable.class.getField(answeList.get(i).getValue());
				ib.setImageResource(field.getInt(null));
			}
			break;
		case TEXT:
			view = findViewById(R.id.tableAnswerTextB);
			for (int i = 0; i < 4; i++) {
				Field field = R.id.class.getField("txtButtonAns" + i);
				Button ib = (Button) findViewById(field.getInt(null));
				ib.setText(answeList.get(i).getValue());
			}
			break;
		case TEXT_FIELD:
			view = findViewById(R.id.inputAnswer);
			break;
		default:
			break;
		}
		view.setVisibility(View.VISIBLE);
	}

	private boolean validateAnswer() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Initialize buttons.
	 */
	private void initButtons() {
		// Next exercise
		((Button) findViewById(R.id.buttonNext)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateAnswer()) {
					// return;
				}
				// TODO update exercise as solved.
				final Bundle b = new Bundle();
				b.putInt("level_id", levelId);
				Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		// TODO add back

		// Display tip
		((ImageButton) findViewById(R.id.imgButtonTip)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = exercise.getTip();
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
	}
}