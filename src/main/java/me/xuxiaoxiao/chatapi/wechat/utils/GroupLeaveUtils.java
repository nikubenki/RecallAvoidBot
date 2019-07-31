package me.xuxiaoxiao.chatapi.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.xuxiaoxiao.chatapi.wechat.WeChatClient;
import me.xuxiaoxiao.chatapi.wechat.entity.MessageCache;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXContact;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXGroup;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXUser;
import me.xuxiaoxiao.chatapi.wechat.entity.contact.WXGroup.Member;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXRevoke;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXUnknown;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXVerify;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.GroupLayout;

public class GroupLeaveUtils {
	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
	private static HashMap<String, Member> oldMembers = new HashMap<String, WXGroup.Member>();
	private static HashMap<String, Member> newMembers = new HashMap<String, WXGroup.Member>();
	private static int memberSize = 0;
	private static String[] leaveWords = { "拉黑吧！有事漂流瓶联系！", "我挥一挥衣袖，不带走一片云彩", "" };

	public static void wholeave(WeChatClient WECHAT_CLIENT, WXMessage message) {

		// 收到电影群消息
		if (message.fromGroup != null && message.fromGroup.name.contains("图央")) {
			// 初始化群员通讯录
			if (oldMembers.size() == 0) {
				WXGroup grp = message.fromGroup;
				oldMembers = grp.members;
				memberSize = oldMembers.size();
				System.out.println("------------------------群员通讯录已就位，当前群成员数量为：" + memberSize);
			}
			// 群组成员数变动
			if (message.fromGroup.members.size() != memberSize) {
				System.out.println("------------------------检测到群聊" + message.fromGroup.name + "通讯录变更");
				newMembers = message.fromGroup.members;
				for (String key : oldMembers.keySet()) {
					Member mem = oldMembers.get(key);
					if (!newMembers.containsKey(key)) {
						WECHAT_CLIENT.sendText(message.fromGroup, "检测到疑似【" + mem.name + "】已退群————拉黑吧，有事漂流瓶联系！");
//						System.out.println("检测到群聊" + message.fromGroup.name + " 疑似【" + mem.name + "】已退群");
						// 更新旧群员通讯录
						oldMembers = message.fromGroup.members;
					}
				}
			}

		}
	}
}