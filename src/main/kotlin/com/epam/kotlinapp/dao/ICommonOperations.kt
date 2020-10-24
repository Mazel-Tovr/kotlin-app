package com.epam.kotlinapp.dao

interface ICommonOperations<T> {
    fun create(entity: T)
    fun getEntity(id: Long):T?
    fun getAll():List<T>
    fun update(entity: T)
    fun delete(entity: T)
    fun delete(id: Long)
}