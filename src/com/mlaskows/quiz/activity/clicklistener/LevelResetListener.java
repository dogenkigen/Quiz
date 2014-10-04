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