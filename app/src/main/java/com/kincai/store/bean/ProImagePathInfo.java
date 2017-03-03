package com.kincai.store.bean;

import java.io.Serializable;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 商品图片路径实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:54:37
 *
 */
public class ProImagePathInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private int pId;
	private String albumPath;
	public ProImagePathInfo() {
	}
	public ProImagePathInfo(int _id, int pId, String albumPath) {
		super();
		this._id = _id;
		this.pId = pId;
		this.albumPath = albumPath;
	}
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getAlbumPath() {
		return albumPath;
	}
	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}
	@Override
	public String toString() {
		return "ProImagePathInfo [_id=" + _id + ", pId=" + pId + ", albumPath="
				+ albumPath + "]";
	}
	
	
	
	
}
