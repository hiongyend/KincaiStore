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
 * @description 用户实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:53:00
 *
 */
public class UserInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private int userId;
	private String username;
	private String password;
	private String sex;
	private String email;
	private String face;
	private String regTime;
	private int activeFlag;
	private int integral;
	private String nickname;
	private int grade;
	private String loginState;//0没登录1登录
	private String uniqueId;//
	private String deviceId;//0android 1ios
	
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public UserInfo(int _id, int userId, String username, String password,
			String sex, String email, String face, String regTime,
			int activeFlag, int integral, String nickname, int grade,
			String loginState, String uniqueId, String deviceId) {
		super();
		this._id = _id;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.sex = sex;
		this.email = email;
		this.face = face;
		this.regTime = regTime;
		this.activeFlag = activeFlag;
		this.integral = integral;
		this.nickname = nickname;
		this.grade = grade;
		this.loginState = loginState;
		this.uniqueId = uniqueId;
		this.deviceId = deviceId;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public int getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}


	public int getIntegral() {
		return integral;
	}


	public void setIntegral(int integral) {
		this.integral = integral;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "UserInfo [_id=" + _id + ", userId=" + userId + ", username="
				+ username + ", password=" + password + ", sex=" + sex
				+ ", email=" + email + ", face=" + face + ", regTime="
				+ regTime + ", activeFlag=" + activeFlag + ", integral="
				+ integral + ", nickname=" + nickname + ", grade=" + grade
				+ ", loginState=" + loginState + ", uniqueId=" + uniqueId
				+ ", deviceId=" + deviceId + "]";
	}

	
	
	
}
