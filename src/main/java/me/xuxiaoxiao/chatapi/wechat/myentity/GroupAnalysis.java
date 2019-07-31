package me.xuxiaoxiao.chatapi.wechat.myentity;

import java.io.Serializable;

public class GroupAnalysis implements Serializable {
	private int ldln_today = 0;
	private int ldln_total = 0;
	private int nmnr_today = 0;
	private int nmnr_total = 0;

	public int getLdln_today() {
		return ldln_today;
	}

	public void setLdln_today(int ldln_today) {
		this.ldln_today = ldln_today;
	}

	public int getLdln_total() {
		return ldln_total;
	}

	public void setLdln_total(int ldln_total) {
		this.ldln_total = ldln_total;
	}

	public int getNmnr_today() {
		return nmnr_today;
	}

	public void setNmnr_today(int nmnr_today) {
		this.nmnr_today = nmnr_today;
	}

	public int getNmnr_total() {
		return nmnr_total;
	}

	public void setNmnr_total(int nmnr_total) {
		this.nmnr_total = nmnr_total;
	}

}
