package me.xuxiaoxiao.chatapi.wechat.utils;

import java.util.HashMap;
import java.util.Map;

import me.xuxiaoxiao.chatapi.wechat.WeChatClient;
import me.xuxiaoxiao.chatapi.wechat.entity.message.WXMessage;
import me.xuxiaoxiao.chatapi.wechat.myentity.GroupAnalysis;
import me.xuxiaoxiao.chatapi.wechat.myentity.GroupRank;

public class GroupAnalysisUtils {
	private static String monvid = "@caa974a57f497182583199e8212593c6d35d75d60f598d8ed53ae30b12ef6367";
	private static GroupAnalysis analysis = null;
	

	public static void analysis(WeChatClient WECHAT_CLIENT, WXMessage message) {
		// 初始化变量
		if (analysis == null) {
			// 试图从文件读取
			Object obj = SerializeUtils.deserialize("analysis");
			if (obj == null) {// 没有读取到文件，新建一个文件并序列化
				analysis = new GroupAnalysis();
				SerializeUtils.serialize(analysis, "analysis");
			} else {
				analysis = (GroupAnalysis) obj;
				// 当天第一次初始化
				analysis.setLdln_today(0);
				analysis.setNmnr_today(0);
			}

		}

		// 收到电影群消息
		if (message.fromGroup != null && message.fromGroup.name.contains("图央")) {
			if (message.fromUser.id.equals(monvid)) {
				String content = message.content;
				// 魔女又说懒得理你了
				if (content.contains("懒得理你")) {
					analysis.setLdln_today(analysis.getLdln_today() + 1);
					analysis.setLdln_total(analysis.getLdln_total() + 1);
					// 发送群聊消息
					WECHAT_CLIENT.sendText(message.fromGroup, "【魔女又说“懒得理你”提醒】\n是谁又让魔女不耐烦了呢？你们这群魔鬼！\n当前是今天第"
							+ analysis.getLdln_today() + "次\n有记录以来是第" + analysis.getLdln_total() + "次。");
				}
				// 魔女又说你们男人
				else if (content.contains("你们男人") || (content.contains("你们") && content.contains("男人"))) {
					analysis.setNmnr_today(analysis.getNmnr_today() + 1);
					analysis.setNmnr_total(analysis.getNmnr_total() + 1);
					// 发送群聊消息
					WECHAT_CLIENT.sendText(message.fromGroup, "【魔女又说“你们男人”提醒】\n又是哪个男人叫魔女烦心！男同胞们可长点心吧！\n当前是今天第"
							+ analysis.getLdln_today() + "次\n有记录以来是第" + analysis.getLdln_total() + "次。");
				}
			}
		}

	}
}
