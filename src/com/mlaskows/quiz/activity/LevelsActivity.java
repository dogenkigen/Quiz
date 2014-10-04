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

import java.util.List;

import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.inject.Inject;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.adapter.LevelsAdapter;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.entity.Level;

/**
 * Activity with levels list.
 * 
 * @author Maciej Laskowski
 * 
 */
@ContentView(R.layout.activity_levels)
public class LevelsActivity extends FullScreenActivity {

	@Inject
	private LevelDao levelDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpAdapter();
	}

	private void setUpAdapter() {
		List<Level> levels = levelDao.queryForAll();
		LevelsAdapter adapter = new LevelsAdapter(this, R.layout.list_item_row, levels);
		ListView listView = (ListView) findViewById(R.id.listLevels);
		listView.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
