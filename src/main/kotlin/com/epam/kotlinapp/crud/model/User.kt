package com.epam.kotlinapp.crud.model


import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Long = -1,
    var name: String,
    var email: String,
    var password: String
) : Entity()
