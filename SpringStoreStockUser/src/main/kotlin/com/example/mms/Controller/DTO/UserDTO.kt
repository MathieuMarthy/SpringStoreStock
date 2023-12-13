package com.example.mms.Controller.DTO

import com.example.mms.Model.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import jdk.jfr.BooleanFlag


// DTO for creating a user
data class UserDTO(
    @field:Email val email: String,
    @field:Size(min = 1, max = 30) val firstName: String,
    @field:Size(min = 1, max = 30) val lastName: String,
    @field:Size(min = 1, max = 30) val address: String,
    @field:BooleanFlag val followingNewsletter: Boolean
) {
    fun asUser() = User(email, firstName, lastName, address, followingNewsletter, null)
}
