package com.example.mms.Repository

import assertk.assertThat
import assertk.assertions.*
import com.example.mms.Model.User
import org.junit.jupiter.api.Test
import java.util.*

open class UserDatabaseTest {

    private fun defaultUser(
        email: String = "email@test.fr",
        firstName: String = "firstName",
        lastName: String = "lastName",
        address: String = "address",
        followingNewsletter: Boolean = true,
        lastCommand: Date? = null
    ): User {
        return User(email, firstName, lastName, address, followingNewsletter, lastCommand)
    }

    lateinit var repo: UserDatabaseRepository

    @Test
    fun `create once working`() {
        val user = defaultUser()
        val result = repo.create(user)
        assertThat(result).isSuccess().isEqualTo(user)
    }

    @Test
    fun `create twice not working`() {
        val user = defaultUser()
        repo.create(user)
        val result = repo.create(user)
        assertThat(result).isFailure()
    }

    @Test
    fun `create twice with different email working`() {
        val user = defaultUser()
        repo.create(user)
        val user2 = defaultUser(email = "cc@cc.fr")
        val result = repo.create(user2)
        assertThat(result).isSuccess().isEqualTo(user2)
    }

    @Test
    fun `get one working`() {
        val user = defaultUser()
        repo.create(user)
        val result = repo.get(user.email)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `get one not working`() {
        val result = repo.get("zzzz")
        assertThat(result).isNull()
    }

    @Test
    fun `get all working`() {
        val user = defaultUser()
        val user2 = defaultUser(email = "cc@cc.fr", firstName = "cc")
        val user3 = defaultUser(email = "pp@pp.fr", firstName = "pp")
        repo.create(user)
        repo.create(user2)
        repo.create(user3)
        val result = repo.getAll()
        assertThat(result).containsExactlyInAnyOrder(user, user2, user3)
    }

    @Test
    fun `get all newletters subscribers`() {
        val user = defaultUser()
        val user2 = defaultUser(email = "cc@cc.fr", firstName = "cc", followingNewsletter = false)
        val user3 = defaultUser(email = "pp@pp.fr", firstName = "pp")
        repo.create(user)
        repo.create(user2)
        repo.create(user3)
        val result = repo.getAllNewsletterFollowers()
        assertThat(result).containsExactlyInAnyOrder(user, user3)
    }

    @Test
    fun `update one working`() {
        val user = defaultUser()
        repo.create(user)
        val user2 = defaultUser(firstName = "cc")
        val result = repo.update(user2.email,user2)
        assertThat(result).isSuccess().isEqualTo(user2)
    }

    @Test
    fun `update one not working`() {
        val user = defaultUser()
        val result = repo.update(user.email,user)
        assertThat(result).isFailure()
    }

    @Test
    fun `delete one working`() {
        val user = defaultUser()
        repo.create(user)
        val result = repo.delete(user.email)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `delete one not working`() {
        val result = repo.delete("zzzz")
        assertThat(result).isNull()
    }

}