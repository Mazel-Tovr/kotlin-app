package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.model.ProductGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.Statement

object ProductGroupOperations : ICommonOperations<ProductGroup> {
    //Connection by default
    private var conn: Connection = ConnectionDB.conn


    override fun create(entity: ProductGroup): ProductGroup {
        val prepareStatement = conn
            .prepareStatement("INSERT INTO PRODUCT_GROUP VALUES (NULL,?)", Statement.RETURN_GENERATED_KEYS)
        prepareStatement.setString(1, entity.groupName)
        prepareStatement.executeUpdate()
        val resultSet = prepareStatement.generatedKeys
        resultSet.next()
        entity.id = resultSet.getLong(1)
        return entity
    }

    override fun getEntity(id: Long): ProductGroup? {
        val prepareStatement = conn
            .prepareStatement("SELECT * FROM PRODUCT_GROUP WHERE id=?")
        prepareStatement.setLong(1, id)

        val resultSet = prepareStatement.executeQuery()

        return if (resultSet.next()) {
            ProductGroup(
                resultSet.getLong("id"), resultSet.getString("group_name"),
            )
        } else {
            null
        }
    }

    override fun update(entity: ProductGroup) {
        val prepareStatement = conn
            .prepareStatement("UPDATE PRODUCT_GROUP SET group_name=? WHERE id=?")
        prepareStatement.setString(1, entity.groupName)
        entity.id?.let { prepareStatement.setLong(2, it) }
            ?: throw DataException("Product group id isn't present ")
        prepareStatement.executeUpdate()
    }


    override fun delete(id: Long) {
        val prepareStatement = conn
            .prepareStatement("DELETE FROM PRODUCT_GROUP WHERE id = ?")
        prepareStatement.setLong(1, id)
        prepareStatement.executeUpdate()
    }

    override fun getAll(): ImmutableList<ProductGroup> {
        val groupList: MutableList<ProductGroup> = ArrayList()
        val prepareStatement = conn
            .prepareStatement("SELECT * FROM PRODUCT_GROUP")

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            groupList += ProductGroup(
                resultSet.getLong("id"), resultSet.getString("group_name"),
            )
        }
        return ImmutableListAdapter(groupList)
    }

}