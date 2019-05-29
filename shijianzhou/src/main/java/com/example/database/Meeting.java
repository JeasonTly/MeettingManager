package com.example.database;

import org.litepal.crud.DataSupport;

/**
 * �����ģ��
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
	 * ��������
	 */
	private String date;

	/**
	 * ���鿪ʼʱ��
	 */
	private int start;

	/**
	 * �������ʱ��
	 */
	private int end;

	/**
	 * �������
	 */
	private String title;

	/**
	 * Ԥ����
	 */
	private String yudingren;

	/**
	 * ��ϯ��
	 */
	private String chuxiren;

	/**
	 * �ⲿ��Ա
	 */
	private String waiburen;

	/**
	 * ��ע
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
