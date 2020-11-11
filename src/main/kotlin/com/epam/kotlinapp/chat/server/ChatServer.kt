package com.epam.kotlinapp.chat.server

import com.epam.kotlinapp.crud.listener.Event
import com.epam.kotlinapp.crud.listener.IObserver
import com.epam.kotlinapp.crud.listener.SubscriptionStorage
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function


data class Session(val id: String)

class Server : IObserver {

    private val serverName: String = "server"

    //private val listOfUsers: MutableList<UserSession> = emptyList<UserSession>() as MutableList<UserSession>;

    // private val listOfListeners:MutableMap<Event,IObserver> = mutableMapOf();

    private val sessionSubscriptionStorage: SubscriptionStorage = SubscriptionStorage()

    private val mapOfUsers: PersistentMap<Session, WebSocketSession> = persistentHashMapOf()

    private val countOfUsers = AtomicInteger()

    private val messages = persistentListOf<String>()

    init {
        countOfUsers.set(0)
    }

    suspend fun joinToServer(
        session: Session,
        webSocketSession: WebSocketSession,
        eventList: List<Event> = Event.values().toList()
    ) {
        //if user is absent
        //val newSession = mapOfUsers.computeIfAbsent(session) { webSocketSession }
        mapOfUsers.put(session,webSocketSession)
        val newSession = mapOfUsers[session]!!//TODO REWRITE

        //subscribe user to events all by default
        eventList.forEach { event -> sessionSubscriptionStorage.addUserToEvent(session, event) }


        //send all message to new user
        for (message in messages) {
            newSession.send(Frame.Text(message))
        }

        sendToAll(serverName, "New user in the chat: ${session.id}")
        sendToAll(serverName, "Users online : ${countOfUsers.incrementAndGet()}")

        sentToCurrentUser("You are subscribe to this REST events $eventList", newSession)
    }

    suspend fun userLeftServer(session: Session, webSocketSession: WebSocketSession) {
        mapOfUsers.remove(session)
        webSocketSession.close()
        sessionSubscriptionStorage.removeUserFromEveryEvent(session)
        sendToAll(serverName, "User ${session.id} left chat")
        sendToAll(serverName, "Users online : ${countOfUsers.decrementAndGet()}")
    }

    suspend fun sendMessage(session: Session, message: String) {
        sendToAll(session.id, message)

        messages.add( if (messages.size < 9) {
            "<${session.id}> $message"
        } else {
            messages.removeAt(0)
            "<${session.id}> $message"
        }
        )

    }

    private suspend fun sendToAll(sender: String, message: String) {
        mapOfUsers.values.forEach { ws -> ws.send(Frame.Text("<$sender> $message")) }
    }

    private suspend fun sentToCurrentUser(message: String, webSocketSession: WebSocketSession) {
        webSocketSession.send(Frame.Text("<$serverName> REST: $message"))
    }

    fun isNickNameFree(name: String): Boolean = mapOfUsers[Session(name)] == null


    override suspend fun onEvent(event: Event, message: String) {
        val allUserByEvent = sessionSubscriptionStorage.getAllUserByEvent(event)

        allUserByEvent.forEach { user ->
            val ws = mapOfUsers[user] ?: throw WebSocketException("Session was closed")
            sentToCurrentUser(message, ws)
        }
//        allUserByEvent.filter { mapOfUsers[it] != null }.forEach {
//            sentToCurrentUser(message, mapOfUsers[it]!!)
//        }

    }

//    private fun <K, V> PersistentMap<K, V>.computeIfAbsent(key: K, mappingFunction: (K)->V): V {
//
//        var v: V? = this[key]
//        if (v == null) {
//            var newValue: V
//            mappingFunction.apply { this@computeIfAbsent.put(key,)}
//
//        }
//        return v
//    }



}
