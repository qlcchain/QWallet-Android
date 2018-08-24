package com.stratagile.qlink.utils.txutils.model.network;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * the "address" payload.
 *
 * @author coranos
 *
 */
public final class AddrPayload implements Payload, ToJsonObject, ByteArraySerializable {

	/**
	 * the address list.
	 */
	private final List<NetworkAddressWithTime> addressList;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the byte buffer to read.
	 */
	public AddrPayload(final ByteBuffer bb) {
		addressList = ModelUtil.readVariableLengthList(bb, NetworkAddressWithTime.class);
	}

	/**
	 * return the address list.
	 *
	 * @return the address list.
	 */
	public List<NetworkAddressWithTime> getAddressList() {
		return Collections.unmodifiableList(addressList);
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		NetworkUtil.write(out, addressList);
		return out.toByteArray();

	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		final JSONArray addressListJson = new JSONArray();
		try {
			json.put("addressList", addressListJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (final NetworkAddressWithTime nawt : addressList) {
			addressListJson.put(nawt.toJSONObject());
		}

		return json;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

}
