package qlc.mng;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

import com.rfksystems.blake2b.Blake2b;
import com.rfksystems.blake2b.security.Blake2bProvider;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.math.Curve;
import net.i2p.crypto.eddsa.math.Field;
import net.i2p.crypto.eddsa.math.ed25519.Ed25519LittleEndianEncoding;
import net.i2p.crypto.eddsa.math.ed25519.Ed25519ScalarOps;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import qlc.utils.Checking;

public class WalletMng {

    private static EdDSANamedCurveSpec ED25519_BLAKE2B_CURVES_PEC;

    static {
        Security.addProvider(new Blake2bProvider());

        Field ED25519_FIELD = new Field(
                256, // b
                Utils.hexToBytes("edffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"), // q
                new Ed25519LittleEndianEncoding());

        Curve ED25519_CURVE = new Curve(ED25519_FIELD,
                Utils.hexToBytes("a3785913ca4deb75abd841414d0a700098e879777940c78c73fe6f2bee6c0352"), // d
                ED25519_FIELD.fromByteArray(Utils.hexToBytes("b0a00e4a271beec478e42fad0618432fa7d7fb3d99004d2b0bdfc14f8024832b"))); // I

        ED25519_BLAKE2B_CURVES_PEC = new EdDSANamedCurveSpec(
                EdDSANamedCurveTable.ED_25519,
                ED25519_CURVE,
                Blake2b.BLAKE2_B_512, // H
                new Ed25519ScalarOps(), // l
                ED25519_CURVE.createPoint( // B
                        Utils.hexToBytes("5866666666666666666666666666666666666666666666666666666666666666"),
                        true)); // Precompute tables for B

        EdDSANamedCurveTable.defineCurve(ED25519_BLAKE2B_CURVES_PEC);
    }

    public  static byte[] createPublicKey(byte[] privateKey) {
        Checking.checkKey(privateKey);
        EdDSAPrivateKeySpec key = new EdDSAPrivateKeySpec(privateKey, ED25519_BLAKE2B_CURVES_PEC);
        return key.getA().toByteArray();
    }

    public static byte[] sign(byte[] hash, byte[] privateKey) {
        try {
            EdDSAEngine edDSAEngine = new EdDSAEngine(MessageDigest.getInstance(Blake2b.BLAKE2_B_512));
            EdDSAPrivateKeySpec edDSAPrivateKeySpec = new EdDSAPrivateKeySpec(privateKey, ED25519_BLAKE2B_CURVES_PEC);
            EdDSAPrivateKey edDSAPrivateKey = new EdDSAPrivateKey(edDSAPrivateKeySpec);
            edDSAEngine.initSign(edDSAPrivateKey);
            edDSAEngine.setParameter(EdDSAEngine.ONE_SHOT_MODE);
            edDSAEngine.update(hash);
            return edDSAEngine.sign();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("It wasn't possible to sign " + Arrays.toString(hash), e);
        }
    }

    public static boolean verify(byte[] signature, byte[] hash, byte[] publicKey) {
        try {
            EdDSAEngine edDSAEngine = new EdDSAEngine(MessageDigest.getInstance(Blake2b.BLAKE2_B_512));
            EdDSAPublicKeySpec edDSAPublicKeySpec = new EdDSAPublicKeySpec(publicKey, ED25519_BLAKE2B_CURVES_PEC);
            EdDSAPublicKey edDSAPublicKey = new EdDSAPublicKey(edDSAPublicKeySpec);
            edDSAEngine.initVerify(edDSAPublicKey);
            edDSAEngine.setParameter(EdDSAEngine.ONE_SHOT_MODE);
            edDSAEngine.update(hash);
            return edDSAEngine.verify(signature);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("It wasn't possible to verify " + Arrays.toString(hash), e);
        }
    }

}
