package com.example.mms.Model

import java.util.*

data class User(
    var email: String,
    val firstName: String,
    val lastName: String,
    val address: String,
    val followingNewsletter: Boolean,
    val lastCommand: Date?
)