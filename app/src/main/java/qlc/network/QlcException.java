package qlc.network;

public class QlcException extends RuntimeException {
	
	private static final long serialVersionUID = -7742545555643041585L;
	
	public final int code;
	
	public QlcException(int code, String message) {
		super(message);
		this.code = code;
	}

}
