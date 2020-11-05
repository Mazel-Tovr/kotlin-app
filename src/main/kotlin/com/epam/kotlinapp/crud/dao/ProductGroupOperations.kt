package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.model.ProductGroup
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
        resultSet.next();
        entity.id = resultSet.getLong(1);
        return entity;
    }

    override fun getEntity(id: Long): ProductGroup? {
        val prepareStatement = conn
                .prepareStatement("SELECT * FROM PRODUCT_GROUP WHERE id=?")
        prepareStatement.setLong(1, id)

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            return ProductGroup(
                    resultSet.getLong("id"), resultSet.getString("group_name"),
            )
        }
        return null
    }

    override fun update(entity: ProductGroup) {
        val prepareStatement = conn
                .prepareStatement("UPDATE PRODUCT_GROUP SET group_name=? WHERE id=?")
        prepareStatement.setString(1, entity.groupName)
        entity.id?.let { prepareStatement.setLong(2, it) }
        prepareStatement.executeUpdate();
    }


    override fun delete(id: Long) {
        val prepareStatement = conn
                .prepareStatement("DELETE FROM PRODUCT_GROUP WHERE id = ?")
        prepareStatement.setLong(1, id);
        prepareStatement.executeUpdate();
    }

    override fun getAll(): List<ProductGroup> {
        val groupList: MutableList<ProductGroup> = ArrayList()
        val prepareStatement = conn
                .prepareStatement("SELECT * FROM PRODUCT_GROUP")

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            groupList += ProductGroup(
                    resultSet.getLong("id"), resultSet.getString("group_name"),
            )
        }
        return groupList
    }

}