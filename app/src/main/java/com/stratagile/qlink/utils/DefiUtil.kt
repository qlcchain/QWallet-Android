package com.stratagile.qlink.utils

import com.socks.library.KLog
import java.math.BigDecimal

object DefiUtil {

    /**
     * A++ = 10
    A+ = 9
    A = 8
    B++ = 7
    B+ = 6
    B = 5
    C = 4
    D = 3
     */
    fun parseDefiRating(rate : Int) : String{
        when(rate) {
            3 -> {
                return "D"
            }
            4 -> {
                return "C"
            }
            5 -> {
                return "B"
            }
            6 -> {
                return "B+"
            }
            7 -> {
                return "B++"
            }
            8 -> {
                return "A"
            }
            9 -> {
                return "A+"
            }
            10 -> {
                return "A++"
            }
            else -> {
                return ""
            }
        }
    }

    fun parseViewepagerScore(postion : Int) : Int{
        when(postion) {
            0 -> {
                return 10
            }
            1 -> {
                return 9
            }
            2 -> {
                return 8
            }
            3 -> {
                return 7
            }
            4 -> {
                return 6
            }
            5 -> {
                return 5
            }
            6 -> {
                return 4
            }
            7 -> {
                return 3
            }
        }
        return 0
    }
    fun parseLocalDefiRating(rate : Int) : String{
        when(rate) {
            7 -> {
                return "D"
            }
            6 -> {
                return "C"
            }
            5 -> {
                return "B"
            }
            4 -> {
                return "B+"
            }
            3 -> {
                return "B++"
            }
            2 -> {
                return "A"
            }
            1 -> {
                return "A+"
            }
            0 -> {
                return "A++"
            }
            else -> {
                return ""
            }
        }
    }

    fun parseUsd(strUsd : String) : String{
        var bigDecimal = BigDecimal(strUsd)
        if (bigDecimal >= BigDecimal("1000000") || bigDecimal <= BigDecimal("-1000000")) {
            return "$" + (bigDecimal.divide(BigDecimal("1000000"), 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "M"
        } else if (bigDecimal >= BigDecimal("1000") || bigDecimal <= BigDecimal("-1000")) {
            return "$" + (bigDecimal.divide(BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "K"
        } else {
            return "$" + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        }
    }

    fun parseValue(strUsd : String, isUsd : Boolean = false) : String{
        var bigDecimal = BigDecimal(strUsd)
        var preStr = ""
        if (isUsd) {
            preStr = "$"
        }
        if (bigDecimal >= BigDecimal("1000000") || bigDecimal <= BigDecimal("-1000000")) {
            return preStr + (bigDecimal.divide(BigDecimal("1000000"), 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "M"
        } else if (bigDecimal >= BigDecimal("1000") || bigDecimal <= BigDecimal("-1000")) {
            return preStr + (bigDecimal.divide(BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "K"
        } else {
            KLog.i("进入其他。。。")
            return preStr + bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()
        }
    }
}