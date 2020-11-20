package com.epam.other

import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

//fun getList(): List<Int> {
//    return arrayListOf(1, 5, 2).sortedDescending()
//}
//
//data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
//    override fun compareTo(other: MyDate): Int = when {
//
//        (year != other.year) -> year - other.year
//        (month != other.month) -> month - other.month
//        else -> dayOfMonth - other.dayOfMonth
//    }
//
//}
//fun compare(date11: MyDate, date12: MyDate) = date11 < date12
//
//class DateRange2(val start: MyDate, val endInclusive: MyDate)
//{
//    operator fun contains(date: MyDate) :Boolean{
//        return date>= start && date<=endInclusive
//    }
//}
//
//fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
//    return date in DateRange2(first, last)
//}
//
//
//operator fun MyDate.rangeTo(other: MyDate) = DateRange1(this,other)
//
//class DateRange1(override val start: MyDate, override val endInclusive: MyDate): ClosedRange<MyDate>
//
//
//fun checkInRange1(date: MyDate, first: MyDate, last: MyDate): Boolean {
//    return date in first..last
//}
//
//
//
//class DateRange(val start: MyDate, val end: MyDate) : Iterable<MyDate> {
//    override fun iterator(): Iterator<MyDate> {
//        return object : Iterator<MyDate> {
//            private val currentDate = start
//
//            override fun hasNext(): Boolean {
//               return currentDate <= end
//            }
//
//            override fun next(): MyDate {
//                return currentDate
//            }
//
//        }
//    }
//}

//fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
//    for (date in firstDate..secondDate) {
//        handler(date)
//    }
//}


//class Invokable {
//    var numberOfInvocations: Int = 0
//        private set
//    operator fun invoke(): Invokable {
//        numberOfInvocations++
//        return this
//    }
//}
//
//fun invokeTwice(invokable: Invokable) = invokable()()

fun buildMutableMap(build: HashMap<Int, String>.() -> Unit): Map<Int, String> {
    val map = HashMap<Int, String>()
    map.build()
    return map
}

fun usage(): Map<Int, String> {
    return buildMutableMap {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}

fun <T> T.myApply(f: T.() -> Unit): T { f(); return this }

fun createString(): String {
    return StringBuilder().myApply {
        append("Numbers: ")
        for (i in 1..10) {
            append(i)
        }
    }.toString()
}

fun createMap(): Map<Int, String> {
    return hashMapOf<Int, String>().myApply {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}



fun <T, C : MutableCollection<T>> Collection<T>.partitionTo(first: C, second: C, predicate: (T) -> Boolean): Pair<C, C> {
    for (element in this) {
        if (predicate(element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}

fun partitionWordsAndLines() {
    val (words, lines) = listOf("a", "a b", "c", "d e").partitionTo(
        ArrayList(),
        ArrayList()
    ) { s -> !s.contains(" ") }
    words == listOf("a", "c")
    lines == listOf("a b", "d e")
}

fun partitionLettersAndOtherSymbols() {
    val (letters, other) = setOf('a', '%', 'r', '}').partitionTo(
        HashSet(),
        HashSet()
    ) { c -> c in 'a'..'z' || c in 'A'..'Z' }
    letters == setOf('a', 'r')
    other == setOf('%', '}')
}

fun main() {
    //com.epam.other.usage().forEach{ println(it.key)}
}
