package com.stratagile.qlink.utils.txutils.model.network;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * the payload for the getblocks method.
 *
 * @author coranos
 *
 */
public final class GetBlocksPayload implements Payload, ToJsonObject, ByteArraySerializable {

	/**
	 * the start hashes.
	 */
	private final List<UInt256> hashStartList;

	/**
	 * the stop hash.
	 */
	public final UInt256 hashStop;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the byte buffer to read.
	 */
	public GetBlocksPayload(final ByteBuffer bb) {
		hashStartList = ModelUtil.readVariableLengthList(bb, UInt256.class);
		hashStop = ModelUtil.getUInt256(bb);
	}

	/**
	 * the constructor.
	 *
	 * @param hashStart
	 *            the start hash to use.
	 * @param hashStop
	 *            the stop hash to use.
	 */
	public GetBlocksPayload(final UInt256 hashStart, final UInt256 hashStop) {
		hashStartList = Arrays.asList(hashStart);
		if (hashStop == null) {
			this.hashStop = new UInt256(new byte[UInt256.SIZE]);
		} else {
			this.hashStop = hashStop;
		}
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		NetworkUtil.writeVarInt(out, hashStartList.size());
		for (int ix = 0; ix < hashStartList.size(); ix++) {
			NetworkUtil.write(out, hashStartList.get(ix).toByteArray());
		}
		NetworkUtil.write(out, hashStop.toByteArray());
		return out.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		final JSONArray hashStartListJson = new JSONArray();
		try {
			json.put("hashStartList", hashStartListJson);
			for (final UInt256 hashStart : hashStartList) {
				hashStartListJson.put(hashStart.toHexString());
			}

			json.put("hashStop", hashStop.toHexString());
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
