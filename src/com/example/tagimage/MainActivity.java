package com.example.tagimage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {
	private RelativeLayout m_rlMark;
	private Button m_btnTagType1;
	private Button m_btnTagType2;
	private Button button;
	private TagImageView tagImageView;
	private int mDownInScreenX = 0;
	private int mDownInScreenY = 0;
	private boolean isExist = false;

	private List<RelativeLayout> markRelativeLayoutList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		tagImageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDownInScreenX = (int) event.getX();
					mDownInScreenY = (int) event.getY();
					swichState(isExist);
					break;
				default:
					break;
				}
				return true;
			}

		});
	}

	private void findViews() {
		m_btnTagType1 = (Button) findViewById(R.id.btn_tag_1);
		m_btnTagType1.setOnClickListener(this);
		m_btnTagType2 = (Button) findViewById(R.id.btn_tag_2);
		m_btnTagType2.setOnClickListener(this);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
		tagImageView = (TagImageView) findViewById(R.id.image_layout);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			tagImageView.removeAllViews();
			break;
		case R.id.btn_tag_1:
			tagImageView.addTag("我要翻转", (mDownInScreenX + 25), (mDownInScreenY - 30));
			swichState(true);
			break;
		case R.id.btn_tag_2:
			tagImageView.addTag("我要翻转", (mDownInScreenX + 25), (mDownInScreenY - 30));
			swichState(true);
			break;
		default:
			break;
		}
	}

	private void addMark() {
		if (markRelativeLayoutList == null) {
			markRelativeLayoutList = new ArrayList<RelativeLayout>();
		}
		m_rlMark = new RelativeLayout(this);
		m_rlMark = (RelativeLayout) View.inflate(this, R.layout.mark, null);
		tagImageView.addView(m_rlMark);
		m_rlMark.setVisibility(View.INVISIBLE);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				setPosition(m_rlMark, mDownInScreenX, mDownInScreenY);
			}
		}, 100);		
		markRelativeLayoutList.add(m_rlMark);
	}

	private void deleteMark() {
		if (markRelativeLayoutList!= null && markRelativeLayoutList.size() != 0) {
			tagImageView.removeView(markRelativeLayoutList.get(markRelativeLayoutList.size() - 1));
		}
		markRelativeLayoutList.remove(markRelativeLayoutList.size() - 1);
	}

	private void swichState(boolean isExist) {
		if (isExist) {
			this.isExist = false;
			m_btnTagType1.setVisibility(View.INVISIBLE);
			m_btnTagType2.setVisibility(View.INVISIBLE);
			deleteMark();
		} else {
			this.isExist = true;
			m_btnTagType1.setVisibility(View.VISIBLE);
			m_btnTagType2.setVisibility(View.VISIBLE);
			addMark();
		}

	}

	private void setPosition(View v, int dx, int dy) {
		int parentWidth = tagImageView.getWidth();
		int parentHeight = tagImageView.getHeight();
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
}
