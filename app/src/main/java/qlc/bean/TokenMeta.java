package qlc.bean;

import java.math.BigInteger;

// 
public class TokenMeta {
		
	private String type;					// token hash
	
	private String header;					// the latest block hash for the token chain
	
	private String representative;			// representative address
	
	private String open;					// the open block hash for the token chain
	
	private BigInteger balance;				// balance for the token
	
	private String account;  				// account that token belong to
	
	private Integer modified;				// timestamp
	
	private Integer blockCount;				// total block number for the token chain
	
	private String tokenName;				// the token name
	
	private String pending;					// pending amount

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getModified() {
		return modified;
	}

	public void setModified(Integer modified) {
		this.modified = modified;
	}

	public Integer getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(Integer blockCount) {
		this.blockCount = blockCount;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

}
