package com.example.mms.Model

import java.util.Date

data class User(
        val email: String,
        val firstName: String,
        val lastName: String,
        val address: String,
        val followingNewsletter: Boolean,
        val lastCommand: Date?
)