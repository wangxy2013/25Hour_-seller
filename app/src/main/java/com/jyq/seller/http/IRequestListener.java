package com.jyq.seller.http;

public interface IRequestListener {

	/**
	 * notify
	 */
	public void notify(String action, String resultCode, String resultMsg, Object obj);
}
