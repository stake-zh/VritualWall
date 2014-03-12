package com.hans.vw.application;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;

public class DialogHandler_FullDialog extends DialogHandler_Base {

	String message;
	String firstText;
	String secondText;
	String thirdText;
	OnClickListener firstlistener;
	OnClickListener secondListener;
	OnClickListener thirdListener;

	public DialogHandler_FullDialog(Activity activity, String title,
			String message, String firstText, String secondText,
			String thirdText, OnClickListener firstlistener,
			OnClickListener secondListener, OnClickListener thirdListener) {
		super(activity, title);
		// TODO Auto-generated constructor stub
		this.message = message;
		this.firstText = firstText;
		this.secondText = secondText;
		this.thirdText = thirdText;
		this.firstlistener = firstlistener;
		this.secondListener = secondListener;
		this.thirdListener = thirdListener;
	}
}
