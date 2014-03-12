package com.hans.vw.activity.travel;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
//import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;

public abstract class Activity_Bottom extends Activity_Base {
	ViewPager layout_container;
	LinearLayout layout_items;
	protected List<ItemView> itemViews;

	List<View> views;
	ItemLayoutParams itemLayoutParams;

	private LocalActivityManager manager = null;
	public Context context = Activity_Bottom.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bottomtabs);
		layout_container = (ViewPager) findViewById(R.id.layout_container);
		layout_items = (LinearLayout) findViewById(R.id.layout_items);
		itemLayoutParams = new ItemLayoutParams();
		layout_container.setFocusable(true);
		itemViews = new ArrayList<ItemView>();
		views = new ArrayList<View>();
		itemViews.addAll(setItems());

		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();
	}

	private void InitViewPager() {
		MyPagerAdapter mpAdapter = new MyPagerAdapter(views);
		for (int i = 0; i < itemViews.size(); i++) {
			Intent intent = itemViews.get(i).getContent();
			views.add(getView(itemViews.get(i).getId(), intent));

			AddItem(itemViews.get(i));

		}
		layout_container.setAdapter(mpAdapter);
		layout_container.setOnPageChangeListener(new MyOnPageChangeListener());
		// layout_container.setCurrentItem(0);
		itemViews.get(0).OnSelected();

	}

	// 动画图片偏移量
	private int offset = 0;
	// 当前页卡编号
	private int currIndex = 0;
	// 动画图片宽度
	private int bmpW;
	/**
	 * 页卡切换监听
	 */
	private final static String TAG = "ConfigTabActivity";

	public void setPage(String id) {
		int i;
		for (i = 0; i < itemViews.size(); i++) {
			if (itemViews.get(i).getId().equalsIgnoreCase(id)) {
				break;
			}
		}

		layout_container.setCurrentItem(i);
		setTitle(itemViews.get(i).getViewTitle());

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			String id = itemViews.get(arg0).getId();
			for (int i = 0; i < itemViews.size(); i++) {
				itemViews.get(i).focusChanged(id);
			}
			setTitle(itemViews.get(arg0).getViewTitle());

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private View getView(String id, Intent intent) {
		// Log.e("ttt", "---\t " + id);

		return manager.startActivity(id, intent).getDecorView();

	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	// protected abstract void setItems();
	protected abstract List<ItemView> setItems();

	protected void AddItem(ItemView item) {

		RelativeLayout relativeLayout = new RelativeLayout(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		relativeLayout.addView(item.getItem(), params);
		layout_items.addView(relativeLayout, itemLayoutParams);
	}

	public ViewPager getLayout_container() {
		return layout_container;
	}

	class ItemLayoutParams extends LinearLayout.LayoutParams {

		public ItemLayoutParams() {
			super(FILL_PARENT, FILL_PARENT, 1);
		}

	}

}
