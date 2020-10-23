package com.epam.kotlinapp.dao


import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

internal object ConnectionDB {
    private val username = "sa"
    private val password = ""
    private lateinit var conn: Connection


    init {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection("jdbc:h2:D:\\GitHub\\simple-kotlin-app\\db\\db", connectionProps)

        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }

   fun getConnection():Connection = this.conn
}