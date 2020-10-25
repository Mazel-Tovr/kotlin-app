package com.epam.kotlinapp.business

import com.epam.kotlinapp.model.Entity

interface ICommonServices<T : Entity> {
    fun create(entity: T)
    fun getEntity(id: Long):T
    fun getAll():List<T>
    fun update(entity: T)
    fun delete(entity: T)
    fun delete(id: Long)
}