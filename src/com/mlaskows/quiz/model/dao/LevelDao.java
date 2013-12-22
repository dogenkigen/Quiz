package com.mlaskows.quiz.model.dao;

import java.sql.SQLException;
import java.util.List;

import roboguice.inject.InjectResource;
import android.util.Log;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.mlaskows.quiz.R;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entity.Level;

@Singleton
public class LevelDao extends BaseDaoImpl<Level, Integer> {

	/** Application name. */
	@InjectResource(R.string.app_name)
	private String applicationName;

	/** Error string. */
	@InjectResource(R.string.error)
	private String errorString;

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

	/* (non-Javadoc)
	 * @see com.j256.ormlite.dao.BaseDaoImpl#queryForId(java.lang.Object)
	 */
	@Override
	public Level queryForId(Integer id) {
		try {
			return super.queryForId(id);
		} catch (SQLException e) {
			Log.e(applicationName, errorString, e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.j256.ormlite.dao.BaseDaoImpl#update(java.lang.Object)
	 */
	@Override
	public int update(Level arg0) {
		try {
			return super.update(arg0);
		} catch (SQLException e) {
			Log.e(applicationName, errorString, e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see com.j256.ormlite.dao.BaseDaoImpl#queryForAll()
	 */
	@Override
	public List<Level> queryForAll() {
		try {
			return super.queryForAll();
		} catch (SQLException e) {
			Log.e(applicationName, errorString, e);
			return null;
		}
	}

}
