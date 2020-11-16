package com.epam.kotlinapp.chat.server

import com.epam.kotlinapp.crud.listener.Event
import com.epam.kotlinapp.crud.listener.IObserver
import com.epam.kotlinapp.crud.listener.SubscriptionStorage
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import java.util.concurrent.atomic.AtomicInteger


data class Session(val id: String)

class Server : IObserver {

    private val serverName: String = "server"

    private val sessionSubscriptionStorage: SubscriptionStorage = SubscriptionStorage()

    private var mapOfSessions: PersistentMap<Session, WebSocketSession> = persistentHashMapOf()

    private val countOfUsers = AtomicInteger()

    private var messageHistory = persistentListOf<String>()

    init {
        countOfUsers.set(0)
    }

    suspend fun joinToServer(
        session: Session,
        webSocketSession: WebSocketSession,
        eventList: List<Event> = Event.values().toList()
    ) {

        val newSession = mapOfSessions.computeIfAbsent(session) { webSocketSession }

        //subscribe user to events all by default
        eventList.forEach { event -> sessionSubscriptionStorage.addSessionToEvent(session, event) }


        //send all message to new user
        for (message in messageHistory) {
            newSession.send(Frame.Text(message))
        }

        sendToAll(serverName, "New user in the chat: ${session.id}")
        sendToAll(serverName, "Users online : ${countOfUsers.incrementAndGet()}")

        sentToCurrentUser("You are subscribe to this REST events $eventList", newSession)
    }

    suspend fun userLeftServer(session: Session, webSocketSession: WebSocketSession) {
        mapOfSessions = mapOfSessions.remove(session)
        webSocketSession.close()
        sessionSubscriptionStorage.removeSessionFromEveryEvent(session)
        sendToAll(serverName, "User ${session.id} left chat")
        sendToAll(serverName, "Users online : ${countOfUsers.decrementAndGet()}")
    }

    suspend fun sendMessage(session: Session, message: String) {
        sendToAll(session.id, message)

        if (messageHistory.size > 9) messageHistory = messageHistory.removeAt(0)
        messageHistory = messageHistory.add("<${session.id}> $message")
    }

    private suspend fun sendToAll(sender: String, message: String) {
        mapOfSessions.values.forEach { ws -> ws.send(Frame.Text("<$sender> $message")) }
    }

    private suspend fun sentToCurrentUser(message: String, webSocketSession: WebSocketSession) {
        webSocketSession.send(Frame.Text("<$serverName> REST: $message"))
    }

    fun isNickNameFree(name: String): Boolean = mapOfSessions[Session(name)] == null


    override suspend fun onEvent(event: Event, message: String) {
        val allUserByEvent = sessionSubscriptionStorage.getAllSessionByEvent(event)

        allUserByEvent.forEach { user ->
            val ws = mapOfSessions[user] ?: throw WebSocketException("Session was closed")
            sentToCurrentUser(message, ws)
        }
    }

    private fun PersistentMap<Session, WebSocketSession>.computeIfAbsent(
        key: Session,
        mappingFunction: () -> WebSocketSession
    ): WebSocketSession {

        val v: WebSocketSession? = this[key]
        if (v == null) {
            val newValue: WebSocketSession = mappingFunction()
            mapOfSessions = put(key, newValue)
            return newValue
        }
        return v
    }
}
