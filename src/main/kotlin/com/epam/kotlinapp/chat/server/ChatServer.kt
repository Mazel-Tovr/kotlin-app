package com.epam.kotlinapp.chat.server

import com.epam.kotlinapp.crud.listener.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.atomicfu.*
import java.util.concurrent.atomic.*


data class Session(val id: String)

class Server : IObserver {

    private val serverName: String = "server"

    private val sessionSubscriptionStorage: SubscriptionStorage = SubscriptionStorage()

    private val sessions = atomic(mutableMapOf<Session, WebSocketSession>())

    private val countOfUsers = AtomicInteger()

    private val messageHistory = atomic(mutableListOf<String>())

    init {
        countOfUsers.set(0)
    }

    suspend fun joinToServer(
        session: Session,
        webSocketSession: WebSocketSession,
        eventList: List<Event> = Event.values().toList()
    ) {
        val newSession = sessions.value.computeIfAbsent(session) { webSocketSession }

        eventList.forEach { event -> sessionSubscriptionStorage.addSessionToEvent(session, event) }

        for (message in messageHistory.value) {
            newSession.send(Frame.Text(message))
        }

        sendToAll(serverName, "New user in the chat: ${session.id}")
        sendToAll(serverName, "Users online : ${countOfUsers.incrementAndGet()}")

        sendToCurrentUser("You are subscribe to this REST events $eventList", newSession)
    }

    suspend fun userLeftServer(session: Session, webSocketSession: WebSocketSession) {
        sessionSubscriptionStorage.removeSessionFromEveryEvent(session)
        sessions.value.remove(session)
        webSocketSession.close()
        sendToAll(serverName, "User ${session.id} left chat")
        sendToAll(serverName, "Users online : ${countOfUsers.decrementAndGet()}")
    }

    suspend fun sendMessage(session: Session, message: String) {
        sendToAll(session.id, message)

        addToMessageHistory(session, message)
    }

    private fun addToMessageHistory(session: Session, message: String) {
        messageHistory.value.let { list ->
            if (list.size > 9) {
                list.removeFirst()
                list.add("<${session.id}> $message")
            } else {
                list.add("<${session.id}> $message")
            }
        }
    }

    private suspend fun sendToAll(sender: String, message: String) {
        sessions.value.values.forEach { ws -> ws.send(Frame.Text("<$sender> $message")) }
    }

    private suspend fun sendToCurrentUser(message: String, webSocketSession: WebSocketSession) {
        webSocketSession.send(Frame.Text("<$serverName> REST: $message"))
    }

    fun isNickNameFree(name: String): Boolean = sessions.value[Session(name)] == null


    override suspend fun onEvent(event: Event, message: String) {
        val usersByEvent = sessionSubscriptionStorage.getAllSessionByEvent(event)

        usersByEvent.forEach { user ->
            sessions.value[user]?.let { sendToCurrentUser(message, it) }
                ?: throw WebSocketException("Session was closed")
        }
    }
}
