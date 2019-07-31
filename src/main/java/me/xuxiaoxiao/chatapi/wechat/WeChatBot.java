package me.xuxiaoxiao.chatapi.wechat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXContact;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXUser;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXText;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXUnknown;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXVerify;
import me.xuxiaoxiao.chatapi.wechat.utils.GroupLeaveUtils;
import me.xuxiaoxiao.chatapi.wechat.utils.GroupRankUtils;

import java.io.File;
import java.util.Scanner;

public class WeChatBot {
	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
	static File cacheFolder = new File("cache");
	/**
	 * 新建一个模拟微信客户端，并绑定一个简单的监听器
	 */
	public static WeChatClient WECHAT_CLIENT = new WeChatClient(new WeChatClient.WeChatListener() {
		@Override
		public void onQRCode(String qrCode) {
			System.out.println("onQRCode：" + qrCode);
		}

		@Override
		public void onLogin() {
			System.out.println(String.format("onLogin：您有%d名好友、活跃微信群%d个", WECHAT_CLIENT.userFriends().size(),
					WECHAT_CLIENT.userGroups().size()));
		}

		@Override
		public void onMessage(WXMessage message) {
//            System.out.println("获取到消息：" + GSON.toJson(message));

			// 针对图央电影群
			if (message.fromGroup != null && message.fromGroup.name.contains("图央")) {
				// 退群播报bot
				GroupLeaveUtils.wholeave(WECHAT_CLIENT, message);
				// 24小时群发言排行榜记录
				GroupRankUtils.rank(WECHAT_CLIENT, message);
				if (message.content.toLowerCase().equals("jojo 排行榜")) {
					GroupRankUtils.getrank(WECHAT_CLIENT, message);
				}
			}
			
		}

		@Override
		public void onContact(WXContact contact, int operate) {
			System.out.println(String.format("检测到联系人变更:%s:%s",
					operate == WeChatClient.ADD_CONTACT ? "新增" : (operate == WeChatClient.DEL_CONTACT ? "删除" : "修改"),
					contact.name));
		}
	}, cacheFolder);

	public static void main(String[] args) {
		// 启动模拟微信客户端
		WECHAT_CLIENT.startup();
	}
}