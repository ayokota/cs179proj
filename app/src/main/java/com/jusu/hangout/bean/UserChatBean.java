package com.jusu.hangout.bean;


public class UserChatBean extends Bean{

	/** 聊天内容   */
	private String tMessage;
	/** 头像    */
	private Integer portrait;
	/**  发送信息当前时间  */
	private String time;
	/** 用户id  */
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String gettMessage() {
		return tMessage;
	}
	public void settMessage(String tMessage) {
		this.tMessage = tMessage;
	}
	public Integer getPortrait() {
		return portrait;
	}
	public void setPortrait(Integer portrait) {
		this.portrait = portrait;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public UserChatBean(String tMessage, Integer portrait, String time,
						int id) {
		super();
		this.tMessage = tMessage;
		this.portrait = portrait;
		this.time = time;
		this.id = id;
	}
	public UserChatBean() {
		super();
	}
	@Override
	public String toString() {
		return "UserChatBean [tMessage=" + tMessage + ", portrait="
				+ portrait + ", time=" + time + ", id=" + id + "]";
	}
	
}
