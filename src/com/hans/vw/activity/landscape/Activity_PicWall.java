package com.hans.vw.activity.landscape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.data.Data_Photos_1;
import com.hans.vw.data.Data_Photos_2;
import com.hans.vw.data.Data_Photos_3;
import com.hans.vw.data.Photo;
import com.hans.vw.view.waterfall.FlowTag;
import com.hans.vw.view.waterfall.FlowView;
import com.hans.vw.view.waterfall.LazyScrollView;
import com.hans.vw.view.waterfall.LazyScrollView.OnScrollListener;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_picwall)
public class Activity_PicWall extends Activity_Base {
	private static final String TAG = "Activity_View";

	@InjectView(R.id.scroll)
	LazyScrollView scroll;
	@InjectView(R.id.layout_container)
	LinearLayout layout_container;

	@InjectView(R.id.txv_title)
	TextView txv_title;
	@Inject
	AssetManager asset_manager;
	@InjectView(R.id.img_add)
	ImageView img_add;

	private int scroll_height;
	private static int column_count = 3;
	private int page_count = 20;
	private int current_page = 0;
	private int item_width;
	private Display display;
	private int[] topIndex;
	private int[] bottomIndex;
	private int[] lineIndex;
	private int[] column_height;
	private HashMap<Integer, FlowView> iviews;
	private HashMap<Integer, String> pins;
	private ArrayList<LinearLayout> waterfall_items;
	private ArrayList<Photo> image_filenames;
	private int loaded_count = 0;
	private HashMap<Integer, Integer>[] pin_mark = null;
	private Context context;
	private int id;
	private String assets_folder = "";
	public ArrayList<Photo> more = new ArrayList<Photo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = this;
		display = this.getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count;
		String title = getIntent().getStringExtra("title");
		id = getIntent().getIntExtra("id", 0);
		txv_title.setText(title);
		img_add.setOnClickListener(onViewClickListener);
		// Photo p = new Photo();
		// p.setDescribe("1234");
		// p.setPath("/sdcard/vm/a40abe2052b0485dae948913afee9870.jpg");
		// more.add(p);
		showSpinnerDialog("加载景点中..");

		baseHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				InitLayout();
				dismissSpinnerDialog();
			}
		}, 1000);

	}

	private Handler handler;

	private void InitLayout() {

		column_height = new int[column_count];
		iviews = new HashMap<Integer, FlowView>();
		pins = new HashMap<Integer, String>();
		pin_mark = new HashMap[column_count];
		this.lineIndex = new int[column_count];
		this.bottomIndex = new int[column_count];
		this.topIndex = new int[column_count];
		for (int i = 0; i < column_count; i++) {
			lineIndex[i] = -1;
			bottomIndex[i] = -1;
			pin_mark[i] = new HashMap();
		}
		current_page = 0;
		loaded_count = 0;
		layout_container.removeAllViews();
		scroll.getView();
		scroll.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onTop() {

				Log.d(TAG, "Scroll to top");
			}

			@Override
			public void onScroll() {
				// TODO
			}

			@Override
			public void onBottom() {
				AddItemToContainer(++current_page, page_count);
			}

			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {

				scroll_height = scroll.getMeasuredHeight();
				Log.d(TAG, "scroll_height:" + scroll_height);

				if (t > oldt) {
					if (t > 2 * scroll_height) {

						for (int k = 0; k < column_count; k++) {

							LinearLayout localLinearLayout = waterfall_items
									.get(k);

							if (pin_mark[k].get(Math.min(bottomIndex[k] + 1,
									lineIndex[k])) <= t + 3 * scroll_height) {
								((FlowView) waterfall_items.get(k).getChildAt(
										Math.min(1 + bottomIndex[k],
												lineIndex[k]))).Reload();

								bottomIndex[k] = Math.min(1 + bottomIndex[k],
										lineIndex[k]);

							}
							Log.d("MainActivity",
									"headIndex:" + topIndex[k] + "  footIndex:"
											+ bottomIndex[k] + "  headHeight:"
											+ pin_mark[k].get(topIndex[k]));
							if (pin_mark[k].get(topIndex[k]) < t - 2
									* scroll_height) {

								int i1 = topIndex[k];
								topIndex[k]++;
								((FlowView) localLinearLayout.getChildAt(i1))
										.recycle();
								Log.d("MainActivity", "recycle,k:" + k
										+ " headindex:" + topIndex[k]);

							}
						}

					}
				} else {

					for (int k = 0; k < column_count; k++) {
						LinearLayout localLinearLayout = waterfall_items.get(k);
						if (pin_mark[k].get(bottomIndex[k]) > t + 3
								* scroll_height) {
							((FlowView) localLinearLayout
									.getChildAt(bottomIndex[k])).recycle();

							bottomIndex[k]--;
						}

						if (pin_mark[k].get(Math.max(topIndex[k] - 1, 0)) >= t
								- 2 * scroll_height) {
							((FlowView) localLinearLayout.getChildAt(Math.max(
									-1 + topIndex[k], 0))).Reload();
							topIndex[k] = Math.max(topIndex[k] - 1, 0);
						}
					}

				}

			}
		});

		waterfall_items = new ArrayList<LinearLayout>();
		handler = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {

				super.dispatchMessage(msg);
			}

			@Override
			public void handleMessage(Message msg) {

				// super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					FlowView v = (FlowView) msg.obj;
					int height = msg.arg2;
					String f = v.getFlowTag().getFileName();
					int columnIndex = GetMinValue(column_height);

					v.setColumnIndex(columnIndex);

					column_height[columnIndex] += height;

					pins.put(v.getId(), f);
					iviews.put(v.getId(), v);
					waterfall_items.get(columnIndex).addView(v);

					lineIndex[columnIndex]++;

					pin_mark[columnIndex].put(lineIndex[columnIndex],
							column_height[columnIndex]);
					bottomIndex[columnIndex] = lineIndex[columnIndex];
					break;
				}

			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};
		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					item_width, LayoutParams.WRAP_CONTENT);

			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			layout_container.addView(itemLayout);
		}
		try {
			initData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		AddItemToContainer(current_page, page_count);

	}

	boolean init = false;

	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;
		int imagecount = image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			Log.e("path:" + currentIndex,
					image_filenames.get(i % (imagecount - 1)).getPath());

			AddImage(image_filenames.get(i % (imagecount - 1)).getPath(),
					(int) (loaded_count % column_count), loaded_count);
			loaded_count++;

		}

	}

	private void AddImage(String filename, int rowIndex, int id) {
		Log.d("pic info", filename + "   " + "   " + rowIndex + "   " + id);

		FlowView item = new FlowView_pic(context);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler);
		FlowTag param = new FlowTag();
		param.setFlowId(id);
		param.setAssetManager(asset_manager);
		param.setFileName(filename);
		param.setItemWidth(item_width);

		item.setFlowTag(param);
		item.LoadImage();
	}

	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; ++i) {

			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}

	private void initData() throws IOException {
		if (image_filenames == null)
			image_filenames = new ArrayList<Photo>();
		image_filenames.clear();
		image_filenames.addAll(more);
		Log.e("more size", more.size() + "");
		switch (id) {
		case 1:
			assets_folder = Data_Photos_1.assets_folder;
			String[] pics = asset_manager.list(assets_folder);
			for (int i = 0; i < pics.length; i++) {
				Photo p = new Photo();
				p.setPath(assets_folder + "/" + pics[i]);
				String desc = Data_Photos_1.desc.get(i
						% (Data_Photos_1.desc.size() - 1));
				p.setDescribe(desc);
				image_filenames.add(p);
			}

			// image_filenames =
			// Arrays.asList();
			break;
		case 2:
			assets_folder = Data_Photos_2.assets_folder;
			String[] pics2 = asset_manager.list(assets_folder);
			for (int i = 0; i < pics2.length; i++) {
				Photo p = new Photo();
				p.setPath(assets_folder + "/" + pics2[i]);
				String desc = Data_Photos_2.desc.get(i
						% (Data_Photos_2.desc.size() - 1));
				p.setDescribe(desc);
				image_filenames.add(p);
			}
			break;
		case 3:
			assets_folder = Data_Photos_3.assets_folder;
			String[] pics3 = asset_manager.list(assets_folder);
			for (int i = 0; i < pics3.length; i++) {
				Photo p = new Photo();
				p.setPath(assets_folder + "/" + pics3[i]);
				String desc = Data_Photos_3.desc.get(i
						% (Data_Photos_3.desc.size() - 1));
				p.setDescribe(desc);
				image_filenames.add(p);
			}
			break;

		default:

			break;
		}

	}

	class FlowView_pic extends FlowView {

		public FlowView_pic(Context c) {
			super(c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			int pid = this.getFlowTag().getFlowId();
			String desc = image_filenames.get(pid).getDescribe();
			Intent intent = new Intent(Activity_PicWall.this,
					Activity_PicDetail.class);
			intent.putExtra("filename", this.getFlowTag().getFileName());
			intent.putExtra("describe", desc);
			startActivity(intent);

		}
	}

	private static final int RC_ADD = 1;

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == img_add) {
				Intent intent = new Intent(Activity_PicWall.this,
						Acticity_New.class);
				startActivityForResult(intent, RC_ADD);

			}

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == RC_ADD && resultCode == Activity.RESULT_OK) {
			String filepath = data.getStringExtra("filepath");
			String desc = data.getStringExtra("describe");
			Photo p = new Photo();
			p.setDescribe(desc);
			p.setPath(filepath);
			more.add(p);
			InitLayout();

		}

	};

}
