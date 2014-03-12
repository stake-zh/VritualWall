package com.hans.vw.activity.landscape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.data.Data_Views;
import com.hans.vw.view.waterfall.FlowTag;
import com.hans.vw.view.waterfall.FlowView;
import com.hans.vw.view.waterfall.LazyScrollView;
import com.hans.vw.view.waterfall.LazyScrollView.OnScrollListener;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_picwall)
public class Activity_View extends Activity_Base {
	private static final String TAG = "Activity_View";

	@InjectView(R.id.scroll)
	LazyScrollView scroll;
	@InjectView(R.id.layout_container)
	LinearLayout layout_container;

	@InjectView(R.id.txv_title)
	TextView txv_title;

	@InjectView(R.id.img_add)
	ImageView img_add;
	@Inject
	AssetManager asset_manager;

	private int scroll_height;
	private static int column_count = 2;
	private int page_count = 15;
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
	private List<String> image_filenames;
	private int loaded_count = 0;
	private HashMap<Integer, Integer>[] pin_mark = null;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = this;
		display = this.getWindowManager().getDefaultDisplay();
		item_width = display.getWidth() / column_count;
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

		txv_title.setText("景点一览");
		img_add.setVisibility(View.GONE);

		showSpinnerDialog("加载景点中..");
		baseHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				InitLayout();
				dismissSpinnerDialog();
			}
		}, 1500);

	}

	private Handler handler;

	private void InitLayout() {
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
			image_filenames = Arrays.asList(asset_manager
					.list(Data_Views.assets_folder));
			for (String s : image_filenames) {

				Log.e("file", s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		AddItemToContainer(current_page, page_count);
	}

	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;

		int imagecount = image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			loaded_count++;
			AddImage(image_filenames.get(i % (imagecount)),
					(int) (loaded_count % column_count), loaded_count);
		}

	}

	private void AddImage(String filename, int rowIndex, int id) {
		FlowView item = new FlowView_views(context);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler);
		FlowTag param = new FlowTag();
		param.setFlowId(id);
		param.setAssetManager(asset_manager);
		param.setFileName(Data_Views.assets_folder + "/" + filename);
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

}
