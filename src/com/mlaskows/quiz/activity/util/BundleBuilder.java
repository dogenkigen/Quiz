/*
 * Copyright (c) 2013, Maciej Laskowski. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact mlaskowsk@gmail.com if you need additional information
 * or have any questions.
 */

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
