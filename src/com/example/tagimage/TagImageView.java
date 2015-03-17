package com.example.tagimage;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagImageView extends RelativeLayout {

	private Context mContext;
	private List<View> mTagViewList;

	public TagImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public TagImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public TagImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void addTag(String content, final int dx, final int dy) {
		if (mTagViewList == null)
			mTagViewList = new ArrayList<View>();
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.tag, null);
		TextView text = (TextView) view.findViewById(R.id.tag_text);
		final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.tag_layout);
		text.setText(content);

		layout.setOnTouchListener(new OnTouchListener() {
			int mCurrentInScreenX = 0;
			int mCurrentInScreenY = 0;
			int mDownInScreenX = 0;
			int mDownInScreenY = 0;
			int mUpInScreenX = 0;
			int mUpInScreenY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (v.getId() == R.id.tag_layout) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mCurrentInScreenX = (int) event.getRawX();
						mCurrentInScreenY = (int) event.getRawY();
						mDownInScreenX = (int) event.getRawX();
						mDownInScreenY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						int mx = x - mDownInScreenX;
						int my = y - mDownInScreenY;

						move(v, mx, my);

						mDownInScreenX = (int) event.getRawX();
						mDownInScreenY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						mUpInScreenX = (int) event.getRawX();
						mUpInScreenY = (int) event.getRawY();
						// 模拟监听点击事件
						if (Math.abs(mUpInScreenX - mCurrentInScreenX) < 5 && Math.abs(mUpInScreenY - mCurrentInScreenY) < 5) {
							deleteTag(v);
						}
						break;
					}
				}

				return true;
			}
		});
		this.addView(layout);
		layout.setVisibility(View.INVISIBLE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setPosition(layout, dx, dy);
			}
		}, 100);
		
		mTagViewList.add(layout);
	}
	
	private void deleteTag(View v) {
		this.removeView(v);
		if (mTagViewList.size() != 0) {
			mTagViewList.remove(v);
		}
	}

	private void setPosition(View v, int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		int l, t, r, b;
		if ((parentWidth - dx) >= v.getWidth()) {
			l = dx;
			r = l + v.getWidth();
		} else {
			r = parentWidth;
			l = parentWidth - v.getWidth();
		}
		if ((parentHeight - dy) >= v.getHeight()) {
			t = dy;
			b = l + v.getHeight();
		} else {
			b = parentHeight;
			t = parentHeight - v.getHeight();
		}
		v.layout(l, t, r, b);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;
		v.setLayoutParams(params);
		v.setVisibility(View.VISIBLE);
	}
	
	private void move(View v, int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		int l = v.getLeft() + dx;
		int t = v.getTop() + dy;
		if (l < 0) {
			l = 0;
		} else if ((l + v.getWidth()) >= parentWidth) {
			l = parentWidth - v.getWidth();
		}
		if (t < 0) {
			t = 0;
		} else if ((t + v.getHeight()) >= parentHeight) {
			t = parentHeight - v.getHeight();
		}
		int r = l + v.getWidth();
		int b = t + v.getHeight();
		v.layout(l, t, r, b);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;
		v.setLayoutParams(params);
		v.setVisibility(View.VISIBLE);
	}

}
