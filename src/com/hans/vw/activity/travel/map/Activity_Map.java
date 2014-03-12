package com.hans.vw.activity.travel.map;

import java.util.ArrayList;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.OverlayItem;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hans.vw.R;
import com.hans.vw.activity.travel.SelfBrick;
import com.hans.vw.application.HansApplication;

public class Activity_Map extends Activity implements MKGeneralListener {

	@InjectView(R.id.mapview)
	com.baidu.mapapi.map.MapView mMapView;
	private MapController mMapController = null;
	private static BMapManager mBMapManager = null;
	public static final String key = "19AE4EE093F0D58B4E356B3BE2E01A601E6DF3E0";
	private MyOverlay itemizedOverlay;
	HansApplication application;
	ArrayList<SelfBrick> bricks;
	Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String str = getIntent().getStringExtra("bricks");

		bricks = gson.fromJson(str, new TypeToken<ArrayList<SelfBrick>>() {
		}.getType());

		application = (HansApplication) getApplication();
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(this);
			if (!mBMapManager.init(key, this)) {
				getDefaultApplication().showToast("初始化失败");
				return;
			}
		}

		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setOnTouchListener(null);
		mMapView.setDoubleClickZooming(false);
		mMapView.setAlwaysDrawnWithCacheEnabled(true);
		mMapView.setAnimationCacheEnabled(false);
		initMapView();
	}

	@Override
	protected void onPause() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	private void initMapView() {
		mMapView.getOverlays().clear();
		Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
		itemizedOverlay = new MyOverlay(marker, mMapView);
		mMapView.getOverlays().add(itemizedOverlay);
		initBricks(bricks);

	}

	private synchronized void initBricks(ArrayList<SelfBrick> bricks) {

		for (int i = 0; i < bricks.size(); i++) {
			SelfBrick b = bricks.get(i);
			String title = b.getLocationDesc();
			int late6 = b.getLat();
			int lone6 = b.getLon();

			GeoPoint p = new GeoPoint(late6, lone6);

			MyOverlayItem overlayItem = new MyOverlayItem(p, title, b.getText());
			int imagasrc = R.drawable.icon_marka;
			switch (i) {
			case 1:
				imagasrc = R.drawable.icon_markb;
				break;
			case 2:
				imagasrc = R.drawable.icon_markc;
				break;
			case 3:
				imagasrc = R.drawable.icon_markd;
				break;
			case 4:
				imagasrc = R.drawable.icon_marke;
				break;
			case 5:
				imagasrc = R.drawable.icon_markf;
				break;
			}
			overlayItem.setMarker(getResources().getDrawable(imagasrc));
			itemizedOverlay.addItem(overlayItem);
			//
		}
		mMapView.refresh();
	}

	@Override
	public void onGetNetworkState(int arg0) {
		// TODO Auto-generated method stub
		getDefaultApplication().ShowErrorAlertDialog(Activity_Map.this, "提示",
				"请检查您的网络");

	}

	@Override
	public void onGetPermissionState(int arg0) {
		// TODO Auto-generated method stub
		getDefaultApplication().ShowErrorAlertDialog(Activity_Map.this, "提示",
				"请检查您的KEY");

	}

	public HansApplication getDefaultApplication() {
		return application;
	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			MyOverlayItem item = (MyOverlayItem) getItem(index);
			for (Object o : itemizedOverlay.getAllItem()) {
				MyOverlayItem myo = (MyOverlayItem) o;
				if (myo != item) {
					myo.hideBalloon();
				}
			}

			item.createAndDisplayBalloonOverlay();
			mMapController.animateTo(item.getPoint());
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {

			return false;
		}

	}

	class MyOverlayItem extends OverlayItem {
		BalloonOverlayView balloonView;
		View clickRegion, closeRegion;
		SelfBrick brick;

		protected BalloonOverlayView<OverlayItem> createBalloonOverlayView() {
			return new BalloonOverlayView<OverlayItem>(Activity_Map.this, 30);
		}

		public MyOverlayItem(GeoPoint arg0, String arg1, String arg2) {
			super(arg0, arg1, arg2);
			// TODO Auto-generated constructor stub
		}

		public void hideBalloon() {
			if (balloonView != null) {
				balloonView.setVisibility(View.GONE);
			}
		}

		private boolean createAndDisplayBalloonOverlay() {
			boolean isRecycled = false;
			if (balloonView == null) {
				balloonView = createBalloonOverlayView();
			} else {
				isRecycled = true;
			}
			clickRegion = (View) balloonView
					.findViewById(R.id.balloon_inner_layout);
			closeRegion = (View) balloonView.findViewById(R.id.balloon_close);
			if (closeRegion != null) {

				closeRegion.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hideBalloon();
					}
				});

			}
			View v = balloonView.findViewById(R.id.balloon_disclosure);
			if (v != null) {
				v.setVisibility(View.VISIBLE);
			}

			balloonView.setVisibility(View.GONE);

			GeoPoint point = getPoint();
			MapView.LayoutParams params = new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					point, MapView.LayoutParams.BOTTOM_CENTER);
			params.mode = MapView.LayoutParams.MODE_MAP;

			balloonView.setVisibility(View.VISIBLE);

			if (isRecycled) {
				balloonView.setLayoutParams(params);
			} else {
				mMapView.addView(balloonView, params);
			}
			balloonView.setData(this);

			return isRecycled;
		}

	}

	public void back(View v) {
		finish();

	}

}
