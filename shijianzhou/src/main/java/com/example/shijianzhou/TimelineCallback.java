package com.example.shijianzhou;

/**
 * ʱ���ᷢ���л��鿪ʼ���߽�����ʱ����õĻص�������
 * 
 * @author zhou
 * 
 */
public interface TimelineCallback {

	/**
	 * ���鿪ʼ��
	 * 
	 * @param start
	 *            ��ʼʱ�䡣
	 * @param end
	 *            ����ʱ�䡣
	 */
	public void huiyiStarted(int start, int end);

	/**
	 * ���������
	 * 
	 * @param start
	 *            ��ʼʱ�䡣
	 * @param end
	 *            ����ʱ�䡣
	 */
	public void huiyiStoped();
}
