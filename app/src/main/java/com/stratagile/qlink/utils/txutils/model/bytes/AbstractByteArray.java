package com.stratagile.qlink.utils.txutils.model.bytes;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Timestamp;


/**
 * an abstract class containing functions commonly used for all immutable fixed
 * length byte arrays.
 *
 * @author coranos
 *
 */
public abstract class AbstractByteArray implements ByteArraySerializable, Comparable<AbstractByteArray>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * the array of bytes.
	 */
	private final byte[] bytes;

	/**
	 * the byte buffer, used for comparison and hashing, but not persisted when
	 * serialized.
	 */
	private final transient ByteBuffer byteBuffer;

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use to fill the byte array. a copy of the byte array
	 *            is used.
	 */
	public AbstractByteArray(final byte[] bytes) {
		if (getByteSize() != -1) {
			if (bytes.length != getByteSize()) {
				throw new RuntimeException(getClass().getSimpleName() + " requires a byte[] of size " + getByteSize()
						+ " the byte[] was of size " + bytes.length);
			}
		}
		this.bytes = bytes;
		byteBuffer = ByteBuffer.wrap(bytes);
	}

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to use to fill the byte array.
	 */
	public AbstractByteArray(final ByteBuffer bb) {
		bytes = new byte[getByteSize()];
		bb.get(bytes);
		byteBuffer = ByteBuffer.wrap(bytes);
	}

	@Override
	public final int compareTo(final AbstractByteArray that) {
		return byteBuffer.compareTo(that.byteBuffer);
	}

	@Override
	public final boolean equals(final Object obj) {
		final AbstractByteArray that = (AbstractByteArray) obj;
		return compareTo(that) == 0;
	}

	/**
	 * returns the byte length.
	 *
	 * @return the length of the byte array.
	 */
	public final int getByteLength() {
		return bytes.length;
	}

	/**
	 * returns a copy of the byte array.
	 *
	 * @return a copy of the byte array. A copy preserves immutability.
	 */
	public final byte[] getBytesCopy() {
		final byte[] ba = new byte[bytes.length];
		System.arraycopy(bytes, 0, ba, 0, bytes.length);
		return ba;
	}

	/**
	 * returns the size of the byte array.
	 *
	 * @return the byte size of this array. This value should not change for the
	 *         life of the object.
	 */
	public abstract int getByteSize();

	@Override
	public final int hashCode() {
		return byteBuffer.hashCode();
	}

	/**
	 * reads in the object.
	 *
	 * @param in
	 *            the input stream to read.
	 * @throws IOException
	 *             if an error occurs.
	 * @throws ClassNotFoundException
	 *             if an error occurs.
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		try {
			final Field f = getClass().getDeclaredField("byteBuffer");
			f.setAccessible(true);
			f.set(this, ByteBuffer.wrap(bytes));
			f.setAccessible(false);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * reads nothing.
	 *
	 * @throws ObjectStreamException
	 *             if an error occurs.
	 */
	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {

	}

	/**
	 * returns a base58 string.
	 *
	 * @return this array as a string in base58.
	 */
	public final String toBase58String() {
		return ModelUtil.toBase58String(bytes);
	}

	/**
	 * returns a base64 string.
	 *
	 * @return this array as a string in base64.
	 */
	public final String toBase64String() {
		return ModelUtil.toBase64String(bytes);
	}

	@Override
	public final byte[] toByteArray() {
		return getBytesCopy();
	}

	/**
	 * returns a hex string.
	 *
	 * @return this array as a hex string.
	 */
	public final String toHexString() {
		return ModelUtil.toHexString(bytes);
	}

	/**
	 * returns a positive BigInteger.
	 *
	 * @return this array as a BigInteger, assuming the bytes represent a signed
	 *         int.
	 */
	public final BigInteger toPositiveBigInteger() {
		return new BigInteger(1, bytes);
	}

	/**
	 * returns a positive BigInteger, in reverse byte order.
	 *
	 * @return this array as a BigInteger, assuming the bytes represent a signed int
	 *         in reverse byte order.
	 */
	public final BigInteger toReverseBytesPositiveBigInteger() {
		final byte[] reverseBytes = getBytesCopy();
		ArrayUtils.reverse(reverseBytes);
		return new BigInteger(1, reverseBytes);
	}

	/**
	 * returns a hex string, in reverse byte order.
	 *
	 * @return this array as a hex string, in reverse byte order.
	 */
	public final String toReverseHexString() {
		return ModelUtil.toReverseHexString(bytes);
	}

	@Override
	public final String toString() {
		return ModelUtil.toReverseHexString(bytes);
	}

	/**
	 * returns this array as a timestamp.
	 *
	 * @return this array as a Timestamp, assuming the bytes represent the number of
	 *         seconds in the timestamp, as a unsigned long.
	 */
	public final Timestamp toTimestamp() {
		return new Timestamp(toPositiveBigInteger().longValue() * 1000);
	}

	/**
	 * writes the object to the stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @throws IOException
	 *             if an error occurs.
	 */
	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		final byte[] ba = toByteArray();
		out.writeInt(ba.length);
		out.write(ba);
	}

}
