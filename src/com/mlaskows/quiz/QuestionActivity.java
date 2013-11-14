package com.mlaskows.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class QuestionActivity extends Activity implements AnimationListener {

	private PopupWindow mPopUp;
	private RelativeLayout mMainLayout;
	TextView mTextTip;
	Animation mAnimFadeIn, mAnimFadeOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_fullscreen);
		// Config elements
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
		});

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