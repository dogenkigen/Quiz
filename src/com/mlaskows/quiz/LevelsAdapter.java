package com.mlaskows.quiz;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mlaskows.quiz.model.entities.Level;

/**
 * Levels list adapter.
 * 
 * @author Maciej Laskowski
 * 
 */
public class LevelsAdapter extends ArrayAdapter<Level> {

	private Context context;
	private List<Level> levels;
	private int resource;

	/**
	 * {@link LevelsAdapter} constructor
	 * 
	 * @param context
	 *            app context
	 * @param resource
	 *            resource id
	 * @param levels
	 *            levels list
	 */
	public LevelsAdapter(Context context, int resource, List<Level> levels) {
		super(context, resource, levels);
		this.context = context;
		this.levels = levels;
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LevelHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resource, parent, false);
			holder = new LevelHolder();
			holder.setProgressBar((ProgressBar) row.findViewById(R.id.progressBarLevel));
			holder.setTxtTitle((TextView) row.findViewById(R.id.textLevel));
			row.setTag(holder);
			// Set custom background for row
			row.setBackgroundDrawable((getContext().getResources().getDrawable(R.drawable.button_main)));
		} else {
			holder = (LevelHolder) row.getTag();
		}
		
		// Set level info
		Level level = levels.get(position);
		holder.getTxtTitle().setText(level.getName());
		holder.getProgressBar().setProgress(level.getProgress());

		return row;
	}

	/**
	 * Holder for {@link Level} element
	 */
	private static class LevelHolder {
		private TextView txtTitle;
		private ProgressBar progressBar;

		public TextView getTxtTitle() {
			return txtTitle;
		}

		public void setTxtTitle(TextView txtTitle) {
			this.txtTitle = txtTitle;
		}

		public ProgressBar getProgressBar() {
			return progressBar;
		}

		public void setProgressBar(ProgressBar progressBar) {
			this.progressBar = progressBar;
		}

	}

}
