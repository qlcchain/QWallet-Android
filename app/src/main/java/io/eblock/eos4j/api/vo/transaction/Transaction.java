package io.eblock.eos4j.api.vo.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:28:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends Base {

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("processed")
	private Processed processed;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Processed getProcessed() {
		return processed;
	}

	public void setProcessed(Processed processed) {
		this.processed = processed;
	}

}
