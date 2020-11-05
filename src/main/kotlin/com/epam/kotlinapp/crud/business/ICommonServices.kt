package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.model.Entity

interface ICommonServices<T : Entity> {
    fun create(entity: T):T?
    fun getEntity(id: Long):T?
    fun getAll():List<T>
    fun update(entity: T)
    fun delete(id: Long)
}