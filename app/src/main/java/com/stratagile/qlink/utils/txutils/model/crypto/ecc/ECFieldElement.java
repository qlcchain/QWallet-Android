package com.stratagile.qlink.utils.txutils.model.crypto.ecc;

import java.math.BigInteger;
import java.util.Random;

public final class ECFieldElement {

	private static BigInteger[] FastLucasSequence(final BigInteger p, final BigInteger P, final BigInteger Q,
			final BigInteger k) {
		final int n = k.bitLength();
		final int s = k.getLowestSetBit();

		BigInteger Uh = BigInteger.ONE;
		BigInteger Vl = BigInteger.valueOf(2);
		BigInteger Vh = P;
		BigInteger Ql = BigInteger.ONE;
		BigInteger Qh = BigInteger.ONE;

		for (int j = n - 1; j >= (s + 1); --j) {
			Ql = (Ql.multiply(Qh)).mod(p);

			if (k.testBit(j)) {
				Qh = (Ql.multiply(Q)).mod(p);
				Uh = (Uh.multiply(Vh)).mod(p);
				Vl = (Vh.multiply(Vl).subtract(P.multiply(Ql))).mod(p);
				Vh = ((Vh.multiply(Vh)).subtract((Qh.shiftLeft(1)))).mod(p);
			} else {
				Qh = Ql;
				Uh = (Uh.multiply(Vl).subtract(Ql)).mod(p);
				Vh = (Vh.multiply(Vl).subtract(P.multiply(Ql))).mod(p);
				Vl = ((Vl.multiply(Vl)).subtract((Ql.shiftLeft(1)))).mod(p);
			}
		}

		Ql = Ql.multiply(Qh).mod(p);
		Qh = Ql.multiply(Q).mod(p);
		Uh = Uh.multiply(Vl).subtract(Ql).mod(p);
		Vl = Vh.multiply(Vl).subtract(P.multiply(Ql)).mod(p);
		Ql = Ql.multiply(Qh).mod(p);

		for (int j = 1; j <= s; ++j) {
			Uh = Uh.multiply(Vl).multiply(p);
			Vl = ((Vl.multiply(Vl)).subtract((Ql.shiftLeft(1)))).mod(p);
			Ql = (Ql.multiply(Ql)).mod(p);
		}

		return new BigInteger[] { Uh, Vl };
	}

	private static BigInteger nextRandomBigInteger(final BigInteger n, final Random rnd) {
		final int nlen = n.bitLength();
		final BigInteger nm1 = n.subtract(BigInteger.ONE);
		BigInteger r, s;
		do {
			s = new BigInteger(nlen + 100, rnd);
			r = s.mod(n);
		} while (s.subtract(r).add(nm1).bitLength() >= (nlen + 100));
		return r;
	}

	public final BigInteger value;

	public final ECCurve curve;

	public ECFieldElement(final BigInteger value, final ECCurve curve) {
		if (value.compareTo(curve.Q) >= 0) {
			throw new RuntimeException("x value " + value + " too large in field element");
		}
		this.value = value;
		this.curve = curve;
	}

	public ECFieldElement add(final ECFieldElement that) {
		return new ECFieldElement((value.add(that.value)).mod(curve.Q), that.curve);
	}

	public ECFieldElement multiply(final ECFieldElement that) {
		return new ECFieldElement((value.multiply(that.value)).mod(curve.Q), curve);
	}

	public ECFieldElement Sqrt() {
		if (curve.Q.testBit(1)) {
			final ECFieldElement z = new ECFieldElement(
					value.modPow((curve.Q.shiftRight(2)).add(BigInteger.ONE), curve.Q), curve);
			return z.Square().equals(this) ? z : null;
		}
		final BigInteger qMinusOne = curve.Q.subtract(BigInteger.ONE);
		final BigInteger legendreExponent = qMinusOne.shiftRight(1);
		if (!value.modPow(legendreExponent, curve.Q).equals(BigInteger.ONE)) {
			return null;
		}
		final BigInteger u = qMinusOne.shiftRight(2);
		final BigInteger k = (u.shiftLeft(1)).add(BigInteger.ONE);
		final BigInteger Q = value;
		final BigInteger fourQ = (Q.shiftLeft(2)).mod(curve.Q);
		BigInteger U, V;
		do {
			final Random rand = new Random();
			BigInteger P;
			do {
				P = nextRandomBigInteger(curve.Q, rand);
			} while ((P.compareTo(curve.Q) >= 0)
					|| ((P.multiply(P)).subtract(fourQ).modPow(legendreExponent, curve.Q) != qMinusOne));
			final BigInteger[] result = FastLucasSequence(curve.Q, P, Q, k);
			U = result[0];
			V = result[1];
			if ((V.multiply(V)).mod(curve.Q) == fourQ) {
				if (V.testBit(0)) {
					V = V.add(curve.Q);
				}
				V = V.shiftRight(1);
				return new ECFieldElement(V, curve);
			}
		} while (U.equals(BigInteger.ONE) || U.equals(qMinusOne));
		return null;
	}

	public ECFieldElement Square() {
		return new ECFieldElement((value.multiply(value)).mod(curve.Q), curve);
	}

}
