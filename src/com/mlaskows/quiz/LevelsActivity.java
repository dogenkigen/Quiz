package com.mlaskows.quiz;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entities.Level;

/**
 * Activity with levels list.
 * 
 * @author Maciej Laskowski
 * 
 */
public class LevelsActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_levels);
		Dao<Level, Integer> dbDao = new DatabaseHelper(getApplicationContext()).getLevelDao();
		try {
			List<Level> levels = dbDao.queryForAll();
			// Create ListView with DownloadsAdapter
			LevelsAdapter adapter = new LevelsAdapter(this, R.layout.list_item_row, levels);
			ListView listView = (ListView) findViewById(R.id.listLevels);
			// listView.setOnItemClickListener(mMessageClickedHandler);
			listView.setAdapter(adapter);

		} catch (SQLException e) {
			Log.e(LevelsActivity.class.getSimpleName(), e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.levels, menu);
		return true;
	}

}
