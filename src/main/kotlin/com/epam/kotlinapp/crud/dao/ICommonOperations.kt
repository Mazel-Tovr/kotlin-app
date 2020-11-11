package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.model.Entity
import kotlinx.collections.immutable.ImmutableList

interface ICommonOperations<T : Entity> {
    fun create(entity: T):T
    fun getEntity(id: Long):T?
    fun getAll():ImmutableList<T>
    fun update(entity: T)
    fun delete(id: Long)
}