package com.stratagile.qlink.utils.Base64;


public interface BinaryDecoder extends Decoder {
    byte[] decode(byte[] var1) throws DecoderException;
}
