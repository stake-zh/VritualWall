package com.hans.vw.activity.base;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;

import com.hans.vw.application.HansApplication;
import com.hans.vw.view.dialog.Dialog_SpinnerDialog;

import roboguice.activity.RoboActivity;

public class Activity_Base extends RoboActivity {

	SharedPreferences preferences;
	Editor editor;
	HansApplication application;
	private Dialog_SpinnerDialog pdialog;
	private final int showPDialog = 1;
	private final int dissmissPDialog = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(Activity_Base.this);
		editor = preferences.edit();
		application = (HansApplication) getApplication();
	}

	public Handler baseHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case showPDialog: {
				if (pdialog == null) {
					pdialog = new Dialog_SpinnerDialog(Activity_Base.this);
					pdialog.setCancelable(false);
				}
				// pdialog.setMessage(msg.obj.toString());
				if (!pdialog.isShowing()) {
					pdialog.show();
				}
				break;
			}
			case dissmissPDialog: {
				if (pdialog != null)
					if (pdialog.isShowing()) {
						pdialog.dismiss();
					}
				break;
			}
			}
			return true;

		}
	});

	public void showSpinnerDialog(String string) {
		Message msg = baseHandler.obtainMessage();
		msg.what = showPDialog;
		msg.obj = string;
		baseHandler.sendMessage(msg);
	}

	public void dismissSpinnerDialog() {
		Message msg = baseHandler.obtainMessage();
		msg.what = dissmissPDialog;
		baseHandler.sendMessage(msg);
	}

	public String getPreference(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

	public int getPreference(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public boolean getPreference(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}

	public float getPreference(String key, float defValue) {
		return preferences.getFloat(key, defValue);
	}

	public long getPreference(String key, long defValue) {
		return preferences.getLong(key, defValue);
	}

	public void setPreference(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void setPreference(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public void setPreference(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setPreference(String key, float value) {
		editor.putFloat(key, value);
		editor.commit();
	}

	public void setPreference(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public HansApplication getDefaultApplication() {
		return application;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void showToast(String msg) {
		getDefaultApplication().showToast(msg);
	}

	public void back(View v) {
		finish();

	}

}
