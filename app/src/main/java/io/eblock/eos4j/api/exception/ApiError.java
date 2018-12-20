package io.eblock.eos4j.api.exception;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:25:31
 */
public class ApiError {

	private String message;

	private String code;

	private Error error;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
