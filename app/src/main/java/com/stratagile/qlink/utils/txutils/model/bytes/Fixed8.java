package com.stratagile.qlink.utils.txutils.model.bytes;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.NumberFormat;


/**
 * an unsigned 64 bit long. The long value is cached, rather than calculated
 * every time.
 *
 * @author coranos
 *
 */
public final class Fixed8 implements ByteArraySerializable {

	/**
	 * the size, 8 bytes.
	 */
	public static final int SIZE = UInt64.SIZE;

	/**
	 * the value.
	 */
	public final long value;

	/**
	 * the value as a UInt64.
	 */
	private final UInt64 valueObj;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the byte buffer to read.
	 */
	public Fixed8(final ByteBuffer bb) {
		valueObj = ModelUtil.getUInt64(bb);
		value = valueObj.toPositiveBigInteger().longValue();
	}

	/**
	 * compares two fixed8 objects.
	 *
	 * @param that
	 *            the other fixed8.
	 * @return 0 if they are equal, otherwise the result of calling compareto on
	 *         their corresponding valueObjs.
	 */
	public int compareTo(final Fixed8 that) {
		return valueObj.compareTo(that.valueObj);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Fixed8) {
			final Fixed8 that = (Fixed8) obj;
			return value == that.value;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}

	@Override
	public byte[] toByteArray() {
		final byte[] ba = valueObj.toByteArray();
		return ba;
	}

	/**
	 * returns a positive BigInteger.
	 *
	 * @return this array as a BigInteger, assuming the bytes represent a signed
	 *         int.
	 */
	public BigInteger toPositiveBigInteger() {
		return valueObj.toPositiveBigInteger();
	}

	@Override
	public String toString() {
		return NumberFormat.getIntegerInstance().format(value);
	}
}
