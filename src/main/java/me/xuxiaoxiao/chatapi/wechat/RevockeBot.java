package me.xuxiaoxiao.chatapi.wechat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xuxiaoxiao.chatapi.wechat.entity.MessageCache;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXContact;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXRevoke;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXVerify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RevockeBot {
	public static Queue<MessageCache> q = new LinkedBlockingQueue<MessageCache>();
	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
	static File cacheFolder = new File("cache");
	private static List<String> groupList = new ArrayList<>();
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
//			System.out.println("获取到消息：" + GSON.toJson(message));
			if (message instanceof WXVerify) {
				// 是好友请求消息，自动同意好友申请
				WECHAT_CLIENT.passVerify((WXVerify) message);
			}

			// 检测到撤回消息
			if (message instanceof WXRevoke) {
//				System.out.println("撤回消息：" + GSON.toJson(message));

				// 只在特定几个群防撤回
				if (message.fromGroup != null) {
					String groupid = message.fromGroup.id;

//					if (!groupList.contains(groupid)) {
//						return;
//					}
				}
				// 个人消息撤回
				else {
//					return;
				}

				// 根据消息ID，从缓存中查找该消息
				WXMessage getMsg = null;
				WXRevoke revoceMsg = (WXRevoke) message;
				String messageid = revoceMsg.msgId + "";
				for (MessageCache c : q) {
					if (c.getMessageid().equals(messageid)) {
						getMsg = c.getMessagebody();
					}
				}
				// 发送撤回通知
				SendMessage.send(WECHAT_CLIENT, getMsg);

			} else {
				// 非撤回消息，加入缓存队列
				if (q.size() >= 100) {// 消息大于上限，移除一条后再缓存
					q.remove();
				}

				MessageCache cache = new MessageCache();
				cache.setMessageid(message.id + "");
				cache.setMessagebody(message);
				q.add(cache);
//				System.out.println("消息加入缓存队列，当前缓存池大小为--" + q.size());
			}

		}

		@Override
		public void onContact(WXContact contact, int operate) {
//			System.out.println(String.format("检测到联系人变更:%s:%s",
//					operate == WeChatClient.ADD_CONTACT ? "新增" : (operate == WeChatClient.DEL_CONTACT ? "删除" : "修改"),
//					contact.name));
		}
	}, cacheFolder);

	public static void main(String[] args) {
		// 启动模拟微信客户端
		WECHAT_CLIENT.startup();

		groupList.add("@@5977ceba8df1ba6a5e557bbd72f4616f6781a4338bade91fcb1ec8627f532a05");// 小群

	}
}
