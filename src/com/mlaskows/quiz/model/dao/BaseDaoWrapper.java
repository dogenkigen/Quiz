package com.mlaskows.quiz.model.dao;

import java.sql.SQLException;

import roboguice.inject.InjectResource;
import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.mlaskows.quiz.R;

//TODO figure out how to organize DAOs
public class BaseDaoWrapper<T, ID> extends BaseDaoImpl<T, ID> {

	/** Application name. */
	@InjectResource(R.string.app_name)
	private String applicationName;

	/** Error string. */
	@InjectResource(R.string.error)
	private String errorString;
	
	protected BaseDaoWrapper(Class<T> dataClass) throws SQLException {
		super(dataClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int update(T arg0) {
		try {
			return super.update(arg0);
		} catch (SQLException e) {
			Log.e(applicationName, errorString, e);
			return 0;
		}
	}
	
}
