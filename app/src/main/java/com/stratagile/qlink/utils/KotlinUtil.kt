package com.stratagile.qlink.utils

data class Person(var age : Int, var name : String)
fun main(args: Array<String>) {
//    sum1(3, 5) { a: Int, b: Int ->
//        a * b
//    }
//    var str = listOf(Person(12, "haha"), Person(45, "hehe"), Person(30, "xixi"))
//    var maxAge = str.maxBy { it.age }!!
//    println(str.filter { it.age == maxAge.age}.map { it.name })
//    str.maxBy { it.age }
//    println(str.maxBy{ it.age })
//    println(sum(1, 3))
//    println(with(StringBuilder()) {
//        for (i in 'A'..'z') {
//            append(i)
//        }
//        toString()
//    })
//    println(StringBuilder().apply {
//        for (i in 'A'..'z') {
//            append(i)
//        }
//    }.toString())

    printStr(person)
}

var sum = { a: Int, b: Int ->
    println("$a + $b = ${a + b}")
    a + b
}

var person : Person = Person(20, "haha")

fun printStr(str : Person?) {
    var up = str?.name?: "xxx"
    println(up)
    println(str?.name?.length)
    str?.let {
        println("person不为空。")
    }
}



fun sum1(a : Int, b : Int, xxx: (a: Int, b: Int) -> Int) {
    println(xxx.invoke(a, b))
}