package com.epam.kotlinapp.crud.listener

import com.epam.kotlinapp.chat.server.User

class SubscriptionStorage {
    private val listOfEvents: Map<Event, MutableList<User>> = mapOf(
        Event.CREATE to mutableListOf(),
        Event.READ to mutableListOf(),
        Event.UPDATE to mutableListOf(),
        Event.DELETE to mutableListOf()
    );


    fun addUserToEvent(user: User, event: Event) {
        if (listOfEvents.containsKey(event))
            listOfEvents[event]?.add(user)
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeUserFromEvent(user: User, event: Event) {
        if (listOfEvents.containsKey(event))
            listOfEvents[event]?.remove(user)
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeUserFromEveryEvent(user: User) {
        listOfEvents.values.forEach { list -> list.remove(user) }
    }

    fun getAllUserByEvent(event: Event): MutableList<User> {
        if (listOfEvents.containsKey(event))
            return listOfEvents[event] ?: mutableListOf()
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")

    }


}