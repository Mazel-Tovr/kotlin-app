package com.epam.kotlinapp.crud.dao.nosql

import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.model.*
import jetbrains.exodus.entitystore.Entity
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.*
import kotlinx.dnq.*
import kotlinx.dnq.query.*
import kotlinx.dnq.util.*

object UserOperationImpl : ICommonOperations<User> {

    private val conn = CommonStore.entityStore

    override fun create(entity: User) = conn.transactional {
        entity.toXdUser().toUser()
    }

    override fun getEntity(id: Long): User? = conn.transactional(readonly = true) {
        XdUser.all().asSequence().firstOrNull { it.entityId.localId == id }?.toUser()
    }

    override fun getAll(): ImmutableList<User> = conn.transactional(readonly = true) {
        XdUser.all().toList().map { it.toUser() }.let { ImmutableListAdapter(it) }
    }

    override fun update(entity: User): Unit = conn.transactional {
        XdUser.all().asSequence().first { it.entityId.localId == entity.id }
            .update {
                userName = entity.name
                email = entity.email
                password = entity.password
            }
    }

    override fun delete(id: Long): Unit = conn.transactional {
        XdUser.all().asSequence().firstOrNull { it.entityId.localId == id }?.delete()
    }
}


private fun User.toXdUser() = XdUser.new {
    userName = this@toXdUser.name
    email = this@toXdUser.email
    password = this@toXdUser.password
}


private fun Entity.toUser() = User(
    id.localId,
    getProperty("userName") as String,
    getProperty("email") as String,
    getProperty("password") as String
)

private fun XdUser.toUser() = User(entityId.localId, userName, email, password)

private fun XdUser.update(builder: XdUser.() -> Unit): XdUser = apply { builder() }


class XdUser(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<XdUser>()

    var userName by xdRequiredStringProp()
    var email by xdRequiredStringProp()
    var password by xdRequiredStringProp()
}
