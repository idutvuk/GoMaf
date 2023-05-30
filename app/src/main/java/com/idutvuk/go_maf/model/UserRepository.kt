package com.idutvuk.go_maf.model

import com.idutvuk.go_maf.model.database.UserDao
import com.idutvuk.go_maf.model.database.entities.User

class UserRepository(private val userDao: UserDao) {
    fun getUser(userId: Long): User {
        return userDao.getUser(userId)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}