package com.example.mms.Repository

import com.example.mms.Model.User

interface UserRepository {
    fun create(user: User): Result<User>
    fun getAllNewsletterFollowers(): List<User>
    fun getAll(): List<User>
    fun get(email: String): User?
    fun update(email: String, user: User): Result<User>
    fun delete(email: String): User?
    fun updateLastCommandDate(email: String): Result<User>
}