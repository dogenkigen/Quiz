package com.mlaskows.quiz;

import android.app.Application;
import android.content.Context;

/**
 * Application class gives access to {@link Context}
 * 
 * @author Maciej Laskowski
 * 
 */
public class QuizApplication extends Application {

	/** Application context. */
	private static Context context;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		QuizApplication.context = getApplicationContext();
	}

	/**
	 * Returns application context.
	 * 
	 * @return application context
	 */
	public static Context getContext() {
		return context;
	}
}
