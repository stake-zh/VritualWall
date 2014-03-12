package com.hans.vw.application;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;

public class DialogHandler_Simple extends DialogHandler_Alert {
	OnClickListener oklistener;

	public DialogHandler_Simple(Activity activity,String title, String message,
			OnClickListener oklistener) {
		super(activity,title, message);
		this.oklistener = oklistener;
		// TODO Auto-generated constructor stub
	}

	public OnClickListener getOklistener() {
		return oklistener;
	}

}
