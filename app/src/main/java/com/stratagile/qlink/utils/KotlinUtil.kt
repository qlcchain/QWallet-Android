package com.stratagile.qlink.utils

fun main(args: Array<String>) {
    sum1(3, 5) { a: Int, b: Int ->
        a * b
    }
//    println(sum(1, 3))
}

var sum = { a: Int, b: Int ->
    println("$a + $b = ${a + b}")
    a + b
}

fun sum1(a : Int, b : Int, xxx: (a: Int, b: Int) -> Int) {
    println(xxx.invoke(a, b))
}