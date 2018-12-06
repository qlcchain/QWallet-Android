package io.eblock.eos4j.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:26:03
 */
public class ErrorDetails {

	private String message;

	private String file;

	private Integer lineNumber;

	private String method;

	private ErrorDetails() {

	}

	@Override
	public String toString() {
		return "ErrorDetails{" +
				"message='" + message + '\'' +
				", file='" + file + '\'' +
				", lineNumber=" + lineNumber +
				", method='" + method + '\'' +
				'}';
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	@JsonProperty("line_number")
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
