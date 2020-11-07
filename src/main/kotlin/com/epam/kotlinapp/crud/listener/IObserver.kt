package com.epam.kotlinapp.crud.listener

import kotlinx.coroutines.CoroutineScope

interface IObserver {
    suspend fun onEvent(event: Event, message: String)
}