package com.epam.kotlinapp.chat.client

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import kotlinx.atomicfu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@KtorExperimentalAPI
@ExperimentalCoroutinesApi
suspend fun main() {
    ChatClient().main()
}

class ChatClient {
    private val exitWord = "exit"
    private val host = "127.0.0.1"
    private val port = 8080
    private val wsPath = "/ws"

    @KtorExperimentalAPI
    val client = HttpClient {
        install(WebSockets)
    }


    @KtorExperimentalAPI
    @ExperimentalCoroutinesApi
    private suspend fun createSession() {
        client.ws(
            method = HttpMethod.Get,
            host = host,
            port = port, path = wsPath
        )
        {

            println("Enter nick name and event you want to subscribe (By default all) ")
            println("For example nickname: Sanya \nEvents: (read,create,delete,update) or nothing to all ")
            print("Enter nickname: ")
            this.send(Frame.Text(readLine() ?: ""))
            print("Enter events: ")
            this.send(Frame.Text(readLine() ?: ""))
            launch {
                while (true) {
                    val line = withContext(Dispatchers.IO) { readLine() } ?: ""
                    if (line != exitWord) send(line)
                }
            }

            while (true) {
                when (val frame = incoming.receive()) {
                    is Frame.Text -> println(frame.readText())
                }
            }


        }
    }


    @KtorExperimentalAPI
    @ExperimentalCoroutinesApi
    suspend fun main() {
        createSession()
    }


}
