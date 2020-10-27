package com.epam.kotlinapp.chat.client

import com.epam.kotlinapp.chat.server.User
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import kotlin.concurrent.thread


suspend fun main() {
    ChatClient().main()
}

class ChatClient {

    val exitWord = "exit"
    val host = "127.0.0.1"
    val port = 8080
    val wsPath = "/ws"

    @KtorExperimentalAPI
    val client = HttpClient {
        install(WebSockets)
    }


    @ExperimentalCoroutinesApi
    private suspend fun createSession() {
        client.ws(
            method = HttpMethod.Get,
            host = host,
            port = port, path = wsPath
        )
        {

            print("Enter nick name: ")
            this.send(Frame.Text(readLine() ?: ""))
            thread(start = true) {
                while (true) {
                    when (val frame = incoming.poll()) {
                        is Frame.Text -> println(frame.readText())
                    }
                }
            }
            while (true) {
                val line = readLine() ?: ""
                if (line != exitWord) send(line)
                when (val frame = incoming.poll()) {
                    is Frame.Text -> println(frame.readText())
                }
            }

        }
    }


    @ExperimentalCoroutinesApi
    suspend fun main() {
        createSession()
    }


}