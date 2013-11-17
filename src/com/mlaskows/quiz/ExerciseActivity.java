package com.mlaskows.quiz;

import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.mlaskows.quiz.model.DatabaseHelper;
import com.mlaskows.quiz.model.entities.Answer;
import com.mlaskows.quiz.model.entities.Exercise;
import com.mlaskows.quiz.model.entities.Level;
import com.mlaskows.quiz.model.entities.Question;

/**
 * This Activity displays exercise with {@link Question} and
 * its {@link Answer}s.
 * 
 * @author Maciej Laskowski
 * 
 */
public class ExerciseActivity extends Activity implements AnimationListener {

	private PopupWindow mPopUp;
	private RelativeLayout mMainLayout;
	TextView mTextTip;
	Animation mAnimFadeIn, mAnimFadeOut;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_exercise);

		// Get levelId from Bundle
		Bundle b = getIntent().getExtras();
		int levelId = b.getInt("level_id");
		
		Dao<Level, Integer> dbDao = new DatabaseHelper(getApplicationContext()).getLevelDao();
		try {
			Level level = dbDao.queryForId(levelId);
			Exercise exercise = null;
			for (Exercise e : level.getExercises()) {
				if (!e.isSolved()) {
					exercise = e;
					break;
				}
			}
			if (exercise != null) {
				System.out.println(exercise.getId());
				// TODO display all
			} else {
				// TODO this level is solved. handle this
				// situation.
			}
		} catch (SQLException e) {
			Log.e(ExerciseActivity.class.getSimpleName(), e.getMessage());
		}
		

		/*		// Config elements
				mMainLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_question,
						null);
				mAnimFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter);
				mAnimFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit);
				mAnimFadeIn.setAnimationListener(this);
				mAnimFadeOut.setAnimationListener(this);
				setContentView(mMainLayout);
				mTextTip = (TextView) findViewById(R.id.textTip);
				((Button) findViewById(R.id.buttonTip)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						handlePopup();
					}
				});*/

	}

	private void handlePopup() {
		if (mTextTip.getVisibility() == View.INVISIBLE) {
			// Make fade in elements Visible first
			mTextTip.setVisibility(View.VISIBLE);
			// start fade in animation
			mTextTip.startAnimation(mAnimFadeIn);
			LayoutParams params = new LayoutParams(mTextTip.getLayoutParams());
			// stackoverflow.com/questions/4814124/how-to-change-margin-of-textview
			// http://www.androidhive.info/2013/06/android-working-with-xml-animations/
			params.setMargins(40, 40, 40, 40);
			mTextTip.setLayoutParams(params);
		} else {
			// start fade out animation
			mTextTip.startAnimation(mAnimFadeOut);
			mTextTip.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}