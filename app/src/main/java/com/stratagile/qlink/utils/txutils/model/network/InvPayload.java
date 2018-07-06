package com.stratagile.qlink.utils.txutils.model.network;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * the inventory payload.
 *
 * @author coranos
 *
 */
public final class InvPayload implements Payload, ToJsonObject, ByteArraySerializable {

	/**
	 * the max number of hashes in a inventory payload.
	 */
	public static final int MAX_HASHES = 2000;

	/**
	 * the inventory type.
	 */
	private final byte type;

	/**
	 * the list of hashes.
	 */
	private final List<UInt256> hashes;

	/**
	 * the inventory type, as an enum.
	 */
	private final InventoryType typeEnum;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the byte buffer to read.
	 */
	public InvPayload(final ByteBuffer bb) {
		type = bb.get();
		typeEnum = InventoryType.valueOf(type);
		hashes = ModelUtil.readVariableLengthList(bb, UInt256.class);
	}

	/**
	 * the constructor.
	 *
	 * @param typeEnum
	 *            the inventory type enum.
	 * @param hashes
	 *            the list of hashes.
	 */
	public InvPayload(final InventoryType typeEnum, final UInt256... hashes) {
		this.typeEnum = typeEnum;
		type = typeEnum.getTypeByte();
		this.hashes = new ArrayList<>(Arrays.asList(hashes));
	}

	/**
	 * return the list of hashes.
	 *
	 * @return the list of hashes.
	 */
	public List<UInt256> getHashes() {
		return Collections.unmodifiableList(hashes);
	}

	/**
	 * return the type enum.
	 *
	 * @return the type enum.
	 */
	public InventoryType getType() {
		return typeEnum;
	}

	@Override
	public byte[] toByteArray() {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(new byte[] { type });
			NetworkUtil.writeVarInt(out, hashes.size());
			for (int ix = 0; ix < hashes.size(); ix++) {
				out.write(hashes.get(ix).toByteArray());
			}
			return out.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("type", type);
			final JSONArray hashesJson = new JSONArray();
			json.put("hashes", hashesJson);
			for (final UInt256 hash : hashes) {
				hashesJson.put(hash.toHexString());
			}
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
