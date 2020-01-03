package qlc.bean;

import java.math.BigInteger;

import qlc.bean.Block;

public final class StateBlock extends Block {
	
	private String type;
	
	private String token;

	private String address;
	
	private BigInteger balance;
	
	private BigInteger vote;
	
	private BigInteger network;
	
	private BigInteger storage;
	
	private BigInteger oracle;
	
	private String previous;
	
	private String link;
	
	private String sender;
	
	private String receiver;
	
	private String message;
	
	private String data;
	
	private Long povHeight;
	
	private Integer quota;
	
	private Long timestamp;
	
	private String extra;
	
	private String representative;
	
	private String work;
	
	private String signature;

	public StateBlock() {
		super();
	}

	public StateBlock(String type, String token, String address, BigInteger balance, String previous, 
			String link, Long timestamp, String representative) {
		super();
		this.type = type;
		this.token = token;
		this.address = address;
		this.balance = balance;
		this.previous = previous;
		this.link = link;
		this.timestamp = timestamp;
		this.representative = representative;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
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

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getPovHeight() {
		return povHeight;
	}

	public void setPovHeight(Long povHeight) {
		this.povHeight = povHeight;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
