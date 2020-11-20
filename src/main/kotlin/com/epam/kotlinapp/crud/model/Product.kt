package com.epam.kotlinapp.crud.model


import kotlinx.serialization.Serializable


@Serializable
data class Product (
    var id: Long = -1,
    var productName: String,
    var price: Int,
    var description: String,
    var groupId: Long,
    var userId: Long
) : Entity()
