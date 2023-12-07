package com.example.mms.Models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

@Entity
@Table(name = "users")
data class User(
        @Id
        val email: String,
        val firstName: String,
        val lastName: String,
        val address: String,
        val followingNewsletter: Boolean,
        val lastCommand: Date
)
