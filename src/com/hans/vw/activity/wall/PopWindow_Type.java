package com.hans.vw.activity.wall;

import com.hans.vw.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopWindow_Type extends PopupWindow {

	public static final int TYPE_ALL = 4;
	public static final int TYPE_100 = 0;
	public static final int TYPE_500 = 1;
	public static final int TYPE_1000 = 2;
	public static final int TYPE_5000 = 3;

	LayoutInflater mInflater;
	TextView txv_all, txv_100, txv_500, txv_1000, txv_5000;
	int type;
	OnTypeSelectedListener listener;

	TextView[] views = new TextView[5];

	public PopWindow_Type(Activity context, int w, int h,
			OnTypeSelectedListener listener) {
		// TODO Auto-generated constructor stub
		super(context);
		this.listener = listener;
		setBackgroundDrawable(new ColorDrawable(0));// 关键
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.layout_typeselect, null);
		txv_all = (TextView) view.findViewById(R.id.txv_all);
		txv_100 = (TextView) view.findViewById(R.id.txv_100);
		txv_500 = (TextView) view.findViewById(R.id.txv_500);
		txv_1000 = (TextView) view.findViewById(R.id.txv_1000);
		txv_5000 = (TextView) view.findViewById(R.id.txv_5000);

		view.setFocusableInTouchMode(true);
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		setHeight(h);
		setWidth(w);
		txv_all.setOnClickListener(onSelectTypeClickListener);
		txv_100.setOnClickListener(onSelectTypeClickListener);
		txv_500.setOnClickListener(onSelectTypeClickListener);
		txv_1000.setOnClickListener(onSelectTypeClickListener);
		txv_5000.setOnClickListener(onSelectTypeClickListener);
		type = TYPE_ALL;
		views[0] = txv_100;
		views[1] = txv_500;
		views[2] = txv_1000;
		views[3] = txv_5000;
		views[4] = txv_all;
	}

	private OnClickListener onSelectTypeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == txv_all) {
				type = TYPE_ALL;
			} else if (v == txv_100) {
				type = TYPE_100;
			} else if (v == txv_500) {
				type = TYPE_500;
			} else if (v == txv_1000) {
				type = TYPE_1000;
			} else if (v == txv_5000) {
				type = TYPE_5000;
			}

			if (listener != null) {
				listener.selected(type);
			}

			dismiss();

		}
	};

	public void showAsDropDown(View anchor, int xoff, int yoff) {
		type();
		super.showAsDropDown(anchor, xoff, yoff);

	}

	private void type() {

		for (int i = 0; i < views.length; i++) {
			if (i == type) {
				views[i].setTextColor(Color.DKGRAY);
				views[i].setBackgroundResource(R.drawable.bg_search_typeselected);
			} else {
				views[i].setTextColor(Color.WHITE);
				views[i].setBackgroundResource(R.drawable.bg_search_typeselecting);
			}

		}

	}

	public interface OnTypeSelectedListener {

		public void selected(int type);

	}

	public int getType() {
		return type;
	}

}
