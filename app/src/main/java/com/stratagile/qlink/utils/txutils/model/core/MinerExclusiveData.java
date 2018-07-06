package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt32;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * exclusive data for miner transactions.
 *
 * @author coranos
 */
public final class MinerExclusiveData implements ExclusiveData, ToJsonObject, ByteArraySerializable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * the nonce.
	 * <p>
	 * https://en.wikipedia.org/wiki/Cryptographic_nonce
	 * <p>
	 * In cryptography, a nonce is an arbitrary number that can only be used once.
	 */
	public final UInt32 nonce;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public MinerExclusiveData(final ByteBuffer bb) {
		nonce = ModelUtil.getUInt32(bb);
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, nonce, true);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("nonce", nonce.toReverseHexString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
