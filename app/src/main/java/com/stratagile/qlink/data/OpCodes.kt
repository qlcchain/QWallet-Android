package com.stratagile.qlink.data

import unsigned.toUByte


/**
 * Created by drei on 1/19/18.
 */

enum class OPCODE(value: Byte) {
    // Constants
    PUSH0(0x00.toUByte()),
    //Constamts
    PUSHBYTES1(0x01.toUByte()), // 01-4B The next opcode bytes is data to be pushed onto the stack
    PUSHBYTES75(0x4B.toUByte()),
    PUSHDATA1(0x4C.toUByte()), // The next byte contains the number of bytes to be pushed onto the stack.
    PUSHDATA2(0x4D.toUByte()), // The next two bytes contain the number of bytes to be pushed onto the stack.
    PUSHDATA4(0x4E.toUByte()), // The next four bytes contain the number of bytes to be pushed onto the stack.
    PUSHM1(0x4F.toUByte()), // The number -1 is pushed onto the stack.
    PUSH1(0x51.toUByte()), // The number 1 is pushed onto the stack.
    PUSH2(0x52.toUByte()), // The number 2 is pushed onto the stack.
    PUSH3(0x53.toUByte()), // The number 3 is pushed onto the stack.
    PUSH4(0x54.toUByte()), // The number 4 is pushed onto the stack.
    PUSH5(0x55.toUByte()), // The number 5 is pushed onto the stack.
    PUSH6(0x56.toUByte()), // The number 6 is pushed onto the stack.
    PUSH7(0x57.toUByte()), // The number 7 is pushed onto the stack.
    PUSH8(0x58.toUByte()), // The number 8 is pushed onto the stack.
    PUSH9(0x59.toUByte()), // The number 9 is pushed onto the stack.
    PUSH10(0x5A.toUByte()), // The number 10 is pushed onto the stack.
    PUSH11(0x5B.toUByte()), // The number 11 is pushed onto the stack.
    PUSH12(0x5C.toUByte()), // The number 12 is pushed onto the stack.
    PUSH13(0x5D.toUByte()), // The number 13 is pushed onto the stack.
    PUSH14(0x5E.toUByte()), // The number 14 is pushed onto the stack.
    PUSH15(0x5F.toUByte()), // The number 15 is pushed onto the stack.
    PUSH16(0x60.toUByte()), // The number 16 is pushed onto the stack.

    //Flow Control
    NOP(0x61.toUByte()), // Does nothing.
    JMP(0x62.toUByte()),
    JMPIF(0x63.toUByte()),
    JMPIFNOT(0x64.toUByte()),
    CALL(0x65.toUByte()),
    RET(0x66.toUByte()),
    APPCALL(0x67.toUByte()),
    SYSCALL(0x68.toUByte()),
    TAILCALL(0x69.toUByte()),

    //Stack
    DUPFROMALTSTACK(0x6A.toUByte()),
    TOALTSTACK(0x6B.toUByte()), // Puts the input onto the top of the alt stack. Removes it from the main stack.
    FROMALTSTACK(0x6C.toUByte()), // Puts the input onto the top of the main stack. Removes it from the alt stack.
    XDROP(0x6D.toUByte()),
    XSWAP(0x72.toUByte()),
    XTUCK(0x73.toUByte()),
    DEPTH(0x74.toUByte()), // Puts the number of stack items onto the stack.
    DROP(0x75.toUByte()), // Removes the top stack item.
    DUP(0x76.toUByte()), // Duplicates the top stack item.
    NIP(0x77.toUByte()), // Removes the second-to-top stack item.
    OVER(0x78.toUByte()), // Copies the second-to-top stack item to the top.
    PICK(0x79.toUByte()), // The item n back in the stack is copied to the top.
    ROLL(0x7A.toUByte()), // The item n back in the stack is moved to the top.
    ROT(0x7B.toUByte()), // The top three items on the stack are rotated to the left.
    SWAP(0x7C.toUByte()), // The top two items on the stack are swapped.
    TUCK(0x7D.toUByte()), // The item at the top of the stack is copied and inserted before the second-to-top item.

    //Splice
    CAT(0x7E.toUByte()), // Concatenates two strings.
    SUBSTR(0x7F.toUByte()), // Returns a section of a string.
    LEFT(0x80.toUByte()), // Keeps only characters left of the specified point in a string.
    RIGHT(0x81.toUByte()), // Keeps only characters right of the specified point in a string.
    SIZE(0x82.toUByte()), // Returns the length of the input string.

    //BITWISE LOGIC
    INVERT(0x83.toUByte()), // Flips all of the bits in the input.
    AND(0x84.toUByte()), // Boolean and between each bit in the inputs.
    OR(0x85.toUByte()), // Boolean or between each bit in the inputs.
    XOR(0x86.toUByte()), // Boolean exclusive or between each bit in the inputs.
    EQUAL(0x87.toUByte()), // Returns 1 if the inputs are exactly equal, 0 otherwise.


    // Arithmetic
    // Note: Arithmetic inputs are limited to signed 32-bit integers, but may overflow their output.
    INC(0x8B.toUByte()), // 1 is added to the input.
    DEC(0x8C.toUByte()), // 1 is subtracted from the input.
    SIGN(0x8D.toUByte()),
    NEGATE(0x8F.toUByte()), // The sign of the input is flipped.
    ABS(0x90.toUByte()), // The input is made positive.
    NOT(0x91.toUByte()), // If the input is 0 or 1, it is flipped. Otherwise the output will be 0.
    NZ(0x92.toUByte()), // Returns 0 if the input is 0. 1 otherwise.
    ADD(0x93.toUByte()), // a is added to b.
    SUB(0x94.toUByte()), // b is subtracted from a.
    MUL(0x95.toUByte()), // a is multiplied by b.
    DIV(0x96.toUByte()), // a is divided by b.
    MOD(0x97.toUByte()), // Returns the remainder after dividing a by b.
    SHL(0x98.toUByte()), // Shifts a left b bits, preserving sign.
    SHR(0x99.toUByte()), // Shifts a right b bits, preserving sign.
    BOOLAND(0x9A.toUByte()), // If both a and b are not 0, the output is 1. Otherwise 0.
    BOOLOR(0x9B.toUByte()), // If a or b is not 0, the output is 1. Otherwise 0.
    NUMEQUAL(0x9C.toUByte()), // Returns 1 if the numbers are equal, 0 otherwise.
    NUMNOTEQUAL(0x9E.toUByte()), // Returns 1 if the numbers are not equal, 0 otherwise.
    LT(0x9F.toUByte()), // Returns 1 if a is less than b, 0 otherwise.
    GT(0xA0.toUByte()), // Returns 1 if a is greater than b, 0 otherwise.
    LTE(0xA1.toUByte()), // Returns 1 if a is less than or equal to b, 0 otherwise.
    GTE(0xA2.toUByte()), // Returns 1 if a is greater than or equal to b, 0 otherwise.
    MIN(0xA3.toUByte()), // Returns the smaller of a and b.
    MAX(0xA4.toUByte()), // Returns the larger of a and b.
    WITHIN(0xA5.toUByte()), // Returns 1 if x is within the specified range (left-inclusive), 0 otherwise.


    // Crypto
    //RIPEMD160 = A6, // The input is hashed using RIPEMD-160.
    //RIPEMD160(A6, )// The input is hashed using RIPEMD-160.
    SHA1(0xA7.toUByte()), // The input is hashed using SHA-1.
    SHA256(0xA8.toUByte()), // The input is hashed using SHA-256.
    HASH160(0xA9.toUByte()),
    HASH256(0xAA.toUByte()),
    CHECKSIG(0xAC.toUByte()),
    CHECKMULTISIG(0xAE.toUByte()),

    // Array
    ARRAYSIZE(0xC0.toUByte()),
    PACK(0xC1.toUByte()),
    UNPACK(0xC2.toUByte()),
    PICKITEM(0xC3.toUByte()),
    SETITEM(0xC4.toUByte()),
    NEWARRAY(0xC5.toUByte()),
    NEWSTRUCT(0xC6.toUByte()),

    // Exceptions
    THROW(0xF0.toUByte()),
    THROWIFNOT(0xF1.toUByte())
    ;
    var value = value

}