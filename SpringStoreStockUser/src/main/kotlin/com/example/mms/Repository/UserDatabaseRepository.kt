package com.example.mms.Repository

import com.example.mms.Errors.UserAlreadyExistsException
import com.example.mms.Model.User
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Repository
class UserDatabaseRepository(private val jpa: UserJpaRepository) : UserRepository {

    private var logger = LoggerFactory.getLogger(javaClass)

    override fun create(user: User): Result<User> = if (jpa.findById(user.email).isPresent) {
        logger.error("User ${user.email} already in DB")
        Result.failure(UserAlreadyExistsException(user.email))
    } else {
        val saved = jpa.save(user.asEntity())
        logger.info("Creating user ${user.email}")
        Result.success(saved.asUser())
    }

    override fun getAll(): List<User> = jpa.findAll().map { it.asUser() }

    override fun getAllNewsletterFollowers(): List<User> = jpa.findAllByFollowingNewsletter(true).map { it.asUser() }

    override fun get(email: String): User? {
        return jpa.findById(email).map { it.asUser() }.getOrNull()
    }

    override fun update(email: String, user: User): Result<User> = if (jpa.findById(email).isPresent) {
        user.email = email
        val saved = jpa.save(user.asEntity())
        logger.info("Updating user ${user.email}")
        Result.success(saved.asUser())
    } else {
        logger.error("User ${user.email} not found")
        Result.failure(Exception("User not in DB"))
    }

    override fun delete(email: String): User? {
        return jpa.findById(email)
            .also { jpa.deleteById(email) }
            .map { it.asUser() }.getOrNull()
    }

    override fun updateLastCommandDate(email: String): Result<User> {
        val optionalUser = jpa.findById(email)
        if (optionalUser.isPresent) {
            val user = optionalUser.get()
            user.lastCommand = Date()
            val saved = jpa.save(user)
            logger.info("Updating last command date for user ${user.email}")
            return Result.success(saved.asUser())
        }
        logger.error("User $email not found")
        return Result.failure(Exception("User not in DB"))
    }
}

interface UserJpaRepository : JpaRepository<UserEntity, String> {
    fun findAllByFollowingNewsletter(followingNewsletter: Boolean): List<UserEntity>
}
