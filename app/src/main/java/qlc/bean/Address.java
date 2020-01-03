package qlc.bean;

public final class Address {
	
	private final String address;
	
	private final String publicKey;
	
	private final String privateKey;
	
	public Address(String address, String publicKey, String privateKey) {
		this.address = address;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public String getAddress() {
		return address;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

}
