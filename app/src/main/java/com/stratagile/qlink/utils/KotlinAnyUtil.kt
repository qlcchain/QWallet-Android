package com.stratagile.qlink.utils

fun Any.getLine() : String {
    return " Line " + Thread.currentThread().getStackTrace()[1].getLineNumber().toString()
}