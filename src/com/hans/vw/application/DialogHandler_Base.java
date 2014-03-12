package com.hans.vw.application;

import android.app.Activity;

public class DialogHandler_Base {
	String title;
	Activity activity;

	public DialogHandler_Base(Activity activity, String title) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.activity = activity;
	}

	public Activity getActivity() {
		return activity;
	}
	public String getTitle() {
		return title;
	}
}
