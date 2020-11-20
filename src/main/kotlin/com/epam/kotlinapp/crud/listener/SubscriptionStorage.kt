package com.epam.kotlinapp.crud.listener

import com.epam.kotlinapp.chat.server.Session
import kotlinx.atomicfu.*
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.ImmutableListAdapter

class SubscriptionStorage {

    private val listOfEvents = atomic(
        persistentHashMapOf<Event, AtomicRef<MutableList<Session>>>(
            Event.CREATE to atomic(mutableListOf()),
            Event.READ to atomic(mutableListOf()),
            Event.UPDATE to atomic(mutableListOf()),
            Event.DELETE to atomic(mutableListOf())
        )
    )

    fun addSessionToEvent(session: Session, event: Event) {
        listOfEvents.value[event]?.value?.add(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeSessionFromEvent(session: Session, event: Event) {
        listOfEvents.value[event]?.value?.remove(session)
            ?: throw IllegalArgumentException("This event \"$event\" is not supported")
    }

    fun removeSessionFromEveryEvent(session: Session) {
        listOfEvents.value.values.forEach { list -> list.value.remove(session) }
    }

    fun getAllSessionByEvent(event: Event): ImmutableList<Session> {
        if (listOfEvents.value.containsKey(event))
            return listOfEvents.value[event]?.let { ImmutableListAdapter(it.value) } ?: persistentListOf()
        else
            throw IllegalArgumentException("This event \"$event\" is not supported")
    }
}


