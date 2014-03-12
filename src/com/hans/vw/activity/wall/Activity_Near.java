package com.hans.vw.activity.wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.activity.wall.Activity_PublicWall.ViewHolder;
import com.hans.vw.activity.wall.PopWindow_Type.OnTypeSelectedListener;
import com.hans.vw.data.Data_PublicWall;

@ContentView(R.layout.activity_near)
public class Activity_Near extends Activity_Base {
	@InjectView(R.id.list)
	ListView list;
	@InjectView(R.id.btn_distance)
	Button btn_distance;
	PopWindow_Type pop_distance;
	int distance = 10000;
	@Inject
	LayoutInflater mLayoutInflater;
	@Inject
	AssetManager asset_manager;

	ArrayList<POI> allPois = new ArrayList<POI>();

	ArrayList<POI> Pois = new ArrayList<POI>();

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btn_distance) {

				pop_distance.showAsDropDown(v, 0, 5);
			}
		}
	};
	private OnTypeSelectedListener onTypeSelectedListener = new OnTypeSelectedListener() {

		@Override
		public void selected(int type) {
			// TODO Auto-generated method stub

			switch (type) {
			case PopWindow_Type.TYPE_100:
				distance = 100;
				break;
			case PopWindow_Type.TYPE_500:
				distance = 500;
				break;
			case PopWindow_Type.TYPE_1000:
				distance = 1000;
				break;
			case PopWindow_Type.TYPE_5000:
				distance = 5000;
				break;
			case PopWindow_Type.TYPE_ALL:
				distance = 10000;
				break;
			}
			btn_distance.setText("范围:" + distance(distance));

			data();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn_distance.setOnClickListener(onViewClickListener);
		pop_distance = new PopWindow_Type(Activity_Near.this, 150, 300,
				onTypeSelectedListener);
		for (int i = 0; i < Data_PublicWall.pois.size(); i++) {
			allPois.add(Data_PublicWall.pois.get(i));

		}

		getDefaultApplication().ShowSimpleDialog(Activity_Near.this, "提示",
				"终端需要先对您进行定位.", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						showSpinnerDialog("");
						baseHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								dismissSpinnerDialog();
								data();

							}
						}, 3000);

					}
				});

	}

	private BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder view;
			if (arg1 == null)
				view = new ViewHolder();
			else
				view = (ViewHolder) arg1;

			view.setDetails(Pois.get(arg0), arg0);

			return view;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return Pois.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Pois.size();
		}
	};

	class ViewHolder extends LinearLayout {

		TextView txv_index, txv_name, txv_customerName, txv_distance, txv_text,
				txv_vote, txv_brick;
		ImageView img_pp, img_vote;
		View layout_vote, layout_brick;
		RatingBar rb_rate;
		POI poi;
		int index;

		public ViewHolder() {
			super(Activity_Near.this);
			// TODO Auto-generated constructor stub
			mLayoutInflater.inflate(R.layout.adapter_near, this);

			txv_index = (TextView) findViewById(R.id.txv_index);
			txv_name = (TextView) findViewById(R.id.txv_name);
			txv_distance = (TextView) findViewById(R.id.txv_distance);

			txv_customerName = (TextView) findViewById(R.id.txv_customerName);
			txv_text = (TextView) findViewById(R.id.txv_text);
			txv_brick = (TextView) findViewById(R.id.txv_brick);
			txv_vote = (TextView) findViewById(R.id.txv_vote);
			img_pp = (ImageView) findViewById(R.id.img_pp);
			img_vote = (ImageView) findViewById(R.id.img_vote);
			layout_vote = findViewById(R.id.layout_vote);
			layout_brick = findViewById(R.id.layout_brick);
			rb_rate = (RatingBar) findViewById(R.id.rb_rate);

		}

		public void setDetails(POI p, int index) {
			this.poi = p;
			this.index = index;
			txv_index.setText(String.valueOf(index + 1));
			txv_name.setText(p.getName());
			txv_distance.setText(distance(p.getDistance()));
			if (p.isCustom()) {
				img_pp.setVisibility(View.VISIBLE);
				txv_customerName.setText(" by:" + p.getCustomerName());
			} else {
				img_pp.setVisibility(View.GONE);
				txv_customerName.setVisibility(View.GONE);

			}
			rb_rate.setRating((float) p.getRate());
			txv_text.setText(p.getText());
			txv_vote.setText(String.valueOf(p.getGreate()));
			txv_brick.setText(String.valueOf(p.getDiscuss()));
			if (p.isHasGreated()) {
				layout_vote
						.setBackgroundResource(R.drawable.btn_readdetail_pressed);
				layout_vote.setOnClickListener(null);
				img_vote.setImageResource(R.drawable.icon_for_active);
				txv_vote.setTextColor(Color.RED);
			} else {
				layout_vote
						.setBackgroundResource(R.drawable.btn_readdetail_normal);
				layout_vote.setOnClickListener(onClickListener);
				img_vote.setImageResource(R.drawable.icon_for_enable);

				txv_vote.setTextColor(Color.DKGRAY);

			}
			layout_brick.setOnClickListener(onClickListener);

		}

		private OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v == layout_vote) {
					poi.setGreate(poi.getGreate() + 1);
					poi.setHasGreated(true);

					setDetails(poi, index);
				} else if (v == layout_brick) {
					Intent intent = new Intent(Activity_Near.this,
							Activity_PublicWall.class);
					startActivity(intent);
				}

			}
		};

	}

	private void data() {
		Pois.clear();

		POI cp = new POI(0, 0, "当前位置", "北纬39度，东经116度", 100, 6, false, "", 0,
				false, 10);

		Pois.add(cp);

		for (POI p : allPois) {

			if (p.getDistance() <= distance) {

				Pois.add(p);

			}
		}
		Collections.sort(Pois, new Comparator<POI>() {

			@Override
			public int compare(POI lhs, POI rhs) {
				// TODO Auto-generated method stub
				return Double.compare(rhs.rate, lhs.rate);
			}
		});

		if (list.getAdapter() == null)
			list.setAdapter(adapter);
		else
			adapter.notifyDataSetChanged();

	}

	private String distance(int d) {
		if (d > 1000)
			return d / 1000 + "km";
		else
			return d + "m";

	}
}
