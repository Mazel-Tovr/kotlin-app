package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.model.Entity

interface ICommonOperations<T : Entity> {
    fun create(entity: T):T
    fun getEntity(id: Long):T?
    fun getAll():List<T>
    fun update(entity: T)
    fun delete(id: Long)
}