package com.mlaskows.quiz.model.dao;

import java.sql.SQLException;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entity.Level;

@Singleton
public class LevelDao extends BaseDaoImpl<Level, Integer> {

	public LevelDao() throws SQLException {
		super(DatabaseHelper.getConnectionSrc(), Level.class);
		this.initialize();
	}

	public LevelDao(ConnectionSource connectionSource, Class<Level> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public LevelDao(Class<Level> dataClass) throws SQLException {
		super(dataClass);
	}

	public LevelDao(ConnectionSource connectionSource, DatabaseTableConfig<Level> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

}
