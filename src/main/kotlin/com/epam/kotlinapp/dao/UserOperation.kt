package com.epam.kotlinapp.dao

import com.epam.kotlinapp.model.User

object UserOperation : ICommonOperations<User> {
//    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun create(entity: User) {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("INSERT INTO USER VALUES ('null',?,?,?)")
        prepareStatement.setString(1,entity.name)
        prepareStatement.setString(2,entity.email)
        prepareStatement.setString(3,entity.password)
        prepareStatement.executeUpdate()

    }

    override fun getEntity(id: Long): User? {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("SELECT * FROM USER WHERE id=?")
        prepareStatement.setLong(1, id)

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            return User(
                resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("email"), resultSet.getString("password")
            )
        }
        return null
    }

    override fun update(entity: User) {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("UPDATE USER SET name=?,email=?,password=? WHERE id=?")
        prepareStatement.setString(1,entity.name)
        prepareStatement.setString(2,entity.email)
        prepareStatement.setString(3,entity.password)
        entity.id?.let { prepareStatement.setLong(4, it) }
        prepareStatement.executeUpdate();
    }

    override fun delete(entity: User) {
        entity.id?.let { delete(it) }
    }

    override fun delete(id: Long) {
        val prepareStatement = ConnectionDB.conn
            .prepareStatement("DELETE FROM USER WHERE id = ?")
        prepareStatement.setLong(1,id);
        prepareStatement.executeUpdate();
    }

    override fun getAll(): List<User> {
        val userList:MutableList<User> = ArrayList()

        val prepareStatement = ConnectionDB.conn
            .prepareStatement("SELECT * FROM USER")

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            userList += User(
                resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("email"), resultSet.getString("password")
            )

        }
        return userList;
    }
}