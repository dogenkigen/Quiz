package com.mlaskows.quiz.activity.util;

import android.os.Bundle;

public class BundleBuilder {

	private Bundle bundle = new Bundle();

	public BundleBuilder withString(String valueName, String value) {
		bundle.putString(valueName, value);
		return this;
	}

	public BundleBuilder withInteger(String valueName, int value) {
		bundle.putInt(valueName, value);
		return this;
	}

	public BundleBuilder withBoolean(String valueName, boolean value) {
		bundle.putBoolean(valueName, value);
		return this;
	}

	public Bundle build() {
		return bundle;
	}

}
