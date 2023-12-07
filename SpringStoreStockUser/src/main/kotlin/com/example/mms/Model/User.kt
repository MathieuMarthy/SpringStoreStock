package com.example.mms.Model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date

data class User(
        val email: String,
        val firstName: String,
        val lastName: String,
        val address: String,
        val followingNewsletter: Boolean,
        val lastCommand: Date
)