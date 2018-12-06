package io.eblock.eos4j.api.vo.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:26:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Key {

	private String key;

	private Long weight;

	public Key() {

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
}
