package com.epam.kotlinapp.crud.model


import kotlinx.serialization.Serializable

@Serializable
data class ProductGroup(
    var id: Long?,
    var groupName: String
) : Entity()