package com.hans.vw.view.dialog;
import com.hans.vw.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public abstract class Dialog_Base extends Dialog {

	Dialog instance;
	LinearLayout layout_views, layout_mainview;
	RelativeLayout layout_bottom;
	ScrollView layout_body;
	RelativeLayout layout_upper;
	TextView txv_btn1, txv_btn2, txv_btn3, txv_title;
	CharSequence text_btn1, text_btn2, text_btn3, title;
	OnClickListener listener_btn1, listener_btn2, listener_btn3;
	boolean hasInitView = false;
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int CENTER = 2;

	public abstract int getDirection();

	public abstract boolean enableAnimation();

	public Dialog_Base(Context context, String title) {
		super(context, R.style.dialog);
		instance = this;
		setTitle(title);
	}

	protected void initValues() {
		setContentView(getMidView());
		if (!TextUtils.isEmpty(title)) {
			txv_title.setText(title);
		}
		if (!TextUtils.isEmpty(text_btn1)) {
			txv_btn1.setText(text_btn1);
			txv_btn1.setOnClickListener(new clickListener(listener_btn1,
					getWhich()));
			txv_btn1.setVisibility(View.VISIBLE);
		} else {
			txv_btn1.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(text_btn2)) {
			txv_btn2.setText(text_btn2);
			txv_btn2.setOnClickListener(new clickListener(listener_btn2,
					getWhich()));
			txv_btn2.setVisibility(View.VISIBLE);
		} else {
			txv_btn2.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(text_btn3)) {
			txv_btn3.setText(text_btn3);
			txv_btn3.setOnClickListener(new clickListener(listener_btn3,
					getWhich()));
			txv_btn3.setVisibility(View.VISIBLE);
		} else {
			txv_btn3.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(title)) {
			txv_title.setVisibility(View.GONE);
		} else {
			txv_title.setVisibility(View.VISIBLE);

		}
		Window win = instance.getWindow();
		LayoutParams params = win.getAttributes();
		switch (getDirection()) {
		case TOP:
			win.setGravity(Gravity.CENTER | Gravity.TOP);
			params.x = 0;
			// params.y = 0;
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;

			if (enableAnimation()) {
				win.setWindowAnimations(R.style.AnimTopPopup);
			}
			break;
		case BOTTOM:
			win.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			// params.x = 0;
			// params.y = 0;
			// params.width = LayoutParams.FILL_PARENT;
			// params.height = LayoutParams.WRAP_CONTENT;
			if (enableAnimation()) {
				win.setWindowAnimations(R.style.AnimBottomPopup);
			}
			break;
		case CENTER:
			params.width = LayoutParams.WRAP_CONTENT;
			params.height = LayoutParams.WRAP_CONTENT;
			break;
		}
		win.setAttributes(params);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated msuper.setTitle(title);
		this.title = title;
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		if (!hasInitView) {
			super.setContentView(R.layout.layout_dialog_base);
			layout_views = (LinearLayout) findViewById(R.id.layout_views);
			layout_upper = (RelativeLayout) findViewById(R.id.layout_upper);
			layout_body = (ScrollView) findViewById(R.id.layout_body);
			layout_mainview = (LinearLayout) findViewById(R.id.layout_mainview);
			layout_bottom = (RelativeLayout) findViewById(R.id.layout_bottom);
			if (getAllBackground() <= 0) {

				layout_upper.setBackgroundResource(getUpperBackground());
				layout_body.setBackgroundResource(getBodyBackground());
				layout_bottom.setBackgroundResource(getBottomBackground());
			} else {
				layout_mainview.setBackgroundResource(getAllBackground());
			}
			txv_title = (TextView) findViewById(R.id.txv_title);
			txv_btn1 = (TextView) findViewById(R.id.txv_btn1);
			txv_btn2 = (TextView) findViewById(R.id.txv_btn2);
			txv_btn3 = (TextView) findViewById(R.id.txv_btn3);

			if (view != null && layout_views.getChildCount() == 0) {

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				layout_views.addView(view, params);
			}
			hasInitView = true;
		}
	}

	public abstract int getWhich();

	public abstract int getAllBackground();

	public abstract int getUpperBackground();

	public abstract int getBodyBackground();

	public abstract int getBottomBackground();

	public abstract View getMidView();

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		initValues();
	}

	public void setButton(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub

		text_btn1 = text;
		listener_btn1 = listener;
	}

	public void setButton2(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub
		text_btn2 = text;
		listener_btn2 = listener;
	}

	public void setButton3(CharSequence text, OnClickListener listener) {
		// TODO Auto-generated method stub
		text_btn3 = text;
		listener_btn3 = listener;
	}

	class clickListener implements View.OnClickListener {

		private OnClickListener listener;
		private int which;

		public clickListener(OnClickListener listener, int which) {
			// TODO Auto-generated constructor stub
			this.listener = listener;
			this.which = which;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (listener != null) {
				listener.onClick(instance, which);
			}
			if (instance.isShowing())
				instance.dismiss();

		}
	}

}
