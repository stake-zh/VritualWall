package com.hans.vw.view.dialog;
import com.ctsi.utils.Utils;
import com.hans.vw.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Dialog_Tips extends Dialog_Base {

	public static final int TYPE_ERROR = 1;
	public static final int TYPE_INFO = 2;
	public static final int TYPE_YESNO = 3;
	ScrollView scv;
	int type;
	TextView txv_msg;
	ImageView img_type;
	View view;

	public Dialog_Tips(Context context, int type, String title, String text) {
		super(context, title);
		// TODO Auto-generated constructor stub
		this.type = type;
		LayoutInflater mInflater = LayoutInflater.from(getContext());
		this.view = mInflater.inflate(R.layout.layout_dialog_tips, null);
		txv_msg = (TextView) view.findViewById(R.id.txv_msg);
		txv_msg.setText(text);
		img_type = (ImageView) view.findViewById(R.id.img_type);
		if (getImageSource(type) == -1) {
			img_type.setVisibility(View.GONE);
		} else {
			img_type.setVisibility(View.VISIBLE);
			img_type.setImageResource(getImageSource(type));
		}
		scv = (ScrollView) view.findViewById(R.id.scv);
		scv.getViewTreeObserver().addOnPreDrawListener(listener);
	}

	OnPreDrawListener listener = new OnPreDrawListener() {

		@Override
		public boolean onPreDraw() {
			// TODO Auto-generated method stub
			int height = scv.getMeasuredHeight();
			LayoutParams params = scv.getLayoutParams();
			int max = Utils.convertDip2Px(getContext(), 280);
			Log.e("height", height + "");
			Log.e("max", max + "");
			if (height == 0) {
				height = max;
			}
			params.height = height > max ? max : height;
			scv.setLayoutParams(params);
			scv.getViewTreeObserver().removeOnPreDrawListener(this);
			return true;
		}
	};

	public void setMessage(String msg) {
		txv_msg.setText(msg);
	}

	@Override
	public int getDirection() {
		// TODO Auto-generated method stub
		return Dialog_Base.TOP;
	}

	@Override
	public boolean enableAnimation() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getMidView() {
		// TODO Auto-generated method stub
		return view;
	}

	public int getUpperBackground() {
		return R.drawable.bg_dialog_upper;
	}

	public int getBodyBackground() {
		return R.drawable.bg_dialog_mid;
	}

	public int getBottomBackground() {
		return R.drawable.bg_dialog_bottom;
	}

	private int getImageSource(int type) {
		int result = -1;
		switch (type) {
		case TYPE_ERROR:
			result = R.drawable.icon_dialog_error;
			break;
		case TYPE_INFO:
			result = R.drawable.icon_dialog_info;
			break;
		case TYPE_YESNO:
			result = R.drawable.icon_dialog_yesno;
			break;

		}
		return result;
	}

	@Override
	public int getWhich() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAllBackground() {
		// TODO Auto-generated method stub
		return 0;
	}

}
