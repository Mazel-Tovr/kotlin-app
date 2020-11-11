package com.epam.kotlinapp.crud.dao

import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import java.sql.Connection
import java.sql.Statement

object UserOperations : ICommonOperations<User> {
//    val logger: Logger = LoggerFactory.getLogger(javaClass)

    private var conn: Connection = ConnectionDB.conn

    override fun create(entity: User): User {
        val prepareStatement = conn
            .prepareStatement("INSERT INTO USER VALUES (NULL,?,?,?)", Statement.RETURN_GENERATED_KEYS)
        prepareStatement.setString(1, entity.name)
        prepareStatement.setString(2, entity.email)
        prepareStatement.setString(3, entity.password)
        prepareStatement.executeUpdate()
        val resultSet = prepareStatement.generatedKeys
        resultSet.next()
        entity.id = resultSet.getLong(1)
        return entity
    }

    override fun getEntity(id: Long): User? {
        val prepareStatement = conn
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
        val prepareStatement = conn
            .prepareStatement("UPDATE USER SET name=?,email=?,password=? WHERE id=?")
        prepareStatement.setString(1, entity.name)
        prepareStatement.setString(2, entity.email)
        prepareStatement.setString(3, entity.password)
        entity.id?.let { prepareStatement.setLong(4, it) }
            ?: throw DataException("User id isn't present ")
        prepareStatement.executeUpdate()
    }


    override fun delete(id: Long) {
        val prepareStatement = conn
            .prepareStatement("DELETE FROM USER WHERE id = ?")
        prepareStatement.setLong(1, id)
        prepareStatement.executeUpdate()
    }

    override fun getAll(): ImmutableList<User> {
        val userList: MutableList<User> = ArrayList()

        val prepareStatement = conn
            .prepareStatement("SELECT * FROM USER")

        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            userList += User(
                resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("email"), resultSet.getString("password")
            )

        }
        return ImmutableListAdapter(userList)
    }
}