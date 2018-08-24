package com.stratagile.qlink.utils.txutils.model.crypto.ecc;

import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;

import java.math.BigInteger;


public class ECCurve {

	/// <summary>
	/// 曲线secp256k1
	/// </summary>
	public static final ECCurve Secp256k1 = new ECCurve(
			new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16), BigInteger.ZERO,
			BigInteger.valueOf(7),
			new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16),
			ModelUtil.decodeHex("04" + "79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798"
					+ "483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8"));

	/// <summary>
	/// 曲线secp256r1
	/// </summary>
	public static final ECCurve Secp256r1 = new ECCurve(
			new BigInteger("00FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", 16),
			new BigInteger("00FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", 16),
			new BigInteger("005AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", 16),
			new BigInteger("00FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16),
			ModelUtil.decodeHex("04" + "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296"
					+ "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5"));

	public final BigInteger Q;

	public final ECFieldElement A;

	public final ECFieldElement B;

	public final BigInteger N;

	public final ECPoint Infinity;

	public final ECPoint G;

	private ECCurve(final BigInteger Q, final BigInteger A, final BigInteger B, final BigInteger N, final byte[] G) {
		this.Q = Q;
		this.A = new ECFieldElement(A, this);
		this.B = new ECFieldElement(B, this);
		this.N = N;
		Infinity = new ECPoint(new byte[1], null, null, this);
		this.G = ECPoint.DecodePoint(G, this);
	}
}
