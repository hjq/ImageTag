package com.example.tagimage;

import java.io.Serializable;

import android.view.View;

public class TagResource implements Serializable{

	/**
	 * 记录标签的所有状态
	 */
	private static final long serialVersionUID = 1L;
	public long mId; // 用于编辑标签时控制数据更新
	public String id;
	public int type; // 1：自定义 2：化妆品
	public String title;
	public float xscale;
	public float yscale;	
}
