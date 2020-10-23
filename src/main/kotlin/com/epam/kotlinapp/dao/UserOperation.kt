package com.epam.kotlinapp.dao

class UserOperation
{

    fun deleteUser(id:Int)
    {
        ConnectionDB.getConnection()
    }
}