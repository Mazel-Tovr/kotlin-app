package com.epam.kotlinapp.chat.server

import io.ktor.client.features.*
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

data class User(val name: String)

class Server {

    //private val listOfUsers: MutableList<UserSession> = emptyList<UserSession>() as MutableList<UserSession>;

    private val listOfUsers: MutableMap<User, WebSocketSession> = ConcurrentHashMap();

    private val countOfUsers = AtomicInteger();

    private val messages = ArrayList<String>(10);

    init {
        countOfUsers.set(0);
    }

    suspend fun joinToServer(user: User, webSocketSession: WebSocketSession) {
        //if user is absent
        val newSession = listOfUsers.computeIfAbsent(user) { webSocketSession }

        //send all message to new user
        for (message in messages) {
            newSession.send(Frame.Text(message))
        }
        sendToAll("server", "New user in the chat: ${user.name} ")
        sendToAll("server", "Users online : ${countOfUsers.incrementAndGet()}")
    }

    suspend fun userLeftServer(user: User, webSocketSession: WebSocketSession) {
        listOfUsers[user]!!.close()
        listOfUsers.remove(user)
        sendToAll("server", "User ${user.name} left chat");
        sendToAll("server", " Users online : ${countOfUsers.decrementAndGet()}")
    }

    suspend fun sendMessage(user: User, message: String) {
        sendToAll(user.name, message)
        synchronized(messages)
        {
            messages += if (messages.size < 9) {
                "<${user.name}> $message"
            } else {
                messages.removeFirst()
                "<${user.name}> $message"
            }
        }


    }

    private suspend fun sendToAll(sender: String, message: String) {
        listOfUsers.values.forEach { ws -> ws.send(Frame.Text("<$sender> $message")) }
    }

    fun isNickNameFree(name: String): Boolean = listOfUsers[User(name)] == null
}
