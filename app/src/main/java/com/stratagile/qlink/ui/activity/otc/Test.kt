package com.stratagile.qlink.ui.activity.otc

fun main(args : Array<String>) {
    var txid = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
    try {
        var addStr = ""
        var addCount = 64 - txid.length
        if (addCount > 0) {
            for (i in 0..addCount) {
                addStr+= "0"
            }
        }
        txid = addStr + txid
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(txid)
}