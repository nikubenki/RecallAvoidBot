package me.xuxiaoxiao.chatapi.wechat;

import java.io.File;

import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXContact;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXFile;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXImage;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXText;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXVideo;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXVoice;

/**
 * 通用发送撤回消息工具类
 * 
 * @author Herrywxr
 *
 */
public class SendMessage {
	public static void send(WeChatClient WECHAT_CLIENT, WXMessage getMsg) {
		WeChatApi wxAPI = WECHAT_CLIENT.getWxAPI();
		if (getMsg == null) {
			return;
		}
		WXContact target = null;
		if (getMsg.fromUser != null && getMsg.fromGroup != null) {// 群消息
			target = getMsg.fromGroup;
		} else if (getMsg.fromUser != null) {// 用户消息
			target = getMsg.fromUser;
		}

		// 判断消息类型
		if (getMsg instanceof WXText) {// 文本消息，直接发送
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了消息：\n【" + getMsg.content + "】");
		} else if (getMsg instanceof WXImage) {// 图片消息
			File img = wxAPI.webwxgetmsgimg(getMsg.id, "big");
			// 先发送文字提示
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了一条图片消息");
			// 再发送媒体
			WECHAT_CLIENT.sendFile(target, img);
		} else if (getMsg instanceof WXVoice) {// 语音消息
			File voi = wxAPI.webwxgetvoice(getMsg.id);
			// 发送文字提示
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了一条语音消息");
			// 再发送媒体
			WECHAT_CLIENT.sendFile(target, voi);
		} else if (getMsg instanceof WXVideo) {// 视频消息
			File vid = wxAPI.webwxgetvideo(getMsg.id);
			// 先发送文字提示
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了一条视频消息");
			// 再发送媒体
			WECHAT_CLIENT.sendFile(target, vid);
		} else if (getMsg instanceof WXFile) {// 附件消息
			// 先发送文字提示
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了一条附件消息（暂不支持发送文件）");
		} else {// 其他消息
			WECHAT_CLIENT.sendText(target, "@" + getMsg.fromUser.name + " 撤回了一条消息\n【不支持的消息类型】");
		}

	}
}
