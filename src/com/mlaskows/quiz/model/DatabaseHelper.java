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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import roboguice.RoboGuice;
import android.content.Context;
import android.content.res.Resources;
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

	/** Name of the database file for application */
	private static final String DATABASE_NAME = "quizes.sqlite";

	/**
	 * Any time changes are made to database objects, may
	 * have to increase the database version.
	 */
	private static final int DATABASE_VERSION = 1;

	/** Instance. */
	private static DatabaseHelper instance = new DatabaseHelper(QuizApplication.getContext());

	/** Application context. */
	private Context context;

	/** Level DAO */
	@Inject
	private LevelDao levelDao;

	/** Exercise DAO */
	@Inject
	private ExerciseDao exerciseDao;

	/** Question DAO */
	@Inject
	private QuestionDao questionDao;

	/** Answer DAO */
	@Inject
	private AnswerDao answerDao;

	/** Scoring DAO */
	@Inject
	private ScoringDao scoringDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase,
	 * com.j256.ormlite.support.ConnectionSource)
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
		try {
			// Create DB
			TableUtils.createTable(cs, Level.class);
			TableUtils.createTable(cs, Exercise.class);
			TableUtils.createTable(cs, Question.class);
			TableUtils.createTable(cs, Answer.class);
			TableUtils.createTable(cs, Scoring.class);
			// Inject manually since only RoboActivity can
			// do it by itself.
			RoboGuice.injectMembers(context, this);

			/* Load content from XML to database.
			 * Children first than parents. 
			 */
			Quiz quiz = loadXml();
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
		} catch (SQLException e) {
			Log.e(DATABASE_NAME, "Can't create database!", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			Log.e(DATABASE_NAME, "Error while creating DB!", e);
		} catch (Exception e) {
			Log.e(DatabaseHelper.class.getSimpleName(), "Cannot load XML to DB!", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 * com.j256.ormlite.support.ConnectionSource, int, int)
	 */
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
			Log.e(DATABASE_NAME, "Exception during DB upgrade!", e);
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

	/**
	 * Loads XML with quiz and returns {@link Quiz} object.
	 * 
	 * @return quiz object
	 * @throws Exception
	 *             when deserialization fails
	 */
	private Quiz loadXml() throws Exception {
		// Get resources
		Resources resources = context.getResources();
		// Determine locale
		Locale locale = resources.getConfiguration().locale;
		String code = locale.getLanguage();
		// Get XML name using reflection
		Field field = null;
		String prefix = context.getString(R.string.xml_prefix);
		try {
			field = R.raw.class.getField(prefix + code);
		} catch (NoSuchFieldException e) {
			// If there is no language available use default
			field = R.raw.class.getField(prefix + context.getString(R.string.default_language));
		}
		// Create InputSream from XML resource
		InputStream source = resources.openRawResource(field.getInt(null));
		// Parse XML
		Serializer serializer = new Persister();
		return serializer.read(Quiz.class, source);
	}

}
