package com.stratagile.qlink.utils.txutils.model.db.h2;

import com.stratagile.qlink.utils.txutils.model.core.Witness;

import java.util.Map;


/**
 * an object mapper for transaction scripts.
 *
 * @author coranos
 *
 */
public final class WitnessMapToObject extends AbstractMapToObject<Witness> {

	@Override
	public Witness toObject(final Map<String, Object> map) {
		final byte[] invocationBa = getBytes(map, "invocation_script");
		final byte[] verificationBa = getBytes(map, "verification_script");

		final Witness witness = new Witness(invocationBa, verificationBa);
		return witness;
	}

}
