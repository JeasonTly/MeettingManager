package com.example.database;

import org.litepal.crud.DataSupport;

/**
 * 会议表模型
 * 
 * @author zhou
 * 
 */
public class Meeting extends DataSupport {

	/**
	 * id
	 */
	private int id;

	/**
	 * 会议日期
	 */
	private String date;

	/**
	 * 会议开始时间
	 */
	private int start;

	/**
	 * 会议结束时间
	 */
	private int end;

	/**
	 * 会议标题
	 */
	private String title;

	/**
	 * 预定人
	 */
	private String yudingren;

	/**
	 * 出席人
	 */
	private String chuxiren;

	/**
	 * 外部人员
	 */
	private String waiburen;

	/**
	 * 备注
	 */
	private String beizhu;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the yudingren
	 */
	public String getYudingren() {
		return yudingren;
	}

	/**
	 * @param yudingren
	 *            the yudingren to set
	 */
	public void setYudingren(String yudingren) {
		this.yudingren = yudingren;
	}

	/**
	 * @return the chuxiren
	 */
	public String getChuxiren() {
		return chuxiren;
	}

	/**
	 * @param chuxiren
	 *            the chuxiren to set
	 */
	public void setChuxiren(String chuxiren) {
		this.chuxiren = chuxiren;
	}

	/**
	 * @return the waiburen
	 */
	public String getWaiburen() {
		return waiburen;
	}

	/**
	 * @param waiburen
	 *            the waiburen to set
	 */
	public void setWaiburen(String waiburen) {
		this.waiburen = waiburen;
	}

	/**
	 * @return the beizhu
	 */
	public String getBeizhu() {
		return beizhu;
	}

	/**
	 * @param beizhu
	 *            the beizhu to set
	 */
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

}
