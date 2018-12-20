package io.eblock.eos4j.api.vo;

import java.util.Date;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:28:43
 */
public class SignParam {
	/**
	 * 最新区块时间
	 */
	private Date headBlockTime;
	/**
	 * 链ID
	 */
	private String chainId;
	/**
	 * 不可逆区块
	 */
	private Long lastIrreversibleBlockNum;
	/**
	 * 上一个区块hash前缀
	 */
	private Long refBlockPrefix;
	/**
	 * 过期时间
	 */
	private Long exp;

	public Date getHeadBlockTime() {
		return headBlockTime;
	}

	public void setHeadBlockTime(Date headBlockTime) {
		this.headBlockTime = headBlockTime;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public Long getLastIrreversibleBlockNum() {
		return lastIrreversibleBlockNum;
	}

	public void setLastIrreversibleBlockNum(Long lastIrreversibleBlockNum) {
		this.lastIrreversibleBlockNum = lastIrreversibleBlockNum;
	}

	public Long getRefBlockPrefix() {
		return refBlockPrefix;
	}

	public void setRefBlockPrefix(Long refBlockPrefix) {
		this.refBlockPrefix = refBlockPrefix;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

}
