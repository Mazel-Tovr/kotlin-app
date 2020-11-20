package com.epam.other

import kotlinx.coroutines.*



suspend fun printTwo() {
    delay(1500)
    println("two")
}

fun printAll() = runBlocking {
    println("one")
    val async = async {
        printTwo()

    }

    val value = async { hardCalculation(123) }
    println(value.await())
    async.await()
    println("three")

}


suspend fun hardCalculation(num: Int): String {
    delay(1000)
    return (num * 100).toString()
}

