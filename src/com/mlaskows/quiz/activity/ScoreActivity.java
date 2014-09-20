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

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.model.dao.ExerciseDao;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.entity.Exercise;
import com.mlaskows.quiz.model.entity.Level;

/**
 * This Activity displays level score.
 * 
 * @author Maciej Laskowski
 * 
 */
public class ScoreActivity extends FullScreenActivity {

	@InjectView(R.id.buttonReset)
	private Button levelResetButton;

	@Inject
	private LevelDao levelDao;

	@Inject
	private ExerciseDao exerciseDao;

	private Level level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		displayScore();
		initButtons();
	}

	private void displayScore() {
		Bundle b = getIntent().getExtras();
		int score = b.getInt("score");
		TextView tv = (TextView) findViewById(R.id.scoreText);
		tv.setText(tv.getText() + "\n" + score);
		level = levelDao.queryForId(b.getInt("level_id"));
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void initButtons() {
		// Reset level
		levelResetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				level.setScore(0);
				for (Exercise exercise : level.getExercises()) {
					exercise.setTipUsed(false);
					exercise.setSolved(false);
					exerciseDao.update(exercise);
				}
				levelDao.update(level);
				Bundle bundle = new Bundle();
				bundle.putInt("level_id", level.getId());
				Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

}
