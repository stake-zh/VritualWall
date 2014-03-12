package com.hans.vw.activity.landscape;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ctsi.utils.Utils;
import com.google.inject.Inject;
import com.hans.vw.R;
import com.hans.vw.activity.base.Activity_Base;
import com.hans.vw.data.Cache;
import com.hans.vw.view.photoview.PhotoView;

@ContentView(R.layout.activity_picdetail)
public class Activity_PicDetail extends Activity_Base {

	@InjectView(R.id.img_body)
	PhotoView img_body;
	@InjectView(R.id.txv_describe)
	TextView txv_describe;
	@InjectView(R.id.layout_container)
	RelativeLayout layout_container;

	@Inject
	AssetManager asset_manager;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String filename = getIntent().getStringExtra("filename");
		String describe = getIntent().getStringExtra("describe");
		txv_describe.setText(describe);

		if (Cache.img_cache.containsKey(filename)) {
			bitmap = Cache.img_cache.get(filename).get();

		} else {
			InputStream buf;
			try {
				buf = new BufferedInputStream(asset_manager.open(filename));
				Options opts = new Options();
				opts.inSampleSize = 2;
				bitmap = BitmapFactory.decodeStream(buf, null, opts);
				Cache.img_cache
						.put(filename, new SoftReference<Bitmap>(bitmap));

			} catch (IOException e) {

				e.printStackTrace();
				bitmap = BitmapFactory.decodeFile(filename);
			}

		}

		if (bitmap != null) {

			int width = bitmap.getWidth();// 获取真实宽高
			int height = bitmap.getHeight();
			int item_width = Utils.convertDip2Px(this, 250);

			int layoutHeight = (height * item_width) / width;// 调整高度

			Log.e(item_width + "", layoutHeight + "");

			LayoutParams lp = new LayoutParams(item_width, layoutHeight);

			img_body.setLayoutParams(lp);
			img_body.invalidate();
			img_body.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}
