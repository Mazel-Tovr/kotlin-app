package com.epam.kotlinapp.chat.server


import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Route.webSocket(server: Server) {
    webSocket("/ws") {
        var flag: Boolean = true// i could do better but ... no time on this
        do {
            val input = incoming.receive()
            val userName = if (input is Frame.Text) input.readText() else ""
            if (userName != "" && server.isNickNameFree(userName)) {
                flag = false;
                val user = User(userName)
                server.joinToServer(user, this)
                try {
                    while (true) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> {
                                server.sendMessage(user, frame.readText())
                            }
                        }
                    }

                } finally {
                    server.userLeftServer(user, this)
                }
            } else {
                this.send(Frame.Text("This nick name is already taken try again"))
            }
        } while (flag)

    }
}

