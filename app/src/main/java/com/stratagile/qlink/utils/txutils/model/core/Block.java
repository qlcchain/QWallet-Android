package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.network.Payload;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * a block of transactions.
 *
 * @author coranos
 *
 */
public final class Block extends AbstractBlockBase implements ToJsonObject, Payload {

	private static final long serialVersionUID = 1L;

	/**
	 * the list of transactions.
	 */
	private final List<Transaction> transactionList;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public Block(final ByteBuffer bb) {
		super(bb);
		transactionList = ModelUtil.readVariableLengthList(bb, Transaction.class);
	}

	/**
	 * return the transaction list.
	 *
	 * @return the transaction list.
	 */
	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeBaseToOutputStream(bout);
		NetworkUtil.write(bout, transactionList);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();
		final boolean ifNullReturnEmpty = false;
		addBaseToJSONObject(json);
		try {
			json.put("tx", ModelUtil.toJSONArray(ifNullReturnEmpty, transactionList));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

}
