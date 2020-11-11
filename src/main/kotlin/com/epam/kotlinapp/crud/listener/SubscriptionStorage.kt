package com.epam.kotlinapp.crud.listener

import com.epam.kotlinapp.chat.server.Session
import kotlinx.collections.immutable.*

class SubscriptionStorage {
    private val listOfEvents: ImmutableMap<Event, PersistentList<Session>> = persistentMapOf(
        Event.CREATE to persistentListOf(),
        Event.READ to persistentListOf(),
        Event.UPDATE to persistentListOf(),
        Event.DELETE to persistentListOf()
    );


    fun addUserToEvent(session: Session, event: Event) {
        listOfEvents[event]?.add(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeUserFromEvent(session: Session, event: Event) {
        listOfEvents[event]?.remove(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeUserFromEveryEvent(session: Session) {
        listOfEvents.values.forEach { list -> list.remove(session) }
    }

    fun getAllUserByEvent(event: Event): PersistentList<Session> {
        if (listOfEvents.containsKey(event))
            return listOfEvents[event] ?: persistentListOf()
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")

    }


}