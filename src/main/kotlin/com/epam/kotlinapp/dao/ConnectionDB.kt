package com.epam.kotlinapp.dao


import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

internal object ConnectionDB {
    private val username = "sa"
    private val password = ""
    lateinit var conn: Connection
    private val path:String = "jdbc:h2:".plus(System.getProperty("user.dir")).plus("\\db\\db")



    init {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(path, username, password)

        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }


}