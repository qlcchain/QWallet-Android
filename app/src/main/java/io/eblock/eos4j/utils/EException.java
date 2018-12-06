package io.eblock.eos4j.utils;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:30:49
 */
public class EException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}