package com.hans.vw.application;

import android.app.Activity;

public class DialogHandler_Error  extends DialogHandler_Base {
	String message;

	public DialogHandler_Error(Activity activity,String title, String message) {
		super(activity,title);
		this.message = message;
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		return message;
	}

}
