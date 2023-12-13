package com.example.mms.Repository

import com.example.mms.Model.User
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "users")
data class UserEntity(
        @Id val email: String,
        val firstName: String,
        val lastName: String,
        val address: String,
        val followingNewsletter: Boolean,
        var lastCommand: Date?
) {
    fun asUser() = User(email, firstName, lastName, address, followingNewsletter, lastCommand)
}
fun User.asEntity() = UserEntity(email, firstName, lastName, address, followingNewsletter, null)