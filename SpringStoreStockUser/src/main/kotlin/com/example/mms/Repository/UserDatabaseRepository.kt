package com.example.mms.Repository

import com.example.mms.Errors.UserAlreadyExistsException
import com.example.mms.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Repository
class UserDatabaseRepository(private val jpa : UserJpaRepository) : UserRepository {

    override fun create(user: User): Result<User> = if (jpa.findById(user.email).isPresent) {
        Result.failure(UserAlreadyExistsException(user.email))
    } else {
        val saved = jpa.save(user.asEntity())
        Result.success(saved.asUser())
    }

    override fun getAll(): List<User> = jpa.findAll().map { it.asUser() }

    override fun getAllNewsletterFollowers(): List<User> =  jpa.findAllByFollowingNewsletter(true).map { it.asUser() }

    override fun get(email: String): User? {
        return jpa.findById(email).map { it.asUser() }.getOrNull()
    }

    override fun update(user: User): Result<User> = if (jpa.findById(user.email).isPresent) {
        val saved = jpa.save(user.asEntity())
        Result.success(saved.asUser())
    } else {
        Result.failure(Exception("User not in DB"))
    }

    override fun delete(email: String): User? {
        return jpa.findById(email)
            .also { jpa.deleteById(email) }
            .map { it.asUser() }.getOrNull()
    }

    override fun updateLastCommandDate(email: String): Result<User> {
        val user = jpa.findById(email)
        if (user.isPresent) {
            user.get().lastCommand = Date()
            val saved = jpa.save(user.get())
            return Result.success(saved.asUser())
        }
        return Result.failure(Exception("User not in DB"))
    }
}

interface UserJpaRepository : JpaRepository<UserEntity, String> {
    fun findAllByFollowingNewsletter(followingNewsletter: Boolean) : List<UserEntity>
}
