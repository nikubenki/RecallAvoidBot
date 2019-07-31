package me.xuxiaoxiao.chatapi.wechat.entity;

import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;

public class MessageCache {
	private String messageid;
	private WXMessage messagebody;

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public WXMessage getMessagebody() {
		return messagebody;
	}

	public void setMessagebody(WXMessage messagebody) {
		this.messagebody = messagebody;
	}


}
