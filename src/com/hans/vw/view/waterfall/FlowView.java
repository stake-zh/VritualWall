package com.hans.vw.view.waterfall;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import com.hans.vw.data.Cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class FlowView extends ImageView implements
		View.OnClickListener, View.OnLongClickListener {
	private static final String TAG = "FlowView";
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	private int columnIndex; // 列
	private int rowIndex; // 行
	private Handler viewHandler;

	public FlowView(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		this.context = c;
		Init();
	}

	public FlowView(Context c, AttributeSet attrs) {
		super(c, attrs);
		this.context = c;
		Init();
	}

	public FlowView(Context c) {
		super(c);
		this.context = c;
		Init();
	}

	private void Init() {
		setOnClickListener(this);
		this.setOnLongClickListener(this);
		setAdjustViewBounds(true);
	}

	@Override
	public boolean onLongClick(View v) {
		Log.d(TAG, "onLongClick");
		Toast.makeText(context, "长按：" + this.flowTag.getFlowId(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			new LoadImageThread().start();
		}
	}

	/**
	 * 重新加载图片
	 */
	public void Reload() {
		if (this.bitmap == null && getFlowTag() != null) {

			new ReloadImageThread().start();
		}
	}

	/**
	 * 释放图片
	 */
	public void recycle() {
		setImageBitmap(null);
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}

	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public FlowView setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		return this;
	}

	class ReloadImageThread extends Thread {

		@Override
		public void run() {
			if (flowTag != null) {

				BufferedInputStream buf;
				try {
					buf = new BufferedInputStream(flowTag.getAssetManager()
							.open(flowTag.getFileName()));
					bitmap = BitmapFactory.decodeStream(buf);

				} catch (IOException e) {

					e.printStackTrace();
				}

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {
							setImageBitmap(bitmap);
						}
					}
				});
			}

		}
	}

	// 加载图片线程
	class LoadImageThread extends Thread {
		LoadImageThread() {
		}

		public void run() {

			if (flowTag != null) {

				if (Cache.img_cache.containsKey(flowTag.getFileName())
						&& Cache.img_cache.get(flowTag.getFileName()).get() != null) {

					bitmap = Cache.img_cache.get(flowTag.getFileName()).get();
				} else {

					InputStream buf = null;
					try {
						buf = new BufferedInputStream(flowTag.getAssetManager()
								.open(flowTag.getFileName()));
						Options opts = new Options();
						opts.inSampleSize = 2;
						bitmap = BitmapFactory.decodeStream(buf, null, opts);
						Cache.img_cache.put(flowTag.getFileName(),
								new SoftReference<Bitmap>(bitmap));

					} catch (Exception e) {
						e.printStackTrace();
						try {
							buf = new FileInputStream(flowTag.getFileName());
							bitmap = BitmapFactory.decodeStream(buf);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}

				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap != null) {
							int width = bitmap.getWidth();// 获取真实宽高
							int height = bitmap.getHeight();

							LayoutParams lp = getLayoutParams();

							int layoutHeight = (height * flowTag.getItemWidth())
									/ width;// 调整高度
							if (lp == null) {
								lp = new LayoutParams(flowTag.getItemWidth(),
										layoutHeight);
							}
							setLayoutParams(lp);

							setImageBitmap(bitmap);
							Handler h = getViewHandler();
							Message m = h.obtainMessage(flowTag.what, width,
									layoutHeight, FlowView.this);
							h.sendMessage(m);
						}
					}
				});

			}
		}
	}
}
