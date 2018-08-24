package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * exclusive data for publish transactions.
 *
 * @author coranos
 */
public final class PublishExclusiveData implements ExclusiveData, ToJsonObject, ByteArraySerializable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * the version.
	 */
	private final byte version;

	/**
	 * the script.
	 */
	public final byte[] script;

	/**
	 * the parameter list.
	 */
	public final List<ContractParameterType> parameterList;

	/**
	 * the return type.
	 */
	public final ContractParameterType returnType;

	/**
	 * the flag saying if it needs storage or not.
	 */
	public final boolean needStorage;

	/**
	 * the name.
	 */
	public final String name;

	/**
	 * the code version.
	 */
	public final String codeVersion;

	/**
	 * the author.
	 */
	public final String author;

	/**
	 * the email.
	 */
	public final String email;

	/**
	 * the description.
	 */
	public final String description;

	/**
	 * the constructor.
	 *
	 * @param version
	 *            the version to use.
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public PublishExclusiveData(final byte version, final ByteBuffer bb) {
		this.version = version;
		script = ModelUtil.getVariableLengthByteArray(bb);
		parameterList = Collections.unmodifiableList(
				Arrays.asList(ContractParameterType.valuesOf(ModelUtil.getVariableLengthByteArray(bb))));
		returnType = ContractParameterType.valueOfByte(ModelUtil.getByte(bb));
		if (version >= 1) {
			needStorage = ModelUtil.getBoolean(bb);
		} else {
			needStorage = false;
		}
		name = ModelUtil.getVariableLengthString(bb);
		codeVersion = ModelUtil.getVariableLengthString(bb);
		author = ModelUtil.getVariableLengthString(bb);
		email = ModelUtil.getVariableLengthString(bb);
		description = ModelUtil.getVariableLengthString(bb);
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.writeByteArray(bout, script);
		final byte[] parameterListBa = new byte[parameterList.size()];
		for (int ix = 0; ix < parameterList.size(); ix++) {
			parameterListBa[ix] = parameterList.get(ix).getTypeByte();
		}
		NetworkUtil.writeByteArray(bout, parameterListBa);
		NetworkUtil.write(bout, new byte[] { returnType.getTypeByte() });

		if (version >= 1) {
			if (needStorage) {
				NetworkUtil.write(bout, new byte[] { 1 });
			} else {
				NetworkUtil.write(bout, new byte[] { 0 });
			}
		}

		NetworkUtil.writeString(bout, name);
		NetworkUtil.writeString(bout, codeVersion);
		NetworkUtil.writeString(bout, author);
		NetworkUtil.writeString(bout, email);
		NetworkUtil.writeString(bout, description);

		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();

		try {
			json.put("script", ModelUtil.toHexString(script));
			final JSONArray parameterListJson = new JSONArray();
			for (final ContractParameterType type : parameterList) {
				parameterListJson.put(type.name());
			}
			json.put("parameterList", parameterListJson);

			json.put("returnType", returnType.name());
			json.put("needStorage", needStorage);
			json.put("name", name);
			json.put("codeVersion", codeVersion);
			json.put("author", author);
			json.put("email", email);
			json.put("description", description);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return json;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

}
