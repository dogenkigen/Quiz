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

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.activity.clicklistener.LevelResetListener;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.entity.Level;

/**
 * This Activity displays level score.
 * 
 * @author Maciej Laskowski
 * 
 */
@ContentView(R.layout.activity_score)
public class ScoreActivity extends FullScreenActivity {

	@InjectView(R.id.buttonReset)
	private Button levelResetButton;

	@Inject
	private LevelDao levelDao;

	private Level level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		setLevel(bundle.getInt(getString(R.string.level_id)));
		setScoreOnTextView(bundle.getInt(getString(R.string.score)));
		initButtons();
	}

	private void setLevel(Integer levelId) {
		level = levelDao.queryForId(levelId);
	}

	private void setScoreOnTextView(int score) {
		TextView tv = (TextView) findViewById(R.id.scoreText);
		tv.setText(tv.getText() + "\n" + score);
	}

	private void initButtons() {
		levelResetButton.setOnClickListener(new LevelResetListener(this, level));
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}


}
