package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.model.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import java.sql.Connection
import java.sql.Statement

object ProductOperations : ICommonOperations<Product> {

    private var conn: Connection = ConnectionDB.conn

    override fun create(entity: Product): Product {
        val prepareStatement = conn
            .prepareStatement("INSERT INTO PRODUCT VALUES (NULL,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)
        prepareStatement.setString(1, entity.productName)
        prepareStatement.setInt(2, entity.price)
        prepareStatement.setString(3, entity.description)
        prepareStatement.setLong(4, entity.groupID)
        prepareStatement.setLong(5, entity.userId)
        prepareStatement.executeUpdate()
        val resultSet = prepareStatement.generatedKeys
        resultSet.next()
        entity.id = resultSet.getLong(1)
        return entity
    }

    override fun getEntity(id: Long): Product? {
        val prepareStatement = conn
            .prepareStatement("SELECT * FROM PRODUCT WHERE id=?")
        prepareStatement.setLong(1, id)

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            return Product(
                resultSet.getLong("id"), resultSet.getString("product_name"),
                resultSet.getInt("price"), resultSet.getString("description"),
                resultSet.getLong("group_id"), resultSet.getLong("user_id")
            )
        }
        return null
    }

    override fun update(entity: Product) {
        val prepareStatement = conn
            .prepareStatement(
                "UPDATE PRODUCT SET product_name=?,price=?,description=?,group_id=?,user_id=? WHERE id=?"
            )
        prepareStatement.setString(1, entity.productName)
        prepareStatement.setInt(2, entity.price)
        prepareStatement.setString(3, entity.description)
        prepareStatement.setLong(4, entity.groupID)
        prepareStatement.setLong(5, entity.userId)
        entity.id?.let { prepareStatement.setLong(6, it) }
            ?: throw DataException("Product id isn't present ")
        prepareStatement.executeUpdate()
    }


    override fun delete(id: Long) {
        val prepareStatement = conn
            .prepareStatement("DELETE FROM PRODUCT WHERE id = ?")
        prepareStatement.setLong(1, id)
        prepareStatement.executeUpdate()
    }

    override fun getAll(): ImmutableList<Product> {
        val productList: MutableList<Product> = ArrayList()
        val prepareStatement = conn
            .prepareStatement("SELECT * FROM PRODUCT")

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            productList += Product(
                resultSet.getLong("id"), resultSet.getString("product_name"),
                resultSet.getInt("price"), resultSet.getString("description"),
                resultSet.getLong("group_id"), resultSet.getLong("user_id")
            )
        }
        return ImmutableListAdapter(productList)
    }
}