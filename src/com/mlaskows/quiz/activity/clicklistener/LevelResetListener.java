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

package com.mlaskows.quiz.activity.clicklistener;

import roboguice.RoboGuice;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.inject.Inject;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.activity.ExerciseActivity;
import com.mlaskows.quiz.activity.ScoreActivity;
import com.mlaskows.quiz.activity.util.ActivityStarter;
import com.mlaskows.quiz.activity.util.BundleBuilder;
import com.mlaskows.quiz.model.dao.ExerciseDao;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.entity.Exercise;
import com.mlaskows.quiz.model.entity.Level;

public class LevelResetListener implements OnClickListener {

	private ScoreActivity scoreActivity;

	private Level level;

	@Inject
	private ExerciseDao exerciseDao;

	@Inject
	private LevelDao levelDao;

	public LevelResetListener(ScoreActivity scoreActivity, Level level) {
		RoboGuice.injectMembers(scoreActivity.getApplicationContext(), this);
		this.scoreActivity = scoreActivity;
		this.level = level;
	}

	@Override
	public void onClick(View v) {
		cleanLevel();
		startNewActivity();
	}

	private void cleanLevel() {
		level.setScore(0);
		for (Exercise exercise : level.getExercises()) {
			exercise.setTipUsed(false);
			exercise.setSolved(false);
			exerciseDao.update(exercise);
		}
		levelDao.update(level);
	}

	private void startNewActivity() {
		Bundle bundle = new BundleBuilder().withInteger(scoreActivity.getString(R.string.level_id), level.getId())
				.build();
		ActivityStarter.start(scoreActivity, bundle, ExerciseActivity.class);
	}
}