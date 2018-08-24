package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * exclusive data for Claim transactions.
 *
 * @author coranos
 *
 */
public final class ClaimExclusiveData implements ExclusiveData, ToJsonObject, ByteArraySerializable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * the list of CoinReferences that represent coin claims.
	 */
	public final List<CoinReference> claims;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public ClaimExclusiveData(final ByteBuffer bb) {
		claims = ModelUtil.readVariableLengthList(bb, CoinReference.class);
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, claims);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		final boolean ifNullReturnEmpty = false;
		try {
			json.put("claims", ModelUtil.toJSONArray(ifNullReturnEmpty, claims));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
