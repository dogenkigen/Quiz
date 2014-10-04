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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mlaskows.quiz.R;

/**
 * Main application activity.
 * 
 * @author Maciej Laskowski
 * 
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends FullScreenActivity {

	@InjectView(R.id.buttonStart)
	private Button buttonStart;

	@InjectView(R.id.buttonMoreGames)
	private Button buttonMoreGames;

	@InjectView(R.id.buttonExit)
	private Button buttonExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initButtons();
	}

	private void initButtons() {
		initStartGameButton();
		initMoreGamesButton();
		initExitButton();
	}

	private void initExitButton() {
		buttonExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createExitDialog().show();
			}
		});
	}

	private void initMoreGamesButton() {
		buttonMoreGames.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(getString(R.string.check_more_games_url)));
				startActivity(browserIntent);
			}
		});
	}

	private void initStartGameButton() {
		buttonStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
				startActivity(intent);
			}
		});
	}

	private Dialog createExitDialog() {
		return new AlertDialog.Builder(this).setTitle(getString(R.string.exit_dialog_header))
				.setMessage(getString(R.string.exit_dialog_text))
				.setPositiveButton(getString(R.string.positive_ans), createPositiveAnswerListener())
				.setNegativeButton(getString(R.string.negative_ans), createNegativeAnswerListener()).create();
	}

	private android.content.DialogInterface.OnClickListener createPositiveAnswerListener() {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Exit application
				finish();
			}
		};
	}

	private android.content.DialogInterface.OnClickListener createNegativeAnswerListener() {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
				return;
			}
		};
	}

	@Override
	public void onBackPressed() {
		createExitDialog().show();
	}
}
