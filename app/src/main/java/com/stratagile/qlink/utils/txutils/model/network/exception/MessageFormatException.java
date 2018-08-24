package com.stratagile.qlink.utils.txutils.model.network.exception;

/**
 * a exception in the message format.
 *
 * @author coranos
 *
 */
public class MessageFormatException extends RuntimeException {

	/**
	 * the serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the constructor.
	 *
	 * @param msg
	 *            the message.
	 */
	public MessageFormatException(final String msg) {
		super(msg);
	}

}
