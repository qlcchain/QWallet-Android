package io.eblock.eos4j.api.vo.transaction.push;

import io.eblock.eos4j.api.vo.BaseVo;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:27:46
 */
public class TxRequest extends BaseVo{

	public TxRequest() {

	}

	public TxRequest(String compression, Tx transaction, String[] signatures) {
		this.compression = compression;
		this.signatures = signatures;
		this.transaction = transaction;
	}

	private String compression;

	private Tx transaction;

	private String[] signatures;

	public String getCompression() {
		return compression;
	}

	public void setCompression(String compression) {
		this.compression = compression;
	}

	public Tx getTransaction() {
		return transaction;
	}

	public void setTransaction(Tx transaction) {
		this.transaction = transaction;
	}

	public String[] getSignatures() {
		return signatures;
	}

	public void setSignatures(String[] signatures) {
		this.signatures = signatures;
	}

}
