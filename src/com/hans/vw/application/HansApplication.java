package com.hans.vw.application;

import java.util.List;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.inject.Inject;
import com.hans.vw.data.Cache;
import com.hans.vw.view.dialog.Dialog_Tips;
import com.hans.vw.view.toast.CtsiToast;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts.Data;
import android.text.TextUtils;

public class HansApplication extends Application {

	private LocationClient mLocationClient = null;

	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		mLocationClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setScanSpan(500);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		mLocationClient.setLocOption(option);
	};

	public void location(BDLocationListener locationListener, long timeout)
			throws LocationStartedException {
		if (mLocationClient != null && mLocationClient.isStarted()
				&& locationListener != null) {
			mLocationClient.registerLocationListener(locationListener);
			mLocationClient.requestLocation();
			handler.postDelayed(stoplocationRunable, timeout);

		} else
			throw new LocationStartedException();
	}

	private Runnable stoplocationRunable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mLocationClient.isStarted()) {
				mLocationClient.stop();
			}
		}
	};

	public class LocationStartedException extends Exception {

	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		// Cache.img_cache.clear();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		// Cache.img_cache.clear();
	}

	private final int showToast = 3;
	private final int dialog_error = 4;
	private final int dialog_info = 5;
	private final int dialog_yesno = 6;
	private final int dialog_simple = 7;
	private final int dialog_full = 8;
	SharedPreferences preferences;
	CtsiToast toast;
	@Inject
	AssetManager asset_manager;

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case showToast: {
				if (toast == null)
					toast = new CtsiToast(getApplicationContext());
				toast.setText(msg.obj.toString());
				toast.show();

				break;
			}
			case dialog_info: {

				DialogInterface.OnClickListener listener = null;
				DialogHandler_Alert handler = (DialogHandler_Alert) msg.obj;
				Dialog_Tips dialog = new Dialog_Tips(handler.getActivity(),
						Dialog_Tips.TYPE_INFO, handler.getTitle(), handler
								.getMsg());
				dialog.setCancelable(false);
				dialog.setButton("OK", listener);
				dialog.show();
				break;
			}
			case dialog_error: {
				DialogInterface.OnClickListener listener = null;
				DialogHandler_Error handler = (DialogHandler_Error) msg.obj;
				Dialog_Tips dialog = new Dialog_Tips(handler.getActivity(),
						Dialog_Tips.TYPE_ERROR, handler.getTitle(), handler
								.getMsg());
				dialog.setCancelable(false);
				dialog.setButton("OK", listener);
				dialog.show();
				break;
			}
			case dialog_yesno: {
				DialogHandler_YesNo handler = (DialogHandler_YesNo) msg.obj;

				Dialog_Tips dialog = new Dialog_Tips(handler.getActivity(),
						Dialog_Tips.TYPE_YESNO, handler.getTitle(), handler
								.getMsg());
				dialog.setCancelable(false);
				dialog.setButton("确定", handler.getOklistener());
				dialog.setButton2("取消", handler.getCancellistener());
				dialog.show();

				break;
			}
			case dialog_simple: {
				DialogHandler_Simple handler = (DialogHandler_Simple) msg.obj;
				Dialog_Tips dialog = new Dialog_Tips(handler.getActivity(), -1,
						handler.getTitle(), handler.getMsg());
				dialog.setCancelable(false);
				dialog.setButton("确定", handler.getOklistener());
				dialog.show();
				break;
			}
			case dialog_full: {
				DialogHandler_FullDialog handler = (DialogHandler_FullDialog) msg.obj;

				Dialog_Tips dialog = new Dialog_Tips(handler.getActivity(), -1,
						handler.title, handler.message);
				dialog.setCancelable(false);
				dialog.setButton(handler.firstText, handler.firstlistener);
				dialog.show();
				if (!TextUtils.isEmpty(handler.thirdText)) {

					dialog.setButton3(handler.thirdText, handler.thirdListener);
					dialog.setButton2(handler.secondText,
							handler.secondListener);

				} else {
					if (!TextUtils.isEmpty(handler.secondText)) {
						dialog.setButton2(handler.secondText,
								handler.secondListener);
					}
				}
				dialog.show();
				break;
			}
			}

			return true;
		}
	});

	public void showToast(String mssage) {
		Message msg = handler.obtainMessage();
		msg.what = showToast;
		msg.obj = mssage;
		handler.sendMessage(msg);
	}

	public void ShowYesNoDialog(Activity activity, String title,
			String message, OnClickListener oklistener,
			OnClickListener cancellistener) {
		Message msg = handler.obtainMessage();
		msg.what = dialog_yesno;
		msg.obj = new DialogHandler_YesNo(activity, title, message, oklistener,
				cancellistener);
		handler.sendMessage(msg);
	}

	public void ShowFullDialog(Activity activity, String title, String message,
			String firstText, String secondText, String thirdText,
			OnClickListener firstlistener, OnClickListener secondListener,
			OnClickListener thirdListener) {
		Message msg = handler.obtainMessage();
		msg.what = dialog_full;
		msg.obj = new DialogHandler_FullDialog(activity, title, message,
				firstText, secondText, thirdText, firstlistener,
				secondListener, thirdListener);
		handler.sendMessage(msg);
	}

	public void ShowErrorAlertDialog(Activity activity, String title,
			String message) {
		Message msg = handler.obtainMessage();
		msg.what = dialog_error;
		msg.obj = new DialogHandler_Error(activity, title, message);
		handler.sendMessage(msg);
	}

	public void ShowInfoAlertDialog(Activity activity, String title,
			String message) {
		Message msg = handler.obtainMessage();
		msg.what = dialog_info;
		msg.obj = new DialogHandler_Alert(activity, title, message);
		handler.sendMessage(msg);
	}

	public void ShowSimpleDialog(Activity activity, String title,
			String message, DialogInterface.OnClickListener listener) {
		Message msg = handler.obtainMessage();
		msg.what = dialog_simple;
		msg.obj = new DialogHandler_Simple(activity, title, message, listener);
		handler.sendMessage(msg);
	}

}
