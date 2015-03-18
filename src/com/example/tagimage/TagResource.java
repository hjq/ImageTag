package com.example.tagimage;

import java.io.Serializable;

import android.view.View;

public class TagResource implements Serializable{

	/**
	 * 记录标签的所有状态
	 */
	private static final long serialVersionUID = 1L;
	public View m_tvTag;
	public String text;
	public float percentX;
	public float percentY;
	
}
