package com.hans.vw.application;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;

public class DialogHandler_YesNo extends DialogHandler_Simple {
	OnClickListener cancellistener;

	public DialogHandler_YesNo(Activity activity,String title, String message,
			OnClickListener oklistener, OnClickListener cancellistener) {
		super(activity,title, message, oklistener);
		this.cancellistener = cancellistener;
		// TODO Auto-generated constructor stub
	}

	public OnClickListener getCancellistener() {
		return cancellistener;
	}

}
