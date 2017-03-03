package com.kincai.store.bean;

import java.io.Serializable;

public class HomeGridInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private String title;
	private String imageUrl;
	
	public HomeGridInfo() {

	}

	public HomeGridInfo(int _id, String title, String imageUrl) {
		super();
		this._id = _id;
		this.title = title;
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "HomeGridInfo [_id=" + _id + ", title=" + title + ", imageUrl="
				+ imageUrl + "]";
	}
	
	
	
	
	
}
