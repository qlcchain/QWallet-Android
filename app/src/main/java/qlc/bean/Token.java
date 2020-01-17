package qlc.bean;

import java.math.BigInteger;

public class Token {

	private String tokenId;
	
	private String tokenName;
	
	private String tokenSymbol;
	
	private BigInteger totalSupply;
	
	private Integer decimals;
	
	private String owner;
	
	private BigInteger pledgeAmount;
	
	private Integer withdrawTime;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getTokenSymbol() {
		return tokenSymbol;
	}

	public void setTokenSymbol(String tokenSymbol) {
		this.tokenSymbol = tokenSymbol;
	}

	public BigInteger getTotalSupply() {
		return totalSupply;
	}

	public void setTotalSupply(BigInteger totalSupply) {
		this.totalSupply = totalSupply;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BigInteger getPledgeAmount() {
		return pledgeAmount;
	}

	public void setPledgeAmount(BigInteger pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}

	public Integer getWithdrawTime() {
		return withdrawTime;
	}

	public void setWithdrawTime(Integer withdrawTime) {
		this.withdrawTime = withdrawTime;
	}

}
