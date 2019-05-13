package com.stratagile.qlink.utils.txutils.model.neo

import com.stratagile.qlink.hexStringToByteArray
import unsigned.toUByte

class TransactionAttribute {
    enum class USAGE(value: Byte) {
        // Constants
        contractHash(0x00.toUByte()),
        ECDH02(0x02.toUByte()),
        ECDH03(0x03.toUByte()),
        Script(0x20.toUByte()),
        Vote(0x30.toUByte()),
        CertUrl(0x80.toUByte()),
        DescriptionUrl(0x81.toUByte()),
        Description(0x90.toUByte()),
        Hash1(0xa1.toUByte()),
        Hash2(0xa2.toUByte()),
        Hash3(0xa3.toUByte()),
        Hash4(0xa4.toUByte()),
        Hash5(0xa5.toUByte()),
        Hash6(0xa6.toUByte()),
        Hash7(0xa7.toUByte()),
        Hash8(0xa8.toUByte()),
        Hash9(0xa9.toUByte()),
        Hash10(0xaa.toUByte()),
        Hash11(0xab.toUByte()),
        Hash12(0xac.toUByte()),
        Hash13(0xad.toUByte()),
        Hash14(0xae.toUByte()),
        Hash15(0xaf.toUByte()),

        Remark(0xf0.toUByte()),
        Remark1(0xf1.toUByte()),
        Remark2(0xf2.toUByte()),
        Remark3(0xf3.toUByte()),
        Remark4(0xf4.toUByte()),
        Remark5(0xf5.toUByte()),
        Remark6(0xf6.toUByte()),
        Remark7(0xf7.toUByte()),
        Remark8(0xf8.toUByte()),
        Remark9(0xf9.toUByte()),
        Remark10(0xfa.toUByte()),
        Remark11(0xfb.toUByte()),
        Remark12(0xfc.toUByte()),
        Remark13(0xfd.toUByte()),
        Remark14(0xfe.toUByte()),
        Remark15(0xff.toUByte());

        var value = value
    }

    var data: ByteArray? = null

    fun descriptionAttribute(description: String): TransactionAttribute{
        val descriptionBytes = description.toByteArray(Charsets.UTF_8)
        val lengthBytes = byteArrayOf(descriptionBytes.count().toUByte())
        data = byteArrayOf(USAGE.Description.value) + lengthBytes + descriptionBytes
        return this
    }

    fun hexDescriptionAttribute(descriptionHex: String): TransactionAttribute {
        val descriptionBytes = descriptionHex.hexStringToByteArray()
        val lengthBytes = byteArrayOf(descriptionBytes.count().toUByte())
        data = byteArrayOf(USAGE.Description.value) + lengthBytes+ descriptionBytes
        return this
    }

    fun remarkAttribute(remark: String): TransactionAttribute{
        val remarkBytes = remark.toByteArray(Charsets.UTF_8)
        val lengthBytes = byteArrayOf(remarkBytes.count().toUByte())
        data = byteArrayOf(USAGE.Remark.value) + lengthBytes + remarkBytes
        return this
    }

    fun scriptAttribute(script: String): TransactionAttribute{
        var attribute: ByteArray =  byteArrayOf(USAGE.Script.value)
        attribute += script.hexStringToByteArray()
        data = attribute
        return this
    }

    fun dapiRemarkAttribute(remark: String): TransactionAttribute{
        val remarkBytes = remark.toByteArray(Charsets.UTF_8)
        val lengthBytes = byteArrayOf(remarkBytes.count().toUByte())
        data = byteArrayOf(USAGE.Remark1.value) + lengthBytes + remarkBytes
        return this
    }
}