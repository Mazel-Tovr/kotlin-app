package com.epam.kotlinapp.crud.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    var id: Long?,
    var productName: String,
    var price: Int,
    var description: String,
    var groupID: Long,
    var userId: Long
) : Entity()