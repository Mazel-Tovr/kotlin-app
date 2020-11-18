package com.epam.kotlinapp.crud.dao.nosql

import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.model.*
import jetbrains.exodus.entitystore.Entity
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.*
import kotlinx.dnq.*
import kotlinx.dnq.query.*

object ProductGroupOperationImp : ICommonOperations<ProductGroup> {

    private val conn = CommonStore.entityStore

    override fun create(entity: ProductGroup) = conn.transactional {
        entity.toXdProductGroup().toProductGroup()
    }

    override fun getEntity(id: Long): ProductGroup? = conn.transactional(readonly = true) {
        XdProductGroup.all().asSequence().firstOrNull { it.entityId.localId == id }?.toProductGroup()
    }

    override fun getAll(): ImmutableList<ProductGroup> = conn.transactional(readonly = true) {
        XdProductGroup.all().toList().map { it.toProductGroup() }.let { ImmutableListAdapter(it) }
    }

    override fun update(entity: ProductGroup): Unit = conn.transactional {
        XdProductGroup.all().asSequence().first { it.entityId.localId == entity.id }
            .update {
                groupName = entity.groupName
            }
    }

    override fun delete(id: Long): Unit = conn.transactional {
        XdProductGroup.all().asSequence().firstOrNull { it.entityId.localId == id }?.delete()
    }
}

private fun ProductGroup.toXdProductGroup() = XdProductGroup.new {
    groupName = this@toXdProductGroup.groupName
}

private fun Entity.toProductGroup() = ProductGroup(
    id.localId,
    getProperty("groupName") as String
)


private fun XdProductGroup.toProductGroup() = ProductGroup(entityId.localId, groupName)

private fun XdProductGroup.update(builder: XdProductGroup.() -> Unit): XdProductGroup = apply { builder() }


class XdProductGroup(entity: Entity) : XdEntity(entity) {
    companion object : XdNaturalEntityType<XdProductGroup>()

    var groupName by xdRequiredStringProp()
}