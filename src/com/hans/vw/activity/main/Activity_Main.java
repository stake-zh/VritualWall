package com.hans.vw.activity.main;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.activity.landscape.Activity_View;
import com.hans.vw.activity.travel.BottomAcivity_Travel;
import com.hans.vw.activity.wall.Activity_Near;
import com.hans.vw.activity.wall.Activity_PublicWall;

@ContentView(R.layout.activity_main)
public class Activity_Main extends Activity_Base {

	@InjectView(R.id.layout_view)
	View layout_view;
	@InjectView(R.id.layout_wall)
	View layout_wall;
	@InjectView(R.id.layout_travel)
	View layout_travel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		layout_view.setOnClickListener(onViewClickListener);
		layout_wall.setOnClickListener(onViewClickListener);
		layout_travel.setOnClickListener(onViewClickListener);
	}

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == layout_view) {
				Intent intent = new Intent(Activity_Main.this,
						Activity_View.class);
				startActivity(intent);

			} else if (v == layout_wall) {
				Intent intent = new Intent(Activity_Main.this,
						Activity_Near.class);
				startActivity(intent);
			} else if (v == layout_travel) {
				Intent intent = new Intent(Activity_Main.this,
						BottomAcivity_Travel.class);
				startActivity(intent);
			}

		}
	};

}
