package com.stratagile.qlink

/**
 * Created by drei on 11/22/17.
 */
import com.stratagile.qlink.core.Base58
import java.math.BigDecimal
import java.security.MessageDigest
import java.nio.ByteBuffer
import java.nio.ByteOrder

private val HEX_CHARS = "0123456789abcdef".toCharArray()
private val HEX_CHARS1 = "0123456789abcdef"
fun ByteArray.toHex() : String{
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun String.hexStringToByteArray1() : ByteArray {
    val hex = toLowerCase()
    val result = ByteArray(length / 2)

    for (i in 0 until hex.length step 2) {
        val firstIndex = HEX_CHARS1.indexOf(hex[i])
        val secondIndex = HEX_CHARS1.indexOf(hex[i + 1])

        val octet = firstIndex.shl(4).or(secondIndex)
        result.set(i.shr(1), octet.toByte())
    }

    return result
}

fun String.hexStringToByteArray() : ByteArray {
    val len = this.length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return data
}

fun String.littleEndianHexStringToInt64(): Long {
    var b = this.hexStringToByteArray()
    val len = b.size
    var i = 0
    //we need to pad to match whatever type we need. If the number of bytes is less than target type, Java returns BufferUnderFlow exception.
    while (i < 8) { //long 8 bytes
        b += 0.toByte()
        i += 1
    }
    val x = java.nio.ByteBuffer.wrap(b).order(java.nio.ByteOrder.LITTLE_ENDIAN).long
    return x
}

fun String.hashFromAddress(): String {
    val bytes = Base58.decodeChecked(this)
    val shortened = bytes.sliceArray(IntRange(0,20))
    return shortened.sliceArray(IntRange(1,shortened.count() - 1)).toHex()
}


fun String.hash160(): String {
    val bytes = Base58.decodeChecked(this)
    val shortened = bytes.sliceArray(IntRange(1,bytes.count() - 1))
    return shortened.reversedArray().toHex()
}

fun to8BytesArray(value: Int, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): ByteArray {
    return ByteBuffer.allocate(8).order(byteOrder).putInt(value).array()
}


fun to8BytesArray(value: Long): ByteArray {
    return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array()
}

fun toMinimumByteArray(value: Int): ByteArray {
    val bytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array()
    var minBytes = byteArrayOf()
    var foundFirstNonZero = false
    for (byte in bytes.reversed()) {
        if(byte == 0x00.toByte() && !foundFirstNonZero) {
            continue
        }
        foundFirstNonZero = true
        minBytes += byteArrayOf(byte)
    }
    return minBytes
}

fun String.toSafeDecimal(): BigDecimal {
    return BigDecimal(this)
}

fun Double.toSafeDecimal(): BigDecimal {
    return BigDecimal(this)
}

fun BigDecimal.toSafeMemory(decimals: Int): Long {
    return this.multiply(BigDecimal(Math.pow(10.0, decimals.toDouble()))).toLong()
}

fun Byte.toPositiveInt() = toInt() and 0xFF

