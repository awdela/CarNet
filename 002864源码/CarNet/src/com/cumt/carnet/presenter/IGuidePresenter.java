package com.cumt.carnet.presenter;
/**
 * ����fragment��һЩ���ܷ���
 * @author wangcan
 *
 */
public interface IGuidePresenter {
	public void exchangeTextData();//���ڽ�������edittext������
	public void clearData();//���û����ȡ��ʱ���ã�����list��hash�洢�����ݣ�textview�������Լ��ָ�title����ʾ
	public void startGuide();//�û���������������������ܣ���������ǻ����ʼ����ֹλ�õľ�γ�����꣬Ȼ����е���
}
