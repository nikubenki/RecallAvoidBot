package me.xuxiaoxiao.chatapi.wechat.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.xuxiaoxiao.chatapi.wechat.WeChatClient;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.myentity.GroupRank;

public class GroupRankUtils {
	private static GroupRank rank = null;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void rank(WeChatClient WECHAT_CLIENT, WXMessage message) {

		// 初始化变量
		if (rank == null) {
			// 试图从文件读取
			Object obj = null;
			try {
				obj = SerializeUtils.deserialize("rank");
			} catch (Exception e) {

			}
			if (obj == null) {// 没有读取到文件，新建一个文件并序列化
				rank = new GroupRank();
				rank.setDate(sdf.format(new Date()));
				rank.setGrouprank(new LinkedHashMap<String, Integer>());
				SerializeUtils.serialize(rank, "rank");
			} else {
				rank = (GroupRank) obj;
				// 当天第一次初始化，清空数据
				if (!rank.getDate().equals(sdf.format(new Date()))) {
					rank.setDate(sdf.format(new Date()));
					rank.setGrouprank(new LinkedHashMap<String, Integer>());
				}
			}
		}
		// 记录群成员发言次数
		String fromUser = message.fromUser.name;
		Map<String, Integer> rkmap = rank.getGrouprank();
		if (rkmap.get(fromUser) == null) {
			rkmap.put(fromUser, 1);
		} else {
			Integer cnt = rkmap.get(fromUser);
			rkmap.put(fromUser, cnt + 1);
		}

		// 排序
		// 新建一个list用于排序
		List<Map.Entry<String, Integer>> sortList = new ArrayList<Map.Entry<String, Integer>>();
		sortList.addAll(rank.getGrouprank().entrySet());
		GroupRank gr = new GroupRank();
		Collections.sort(sortList, gr);

		// 用一个临时map，接收排序后的结果
		Map<String, Integer> cachemap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : sortList) {
//				System.out.println(entry.getKey() + ":" + entry.getValue());
			cachemap.put(entry.getKey(), entry.getValue());
		}
		rank.setGrouprank(cachemap);

		// 记录后存储到文件
		SerializeUtils.serialize(rank, "rank");
//		System.out.println("------------------------------------------------");
//		System.out.println(rank.toString());
	}

	public static void getrank(WeChatClient WECHAT_CLIENT, WXMessage message) {
		WECHAT_CLIENT.sendText(message.fromGroup, rank.toString());
	}
}
