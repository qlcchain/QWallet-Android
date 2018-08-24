package com.stratagile.qlink.utils.txutils.model.network;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt16;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt32;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt64;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * the version payload.
 *
 * @author coranos
 *
 */
public final class VersionPayload implements ByteArraySerializable, Payload {

	/**
	 * the logger.
	 */

	/**
	 * the version.
	 */
	public final UInt32 version;

	/**
	 * services.
	 */
	public final UInt64 services;

	/**
	 * timestamp.
	 */
	public final UInt32 timestamp;

	/**
	 * port.
	 */
	public final UInt16 port;

	/**
	 * nonce.
	 */
	public final UInt32 nonce;

	/**
	 * useragent.
	 */
	public final String userAgent;

	/**
	 * start block height.
	 */
	public final UInt32 startHeight;

	/**
	 * relay, if true, relay.
	 */
	public final boolean relay;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to use.
	 */
	public VersionPayload(final ByteBuffer bb) {
		version = new UInt32(ModelUtil.getFixedLengthByteArray(bb, 4, true));
		services = new UInt64(ModelUtil.getFixedLengthByteArray(bb, 8, true));
		timestamp = new UInt32(ModelUtil.getFixedLengthByteArray(bb, 4, true));
		port = new UInt16(ModelUtil.getFixedLengthByteArray(bb, 2, true));
		nonce = new UInt32(ModelUtil.getFixedLengthByteArray(bb, 4, true));
		try {
			userAgent = new String(ModelUtil.getVariableLengthByteArray(bb), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		startHeight = new UInt32(ModelUtil.getFixedLengthByteArray(bb, 4, true));

		if (bb.get() == 0) {
			relay = false;
		} else {
			relay = true;
		}
	}

	/**
	 * the constructor.
	 *
	 * @param timestamp
	 *            the timestamp to use.
	 * @param port
	 *            the port to use.
	 * @param nonce
	 *            the nonce to use.
	 * @param userAgent
	 *            the user agent to use.
	 * @param startHeight
	 *            the starting block height.
	 */
	public VersionPayload(final long timestamp, final int port, final int nonce, final String userAgent,
			final long startHeight) {
		version = new UInt32(NetworkUtil.getIntByteArray(0));
		services = new UInt64(NetworkUtil.getLongByteArray(1));
		this.timestamp = new UInt32(NetworkUtil.getIntByteArray(timestamp));
		this.port = new UInt16(NetworkUtil.getShortByteArray(port));
		this.nonce = new UInt32(NetworkUtil.getIntByteArray(nonce));
		this.userAgent = userAgent;
		this.startHeight = new UInt32(NetworkUtil.getIntByteArray(startHeight));
		relay = true;
	}

	@Override
	public byte[] toByteArray() {
		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] versionBa = version.getBytesCopy();
			ArrayUtils.reverse(versionBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version {}", Hex.encodeHexString(versionBa));
//			}
			out.write(versionBa);
			final byte[] servicesBa = services.getBytesCopy();
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload services {}", Hex.encodeHexString(servicesBa));
//			}
			ArrayUtils.reverse(servicesBa);
			out.write(servicesBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services {}", Hex.encodeHexString(out.toByteArray()));
//			}
			final byte[] timestampBa = timestamp.getBytesCopy();
			ArrayUtils.reverse(timestampBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload timestamp {}", Hex.encodeHexString(timestampBa));
//			}
			out.write(timestampBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp {}", Hex.encodeHexString(out.toByteArray()));
//			}
			final byte[] portBa = port.getBytesCopy();
			ArrayUtils.reverse(portBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload port {}", Hex.encodeHexString(portBa));
//			}
			out.write(portBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp+port {}", Hex.encodeHexString(out.toByteArray()));
//			}
			final byte[] nonceBa = nonce.getBytesCopy();
			ArrayUtils.reverse(nonceBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload nonce {}", Hex.encodeHexString(nonceBa));
//			}
			out.write(nonceBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp+port+nonce {}",
//						Hex.encodeHexString(out.toByteArray()));
//			}
			NetworkUtil.writeString(out, userAgent);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp+port+nonce+userAgent {}",
//						Hex.encodeHexString(out.toByteArray()));
//			}

			final byte[] startHeightBa = startHeight.getBytesCopy();
			ArrayUtils.reverse(startHeightBa);
//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp+port+nonce+userAgent+startHeight {}",
//						Hex.encodeHexString(out.toByteArray()));
//			}

			out.write(startHeightBa);

			if (relay) {
				out.write(new byte[] { 1 });
			} else {
				out.write(new byte[] { 0 });
			}

//			if (LOG.isTraceEnabled()) {
//				LOG.trace("versionPayload version+services+timestamp+port+nonce+userAgent+startHeight+relay {}",
//						Hex.encodeHexString(out.toByteArray()));
//			}

			return out.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		final JSONObject json = new JSONObject();

		try {
			json.put("version", version.toPositiveBigInteger());
			json.put("services", services.toPositiveBigInteger());
			json.put("timestamp", timestamp.toPositiveBigInteger());
			json.put("port", port.toPositiveBigInteger());
			json.put("nonce", nonce.toPositiveBigInteger());
			json.put("userAgent", userAgent);
			json.put("startHeight", startHeight.toPositiveBigInteger());
			json.put("relay", relay);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}
}
