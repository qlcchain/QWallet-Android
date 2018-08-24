package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * the witness.
 *
 * @author coranos
 *
 */
public final class Witness implements ToJsonObject, ByteArraySerializable, Serializable, Comparable<Witness> {

	private static final long serialVersionUID = 1L;

	/**
	 * the invocation script.
	 */
	private final byte[] invocationScript;

	/**
	 * the verification script.
	 */
	private final byte[] verificationScript;

	/**
	 * the constructor.
	 *
	 * @param invocationScript
	 *            the invocation script to use.
	 * @param verificationScript
	 *            the verification script to use.
	 */
	public Witness(final byte[] invocationScript, final byte[] verificationScript) {
		this.invocationScript = invocationScript;
		this.verificationScript = verificationScript;
	}

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public Witness(final ByteBuffer bb) {
		invocationScript = ModelUtil.getVariableLengthByteArray(bb);
		verificationScript = ModelUtil.getVariableLengthByteArray(bb);
	}

	@Override
	public int compareTo(final Witness that) {
		final int iC = ByteBuffer.wrap(invocationScript).compareTo(ByteBuffer.wrap(that.invocationScript));
		if (iC != 0) {
			return iC;
		}
		final int vC = ByteBuffer.wrap(verificationScript).compareTo(ByteBuffer.wrap(that.verificationScript));
		if (vC != 0) {
			return vC;
		}

		return 0;
	}

	/**
	 * return a clone of the invocation script.
	 *
	 * @return a clone of the invocation script.
	 */
	public byte[] getCopyOfInvocationScript() {
		return invocationScript.clone();
	}

	/**
	 * return a clone of the verification script.
	 *
	 * @return a clone of the verification script.
	 */
	public byte[] getCopyOfVerificationScript() {
		return verificationScript.clone();
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.writeByteArray(bout, invocationScript);
		NetworkUtil.writeByteArray(bout, verificationScript);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("invocation", new String(Hex.encodeHex(invocationScript)));
			json.put("verification", new String(Hex.encodeHex(verificationScript)));
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
