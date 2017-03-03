package com.kincai.store.bean;


public class RobotChatMessageInfo {
	private String name;
	private String msg;
	private Type type;
	public enum Type {
		INCOMING, OUTCOMING
	}

	public RobotChatMessageInfo() {
	}

	public RobotChatMessageInfo(String msg, Type type) {
		super();
		this.msg = msg;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
