package com.stratagile.qlink.utils.txutils.model.util;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;



/**
 * the utility for writing to a network output stream.
 *
 * @author coranos
 *
 */
public final class NetworkUtil {

	/**
	 * UTF8 encoding.
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * the int byte array.
	 *
	 * @param x
	 *            the number to write (can be a long, only the lower bytes are
	 *            written);
	 * @return the byte array.
	 */
	public static byte[] getIntByteArray(final long x) {
		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt((int) x);
		final byte[] ba = buffer.array();
		return ba;
	}

	/**
	 * the long byte array.
	 *
	 * @param x
	 *            the number to write
	 * @return the byte array.
	 */
	public static byte[] getLongByteArray(final long x) {
		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		final byte[] ba = buffer.array();
		return ba;
	}

	/**
	 * the short byte array.
	 *
	 * @param x
	 *            the number to write (can be a long, only the lower bytes are
	 *            written);
	 * @return the byte array.
	 */
	public static byte[] getShortByteArray(final long x) {
		final ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
		buffer.putShort((short) x);
		final byte[] ba = buffer.array();
		return ba;
	}

	/**
	 * writes a byte array to an output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param ba
	 *            the byte array to use.
	 */
	public static void write(final OutputStream out, final byte... ba) {
		try {
			out.write(ba);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * writes a byte array to an output stream, reversing it if needed.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param ba
	 *            the byte array to use.
	 * @param reversed
	 *            if true, reverse the byte array before writing.
	 */
	public static void write(final OutputStream out, final byte[] ba, final boolean reversed) {
		if (reversed) {
			final byte[] ba1 = new byte[ba.length];
			System.arraycopy(ba, 0, ba1, 0, ba.length);
			write(out, ba1);
		} else {
			write(out, ba);
		}
	}

	/**
	 * writes a byte array to an output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param ba
	 *            the byte array to use.
	 * @param offset
	 *            the offset.
	 * @param length
	 *            the length.
	 */
	public static void write(final OutputStream out, final byte[] ba, final int offset, final int length) {
		try {
			out.write(ba, offset, length);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * writes a list of ByteArraySerializable objects to a output stream. first
	 * writes the size using writeVarInt, then writes the objects in the list.
	 *
	 * @param out
	 *            the Output Stream to use.
	 * @param list
	 *            the list of objects to write.
	 * @param <T>
	 *            the ByteArraySerializable type.
	 */
	public static <T extends ByteArraySerializable> void write(final OutputStream out, final List<T> list) {
		writeVarInt(out, list.size());
		for (final T t : list) {
			write(out, t.toByteArray());
		}
	}

	/**
	 * writes a ByteArraySerializable to an output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param t
	 *            the object to write.
	 * @param reversed
	 *            if true, reverse the byte array before writing.
	 * @param <T>
	 *            the ByteArraySerializable type.
	 */
	public static <T extends ByteArraySerializable> void write(final OutputStream out, final T t,
			final boolean reversed) {
		final byte[] ba = t.toByteArray();
		if (reversed) {
			ArrayUtils.reverse(ba);
		}
		write(out, ba);
	}

	/**
	 * writes a byte to the output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param value
	 *            the byte to write.
	 */
	private static void writeByte(final OutputStream out, final byte value) {
		write(out, new byte[] { value });
	}

	/**
	 * writes a byte array to an output stream. first it writes the length, using
	 * writeVarInt, then it writes the buyte array.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param byteArray
	 *            the byte array to use.
	 */
	public static void writeByteArray(final OutputStream out, final byte[] byteArray) {
		writeVarInt(out, byteArray.length);
		write(out, byteArray);
	}

	/**
	 * writes an integer to an output stream.
	 *
	 * @param out
	 *            the output stream.
	 * @param x
	 *            the integer. (up to a long may be passed in, only the lower bytes
	 *            are written).
	 */
	private static void writeInt(final OutputStream out, final long x) {
		final byte[] ba = getIntByteArray(x);
		write(out, ba);
	}

	/**
	 * writes a long to an output stream.
	 *
	 * @param out
	 *            the output stream.
	 * @param x
	 *            the long.
	 */
	public static void writeLong(final OutputStream out, final long x) {
		final byte[] ba = getLongByteArray(x);
		write(out, ba);
	}

	/**
	 * writes a short as a byte array to the output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param x
	 *            the short to write. (the short can be any integer even a long,
	 *            only the lower bytes will be written).
	 */
	private static void writeShort(final OutputStream out, final long x) {
		final byte[] ba = getShortByteArray(x);
		ArrayUtils.reverse(ba);
		write(out, ba);
	}

	/**
	 * writes the string to the byte array.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param str
	 *            the string to use.
	 * @param length
	 *            the string length to use.
	 * @throws UnsupportedEncodingException
	 *             if an error occurs.
	 */
	public static void writeString(final OutputStream out, final int length, final String str) {
		try {
			final byte[] ba = str.getBytes(UTF_8);
			final ByteBuffer bb = ByteBuffer.allocate(length);
			bb.put(ba);
			write(out, bb.array());
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * writes the string to the byte array.
	 *
	 * @param out
	 *            the output stream to use.
	 * @param str
	 *            the string to use.
	 * @throws UnsupportedEncodingException
	 *             if an error occurs.
	 */
	public static void writeString(final OutputStream out, final String str) {
		try {
			final byte[] ba = str.getBytes(UTF_8);
			writeVarInt(out, ba.length);
			write(out, ba);
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * write a variable intot the output stream.
	 *
	 * @param out
	 *            the output stream.
	 * @param value
	 *            the long to be written as a variable int.
	 */
	public static void writeVarInt(final OutputStream out, final long value) {
		if (value < 0) {
			throw new RuntimeException("value out of range:" + value);
		}
		if (value < 0xFD) {
			writeByte(out, (byte) value);
		} else if (value <= 0xFFFF) {
			writeByte(out, (byte) 0xFD);
			writeShort(out, value);
		} else if (value <= 0xFFFFFFFF) {
			writeByte(out, (byte) 0xFE);
			writeInt(out, value);
		} else {
			writeByte(out, (byte) 0xFF);
			writeLong(out, value);
		}
	}

	/**
	 * the constructor.
	 */
	private NetworkUtil() {
	}
}
