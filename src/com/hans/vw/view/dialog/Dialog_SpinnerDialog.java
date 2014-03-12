package com.hans.vw.view.dialog;
import com.hans.vw.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Dialog_SpinnerDialog extends Dialog {

	Dialog instance;
	LinearLayout layout_views, layout_mainview;
	ImageView img_spinner;

	public Dialog_SpinnerDialog(Context context) {
		super(context, R.style.dialog);
		instance = this;
		initValues();
		img_spinner = (ImageView) findViewById(R.id.img_spinner);
		img_spinner.setBackgroundResource(R.drawable.img_spinner);

	}

	protected void initValues() {
		setContentView(R.layout.layout_dialog_spinner);
		Window win = instance.getWindow();
		LayoutParams params = win.getAttributes();

		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		win.setAttributes(params);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		AnimationDrawable ad = (AnimationDrawable) img_spinner.getBackground();
		ad.start();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		AnimationDrawable ad = (AnimationDrawable) img_spinner.getBackground();
		ad.stop();
	}

}
