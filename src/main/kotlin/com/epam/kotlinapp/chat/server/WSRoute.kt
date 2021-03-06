package com.epam.kotlinapp.chat.server


import com.epam.kotlinapp.crud.listener.Event
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Route.webSocket(server: Server) {
    webSocket("/ws") {
        do {
            var input = incoming.receive()
            val userName = if (input is Frame.Text) input.readText() else ""
            input = incoming.receive()
            val eventList = if (input is Frame.Text) input.readText() else ""
            if (userName != "" && server.isNickNameFree(userName)) {
                val user = Session(userName)
                server.joinToServer(user, this, eventList.parseToEventsList())
                try {
                    while (true) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> {
                                frame.readText().let { if(it.isNotEmpty())server.sendMessage(user, it) }
                            }
                        }
                    }
                } finally {
                    server.userLeftServer(user, this)
                }
            } else {
                this.send(Frame.Text("This nick name is already taken try again"))
            }
        } while (true)
    }
}

private fun String.parseToEventsList(): List<Event> {
    if (this.isEmpty()) return Event.values().toList()
    val array = this.split(",")
    val list = mutableListOf<Event>()
    for (event in array) {
        try {
            list += Event.valueOf(event.toUpperCase())
        } catch (ex: IllegalArgumentException) {
            println("unable to parse this input")
        }
    }
    return list
}


