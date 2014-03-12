package com.hans.vw.activity.landscape;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctsi.utils.DataUtil;
import com.ctsi.utils.DateUtil;
import com.ctsi.utils.Utils;
import com.google.inject.Inject;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.data.Cache;

@ContentView(R.layout.activity_new)
public class Acticity_New extends Activity_Base {

	private int RESULT_CAPTURE_IMAGE = 1;
	private static String savePath = Environment.getExternalStorageDirectory()
			.getAbsoluteFile() + "/vm";
	@InjectView(R.id.layout_pp)
	View layout_pp;
	@InjectView(R.id.img_add_pic)
	ImageView img_add_pic;
	@InjectView(R.id.img_pp)
	ImageView img_pp;
	@InjectView(R.id.txv_pp)
	TextView txv_pp;
	@InjectView(R.id.txv_limit)
	TextView txv_limit;
	@InjectView(R.id.edt_text)
	EditText edt_text;
	@InjectView(R.id.txv_submit)
	View txv_submit;
	String filename;

	@Inject
	InputMethodManager mInput;

	boolean isPublic = true;

	private OnClickListener onViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == layout_pp) {
				isPublic = !isPublic;
				if (isPublic) {
					txv_pp.setText("公开");
					txv_pp.setTextColor(getResources().getColor(R.color.blue));
					img_pp.setImageResource(R.drawable.compose_publicbutton_background);

				} else {

					txv_pp.setText("私人");
					txv_pp.setTextColor(getResources().getColor(R.color.red));
					img_pp.setImageResource(R.drawable.compose_privatebutton_background);

				}

			} else if (v == img_add_pic) {
				String fileName = DataUtil.getUUID() + ".jpg";
				Intent imageCaptureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				File out = new File(savePath, fileName);
				Uri uri = Uri.fromFile(out);
				imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE);
			} else if (v == txv_submit) {

				getDefaultApplication().ShowYesNoDialog(Acticity_New.this,
						"提示", "确认要上传次照片",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								showSpinnerDialog("");
								baseHandler.postDelayed(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										dismissSpinnerDialog();
										Intent data = new Intent();
										data.putExtra("filepath", filename);
										data.putExtra("describe", edt_text
												.getText().toString());
										setResult(Activity.RESULT_OK, data);
										finish();
									}
								}, 1000);

							}
						}, null);

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("onCreate", "123");
		File f = new File(savePath);
		f.mkdirs();
		edt_text.requestFocus();
		edt_text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				100) });
		edt_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				txv_limit.setText(s.length() + "/" + 100);

			}
		});
		layout_pp.setOnClickListener(onViewClickListener);
		img_add_pic.setOnClickListener(onViewClickListener);
		txv_submit.setOnClickListener(onViewClickListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == RESULT_CAPTURE_IMAGE)
				&& (resultCode == Activity.RESULT_OK)) {
			// Check if the result includes a thumbnail Bitmap
			readIntent(data);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	private void readIntent(Intent data) {
		if (data != null) {
			Log.e("readIntent", "123");
			// TODO Do something with the full image stored
			// in outputFileUri. Perhaps copying it to the app folder
			Uri uri = data.getData();
			String path = uri.getPath();
			Options options = new Options();
			options.inSampleSize = 2;
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			Cache.img_cache.put(path, new SoftReference<Bitmap>(bm));
			img_add_pic.setImageBitmap(bm);
			filename = path;
			img_add_pic.setOnClickListener(null);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		baseHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mInput.showSoftInput(edt_text, 0);
			}
		}, 400);

	}
}
