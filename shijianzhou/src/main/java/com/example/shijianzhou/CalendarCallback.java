package com.example.shijianzhou;

/**
 * 日历被点击后提供给其它类的回调接口。
 * 
 * @author zhou
 * 
 */
public interface CalendarCallback {
	/**
	 * 函数
	 * 
	 * @param scheduleYear
	 *            年
	 * @param scheduleMonth
	 *            月
	 * @param scheduleDay
	 *            日
	 */
	public void calendarcallback(String scheduleYear, String scheduleMonth,
			String scheduleDay);
}
