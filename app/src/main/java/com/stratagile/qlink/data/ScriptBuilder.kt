package com.stratagile.qlink.data

import com.stratagile.qlink.hexStringToByteArray
import com.stratagile.qlink.to8BytesArray
import com.stratagile.qlink.toHex
import com.stratagile.qlink.toMinimumByteArray
import java.nio.ByteOrder

/**
 * Created by drei on 1/19/18.
 */
class ScriptBuilder {
    var bytes = byteArrayOf()
    fun getScriptHexString(): String {
        return bytes.toHex()
    }

    fun pushOpCode(opcode: OPCODE) {
        bytes += byteArrayOf(opcode.value)
    }

    fun pushBool(value: Boolean) {
        if (value == true) {
            pushOpCode(OPCODE.PUSH1)
        } else {
            pushOpCode(OPCODE.PUSH0)
        }
    }

    fun pushInt(value: Long) {

        when (value) {
            (-1).toLong() -> pushOpCode(OPCODE.PUSHM1)
            0.toLong() -> pushOpCode(OPCODE.PUSH0)
            1.toLong() -> pushOpCode(OPCODE.PUSH1)
            2.toLong() -> pushOpCode(OPCODE.PUSH2)
            3.toLong() -> pushOpCode(OPCODE.PUSH3)
            4.toLong() -> pushOpCode(OPCODE.PUSH4)
            5.toLong() -> pushOpCode(OPCODE.PUSH5)
            6.toLong() -> pushOpCode(OPCODE.PUSH6)
            7.toLong() -> pushOpCode(OPCODE.PUSH7)
            8.toLong() -> pushOpCode(OPCODE.PUSH8)
            9.toLong() -> pushOpCode(OPCODE.PUSH9)
            10.toLong() -> pushOpCode(OPCODE.PUSH10)
            11.toLong() -> pushOpCode(OPCODE.PUSH11)
            12.toLong() -> pushOpCode(OPCODE.PUSH12)
            13.toLong() -> pushOpCode(OPCODE.PUSH13)
            14.toLong() -> pushOpCode(OPCODE.PUSH14)
            15.toLong() -> pushOpCode(OPCODE.PUSH15)
            16.toLong() -> pushOpCode(OPCODE.PUSH16)
            else -> pushData(to8BytesArray(value).toHex())
        }
    }

    fun appendByteArray(bytesToAppend: ByteArray) {
        for(byte in bytesToAppend) {
            bytes += byteArrayOf(byte)
        }
    }

    fun pushHexString(stringValue: String) {
        val stringBytes = stringValue.hexStringToByteArray()
        if (stringBytes.size < 0x4B) {
            appendByteArray(toMinimumByteArray(stringBytes.size))
            appendByteArray(stringBytes)
        } else if (stringBytes.size < 0x100) {
            pushOpCode(OPCODE.PUSHDATA1)
            appendByteArray(toMinimumByteArray(stringBytes.size))
            appendByteArray(stringBytes)
        } else if (stringBytes.size < 0x10000) {
            pushOpCode(OPCODE.PUSHDATA2)
            appendByteArray(toMinimumByteArray(stringBytes.size))
            appendByteArray(stringBytes)
        } else {
            pushOpCode(OPCODE.PUSHDATA4)
            appendByteArray(toMinimumByteArray(stringBytes.size))
            appendByteArray(stringBytes)
        }
    }

    fun pushArray(array: Array<Any?>) {
        for (elem in array) {
            pushData(elem)
        }
        pushInt(array.size.toLong())
        pushOpCode(OPCODE.PACK)
    }

    fun resetScript() {
        bytes = byteArrayOf()
    }

    fun pushData(data: Any?) {
        if (data == null) {
            pushBool(false)
            return
        }
        val unwrappedData = data!!

        if (unwrappedData is String) {
            pushHexString(unwrappedData)
        } else if (unwrappedData is Boolean) {
            pushBool(unwrappedData)
        } else if (unwrappedData is Long) {
            pushInt(unwrappedData)
        } else if (unwrappedData is Array<*>) {
            pushArray(unwrappedData as Array<Any?>)
        } else {
            return
        }
    }

    fun pushContractInvoke(scriptHash: String, operation: String? = null, args: Any? = null, useTailCall: Boolean = false) {
        pushData(args)
        if (operation != null) {
            val hex = operation.toByteArray().toHex()
            pushData(hex)
        }
        if (scriptHash.length != 40) {
            //You're fucked
            throw(Exception("Invalid Script Hash"))
        }
        if (useTailCall) {
            pushOpCode(OPCODE.TAILCALL)
        } else {
            pushOpCode(OPCODE.APPCALL)
            val toAppendBytes = scriptHash.hexStringToByteArray()
            toAppendBytes.reverse()
            appendByteArray(toAppendBytes)
        }
    }
}