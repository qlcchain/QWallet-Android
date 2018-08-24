package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * the transaction attributes.
 *
 * @author coranos
 *
 */
public final class TransactionAttribute implements ToJsonObject, ByteArraySerializable, Serializable {

	/**
	 * unknown usage.
	 */
	private static final String UNKNOWN_USAGE = "unknown usage:";

	private static final long serialVersionUID = 1L;

	/**
	 * the usage of the transaction attribute.
	 */
	public final TransactionAttributeUsage usage;

	/**
	 * the data for the transaction attribute.
	 */
	private final byte[] data;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public TransactionAttribute(final ByteBuffer bb) {
		usage = TransactionAttributeUsage.valueOfByte(ModelUtil.getByte(bb));

		switch (usage) {
		case CONTRACT_HASH:
		case VOTE:
		case HASH_01:
		case HASH_02:
		case HASH_03:
		case HASH_04:
		case HASH_05:
		case HASH_06:
		case HASH_07:
		case HASH_08:
		case HASH_09:
		case HASH_10:
		case HASH_11:
		case HASH_12:
		case HASH_13:
		case HASH_14:
		case HASH_15:
			data = ModelUtil.getFixedLengthByteArray(bb, 32, false);
			break;
		case ECDH02:
		case ECDH03: {
			final byte[] readData = ModelUtil.getFixedLengthByteArray(bb, 32, false);
			data = new byte[readData.length + 1];
			System.arraycopy(readData, 0, data, 1, readData.length);
			data[0] = usage.getTypeByte();
			break;
		}
		case SCRIPT:
			data = ModelUtil.getFixedLengthByteArray(bb, 20, false);
			break;
		case DESCRIPTION_URL: {
			final int length = ModelUtil.getByte(bb) & 0xff;
			data = ModelUtil.getFixedLengthByteArray(bb, length, false);
			break;
		}
		case DESCRIPTION:
		case REMARK_00:
		case REMARK_01:
		case REMARK_02:
		case REMARK_03:
		case REMARK_04:
		case REMARK_05:
		case REMARK_06:
		case REMARK_07:
		case REMARK_08:
		case REMARK_09:
		case REMARK_10:
		case REMARK_11:
		case REMARK_12:
		case REMARK_13:
		case REMARK_14:
		case REMARK_15: {
			data = ModelUtil.getVariableLengthByteArray(bb);
			break;
		}
		default:
			throw new RuntimeException(UNKNOWN_USAGE + usage);
		}
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, usage.getTypeByte());

		switch (usage) {
		case CONTRACT_HASH:
		case VOTE:
		case HASH_01:
		case HASH_02:
		case HASH_03:
		case HASH_04:
		case HASH_05:
		case HASH_06:
		case HASH_07:
		case HASH_08:
		case HASH_09:
		case HASH_10:
		case HASH_11:
		case HASH_12:
		case HASH_13:
		case HASH_14:
		case HASH_15:
			NetworkUtil.write(bout, data);
			break;
		case ECDH02:
		case ECDH03: {
			NetworkUtil.write(bout, data, 1, data.length - 1);
			break;
		}
		case SCRIPT:
			NetworkUtil.write(bout, data);
			break;
		case DESCRIPTION_URL: {
			final byte b = (byte) data.length;
			NetworkUtil.write(bout, b);
			NetworkUtil.write(bout, data);
			break;
		}
		case DESCRIPTION:
		case REMARK_00:
		case REMARK_01:
		case REMARK_02:
		case REMARK_03:
		case REMARK_04:
		case REMARK_05:
		case REMARK_06:
		case REMARK_07:
		case REMARK_08:
		case REMARK_09:
		case REMARK_10:
		case REMARK_11:
		case REMARK_12:
		case REMARK_13:
		case REMARK_14:
		case REMARK_15: {
			NetworkUtil.writeByteArray(bout, data);
			break;
		}
		default:
			throw new RuntimeException(UNKNOWN_USAGE + usage);
		}

		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("usage", usage);
			json.put("data", ModelUtil.toHexString(data));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
