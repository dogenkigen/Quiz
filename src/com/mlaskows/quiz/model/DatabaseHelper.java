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

package com.mlaskows.quiz.model;

import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;
import roboguice.inject.InjectResource;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mlaskows.quiz.QuizApplication;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.model.dao.AnswerDao;
import com.mlaskows.quiz.model.dao.ExerciseDao;
import com.mlaskows.quiz.model.dao.LevelDao;
import com.mlaskows.quiz.model.dao.QuestionDao;
import com.mlaskows.quiz.model.dao.ScoringDao;
import com.mlaskows.quiz.model.entity.Answer;
import com.mlaskows.quiz.model.entity.Exercise;
import com.mlaskows.quiz.model.entity.Level;
import com.mlaskows.quiz.model.entity.Question;
import com.mlaskows.quiz.model.entity.Quiz;
import com.mlaskows.quiz.model.entity.Scoring;

/**
 * Creates database in first application run. Also can
 * return DAO. To enable ORMLite logs execute 'setprop
 * log.tag.ORMLite DEBUG' on device
 * 
 * @author Maciej Laskowski
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_FILE_NAME = "quizes.sqlite";

	/**
	 * Any time changes are made to database objects, may
	 * have to increase the database version.
	 */
	private static final int DATABASE_VERSION = 1;

	private static DatabaseHelper instance = new DatabaseHelper(QuizApplication.getContext());

	private Context applicationContext;

	@Inject
	private LevelDao levelDao;

	@Inject
	private ExerciseDao exerciseDao;

	@Inject
	private QuestionDao questionDao;

	@Inject
	private AnswerDao answerDao;

	@Inject
	private ScoringDao scoringDao;

	@InjectResource(R.string.app_name)
	private String applicationName;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
		this.applicationContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
		try {
			createDataBase(cs);
			// Inject manually since only RoboActivity can
			// do it by itself.
			RoboGuice.injectMembers(applicationContext, this);
			loadContentToDataBase();
		} catch (SQLException e) {
			Log.e(applicationName, "Can't create database!", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Log.e(applicationName, "Error while creating DB!", e);
		} catch (Exception e) {
			Log.e(applicationName, "Cannot load XML to DB!", e);
		}

	}

	private void loadContentToDataBase() throws Exception, java.sql.SQLException {
		Quiz quiz = new XmlDataLoader(applicationContext).loadXml();
		// Children first than parents.
		for (Level level : quiz.getLevels()) {
			for (Exercise exercise : level.getExercises()) {
				exercise.setLevel(level);
				questionDao.create(exercise.getQuestion());
				questionDao.refresh(exercise.getQuestion());
				exerciseDao.create(exercise);
				for (Answer answer : exercise.getAnswers()) {
					answer.setExercise(exercise);
					answerDao.create(answer);
				}
			}
			Scoring scoring = level.getScoring();
			scoring.setLevel(level);
			scoringDao.create(scoring);
			levelDao.create(level);
		}
	}

	private void createDataBase(ConnectionSource cs) throws java.sql.SQLException {
		TableUtils.createTable(cs, Level.class);
		TableUtils.createTable(cs, Exercise.class);
		TableUtils.createTable(cs, Question.class);
		TableUtils.createTable(cs, Answer.class);
		TableUtils.createTable(cs, Scoring.class);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
		try {
			List<String> allSql = new ArrayList<String>();
			// DB upgrade can depend on version is upgrading
			// from
			switch (oldVersion) {
			case 1:
				// allSql.add("SQL query 1 here");
				// allSql.add("SQL query 2 here");
			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}
		} catch (SQLException e) {
			Log.e(applicationName, "Exception during DB upgrade!", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns {@link ConnectionSource} object.
	 * 
	 * @return {@link ConnectionSource} object
	 */
	public static ConnectionSource getConnectionSrc() {
		return instance.getConnectionSource();
	}
}
