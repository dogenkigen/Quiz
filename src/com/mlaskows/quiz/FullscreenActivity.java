package com.mlaskows.quiz;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.j256.ormlite.dao.Dao;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entities.Answer;
import com.mlaskows.quiz.model.entities.Exercise;
import com.mlaskows.quiz.model.entities.Level;

//http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
public class FullscreenActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_fullscreen);
		initButtons();
		Dao<Level, Integer> dbDao = new DatabaseHelper(getApplicationContext()).getLevelDao();
		try {
			List<Level> levels = dbDao.queryForAll();
			for (Level level : levels) {
				System.out.println(level);
				for (Exercise exercise : level.getExercises()) {
					System.out.println(exercise);
					System.out.println(exercise.getQuestion());
					for (Answer answer : exercise.getAnswers()) {
						System.out.println(answer);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	private void initButtons() {
		// Start game
		((Button) findViewById(R.id.buttonStart)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
				startActivity(intent);
			}
		});

		// More games
		((Button) findViewById(R.id.buttonMoreGames)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
				startActivity(browserIntent);
			}
		});

		// Exit app
		((Button) findViewById(R.id.buttonExit)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
