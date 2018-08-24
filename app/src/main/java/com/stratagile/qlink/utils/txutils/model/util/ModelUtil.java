package com.stratagile.qlink.utils.txutils.model.util;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.Fixed8;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt128;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt16;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt32;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * the utilities for editing the neo model.
 *
 * @author coranos
 *
 */
public final class ModelUtil {

	/**
	 * a fixed8 representation of zero.
	 */
	public static final Fixed8 FIXED8_ZERO = ModelUtil.getFixed8(BigInteger.ZERO);

	/**
	 * the UTF-8 charset.
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * gas.
	 */
	public static final String GAS = "gas";

	/**
	 * neo.
	 */
	public static final String NEO = "neo";

	/**
	 * the logger.
	 */

	/**
	 * the encoded byte to mean a variable length is a long.
	 */
	private static final byte LENGTH_LONG = (byte) 0xFF;

	/**
	 * the encoded byte to mean a variable length is a int.
	 */
	private static final byte LENGTH_INT = (byte) 0xFE;

	/**
	 * the encoded byte to mean a variable length is a short.
	 */
	private static final byte LENGTH_SHORT = (byte) 0xFD;

	/**
	 * the NEO coin hash.
	 */
	public static final String NEO_HASH_HEX_STR = "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b";

	/**
	 * the GAS coin hash.
	 */
	public static final String GAS_HASH_HEX_STR = "602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";

	/**
	 * the hash of the NEO registration transaction.
	 */
	public static final UInt256 NEO_HASH;

	/**
	 * the hash of the GAS registration transaction.
	 */
	public static final UInt256 GAS_HASH;

	/**
	 * the divisor to use to convert a Fixed8 value to a decimal.
	 */
	public static final long DECIMAL_DIVISOR = 100000000;

	static {

		try {
			final byte[] neoBa = Hex.decodeHex(NEO_HASH_HEX_STR.toCharArray());
			// ArrayUtils.reverse(neoBa);
			NEO_HASH = new UInt256(neoBa);

			final byte[] gasBa = Hex.decodeHex(GAS_HASH_HEX_STR.toCharArray());
			// ArrayUtils.reverse(gasBa);
			GAS_HASH = new UInt256(gasBa);
		} catch (final DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * the address version.
	 */
	public static final byte ADDRESS_VERSION = 23;

	/**
	 * adds two Fixed8 values.
	 *
	 * @param value1
	 *            the first value
	 * @param value2
	 *            the second value.
	 * @return the sum of the two values.
	 */
	public static Fixed8 add(final Fixed8 value1, final Fixed8 value2) {
		final BigInteger oldBi = value1.toPositiveBigInteger();
		final BigInteger valBi = value2.toPositiveBigInteger();
		final BigInteger newBi = oldBi.add(valBi);
		final Fixed8 newValue = getFixed8(newBi);
		return newValue;
	}

	/**
	 * return the scripthash of the address.
	 *
	 * @param address
	 *            the address to use.
	 * @return the scripthash of the address.
	 */
	public static UInt160 addressToScriptHash(final String address) {
//		if (LOG.isTraceEnabled()) {
//			LOG.trace("addressToScriptHash.address:{}", address);
//		}
		final byte[] dataAndChecksum = Base58Util.decode(address);
//		if (LOG.isTraceEnabled()) {
//			LOG.trace("addressToScriptHash.dataAndChecksum:{}", Hex.encodeHexString(dataAndChecksum));
//		}
		final byte[] data = new byte[20];
		System.arraycopy(dataAndChecksum, 4, data, 0, data.length);
//		if (LOG.isTraceEnabled()) {
//			LOG.trace("addressToScriptHash.data:{}", Hex.encodeHexString(data));
//		}
		return new UInt160(data);
	}

	/**
	 * copies and reverses a byte array.
	 *
	 * @param input
	 *            the byte array to copy and reverse.
	 * @return a copy of the byte array, in reverse byte order.
	 */
	public static byte[] copyAndReverse(final byte[] input) {
		final byte[] revInput = new byte[input.length];
		System.arraycopy(input, 0, revInput, 0, input.length);
		ArrayUtils.reverse(revInput);
		return revInput;
	}

	/**
	 * decodes a hex string.
	 *
	 * @param string
	 *            the string to decode.
	 * @return the decoded hex string.
	 */
	public static byte[] decodeHex(final String string) {
		try {
			return Hex.decodeHex(string.toCharArray());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * converts a byte array to a BigInteger.
	 *
	 * @param ba
	 *            the byte array to use.
	 * @return the BigInteger.
	 */
	public static BigInteger getBigInteger(final byte[] ba) {
		return getBigInteger(ByteBuffer.wrap(ba));
	}

	/**
	 * converts a ByteBuffer to a BigInteger.
	 *
	 * @param bb
	 *            the ByteBuffer to use.
	 * @return the BigInteger.
	 */
	public static BigInteger getBigInteger(final ByteBuffer bb) {
		final byte lengthType = bb.get();

		final int length;
		if (lengthType == LENGTH_SHORT) {
			length = 2;
		} else if (lengthType == LENGTH_INT) {
			length = 4;
		} else if (lengthType == LENGTH_LONG) {
			length = 8;
		} else {
			length = -1;
		}

		if (length == -1) {
			final BigInteger retval = new BigInteger(1, new byte[] { lengthType });
			return retval;
		}

		final byte[] ba = new byte[length];
		bb.get(ba);

		ArrayUtils.reverse(ba);
		final BigInteger retval = new BigInteger(1, ba);

		return retval;
	}

	/**
	 * gets a boolean from a ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return true if the next byte was not zero, false otherwise.
	 */
	public static boolean getBoolean(final ByteBuffer bb) {
		return bb.get() != 0;
	}

	/**
	 * gets a byte from a ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the byte.
	 */
	public static byte getByte(final ByteBuffer bb) {
		return bb.get();
	}

	/**
	 * creates a new Fixed8 from a BigInteger.
	 *
	 * @param newBi
	 *            the BigInteger to use.
	 * @return the new Fixed8.
	 */
	public static Fixed8 getFixed8(final BigInteger newBi) {
		final byte[] ba = new byte[UInt64.SIZE];
		final byte[] biBa = newBi.toByteArray();
		final int destPos;
		final int srcPos;
		final int length;
		if (biBa.length <= ba.length) {
			destPos = UInt64.SIZE - biBa.length;
			srcPos = 0;
			length = biBa.length;
		} else if (biBa[0] == 0) {
			destPos = 0;
			srcPos = 1;
			length = biBa.length - 1;
		} else {
			destPos = UInt64.SIZE - biBa.length;
			srcPos = 0;
			length = biBa.length;
		}
		try {
			System.arraycopy(biBa, srcPos, ba, destPos, length);
			ArrayUtils.reverse(ba);
			final Fixed8 newValue = new Fixed8(ByteBuffer.wrap(ba));
			return newValue;
		} catch (final ArrayIndexOutOfBoundsException e) {
			final JSONObject msgJson = new JSONObject();
//			msgJson.put("ba", Hex.encodeHexString(ba));
//			msgJson.put("biBa", Hex.encodeHexString(biBa));
//			msgJson.put("destPos", destPos);
//			msgJson.put("srcPos", srcPos);
//			msgJson.put("length", length);
			final String msg = msgJson.toString();
			throw new RuntimeException(msg, e);
		}
	}

	/**
	 * returned a Fixed8.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the new Fixed8.
	 */
	public static Fixed8 getFixed8(final ByteBuffer bb) {
		return new Fixed8(bb);
	}

	/**
	 * gets a fixed length byte array from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @param size
	 *            the size of the byte array.
	 * @param reverse
	 *            if true, reverse the byte array.
	 * @return the fixed length byte array.
	 */
	public static byte[] getFixedLengthByteArray(final ByteBuffer bb, final int size, final boolean reverse) {
		final byte[] ba = new byte[size];
		bb.get(ba);
		if (reverse) {
			ArrayUtils.reverse(ba);
		}
		return ba;
	}

	/**
	 * returns a String, which was previously encoded as a fixed length UTF-8 byte
	 * array.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @param length
	 *            the length to use.
	 * @return the string.
	 */
	public static String getFixedLengthString(final ByteBuffer bb, final int length) {
		final byte[] ba = getFixedLengthByteArray(bb, length, false);
		try {
			return new String(ba, UTF_8);
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * returns a UInt128 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the new UInt128.
	 */
	public static UInt128 getUInt128(final ByteBuffer bb) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt128.SIZE, true);
		return new UInt128(ba);
	}

	/**
	 * returns a UInt16 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the new UInt16.
	 */
	public static UInt16 getUInt16(final ByteBuffer bb) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt16.SIZE, true);
		return new UInt16(ba);
	}

	/**
	 * returns a UInt160 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @param reverse
	 *            if true, reverse the byte array creating the data used to create
	 *            the object before creating the object.
	 * @return the new UInt160.
	 */
	public static UInt160 getUInt160(final ByteBuffer bb, final boolean reverse) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt160.SIZE, true);
		if (reverse) {
			ArrayUtils.reverse(ba);
		}
		return new UInt160(ba);
	}

	/**
	 * returns a UInt256 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the byte buffer to use.
	 * @return the new UInt256.
	 */
	public static UInt256 getUInt256(final ByteBuffer bb) {
		return getUInt256(bb, false);
	}

	/**
	 * returns a UInt256 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @param reverse
	 *            if true, reverse the byte array creating the data used to create
	 *            the object before creating the object.
	 * @return the new UInt256.
	 */
	public static UInt256 getUInt256(final ByteBuffer bb, final boolean reverse) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt256.SIZE, true);
		if (reverse) {
			ArrayUtils.reverse(ba);
		}
		return new UInt256(ba);
	}

	/**
	 * returns a UInt32 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the new UInt32.
	 */
	public static UInt32 getUInt32(final ByteBuffer bb) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt32.SIZE, true);
		return new UInt32(ba);
	}

	/**
	 * returns a UInt64 read from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the new UInt64.
	 */
	public static UInt64 getUInt64(final ByteBuffer bb) {
		final byte[] ba = getFixedLengthByteArray(bb, UInt64.SIZE, true);
		return new UInt64(ba);
	}

	/**
	 * gets a variable length byte array from the ByteBuffer.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return a variable length byte array.
	 */
	public static byte[] getVariableLengthByteArray(final ByteBuffer bb) {
		final BigInteger length = getBigInteger(bb);
		final byte[] ba = new byte[length.intValue()];
		bb.get(ba);
		return ba;
	}

	/**
	 * returns a String, which was previously encoded as a variable length UTF-8
	 * byte array.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 * @return the string.
	 */
	public static String getVariableLengthString(final ByteBuffer bb) {
		final byte[] ba = getVariableLengthByteArray(bb);
		try {
			return new String(ba, UTF_8);
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * reads a variable length list of byte array serializable objects.
	 *
	 * @param bb
	 *            the byte buffer to read.
	 * @param cl
	 *            the class of the objects in the list, which must implement
	 *            ByteArraySerializable.
	 * @param <T>
	 *            the type of the objects in the list.
	 * @return the list.
	 */
	public static <T extends ByteArraySerializable> List<T> readVariableLengthList(final ByteBuffer bb,
																				   final Class<T> cl) {
		final BigInteger lengthBi = getBigInteger(bb);
		final int length = lengthBi.intValue();

//		LOG.trace("readArray length {} class {}", length, cl.getSimpleName());

		final List<T> list = new ArrayList<>();
		for (int ix = 0; ix < length; ix++) {

//			LOG.trace("STARTED readArray class {} [{}]", cl.getSimpleName(), ix);
			final T t;
			try {
				final Constructor<T> con = cl.getConstructor(ByteBuffer.class);
				t = con.newInstance(bb);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(
						"error reading record " + (ix + 1) + " of " + length + " class " + cl.getSimpleName(), e);
			}

//			LOG.trace("SUCCESS readArray class {} [{}]: {} {}", cl.getSimpleName(), ix,
//					Hex.encodeHexString(t.toByteArray()), t);

			list.add(t);
		}
		return list;
	}

	/**
	 * coverts a scriptHash to an address.
	 *
	 * @param scriptHash
	 *            the scriptHash to use.
	 * @return the address.
	 */
	public static String scriptHashToAddress(final UInt160 scriptHash) {
		final byte[] data = new byte[21];

//		if (LOG.isTraceEnabled()) {
//			LOG.trace("toAddress ADDRESS_VERSION {}", ModelUtil.toHexString(ADDRESS_VERSION));
//		}

		final byte[] scriptHashBa = scriptHash.toByteArray();
		System.arraycopy(scriptHashBa, 0, data, 0, scriptHashBa.length);

		data[data.length - 1] = ADDRESS_VERSION;
//		if (LOG.isTraceEnabled()) {
//			LOG.trace("toAddress data {}", ModelUtil.toHexString(data));
//		}

		final byte[] dataAndChecksum = new byte[25];
		System.arraycopy(data, 0, dataAndChecksum, 4, data.length);

		ArrayUtils.reverse(data);
		final byte[] hash = SHA256HashUtil.getDoubleSHA256Hash(data);
		final byte[] hash4 = new byte[4];
		System.arraycopy(hash, 0, hash4, 0, 4);
		ArrayUtils.reverse(hash4);
		System.arraycopy(hash4, 0, dataAndChecksum, 0, 4);
//		if (LOG.isTraceEnabled()) {
//			LOG.trace("toAddress dataAndChecksum {}", ModelUtil.toHexString(dataAndChecksum));
//		}

		final String address = toBase58String(dataAndChecksum);
		return address;
	}

	/**
	 * subtracts two Fixed8 values.
	 *
	 * @param left
	 *            the left value
	 * @param right
	 *            the right value.
	 * @return left minus right
	 */
	public static Fixed8 subtract(final Fixed8 left, final Fixed8 right) {
		final BigInteger leftBi = left.toPositiveBigInteger();
		final BigInteger rightBi = right.toPositiveBigInteger();
		final BigInteger newBi = rightBi.subtract(leftBi);
		if (newBi.signum() < 0) {
			throw new RuntimeException("tried to subtract " + leftBi + "(Fixed8:" + left + ")  from " + rightBi
					+ " (Fixed8:" + right + ")" + " cannot have a negative fixed8 with value " + newBi + ".");
		}
		final Fixed8 newValue = getFixed8(newBi);
		return newValue;
	}

	/**
	 * converts an array of bytes to a base58 string.
	 *
	 * @param bytes
	 *            the bytes to use.
	 * @return the new string.
	 */
	public static String toBase58String(final byte[] bytes) {
		return Base58Util.encode(bytes);
	}

	/**
	 * converts an array of bytes to a base64 string.
	 *
	 * @param bytes
	 *            the bytes to use.
	 * @return the new string.
	 */
	public static String toBase64String(final byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * returns the list of byte arrays as a encoded byte array.
	 *
	 * @param baList
	 *            the byte array list.
	 * @return the encoded byte array.
	 */
	public static byte[] toByteArray(final byte[]... baList) {
		return toByteArray(Arrays.asList(baList));
	}

	/**
	 * converts a list of byte arrays into a byte array.
	 *
	 * @param baList
	 *            the byte array list to use.
	 * @return the byte array.
	 */
	public static byte[] toByteArray(final List<byte[]> baList) {
		final ByteArrayOutputStream bout;
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			NetworkUtil.writeLong(out, baList.size());
			for (final byte[] ba : baList) {
				NetworkUtil.writeByteArray(out, ba);
			}
			bout = out;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return bout.toByteArray();
	}

	/**
	 * converts a byte array into a list of byte arrays.
	 *
	 * @param ba
	 *            the byte array to use.
	 * @return the byte array.
	 */
	public static List<byte[]> toByteArrayList(final byte[] ba) {
		final List<byte[]> baList = new ArrayList<>();
		final ByteBuffer listBb = ByteBuffer.wrap(ba);
		final long size = listBb.getLong();
		for (long ix = 0; ix < size; ix++) {
			final byte[] keyBa = ModelUtil.getVariableLengthByteArray(listBb);
			baList.add(keyBa);
		}
		return baList;
	}

	/**
	 * converts a byte array to a hex string.
	 *
	 * @param ba
	 *            the byte array to encode.
	 * @return the string.
	 */
	public static String toHexString(final byte... ba) {
		return new String(Hex.encodeHex(ba));
	}

	/**
	 * converts a list of objects that implement the ToJsonObject interface into a
	 * JSONArray of JSONObjects.
	 *
	 * @param ifNullReturnEmpty
	 *            if the list is null, return an empty list. If this value is false,
	 *            return null for a null list.
	 * @param list
	 *            the list of objects to use.
	 * @param <T>
	 *            the type of the objects that implements ToJsonObject .
	 * @return the JSONArray of JSONObjects.
	 */
	public static <T extends ToJsonObject> JSONArray toJSONArray(final boolean ifNullReturnEmpty, final List<T> list) {
		if (list == null) {
			if (ifNullReturnEmpty) {
				return new JSONArray();
			} else {
				return null;
			}
		}
		final JSONArray jsonArray = new JSONArray();

		for (final T t : list) {
			jsonArray.put(t.toJSONObject());
		}

		return jsonArray;
	}

	/**
	 * converts a byte array to a hex string in reverse byte order.
	 *
	 * @param bytes
	 *            the array of bytes.
	 * @return the string.
	 */
	public static String toReverseHexString(final byte... bytes) {
		final byte[] ba = new byte[bytes.length];
		System.arraycopy(bytes, 0, ba, 0, bytes.length);
		ArrayUtils.reverse(ba);
		final BigInteger bi = new BigInteger(1, ba);
		return bi.toString(16);
	}

	/**
	 * converts the value to a double, by dividing by DECIMAL_DIVISOR.
	 *
	 * @param value
	 *            the long value to convert.
	 * @return the converted value
	 */
	public static double toRoundedDouble(final long value) {
		final double input = value / DECIMAL_DIVISOR;
		return input;
	}

	/**
	 * converts the value to a double, by dividing by DECIMAL_DIVISOR. then formats
	 * it to a string with two decimal places.
	 *
	 * @param value
	 *            the long value to convert.
	 * @return the converted value as a string.
	 */
	public static String toRoundedDoubleAsString(final long value) {
		final double input = toRoundedDouble(value);
		return String.format("%.2f", input);
	}

	/**
	 * converts the value to a long, by dividing by DECIMAL_DIVISOR.
	 *
	 * @param value
	 *            the long value to convert.
	 * @return the converted value as a string.
	 */
	public static long toRoundedLong(final long value) {
		final long input = value / DECIMAL_DIVISOR;
		return input;
	}

	/**
	 * converts the value to a long, by dividing by DECIMAL_DIVISOR. then formats it
	 * to a string.
	 *
	 * @param value
	 *            the long value to convert.
	 * @return the converted value as a string.
	 */
	public static String toRoundedLongAsString(final long value) {
		final long input = toRoundedLong(value);
		return Long.toString(input);
	}

	/**
	 * the constructor.
	 */
	private ModelUtil() {

	}

}
