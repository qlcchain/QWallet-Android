package qlc.bean;

import java.math.BigInteger;
import java.util.List;

public class Pending {

	private String address;
	
	private List<PendingInfo> infoList;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<PendingInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<PendingInfo> infoList) {
		this.infoList = infoList;
	}

	public class PendingInfo {
		
		private String source;
		
		private BigInteger amount;
		
		private String type;
		
		private String tokenName;
		
		private String hash;
		
		private Long timestamp;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public BigInteger getAmount() {
			return amount;
		}

		public void setAmount(BigInteger amount) {
			this.amount = amount;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTokenName() {
			return tokenName;
		}

		public void setTokenName(String tokenName) {
			this.tokenName = tokenName;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}

	}
}
