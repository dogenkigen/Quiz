package com.mlaskows.quiz.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityStarter {

	public static void start(Activity startingActivity, Bundle bundle, Class<?> cls) {
		Intent intent = new Intent(startingActivity.getApplicationContext(), cls);
		intent.putExtras(bundle);
		startingActivity.startActivity(intent);
	}

}
