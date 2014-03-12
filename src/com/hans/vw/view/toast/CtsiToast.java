package com.hans.vw.view.toast;

import com.hans.vw.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CtsiToast extends Toast {

	LayoutInflater mInflater;
	View view;
	TextView txv_text;

	public CtsiToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.layout_toast, null);
		txv_text = (TextView) view.findViewById(R.id.txv_text);
		setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
		setDuration(Toast.LENGTH_LONG);
		setView(view);
	}

	@Override
	public void setText(CharSequence s) {
		// TODO Auto-generated method stub
		// super.setText(s);
		txv_text.setText(s);
	}

}
