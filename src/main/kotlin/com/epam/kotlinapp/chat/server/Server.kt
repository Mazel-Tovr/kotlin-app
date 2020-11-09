package com.epam.kotlinapp.chat.server

import com.epam.kotlinapp.crud.listener.Event
import com.epam.kotlinapp.crud.listener.IObserver
import com.epam.kotlinapp.crud.listener.SubscriptionStorage
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

//data class User(val name: String, val eventsList: List<Events>)
data class User(val name: String)

class Server : IObserver {

    private val serverName: String = "server"

    //private val listOfUsers: MutableList<UserSession> = emptyList<UserSession>() as MutableList<UserSession>;

    // private val listOfListeners:MutableMap<Event,IObserver> = mutableMapOf();

    private val usersSubscriptionStorage: SubscriptionStorage = SubscriptionStorage()

    private val mapOfUsers: MutableMap<User, WebSocketSession> = ConcurrentHashMap()

    private val countOfUsers = AtomicInteger()

    private val messages = ConcurrentLinkedQueue<String>()

    init {
        countOfUsers.set(0)
    }

    suspend fun joinToServer(
        user: User,
        webSocketSession: WebSocketSession,
        eventList: List<Event> = Event.values().toList()
    ) {
        //if user is absent
        val newSession = mapOfUsers.computeIfAbsent(user) { webSocketSession }

        //subscribe user to events all by default
        eventList.forEach { event -> usersSubscriptionStorage.addUserToEvent(user, event) }


        //send all message to new user
        for (message in messages) {
            newSession.send(Frame.Text(message))
        }

        sendToAll(serverName, "New user in the chat: ${user.name}")
        sendToAll(serverName, "Users online : ${countOfUsers.incrementAndGet()}")

        sentToCurrentUser("You are subscribe to this REST events $eventList",newSession)
    }

    suspend fun userLeftServer(user: User, webSocketSession: WebSocketSession) {
        mapOfUsers.remove(user)
        webSocketSession.close()
        usersSubscriptionStorage.removeUserFromEveryEvent(user)
        sendToAll(serverName, "User ${user.name} left chat")
        sendToAll(serverName, "Users online : ${countOfUsers.decrementAndGet()}")
    }

    suspend fun sendMessage(user: User, message: String) {
        sendToAll(user.name, message)

        messages += if (messages.size < 9) {
            "<${user.name}> $message"
        } else {
            messages.remove()
            "<${user.name}> $message"
        }


    }

    private suspend fun sendToAll(sender: String, message: String) {
        mapOfUsers.values.forEach { ws -> ws.send(Frame.Text("<$sender> $message")) }
    }

    private suspend fun sentToCurrentUser(message: String, webSocketSession: WebSocketSession) {
        webSocketSession.send(Frame.Text("<$serverName> REST: $message"))
    }

    fun isNickNameFree(name: String): Boolean = mapOfUsers[User(name)] == null


    override suspend fun onEvent(event: Event, message: String) {
        val allUserByEvent = usersSubscriptionStorage.getAllUserByEvent(event)

        allUserByEvent.forEach { user ->
            val ws = mapOfUsers[user] ?: throw WebSocketException("Session was closed")
            sentToCurrentUser(message, ws)
        }
//        allUserByEvent.filter { mapOfUsers[it] != null }.forEach {
//            sentToCurrentUser(message, mapOfUsers[it]!!)
//        }

    }


}
