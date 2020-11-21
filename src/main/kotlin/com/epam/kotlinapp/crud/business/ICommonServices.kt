package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.model.*
import kotlinx.collections.immutable.*

interface ICommonServices<T : Entity> {
    fun create(entity: T):T
    fun getEntity(id: Long):T
    fun getAll():ImmutableList<T>
    fun update(entity: T)
    fun delete(id: Long)
}
