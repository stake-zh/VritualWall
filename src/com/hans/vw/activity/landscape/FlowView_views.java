package com.hans.vw.activity.landscape;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.view.waterfall.FlowView;

public class FlowView_views extends FlowView {

	Activity_Base activity;

	public FlowView_views(Context c) {
		super(c);
		this.activity = (Activity_Base) c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(activity, Activity_PicWall.class);
		switch (this.getFlowTag().getFlowId()) {
		case 1:
			intent.putExtra("id", getId());
			intent.putExtra("title", "故宫");
			activity.startActivity(intent);

			break;
		case 2:
			intent.putExtra("id", getId());
			intent.putExtra("title", "天安门");
			activity.startActivity(intent);

			break;
		case 3:
			intent.putExtra("id", getId());
			intent.putExtra("title", "颐和园");
			activity.startActivity(intent);

			break;

		default:
			activity.getDefaultApplication().ShowInfoAlertDialog(activity,
					"提示", "系统未补充有效数据");

			break;
		}

	}
}
