package me.xuxiaoxiao.chatapi.wechat.myentity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class GroupRank implements Serializable, Comparator<Map.Entry<String, Integer>> {
	private String date;
	private Map<String, Integer> grouprank;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<String, Integer> getGrouprank() {
		return grouprank;
	}

	public void setGrouprank(Map<String, Integer> grouprank) {
		this.grouprank = grouprank;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("24小时群发言排行榜\n\n");
		int seq = 1;
		for (String key : grouprank.keySet()) {
			sb.append(seq + ". " + key + "\t" + grouprank.get(key) + "次\n");
			seq++;
			if (seq > 10) {
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public int compare(Entry<String, Integer> rk1, Entry<String, Integer> rk2) {
		return rk2.getValue() - rk1.getValue();
	}

}
