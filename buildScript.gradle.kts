project.extra["Test"] = "113312"

project.tasks.create("Task from build script") {
    doLast {
        println(getTestString())
    }
}

fun getTestString(): String {
    println("Hello from build script")
    return "Test string"
}
