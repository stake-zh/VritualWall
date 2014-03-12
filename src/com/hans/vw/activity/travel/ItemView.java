package com.hans.vw.activity.travel;

import com.ctsi.utils.DataUtil;
import com.ctsi.utils.DateUtil;
import com.ctsi.utils.Utils;
import com.hans.vw.R;

import android.util.Log;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemView {

	private String id;
	private View item;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private RelativeLayout layout_main;
	private ImageView img_item;
	private TextView txv_item;
	private Intent intent;
	private Activity_Bottom mContext;

	private String viewTitle;
	private View.OnClickListener itemOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (ItemView item : mContext.itemViews) {
				if (id.equals(item.id)) {
					item.OnSelected();
					mContext.setPage(id);
				} else {
					item.OnLostSelected();

				}

			}
		}
	};
	View.OnFocusChangeListener itemFocusListener = new View.OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (!hasFocus) {

			} else {

				// mContext.getLayout_container().removeAllViews();
				// mContext.getLayout_container().setCurrentItem(1);
			}
		}
	};

	public ItemView(Activity_Bottom context) {
		this.mContext = context;
		id = DataUtil.getUUID();
		item = View.inflate(context, R.layout.layout_bottomtab_item, null);
		img_item = (ImageView) item.findViewById(R.id.img_item);
		txv_item = (TextView) item.findViewById(R.id.txv_item);
		txv_item.setTextColor(Color.BLACK);
		layout_main = (RelativeLayout) item.findViewById(R.id.layout_main);
		item.setFocusableInTouchMode(true);
		layout_main.setOnClickListener(itemOnClickListener);

	}

	public void setContent(Intent intent) {
		this.intent = intent;
	}

	protected Intent getContent() {
		return intent;
	}

	public void setIndicate(int source, String text) {
		img_item.setImageResource(source);
		txv_item.setText(text);
		this.viewTitle = text;
	}

	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	protected void OnSelected() {
		// txv_item.setBackgroundResource(R.drawable.bg_text_bottomitem);
		layout_main.setBackgroundResource(R.drawable.bg_tab_select);
	}

	protected void OnLostSelected() {
		txv_item.setBackgroundDrawable(null);
		layout_main.setBackgroundDrawable(null);
	}

	public View getItem() {
		return item;
	}

	void focusChanged(String m) {
		if (this.id.equalsIgnoreCase(m)) {
			OnSelected();
		} else {
			OnLostSelected();
		}
	}
}
