package io.eblock.eos4j.api.vo.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:27:07
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequiredAuth {

	private List<String> accounts;

	private List<Key> keys;

	private Long threshold;

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public List<Key> getKeys() {
		return keys;
	}

	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return "RequiredAuth [accounts=" + accounts + ", keys=" + keys + ", threshold=" + threshold + "]";
	}
	
}
