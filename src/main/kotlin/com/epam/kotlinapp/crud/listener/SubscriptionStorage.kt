package com.epam.kotlinapp.crud.listener

import com.epam.kotlinapp.chat.server.Session
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

class SubscriptionStorage {
    private val listOfEvents: ImmutableMap<Event, MutableList<Session>> = persistentMapOf(
        Event.CREATE to mutableListOf(),
        Event.READ to mutableListOf(),
        Event.UPDATE to mutableListOf(),
        Event.DELETE to mutableListOf()
    )


    fun addSessionToEvent(session: Session, event: Event) {
        listOfEvents[event]?.add(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeSessionFromEvent(session: Session, event: Event) {
        listOfEvents[event]?.remove(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeSessionFromEveryEvent(session: Session) {
        listOfEvents.values.forEach { list -> list.remove(session) }
    }

    fun getAllSessionByEvent(event: Event): ImmutableList<Session> {
        if (listOfEvents.containsKey(event))
            return listOfEvents[event]?.let { ImmutableListAdapter(it) } ?: persistentListOf()
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")
    }
}


