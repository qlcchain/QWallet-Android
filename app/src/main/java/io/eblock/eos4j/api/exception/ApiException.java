package io.eblock.eos4j.api.exception;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:25:46
 */
public class ApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ApiError error;

	public ApiException(ApiError apiError) {
		this.error = apiError;
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}

	@Override
	public String getMessage() {
		if (error != null) {
			return error.getMessage();
		}
		return super.getMessage();
	}
}
