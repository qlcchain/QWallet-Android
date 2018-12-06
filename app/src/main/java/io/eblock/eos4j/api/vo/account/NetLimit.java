package io.eblock.eos4j.api.vo.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:26:54
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetLimit {

	private Long used;

	private Long available;

	private Long max;

	public NetLimit() {

	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "NetLimit [used=" + used + ", available=" + available + ", max=" + max + "]";
	}

	
}
