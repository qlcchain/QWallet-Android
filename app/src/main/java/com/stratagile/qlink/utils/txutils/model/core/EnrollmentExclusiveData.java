package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.crypto.ecc.ECCurve;
import com.stratagile.qlink.utils.txutils.model.crypto.ecc.ECPoint;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * exclusive data for enrollment transactions.
 *
 * @author coranos
 */
public final class EnrollmentExclusiveData implements ExclusiveData, ToJsonObject, ByteArraySerializable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * the public key.
	 */
	public final ECPoint publicKey;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public EnrollmentExclusiveData(final ByteBuffer bb) {
		publicKey = ECPoint.DeserializeFrom(bb, ECCurve.Secp256r1);
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, publicKey.toByteArray());
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("publicKey", ModelUtil.toHexString(publicKey.toByteArray()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
