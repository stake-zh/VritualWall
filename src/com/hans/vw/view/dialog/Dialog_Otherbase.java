package com.hans.vw.view.dialog;

import com.hans.vw.R;

import android.content.Context;

public abstract class Dialog_Otherbase extends Dialog_Base {

	public Dialog_Otherbase(Context context, String title) {
		super(context, title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getAllBackground() {
		// TODO Auto-generated method stub
		return R.drawable.dialog_bg;
	}

}
