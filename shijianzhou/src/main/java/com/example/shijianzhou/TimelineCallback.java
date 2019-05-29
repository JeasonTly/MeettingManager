package com.example.shijianzhou;

/**
 * 时间轴发现有会议开始或者结束的时候调用的回调方法。
 * 
 * @author zhou
 * 
 */
public interface TimelineCallback {

	/**
	 * 会议开始。
	 * 
	 * @param start
	 *            开始时间。
	 * @param end
	 *            结束时间。
	 */
	public void huiyiStarted(int start, int end);

	/**
	 * 会议结束。
	 * 
	 * @param start
	 *            开始时间。
	 * @param end
	 *            结束时间。
	 */
	public void huiyiStoped();
}
