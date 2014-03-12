package com.hans.vw.activity.wall;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ctsi.utils.Utils;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.activity.wall.PopWindow_Type.OnTypeSelectedListener;
import com.hans.vw.data.Cache;
import com.hans.vw.data.Data_PublicWall;
import com.hans.vw.data.Photo;

@ContentView(R.layout.activity_publicwall)
public class Activity_PublicWall extends Activity_Base {
	public static final int RC_ADD = 1;

	@InjectView(R.id.list)
	ListView list;
	@InjectView(R.id.btn_distance)
	Button btn_distance;
	@InjectView(R.id.img_add)
	View img_add;
	@InjectView(R.id.img_mode)
	ImageView img_mode;
	@Inject
	LayoutInflater mLayoutInflater;
	@Inject
	AssetManager asset_manager;
	ArrayList<Brick> allbricks = new ArrayList<Brick>();

	ArrayList<Brick> bricks = new ArrayList<Brick>();
	PopWindow_Type pop_distance;
	int distance = 10000;
	boolean isPrivate = false;

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btn_distance) {

				pop_distance.showAsDropDown(v, 0, 5);
			} else if (v == img_mode) {
				isPrivate = !isPrivate;
				if (isPrivate) {
					img_mode.setImageResource(R.drawable.icon_public);
				} else {
					img_mode.setImageResource(R.drawable.icon_private);
				}

				loadData();

			} else if (v == img_add) {
				Intent intent = new Intent(Activity_PublicWall.this,
						Acticity_NewBrick.class);
				startActivityForResult(intent, RC_ADD);

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

			loadData();
		}
	};

	private String distance(int d) {
		if (d > 1000)
			return d / 1000 + "km";
		else
			return d + "m";

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pop_distance = new PopWindow_Type(Activity_PublicWall.this, 150, 300,
				onTypeSelectedListener);
		btn_distance.setOnClickListener(onViewClickListener);
		img_mode.setOnClickListener(onViewClickListener);
		img_add.setOnClickListener(onViewClickListener);

		for (int i = 0; i < Data_PublicWall.bricks.size(); i++) {
			allbricks.add(Data_PublicWall.bricks.get(i));

		}

		loadData();

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

		TextView txv_name, txv_time, txv_distance, txv_text;
		ImageView img_pp, img_pic, img_feedback;
		Brick brick;

		public ViewHolder() {
			super(Activity_PublicWall.this);
			// TODO Auto-generated constructor stub
			mLayoutInflater.inflate(R.layout.adapter_publicwall, this);
			txv_name = (TextView) findViewById(R.id.txv_name);
			txv_time = (TextView) findViewById(R.id.txv_time);
			txv_distance = (TextView) findViewById(R.id.txv_distance);
			txv_text = (TextView) findViewById(R.id.txv_text);
			img_pp = (ImageView) findViewById(R.id.img_pp);
			img_pic = (ImageView) findViewById(R.id.img_pic);
			img_feedback = (ImageView) findViewById(R.id.img_feedback);

		}

		public void setDetail(Brick brick) {

			txv_name.setText(brick.getName());
			txv_time.setText(t(brick.getTime()));
			txv_distance.setText(brick.getDistance() + "m");
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
						Options opts = new Options();
						opts.inSampleSize = 2;
						bitmap = BitmapFactory.decodeStream(buf, null, opts);
						Cache.img_cache.put(filename,
								new SoftReference<Bitmap>(bitmap));
					} catch (IOException e) {

						e.printStackTrace();
						bitmap = BitmapFactory.decodeFile(filename);
					}

				}

				if (bitmap != null) {
					int width = bitmap.getWidth();// 获取真实宽高
					int height = bitmap.getHeight();
					int item_width = Utils.convertDip2Px(
							Activity_PublicWall.this, 150);

					int layoutHeight = (height * item_width) / width;// 调整高度

					Log.e(item_width + "", layoutHeight + "");

					LayoutParams lp = new LayoutParams(item_width, layoutHeight);
					img_pic.setLayoutParams(lp);
					img_pic.setImageBitmap(bitmap);
				}
			}
			img_feedback.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getDefaultApplication().ShowInfoAlertDialog(
							Activity_PublicWall.this, "提示", "这里链接回帖功能");
				}
			});

		}

		private String t(long time) {

			if (time / 3600 > 0) {

				return time / 3600 + "小时前";
			} else {
				if (time < 60)
					time = 60;

				return time / 60 + "分钟前";

			}

		}

	}

	public static final int IMAGE_WIDTH = 150;

	private void caculateBitmap(ImageView img, Bitmap bitmap) {

		if (bitmap != null) {

			int width = bitmap.getWidth();// 获取真实宽高
			int height = bitmap.getHeight();
			int item_width = Utils.convertDip2Px(Activity_PublicWall.this,
					IMAGE_WIDTH);
			int layoutHeight = (height * item_width) / width;// 调整高度
			LayoutParams lp = new LayoutParams(item_width, layoutHeight);
			img.setLayoutParams(lp);
			img.setImageBitmap(bitmap);
		}
	}

	private Bitmap ShotcutBitmap(String filepath) {
		Options opts = new Options();
		opts.inSampleSize = 4;// 图片缩小4*4=16倍
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, opts);
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_ADD && resultCode == Activity.RESULT_OK) {
			String filepath = data.getStringExtra("filepath");
			String desc = data.getStringExtra("describe");
			Brick b = new Brick();
			b.setDistance(200);
			b.setIsmale(true);
			b.setName("Hans");
			b.setPicPath(filepath);
			b.setText(desc);
			b.setTime(50);

			allbricks.add(0, b);

			loadData();

		}
	}

	private void loadData() {
		showSpinnerDialog("");

		bricks.clear();
		for (Brick b : allbricks) {

			if (b.getDistance() <= distance) {
				if (!isPrivate) {
					bricks.add(b);

				}

				else if (b.getName().equals("Hans")) {
					bricks.add(b);

				}
			}
		}
		Collections.sort(bricks, new Comparator<Brick>() {

			@Override
			public int compare(Brick lhs, Brick rhs) {
				// TODO Auto-generated method stub
				return (int) (lhs.getTime() - rhs.getTime());
			}
		});

		baseHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dismissSpinnerDialog();
				if (list.getAdapter() == null)
					list.setAdapter(adapter);
				else
					adapter.notifyDataSetChanged();
			}
		}, 1000);

	}

}
