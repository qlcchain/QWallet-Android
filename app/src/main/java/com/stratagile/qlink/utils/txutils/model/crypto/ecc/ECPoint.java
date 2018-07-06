package com.stratagile.qlink.utils.txutils.model.crypto.ecc;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;


public final class ECPoint {

	private static byte[] concat(final byte[] ba1, final byte[] ba2) {
		final byte[] ba = new byte[ba1.length + ba2.length];
		System.arraycopy(ba1, 0, ba, 0, ba1.length);
		System.arraycopy(ba2, 0, ba, ba1.length, ba2.length);
		return ba;
	}

	public static ECPoint DecodePoint(final byte[] encoded, final ECCurve curve) {
		ECPoint p = null;
		final int expectedLength = (curve.Q.bitLength() + 7) / 8;
		switch (encoded[0]) {
		// infinity
		case 0x00: {
			if (encoded.length != 1) {
				throw new RuntimeException("Incorrect length for infinity encoding");
			}
			p = curve.Infinity;
			break;
		}
		// compressed
		case 0x02:
			// compressed
		case 0x03: {
			if (encoded.length != (expectedLength + 1)) {
				throw new RuntimeException("Incorrect length for compressed encoding");
			}
			final int yTilde = encoded[0] & 1;

			final BigInteger x1 = new BigInteger(
					concat(reverse(subarray(encoded, 1, encoded.length - 1)), new byte[1]));
			p = DecompressPoint(encoded, yTilde, x1, curve);
			break;
		}
		// uncompressed
		case 0x04:
			// hybrid
		case 0x06:
			// hybrid
		case 0x07: {
			if (encoded.length != ((2 * expectedLength) + 1)) {
				throw new RuntimeException("Incorrect length for uncompressed/hybrid encoding");
			} else {
				final BigInteger x1 = new BigInteger(
						concat(reverse(subarray(encoded, 1, expectedLength)), new byte[1]));
				final BigInteger y1 = new BigInteger(
						concat(reverse(subarray(encoded, 1 + expectedLength, encoded.length - (1 + expectedLength))),
								new byte[1]));
				p = new ECPoint(encoded, new ECFieldElement(x1, curve), new ECFieldElement(y1, curve), curve);
			}
			break;
		}
		default:
			p = new ECPoint(encoded, "Invalid point encoding " + encoded[0]);
		}
		return p;
	}

	private static ECPoint DecompressPoint(final byte[] encoded, final int yTilde, final BigInteger X1,
			final ECCurve curve) {
		try {
			final ECFieldElement x = new ECFieldElement(X1, curve);
			final ECFieldElement alpha = x.multiply(x.Square().add(curve.A)).add(curve.B);
			ECFieldElement beta = alpha.Sqrt();

			//
			// if we can't find a sqrt we haven't got a point on the
			// curve - run!
			//
			if (beta == null) {
				throw new ArithmeticException("Invalid point compression");
			}

			final BigInteger betaValue = beta.value;
			final int bit0 = isEven(betaValue) ? 0 : 1;

			if (bit0 != yTilde) {
				// Use the other root
				beta = new ECFieldElement(curve.Q.subtract(betaValue), curve);
			}

			return new ECPoint(encoded, x, beta, curve);
		} catch (final Exception e) {
			return new ECPoint(encoded, e.getMessage());
		}
	}

	public static ECPoint DeserializeFrom(final ByteBuffer reader, final ECCurve curve) {
		final int expectedLength = (curve.Q.bitLength() + 7) / 8;
		final byte[] buffer = new byte[1 + (expectedLength * 2)];
		buffer[0] = reader.get();
		switch (buffer[0]) {
		case 0x00:
			return curve.Infinity;
		case 0x02:
		case 0x03:
			reader.get(buffer, 1, expectedLength);
			return DecodePoint(subarray(buffer, 0, 1 + expectedLength), curve);
		case 0x04:
		case 0x06:
		case 0x07:
			reader.get(buffer, 1, expectedLength * 2);
			return DecodePoint(buffer, curve);
		default:
			return new ECPoint(buffer, "Invalid point encoding " + buffer[0]);
		}
	}

	public static boolean isEven(final BigInteger number) {
		return number.getLowestSetBit() != 0;
	}

	private static byte[] reverse(final byte[] ba) {
		ArrayUtils.reverse(ba);
		return ba;
	}

	private static byte[] subarray(final byte[] in, final int start, final int length) {
		if ((start + length) > in.length) {
			throw new RuntimeException("array must be of at least size " + (start + length));
		}

		final byte[] out = new byte[length];
		System.arraycopy(in, start, out, 0, length);
		return out;
	}

	public final String error;

	public final byte[] buffer;

	public final ECFieldElement X;

	public final ECFieldElement Y;

	public final ECCurve Curve;

	public ECPoint() {
		this(new byte[0], null, null, ECCurve.Secp256r1, null);
	}

	public ECPoint(final byte[] buffer, final ECFieldElement x, final ECFieldElement y, final ECCurve curve) {
		this(buffer, x, y, curve, null);
	}

	public ECPoint(final byte[] buffer, final ECFieldElement x, final ECFieldElement y, final ECCurve curve,
			final String error) {
		if (((x != null) && (y == null)) || ((x == null) && (y != null))) {
			throw new RuntimeException("Exactly one of the field elements is null");
		}
		this.buffer = buffer;
		X = x;
		Y = y;
		Curve = curve;
		this.error = error;
	}

	public ECPoint(final byte[] buffer, final String error) {
		this(buffer, null, null, ECCurve.Secp256r1, error);
	}

	public boolean getIsInfinity() {
		return (X == null) && (Y == null);
	}

	public int getSize() {
		if (getIsInfinity()) {
			return 1;
		} else {
			return 33;
		}
	}

	public byte[] toByteArray() {
		return buffer;
	}
}
