/*
 * Copyright (c) 2014, Maciej Laskowski. All rights reserved.
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
package com.mlaskows.quiz.model;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.content.res.Resources;

import com.mlaskows.quiz.R;
import com.mlaskows.quiz.model.entity.Quiz;

/**
 * 
 * @author Maciej Laskowski
 *
 */
public class XmlDataLoader {

		/** Application context. */
	private Context context;
	
	public XmlDataLoader(Context context) {
		this.context = context;
	}

	/**
	 * Loads XML with quiz and returns {@link Quiz} object.
	 * 
	 * @return quiz object
	 * @throws Exception
	 *             when deserialization fails
	 */
	public Quiz loadXml() throws Exception {
		Resources resources = context.getResources();
		String languageCode = getLanguageCode(resources);
		// Get XML name using reflection
		Field field = null;
		String prefix = context.getString(R.string.xml_prefix);
		try {
			field = R.raw.class.getField(prefix + languageCode);
		} catch (NoSuchFieldException e) {
			// If there is no language available use default
			field = R.raw.class.getField(prefix + context.getString(R.string.default_language));
		}
		// Create InputSream from XML resource
		InputStream source = resources.openRawResource(field.getInt(null));
		// Parse XML
		Serializer serializer = new Persister();
		return serializer.read(Quiz.class, source);
	}

	private String getLanguageCode(Resources resources) {
		Locale locale = resources.getConfiguration().locale;
		String code = locale.getLanguage();
		return code;
	}
	
}
