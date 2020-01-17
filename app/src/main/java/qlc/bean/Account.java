package qlc.bean;

import java.math.BigInteger;
import java.util.List;


public class Account {
	
	private String account;							// the account address
	
	private BigInteger coinBalance;					// balance of main token of the account (default is QLC)
	
	private BigInteger vote;						
	
	private	BigInteger network;  
	
	private	BigInteger storage;
	
	private	BigInteger oracle;
	
	private	String representative;					// representative address of the account
	
	private List<TokenMeta> tokens;					// each token info for the account

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigInteger getCoinBalance() {
		return coinBalance;
	}

	public void setCoinBalance(BigInteger coinBalance) {
		this.coinBalance = coinBalance;
	}

	public BigInteger getVote() {
		return vote;
	}

	public void setVote(BigInteger vote) {
		this.vote = vote;
	}

	public BigInteger getNetwork() {
		return network;
	}

	public void setNetwork(BigInteger network) {
		this.network = network;
	}

	public BigInteger getStorage() {
		return storage;
	}

	public void setStorage(BigInteger storage) {
		this.storage = storage;
	}

	public BigInteger getOracle() {
		return oracle;
	}

	public void setOracle(BigInteger oracle) {
		this.oracle = oracle;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public List<TokenMeta> getTokens() {
		return tokens;
	}

	public void setTokens(List<TokenMeta> tokens) {
		this.tokens = tokens;
	}
	
}
