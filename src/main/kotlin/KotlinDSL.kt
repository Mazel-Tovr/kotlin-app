data class Person1(var name: String? = null, var age: Int? = null, var address: String? = null)
data class Person(var name: String? = null, var age: Int? = null, var address: Address? = null)
data class Address(var country: String? = null, var town: String? = null, var street: String? = null)


fun main() {
    val person1: Person1 = buildPerson {
        it.name = "Sanya"
        it.age = 31
        it.address = "Kozlovo"
    }
    println(person1)
    val person2 = buildPerson1 {
        name = "Egor"
        age = 23
        address = "Tver"
    }
    println(person2)

    val person3 = buildPerson2 {
        name = "Roma"
        age = 20
        address {
            country = "Russia"
            town = "Tver"
            street = "Lenina"
        }

    }
    println(person3)

    val map: MutableMap<Int, String> = mapBuilder {
        put(1, "odin")
        computeIfAbsent(2) { "dva" }
    }
    println(map)

}


fun buildPerson(build: (Person1) -> Unit): Person1 {
    val person = Person1()
    build(person)
    return person
}

fun buildPerson1(build: Person1.() -> Unit): Person1 {
    val person = Person1()
    person.build()
    return person
}

fun buildPerson2(build: Person.() -> Unit) = Person().apply(build)

fun Person.address(build: Address.() -> Unit) {
    address = Address().apply(build)
}

//Form kotlin koans build map

fun <K, V> mapBuilder(build: MutableMap<K, V>.() -> Unit) = HashMap<K, V>().apply(build)




