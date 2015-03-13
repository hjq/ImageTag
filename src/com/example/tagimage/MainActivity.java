package com.example.tagimage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button button;
	private TagImageView tagImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button);
		tagImageView = (TagImageView) findViewById(R.id.image_layout);
		button.setOnClickListener(new OnClickListener() {
			int i = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tagImageView.addTag("meilapp" + i++, 40, 40);
			}
		});
	}
}
