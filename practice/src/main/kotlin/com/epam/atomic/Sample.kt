package com.epam.atomic

import kotlinx.atomicfu.*

//Короче сампл не получился из за ошибки с зависимостями
class Sample {

    private val atomicLong = atomic(0L)
    private var justLong = 0L;

    private val atomicList = atomic(ArrayList<String>())


    fun experimentTime() {
        for (i in 1..100000) {
            atomicLong.addAndGet(1)
            justLong++
        }
        print(assert(atomicLong.value == justLong))
    }

}

fun main(args: Array<String>) {
    Sample().experimentTime()
}
