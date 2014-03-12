package com.hans.vw.activity.travel;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ctsi.utils.DateUtil;
import com.google.gson.Gson;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.activity.travel.map.Activity_Map;
import com.hans.vw.data.Data_Travel;

@ContentView(R.layout.activity_route)
public class Activtiy_Route extends Activity_Base {

	@InjectView(R.id.list)
	ListView list;
	@Inject
	LayoutInflater mLayoutInflater;
	ArrayList<Route> routes = new ArrayList<Route>();

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String str = intent.getStringExtra("route");
			Route route = gson.fromJson(str, Route.class);
			routes.add(0, route);
			adapter.notifyDataSetChanged();
			getDefaultApplication().showToast("添加路线成功");

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		for (Route r : Data_Travel.routes) {
			routes.add(r);
		}
		list.setAdapter(adapter);
		list.setOnItemClickListener(onItemClickListener);
		registerReceiver(receiver, new IntentFilter("route"));

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
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
			view.setDetails(routes.get(arg0), arg0);
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
			return routes.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return routes.size();
		}
	};

	Gson gson = new Gson();
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			ArrayList<SelfBrick> bricks = routes.get(arg2).getBricks();
			String str = gson.toJson(bricks);
			Intent intent = new Intent(Activtiy_Route.this, Activity_Map.class);
			intent.putExtra("bricks", str);
			startActivity(intent);

		}
	};

	class ViewHolder extends LinearLayout {

		TextView txv_index, txv_name, txv_time, txv_text, txv_startTime,
				txv_endTime, txv_customerName;
		ImageView img_pp;
		Route route;

		public ViewHolder() {
			super(Activtiy_Route.this);
			// TODO Auto-generated constructor stub
			mLayoutInflater.inflate(R.layout.adapter_foot, this);
			txv_index = (TextView) findViewById(R.id.txv_index);
			txv_name = (TextView) findViewById(R.id.txv_name);
			txv_time = (TextView) findViewById(R.id.txv_time);
			txv_text = (TextView) findViewById(R.id.txv_text);
			txv_startTime = (TextView) findViewById(R.id.txv_startTime);
			txv_endTime = (TextView) findViewById(R.id.txv_endTime);
			img_pp = (ImageView) findViewById(R.id.img_pp);
			txv_customerName = (TextView) findViewById(R.id.txv_customerName);
		}

		public void setDetails(Route route, int index) {
			this.route = route;
			txv_index.setText(index + 1 + "");
			txv_name.setText(route.getName());
			txv_time.setText(DateUtil.Date2String(new Date(route.getTime()),
					"yyyy-MM-dd"));
			txv_startTime.setText(DateUtil.Date2String(
					new Date(route.getStartTime()), "yyyy-MM-dd HH:mm"));
			txv_endTime.setText(DateUtil.Date2String(
					new Date(route.getEndTime()), "yyyy-MM-dd HH:mm"));
			txv_text.setText(text(route));
			if (!route.getCustomer().equals("Hans")) {
				txv_customerName.setText("by:" + route.getCustomer());
				img_pp.setImageResource(R.drawable.icon_public);
			} else {
				img_pp.setImageResource(R.drawable.icon_private);
				txv_customerName.setText("");
			}

		}

		private String text(Route r) {
			String t = "(共&站)".replace("&", r.getBricks().size() + "");
			int i = 0;
			t += r.getBricks().get(i).getLocationDesc();
			i++;
			while (t.length() < 25 && i < r.getBricks().size()) {
				t += " > ";
				t += r.getBricks().get(i).getLocationDesc();
				i++;
			}
			return t + "...";

		}
	}
}
