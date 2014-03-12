package com.hans.vw.data;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class Cache {

	public static HashMap<String, SoftReference<Bitmap>> img_cache = new HashMap<String, SoftReference<Bitmap>>();

	public void write(String url, Bitmap bitmap) {
		SoftReference<Bitmap> rb = new SoftReference<Bitmap>(bitmap);
		img_cache.put(url, rb);
	}



}
