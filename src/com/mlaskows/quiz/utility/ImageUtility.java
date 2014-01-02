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

package com.mlaskows.quiz.utility;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Utility to manipulate images.
 * 
 * @author Maciej Laskowski
 * 
 */
public class ImageUtility {

	/**
	 * Private constructor to avoid instantiating.
	 */
	private ImageUtility() {
		;
	}

	/**
	 * Static method for resizing {@link Drawable} objects.
	 * 
	 * @param drawable
	 *            object to resize
	 * @param width
	 *            new object width
	 * @param height
	 *            new object height
	 * @return resized {@link Drawable}
	 */
	@SuppressWarnings("deprecation")
	public static Drawable resizeDrawable(Drawable drawable, int width, int height) {
		if ((drawable == null) || !(drawable instanceof BitmapDrawable)) {
			return drawable;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
		return new BitmapDrawable(bitmapResized);
	}

	/**
	 * Static method for resizing {@link Drawable} objects.
	 * 
	 * @param drawable
	 *            object to resize
	 * @param scaleFactor
	 *            scale factor
	 * @return resized {@link Drawable}
	 */
	public static Drawable resizeDrawable(Drawable drawable, float scaleFactor) {
		int width = Math.round(drawable.getIntrinsicWidth() * scaleFactor);
		int height = Math.round(drawable.getIntrinsicHeight() * scaleFactor);
		return resizeDrawable(drawable, width, height);
	}

}
