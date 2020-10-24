package com.epam.kotlinapp.dao

import com.epam.kotlinapp.model.Product

object ProductOperation : ICommonOperations<Product> {

    override fun create(entity: Product) {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("INSERT INTO USER VALUES ('null',?,?,?,?,?)")
        prepareStatement.setString(1, entity.productName)
        prepareStatement.setInt(2, entity.price)
        prepareStatement.setString(3, entity.description)
        prepareStatement.setLong(4, entity.groupID)
        prepareStatement.setLong(5, entity.userId)
        prepareStatement.executeUpdate()

    }

    override fun getEntity(id: Long): Product? {
        val prepareStatement = ConnectionDB.conn
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
        val prepareStatement = ConnectionDB.conn
            .prepareStatement(
                "UPDATE PRODUCT SET product_name=?,price=?,description=?,group_id=?,user_id=? WHERE id=?"
            )
        prepareStatement.setString(1, entity.productName)
        prepareStatement.setInt(2, entity.price)
        prepareStatement.setString(3, entity.description)
        prepareStatement.setLong(4, entity.groupID)
        prepareStatement.setLong(5, entity.userId)
        entity.id?.let { prepareStatement.setLong(6, it) }
        prepareStatement.executeUpdate();
    }

    override fun delete(entity: Product) {
        entity.id?.let { delete(it) }
    }

    override fun delete(id: Long) {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("DELETE FROM PRODUCT WHERE id = ?")
        prepareStatement.setLong(1, id);
        prepareStatement.executeUpdate();
    }

    override fun getAll(): List<Product> {
       val productList:MutableList<Product> = ArrayList()
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("SELECT * FROM PRODUCT")
        
        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            productList += Product(
                resultSet.getLong("id"), resultSet.getString("product_name"),
                resultSet.getInt("price"), resultSet.getString("description"),
                resultSet.getLong("group_id"), resultSet.getLong("user_id")
            )
        }
        return productList
    }
}