package com.hans.vw.activity.travel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.ctsi.utils.DateUtil;
import com.ctsi.utils.Utils;
import com.google.gson.Gson;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.activity.travel.wheel.NumericWheelAdapter;
import com.hans.vw.activity.travel.wheel.OnWheelChangedListener;
import com.hans.vw.activity.travel.wheel.WheelView;
import com.hans.vw.activity.wall.Brick;
import com.hans.vw.data.Cache;
import com.hans.vw.data.Data_PublicWall;
import com.hans.vw.data.Data_Travel;

@ContentView(R.layout.activity_selfbricks)
public class Activity_SelfBricks extends Activity_Base {

	public static final int RC_ADD = 1;
	@InjectView(R.id.list)
	ListView list;
	@InjectView(R.id.img_add)
	View img_add;
	@InjectView(R.id.img_ok)
	ImageView img_ok;
	@InjectView(R.id.img_cancel)
	ImageView img_cancel;
	@InjectView(R.id.img_time)
	ImageView img_time;
	ArrayList<SelfBrick> allbricks = new ArrayList<SelfBrick>();
	ArrayList<SelfBrick> bricks = new ArrayList<SelfBrick>();
	ArrayList<SelfBrick> selectedBricks = new ArrayList<SelfBrick>();

	@Inject
	LayoutInflater mLayoutInflater;
	@Inject
	AssetManager asset_manager;
	long time;
	boolean selectable = false;

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == img_add) {

				getDefaultApplication().ShowYesNoDialog(
						Activity_SelfBricks.this, "提示", "分享一组新的旅途日记？",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								selectable = true;

								select();
							}
						}, null);

			} else if (v == img_time) {
				showDialog();
			} else if (v == img_ok) {
				ArrayList<SelfBrick> selectSelfBricks = new ArrayList<SelfBrick>();
				for (SelfBrick sb : bricks) {
					if (sb.select) {
						selectSelfBricks.add(sb);
					}
				}
				if (selectSelfBricks.size() > 0) {
					new TextDialog(Activity_SelfBricks.this, selectSelfBricks)
							.show();

				} else {
					getDefaultApplication().ShowErrorAlertDialog(
							Activity_SelfBricks.this, "提示", "据点数量不能为0!");
				}

			} else if (v == img_cancel) {
				selectable = false;
				select();
				adapter.notifyDataSetChanged();
			}

		}
	};
	Gson gson = new Gson();

	class TextDialog extends AlertDialog {
		EditText edt_name;
		ArrayList<SelfBrick> brs;
		private DialogInterface.OnClickListener ok = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = edt_name.getText().toString();
				Route route = new Route();
				route.setTime(new Date().getTime());
				route.setStartTime(brs.get(brs.size() - 1).getTime());
				route.setEndTime(brs.get(0).getTime());
				route.setName(name);
				route.setBricks(brs);
				route.setCustomer("Hans");

				String str = gson.toJson(route);
				Intent intent = new Intent("route");
				intent.putExtra("route", str);
				sendBroadcast(intent);

				selectable = false;
				select();
				adapter.notifyDataSetChanged();

			}
		};
		private DialogInterface.OnClickListener cancel = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		};

		public TextDialog(Context context, ArrayList<SelfBrick> brs) {
			super(context);
			// TODO Auto-generated constructor stub
			edt_name = new EditText(Activity_SelfBricks.this);
			this.brs = brs;
			setCancelable(false);
			setView(edt_name);
			setTitle("请输入路线名称");
			setButton("确认", ok);
			setButton2("取消", cancel);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		img_add.setOnClickListener(onViewClickListener);
		for (int i = 0; i < Data_PublicWall.bricks.size(); i++) {
			allbricks.add(Data_Travel.bricks.get(i));

		}
		img_time.setOnClickListener(onViewClickListener);
		select();
		loadData(new Date().getTime());

	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ViewHolder v = (ViewHolder) arg1;
			if (selectedBricks.contains(v.brick)) {
				v.remove();
				selectedBricks.remove(v.brick);
			} else {
				v.add();
				selectedBricks.add(v.brick);
			}

		}
	};

	private void select() {

		if (!selectable) {
			img_add.setVisibility(View.VISIBLE);
			img_ok.setVisibility(View.GONE);
			img_cancel.setVisibility(View.GONE);
			list.setOnItemClickListener(null);
			img_ok.setOnClickListener(null);
			img_cancel.setOnClickListener(null);
			for (SelfBrick b : bricks) {
				b.select = false;
			}

		} else {
			selectedBricks.clear();
			img_add.setVisibility(View.GONE);
			img_ok.setVisibility(View.VISIBLE);
			img_cancel.setVisibility(View.VISIBLE);
			list.setOnItemClickListener(onItemClickListener);
			img_ok.setOnClickListener(onViewClickListener);
			img_cancel.setOnClickListener(onViewClickListener);

		}

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

			view.setDetail(bricks.get(arg0));

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
			return bricks.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bricks.size();
		}
	};

	class ViewHolder extends LinearLayout {

		TextView txv_name, txv_time, txv_text;
		ImageView img_pp, img_pic;
		View layout_detail;
		SelfBrick brick;

		public ViewHolder() {
			super(Activity_SelfBricks.this);
			// TODO Auto-generated constructor stub
			mLayoutInflater.inflate(R.layout.adapter_selfbrick, this);
			txv_name = (TextView) findViewById(R.id.txv_name);
			txv_time = (TextView) findViewById(R.id.txv_time);
			txv_text = (TextView) findViewById(R.id.txv_text);
			img_pp = (ImageView) findViewById(R.id.img_pp);
			img_pic = (ImageView) findViewById(R.id.img_pic);
			layout_detail = findViewById(R.id.layout_detail);

		}

		public void remove() {
			brick.select = false;
			layout_detail
					.setBackgroundResource(R.drawable.skin_cell_background);
		}

		public void add() {
			brick.select = true;
			layout_detail
					.setBackgroundResource(R.drawable.skin_cell_background_highlighted);

		}

		public void setDetail(SelfBrick brick) {
			this.brick = brick;
			if (brick.select) {

				layout_detail
						.setBackgroundResource(R.drawable.skin_cell_background_highlighted);

			} else {
				layout_detail
						.setBackgroundResource(R.drawable.skin_cell_background);
			}

			txv_name.setText(brick.getLocationDesc());
			txv_time.setText(DateUtil.Date2String(new Date(brick.getTime()),
					"yyyy年MM月dd日 HH点mm分 "));
			txv_text.setText(brick.getText());

			if (brick.isIsmale())
				img_pp.setImageResource(R.drawable.contact_head_male);
			else
				img_pp.setImageResource(R.drawable.contact_head_female);

			if (TextUtils.isEmpty(brick.getPicPath())) {

				img_pic.setImageBitmap(null);
				img_pic.setVisibility(View.GONE);

			} else {

				Bitmap bitmap;
				img_pic.setVisibility(View.VISIBLE);
				String filename = brick.getPicPath();
				Log.e("filename", filename);
				if (Cache.img_cache.containsKey(filename)) {
					bitmap = Cache.img_cache.get(filename).get();

				} else {
					InputStream buf;
					try {
						buf = new BufferedInputStream(
								asset_manager.open(filename));
						bitmap = BitmapFactory.decodeStream(buf);

					} catch (IOException e) {

						e.printStackTrace();
						bitmap = BitmapFactory.decodeFile(filename);
					}

				}

				if (bitmap != null) {
					int width = bitmap.getWidth();// 获取真实宽高
					int height = bitmap.getHeight();
					int item_width = Utils.convertDip2Px(
							Activity_SelfBricks.this, 120);

					int layoutHeight = (height * item_width) / width;// 调整高度

					Log.e(item_width + "", layoutHeight + "");

					LayoutParams lp = new LayoutParams(item_width, layoutHeight);
					img_pic.setLayoutParams(lp);
					img_pic.setImageBitmap(bitmap);
				}
			}

		}

	}

	private void loadData(long time) {

		bricks.clear();
		for (SelfBrick b : allbricks) {
			if (b.time <= time)
				bricks.add(b);
		}
		Collections.sort(bricks, new Comparator<SelfBrick>() {

			@Override
			public int compare(SelfBrick lhs, SelfBrick rhs) {
				// TODO Auto-generated method stub
				return (int) (rhs.getTime() - lhs.getTime());
			}
		});
		if (list.getAdapter() == null)
			list.setAdapter(adapter);
		else
			adapter.notifyDataSetChanged();

	}

	WheelView yearView, monthView, dayView;
	private NumericWheelAdapter yearAdapter;
	private NumericWheelAdapter monthAdapter;
	private NumericWheelAdapter dayAdapter;

	private void showDialog() {
		final Dialog dialog = new Dialog(Activity_SelfBricks.this,
				R.style.dialog);
		Window win = dialog.getWindow();
		// win.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		win.setWindowAnimations(R.style.AnimBottomPopup);
		View dialogView = mLayoutInflater.inflate(
				R.layout.layout_popup_daypicker, null);
		// initWheel
		yearView = (WheelView) dialogView.findViewById(R.id.wheel_year);
		monthView = (WheelView) dialogView.findViewById(R.id.wheel_month);
		dayView = (WheelView) dialogView.findViewById(R.id.wheel_day);
		TextView txvBtnOk = (TextView) dialogView.findViewById(R.id.txv_ok);
		txvBtnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, selectedYear);
				c.set(Calendar.MONTH, selectedMonth - 1);
				c.set(Calendar.DAY_OF_MONTH, selectedDay);
				time = c.getTimeInMillis();
				showSpinnerDialog("");
				baseHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						loadData(time);
						dismissSpinnerDialog();
					}
				}, 1000);

			}
		});
		initWheelView();
		dialog.setContentView(dialogView);
		dialog.show();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = LayoutParams.FILL_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
	}

	private void initWheelView() {

		yearAdapter = new NumericWheelAdapter(1970, 2020);
		yearView.setAdapter(yearAdapter);
		yearView.setLabel("年");
		yearView.setCyclic(false);
		yearView.addChangingListener(wheelChangedListener);

		monthAdapter = new NumericWheelAdapter(1, 12);
		monthView.setAdapter(monthAdapter);
		monthView.setLabel("月");
		monthView.setCyclic(true);
		monthView.addChangingListener(wheelChangedListener);

		dayAdapter = new NumericWheelAdapter(1, 31);
		dayView.setAdapter(dayAdapter);
		dayView.setLabel("日");
		dayView.setCyclic(true);
		dayView.addChangingListener(wheelChangedListener);

		Calendar c = Calendar.getInstance();
		if (time != 0) {
			c.setTimeInMillis(time);
		}

		int curYear = c.get(Calendar.YEAR);
		selectedYear = curYear;
		int curMonth = c.get(Calendar.MONTH);

		selectedMonth = curMonth;
		int curDay = c.get(Calendar.DAY_OF_MONTH);
		selectedDay = curDay;
		yearView.setCurrentItem(curYear - 1970);
		monthView.setCurrentItem(curMonth);
		dayView.setCurrentItem(curDay - 1);

	}

	int selectedYear, selectedMonth, selectedDay;
	private OnWheelChangedListener wheelChangedListener = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (wheel == yearView) {
				selectedYear = Integer.valueOf(yearAdapter.getItem(newValue));
			} else if (wheel == monthView) {
				selectedMonth = Integer.valueOf(monthAdapter.getItem(newValue));
				switch (selectedMonth) {
				case 4:
				case 6:
				case 9:
				case 11:
					dayAdapter.setMaxValue(30);
					break;
				case 2:
					if ((selectedYear % 4 == 0 && selectedYear % 100 != 0)
							|| selectedYear % 400 == 0) {
						dayAdapter.setMaxValue(29);
					} else {
						dayAdapter.setMaxValue(28);
					}
					break;
				default:
					dayAdapter.setMaxValue(31);
					break;
				}
			} else if (wheel == dayView) {
				selectedDay = Integer.valueOf(dayAdapter.getItem(newValue));
			}
		}
	};

}
