package com.example.tagimage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagImageView extends RelativeLayout {

	private Context mContext;
	public List<TagResource> mTagResList;
	public int mPointX;
	public int mPointY;
	public float mPercentX;
	public float mPercentY;
	int tagMaxWidth = 0;

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
		this.mContext = context;
	}

	/**
	 * 发话题 编辑图片添加标签
	 * @param content
	 * @param dx 横向坐标
	 * @param dy 纵向坐标
	 */
	public void addTag(int type, final String content, final int dx, final int dy) {
		if (mTagResList == null)
			mTagResList = new ArrayList<TagResource>();
		final long mId = System.currentTimeMillis() + (long) (Math.random() * 100);
		final TagResource mTagRes = new TagResource();
		coverToPercent(dx, dy);
		mTagRes.mId = mId;
		mTagRes.title = content;
		mTagRes.xscale = mPercentX;
		mTagRes.yscale = mPercentY;
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.tag, null);
		final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.tag_layout);
		final ImageView m_ivLeft = (ImageView) view.findViewById(R.id.tag_iv_left);
		final ImageView m_ivRight = (ImageView) view.findViewById(R.id.tag_iv_right);
		m_ivLeft.setBackgroundResource(getTagType(type));
		m_ivRight.setBackgroundResource(getTagType(type));
		final TextView m_tvTag = (TextView) view.findViewById(R.id.tag_text);
		m_tvTag.setText(content);

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

						moveTag(v, mx, my, mId);
						// 标签到达边界，再有往外移动的动作，才执行
						tagTextBgChange(v, m_tvTag, m_ivLeft, m_ivRight, Math.abs(mx) > 10);

						mDownInScreenX = (int) event.getRawX();
						mDownInScreenY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						mUpInScreenX = (int) event.getRawX();
						mUpInScreenY = (int) event.getRawY();
						// 模拟监听点击事件
						if (Math.abs(mUpInScreenX - mCurrentInScreenX) < 5 && Math.abs(mUpInScreenY - mCurrentInScreenY) < 5) {
							deleteTag(v, mId);
						}
						break;
					}
				}
				return true;
			}
		});

		this.addView(layout);
		layout.setVisibility(View.INVISIBLE);

		// 保证 setPosition 方法在 addView 方法后调用
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				setPosition(layout, m_tvTag, m_ivLeft, m_ivRight, dx, dy);
			}
		}, 100);

		mTagResList.add(mTagRes);
	}

	/**
	 * 话题详情页 添加单个标签
	 * @param content
	 * @param percentX 横向坐标占父view的百分比
	 * @param percentY 纵向坐标占父view的百分比
	 */
	public void addTag(int type, final String content,  float percentX, float percentY) {
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.tag, null);
		final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.tag_layout);
		final ImageView m_ivLeft = (ImageView) view.findViewById(R.id.tag_iv_left);
		final ImageView m_ivRight = (ImageView) view.findViewById(R.id.tag_iv_right);
		m_ivLeft.setBackgroundResource(getTagType(type));
		m_ivRight.setBackgroundResource(getTagType(type));
		final TextView m_tvTag = (TextView) view.findViewById(R.id.tag_text);
		m_tvTag.setText(content);

		m_tvTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			} 
		});

		this.addView(layout);
		layout.setVisibility(View.INVISIBLE);
		coverToCoordinates(percentX, percentY);

		// 保证 setPosition 方法在 addView 方法后调用
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				setPosition(layout, m_tvTag, m_ivLeft, m_ivRight, mPointX, mPointY);
			}
		}, 100);
	}

	/**
	 * 话题详情页 批量添加标签
	 * @param tagResList
	 */
	public void addAllTags(List<TagResource> tagResList) {
		if (tagResList == null) {
			return;
		} else {
			for (TagResource tagResource : tagResList) {
				if (!tagResource.title.equals("")) {
					addTag(tagResource.type, tagResource.title, tagResource.xscale, tagResource.yscale);
				}
			}
		}
	}
	
	/**
	 * 删除标签
	 * @param v
	 * @param id 标签id
	 */
	private void deleteTag(View v, long id) {
		this.removeView(v);
		if (mTagResList != null) {
			for (int i = 0; i < mTagResList.size(); i++) {
				if (mTagResList.get(i).mId == id) {
					mTagResList.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * 初始化，设置标签
	 * @param v
	 * @param tv
	 * @param dx
	 * @param dy
	 */
	private void setPosition(View v, TextView tv, ImageView iv_left, ImageView iv_right, int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		int l, t, r, b;
		if ((parentWidth - dx) >= v.getWidth()) {
			l = dx;
			r = l + v.getWidth();
			iv_left.setVisibility(VISIBLE);
			iv_right.setVisibility(GONE);
			tv.setBackgroundResource(R.drawable.tag_arrow_left);
		} else {
			r = dx;
			l = r - v.getWidth();
			iv_left.setVisibility(GONE);
			iv_right.setVisibility(VISIBLE);
			tv.setBackgroundResource(R.drawable.tag_arrow_right);
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

	/**
	 * 移动标签
	 * @param v
	 * @param dx 横向移动距离
	 * @param dy 纵向移动距离
	 * @param id 标签id
	 */
	private void moveTag(View v, int dx, int dy, long id) {
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

		refreshTagRes(l, t, id);
	}

	/**
	 * 标签达到边界时，再往外滑动，标签反向
	 * @param v
	 * @param tv
	 * @param isNeedChange
	 */
	private void tagTextBgChange(View v, final TextView tv, final ImageView iv_left, final ImageView iv_right, boolean isNeedChange) {
		final int width = tv.getWidth();
		if (v.getRight() == this.getWidth() && isNeedChange) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					iv_left.setVisibility(VISIBLE);
					iv_right.setVisibility(GONE);
					tv.setBackgroundResource(R.drawable.tag_arrow_left);
				}
			}, 100);
		} else if (v.getLeft() == 0 && isNeedChange) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					iv_left.setVisibility(GONE);
					iv_right.setVisibility(VISIBLE);
					tv.setBackgroundResource(R.drawable.tag_arrow_right);
				}
			}, 100);
		}
	}

	/**
	 * 坐标换算成百分比
	 * @param percentX
	 * @param percentY
	 */
	private void coverToCoordinates(float percentX, float percentY) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		mPointX = (int) (parentWidth * percentX);
		mPointY = (int) (parentHeight * percentY);
	}

	/**
	 * 百分比换算成坐标
	 * @param dx
	 * @param dy
	 */
	private void coverToPercent(int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		mPercentX = (float) (dx / parentWidth);
		mPercentY = (float) (dy / parentHeight);
	}

	/**
	 * 移动标签时，更新标签坐标
	 * @param dx
	 * @param dy
	 * @param id 标签id
	 */
	private void refreshTagRes(int dx, int dy, long id) {
		if (mTagResList != null) {
			for (int i = 0; i < mTagResList.size(); i++) {
				if (mTagResList.get(i).mId == id) {
					mTagResList.get(i).xscale = (float) (dx / this.getWidth());
					mTagResList.get(i).yscale = (float) (dy / this.getHeight());
					break;
				}
			}
		}
	}

	// 获取标签类型相对应的锚点图标
	private int getTagType(int type) {
		if (type == 1) {
			// 自定义
			return R.drawable.topic_icon_label_custom_small;
		} else {
			// 化妆品
			return R.drawable.topic_icon_label_makeup_small;
		}
	}
}
